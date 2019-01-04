/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 30.12.18 14:39
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

package ch.dkrieger.bansystem.lib.reason;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.config.mode.BanMode;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.HistoryPoints;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.utils.Document;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BanReason extends KickReason {

    private double divider;
    private BanType historyType;
    private Map<Integer, BanReasonEntry> templateDurations;
    private Map<Integer,BanReasonEntry> pointsDurations;

    public BanReason(int id, HistoryPoints points, String name, String display, String permission, boolean hidden, List<String> aliases
            , Document properties, double divider, BanType historyType, Map<Integer, BanReasonEntry> templateDurations, Map<Integer, BanReasonEntry> pointsDurations) {
        super(id, points, name, display, permission, hidden, aliases,properties);
        this.divider = divider;
        this.historyType = historyType;
        this.templateDurations = templateDurations;
        this.pointsDurations = pointsDurations;
    }
    public BanReason(int id, HistoryPoints points, String name, String display, String permission, boolean hidden, List<String> aliases
            , Document properties, double divider, BanType historyType,Map<Integer, BanReasonEntry> pointsDurations,BanReasonEntry... templateDurations) {
        super(id, points, name, display, permission, hidden, aliases,properties);
        this.divider = divider;
        this.pointsDurations = new LinkedHashMap<>(pointsDurations);
        this.historyType = historyType;
        this.templateDurations = new LinkedHashMap<>();
        for(BanReasonEntry duration : templateDurations) this.templateDurations.put(this.templateDurations.size()+1,duration);
    }
    public double getDivider() {
        return divider;
    }
    public BanType getHistoryType() {
        return historyType;
    }
    public BanType getBanType(){
        return getDefaultDuration().getType();
    }
    public Map<Integer, BanReasonEntry> getDurations() {
        if(BanSystem.getInstance().getConfig().banMode == BanMode.POINT) return pointsDurations;
        else return templateDurations;
    }
    public Map<Integer, BanReasonEntry> getTemplateDurations() {
        return templateDurations;
    }
    public Map<Integer, BanReasonEntry> getPointsDurations() {
        return pointsDurations;
    }
    public BanReasonEntry getDefaultDuration(){
        return getNextDuration(0);
    }
    public BanReasonEntry getNextDuration(NetworkPlayer player){
        if(BanSystem.getInstance().getConfig().banMode == BanMode.POINT){
            if(!BanSystem.getInstance().getConfig().banPointsSeparateChatAndNetwork) return getNextDuration(player.getHistory().getPoints());
            else return getNextDuration(player.getHistory().getPoints(getPoints().getHistoryType()));
        }
        else return getNextDuration(player.getHistory().getBanCount(getHistoryType()));

    }
    public BanReasonEntry getNextDuration(int count){
        Map<Integer, BanReasonEntry> durations = getDurations();
        if(BanSystem.getInstance().getConfig().banMode == BanMode.POINT){
            count+=getPoints().getPoints();
            if(getDivider() > 0 ) count = (int) (count/getDivider());
        }else count++;
        if(durations.containsKey(count)) return durations.get(count);
        else{
            int last = -1;
            BanReasonEntry highest = null;
            for(Map.Entry<Integer, BanReasonEntry> entry : durations.entrySet()){
                if(entry.getKey() == count) return entry.getValue();
                if(entry.getKey() > last && entry.getKey() < count){
                    highest = entry.getValue();
                    last = entry.getKey();
                }
            }
            return highest;
        }
    }
    public Ban toBan(NetworkPlayer player,String message, String staff){
        long timeOut = 0;
        int points = getPoints().getPoints();
        if(!BanSystem.getInstance().getConfig().banPointsSeparateChatAndNetwork) points+=player.getHistory().getPoints();
        else points+=player.getHistory().getPoints(getPoints().getHistoryType());
        BanReasonEntry entry = getNextDuration(player);
        BanType type = entry.getType();
        if(BanSystem.getInstance().getConfig().banMode == BanMode.POINT){
            if(entry.getDuration().getTime() == -2){
                if(getHistoryType() == BanType.NETWORK){
                    timeOut = BanSystem.getInstance().getConfig().banPointsNetworkPermanently <= points
                            ?-1:BanSystem.getInstance().getConfig().banPointsNetworkTime*points;
                }else{
                    timeOut = BanSystem.getInstance().getConfig().banPointsChatPermanently <= points
                            ?-1:BanSystem.getInstance().getConfig().banPointsChatTime*points;
                }
                if(timeOut > 0) timeOut +=System.currentTimeMillis();
            }else timeOut = entry.getDuration().getTime() > 0?System.currentTimeMillis()+entry.getDuration().getMillisTime():-1;
        }else timeOut = entry.getDuration().getTime() > 0?System.currentTimeMillis()+entry.getDuration().getMillisTime():-1;
        return new Ban(player.getUUID(),player.getIP(),getRawDisplay(),message,System.currentTimeMillis(),-1
                ,new HistoryPoints(points,getPoints().getHistoryType()),getID(),staff,new Document(),timeOut,type);
    }
}
