/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 08.11.19, 22:06
 * @Website https://github.com/DevKrieger/DKBans
 *
 * The DKBans Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package ch.dkrieger.bansystem.lib.player.history;

import ch.dkrieger.bansystem.lib.player.history.entry.*;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class History {


    private Map<Integer,HistoryEntry> entries;

    public History(){
        this.entries = new HashMap<>();
    }

    public History(Map<Integer, HistoryEntry> entries) {
        this.entries = entries;
    }

    public int size(){
        return this.entries.size();
    }

    public  Map<Integer, HistoryEntry> getRawEntries(){
        return this.entries;
    }

    public List<HistoryEntry> getEntries(){
        return new ArrayList<>(this.entries.values());
    }

    public List<HistoryEntry> getEntriesSorted(){
        List<HistoryEntry> entries = getEntries();
        entries.sort((o1, o2) -> o1.getTimeStamp() > o2.getTimeStamp()?1:-1);
        return entries;
    }

    public HistoryEntry getEntry(int id){
        return entries.get(id);
    }

    public int getPoints(){
        return getPoints(null);
    }

    public int getPoints(BanType type){
        AtomicInteger points = new AtomicInteger(0);
        GeneralUtil.iterateAcceptedForEach(this.entries.values(), object -> type == null || object.getPoints().getHistoryType().equals(type)
                , object ->{ points.getAndAdd(object.getPoints().getPoints());});
        return points.get();
    }

    public int getBanCount(){
        return getBanCount(BanType.NETWORK)+getBanCount(BanType.CHAT);
    }

    public int getBanCount(BanType type){
        return getBans(type).size();
    }

    public boolean isBanned(){
        return isBanned(BanType.NETWORK) || isBanned(BanType.CHAT);
    }

    public boolean isBanned(BanType type){
        return getBan(type) != null;
    }

    public Ban getBan(BanType type){
        List<Ban> bans = new ArrayList<>();
        List<Unban> unbans = new ArrayList<>();
        GeneralUtil.iterateAcceptedForEach(this.entries.values(), object ->
                        (object instanceof Ban && (type == null || ((Ban) object).getBanType().equals(type))
                        && (((Ban) object).getTimeOut() >= System.currentTimeMillis() || ((Ban) object).getTimeOut() <= 0)) || (object instanceof Unban
                        && (type == null || ((Unban) object).getBanType().equals(type)))
                , object -> {
                    if(object instanceof Ban) bans.add((Ban)object);
                    else unbans.add((Unban)object);
                });

        if(bans.size() > 0 && bans.size() > unbans.size()){
            final Ban[] ban = new Ban[1];
            final long[] timeStamp = {0};
            GeneralUtil.iterateAcceptedForEach(bans,object -> object.getTimeStamp() > timeStamp[0], object -> {
                ban[0] = object;
                timeStamp[0] = object.getTimeStamp();
            });
            if(GeneralUtil.iterateOne(unbans,object -> object.getTimeStamp() >= timeStamp[0]) != null) return null;
            return ban[0];
        }
        return null;
    }
    public List<Ban> getBans(){
        return getBans((BanType)null);
    }

    public List<Ban> getBans(BanType type){
        List<Ban> bans = new ArrayList<>();
        GeneralUtil.iterateAcceptedForEach(this.entries.values()
                ,object -> object instanceof Ban && (type == null || ((Ban) object).getBanType().equals(type))
                ,object -> {
            bans.add((Ban)object);
                });
        return bans;
    }

    public List<Ban> getBans(int reasonID){
        List<Ban> bans = new ArrayList<>();
        GeneralUtil.iterateAcceptedForEach(this.entries.values(),object -> object.getID() == reasonID,object -> bans.add((Ban)object));
        return bans;
    }

    public List<Ban> getBans(String reason){
        List<Ban> bans = new ArrayList<>();
        GeneralUtil.iterateAcceptedForEach(this.entries.values(),object -> object.getReason().equalsIgnoreCase(reason),object -> bans.add((Ban)object));
        return bans;
    }

    public int getWarnCount(){
        return getWarns().size();
    }

    public int getWarnCountSinceLastBan(){
        return getWarnsSinceLastBan().size();
    }

    public int getWarnCountSinceLastBan(int reasonId){
        return getWarnsSinceLastBan(reasonId).size();
    }

    public Warn getLastWarn(){
        final Warn[] warn = new Warn[1];
        final long[] timeStamp = {0};
        GeneralUtil.iterateAcceptedForEach(this.entries.values(),object -> object instanceof Warn
                && object.getTimeStamp() > timeStamp[0] && !isUnwarned(object.getID(),object.getTimeStamp()), object -> {
            warn[0] = (Warn) object;
            timeStamp[0] = object.getTimeStamp();
        });
        return warn[0];
    }

    public List<Warn> getWarns(){
       List<Warn> warns = new ArrayList<>();
       GeneralUtil.iterateAcceptedForEach(this.entries.values(), object -> object instanceof Warn, object -> warns.add((Warn) object));
       return warns;
    }

    public List<Warn> getWarnsSinceLastBan(int reasonId){
        final List<Warn> warns = getWarnsSinceLastBan();
        GeneralUtil.iterateAndRemove(warns, object -> object.getReasonID() != reasonId);
        return warns;
    }

    public List<Warn> getWarnsSinceLastBan(){
        final List<Warn> warns = getWarns();
        Ban ban = getLastBan();
        final long lastBanTimeStamp = ban!= null?ban.getTimeStamp():0;
        GeneralUtil.iterateAndRemove(warns, object -> object.getTimeStamp() < lastBanTimeStamp || isUnwarned(object.getID(),object.getTimeStamp()));
        return warns;
    }

    private boolean isUnwarned(int warnId, long time){
        for (HistoryEntry value : this.entries.values()) {
            if(value instanceof Unwarn && ((((Unwarn) value).getWarnId() == -1 && value.getTimeStamp() > time) || ((Unwarn) value).getWarnId() == warnId)) return true;
        }
        return false;
    }

    public Ban getLastBan(){
        final Ban[] ban = new Ban[1];
        final long[] timeStamp = {0};
        GeneralUtil.iterateAcceptedForEach(this.entries.values(),object -> object instanceof Ban && object.getTimeStamp() > timeStamp[0], object -> {
            ban[0] = (Ban) object;
            timeStamp[0] = object.getTimeStamp();
        });
        return ban[0];
    }

    public Unban getLastUnban(){
        final Unban[] unban = new Unban[1];
        final long[] timeStamp = {0};
        GeneralUtil.iterateAcceptedForEach(this.entries.values(),object -> object instanceof Unban && object.getTimeStamp() > timeStamp[0], object -> {
            unban[0] = (Unban) object;
            timeStamp[0] = object.getTimeStamp();
        });
        return unban[0];
    }

    public Kick getLastKick(){
        final Kick[] kick = new Kick[1];
        final long[] timeStamp = {0};
        GeneralUtil.iterateAcceptedForEach(this.entries.values(),object -> object instanceof Kick && object.getTimeStamp() > timeStamp[0], object -> {
            kick[0] = (Kick) object;
            timeStamp[0] = object.getTimeStamp();
        });
        return kick[0];
    }

    public List<HistoryEntry> getEntries(Filter filter){
        return GeneralUtil.iterateAcceptedReturn(this.entries.values(),filter::accepted);
    }

    private static class Filter {

        private Class<HistoryEntry> type;
        private long from, to;
        private int reasonID;
        private String staff, reason, message, ip;

        public Filter(Class<HistoryEntry> type, long from, long to, int reasonID, String staff, String reason, String message, String ip) {
            this.type = type;
            this.from = from;
            this.to = to;
            this.reasonID = reasonID;
            this.staff = staff;
            this.reason = reason;
            this.message = message;
            this.ip = ip;
        }
        public boolean accepted(HistoryEntry entry){
            return (type == null || entry.getClass() == type)
                    && (reasonID < 1 || entry.getReasonID() == reasonID)
                    && (reason == null || entry.getReason().equalsIgnoreCase(reason))
                    && (message == null || entry.getMessage().equalsIgnoreCase(message))
                    && (ip == null || entry.getIp().equalsIgnoreCase(ip))
                    && (staff == null || entry.getStaff().equalsIgnoreCase(staff))
                    && (from <= 0 || entry.getTimeStamp() >= from)
                    && (to <= 0 || entry.getTimeStamp() <= to);
        }
    }
}
