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

package ch.dkrieger.bansystem.lib.player.history.entry;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.HistoryPoints;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import ch.dkrieger.bansystem.lib.utils.RuntimeTypeAdapterFactory;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.*;

public class Ban extends HistoryEntry {

    private long timeOut;
    private BanType banType;

    private List<BanEditVersion> versions;

    public Ban(UUID uuid, String ip, String reason, String message, long timeStamp, int id, HistoryPoints points, int reasonID
            , String staff, Document properties, long timeOut, BanType banType) {
        super(uuid, ip, reason, message, timeStamp, id, points, reasonID, staff, properties);
        this.timeOut = timeOut;
        this.banType = banType;
        this.versions = new ArrayList<>();
    }
    public long getTimeOut() {
        final long[] time = new long[2];
        final boolean[] found = new boolean[1];
        if(versions.size() > 0){
            GeneralUtil.iterateAcceptedForEach(getVersions(), object -> object instanceof TimeOutBanEdit && time[0] < object.time,object -> {
                time[0] = object.time;
                time[1] = ((TimeOutBanEdit)object).timeOut;
                found[0] = true;
            });
            if(found[0]) return time[1];
        }
        return timeOut;
    }
    public long getDuration(){
        return getTimeOut()-getTimeStamp();
    }
    public long getRemaining(){
        return getTimeOut()-System.currentTimeMillis();
    }
    public long getFirstDuration(){
        return timeOut-getTimeStamp();
    }
    public long getFirstRemaining(){
        return timeOut-System.currentTimeMillis();
    }
    public List<BanEditVersion> getVersions() {
        if(this.versions == null) this.versions = new ArrayList<>();
        return versions;
    }
    public BanEditVersion getVersion(int id){
        return GeneralUtil.iterateOne(getVersions(), object -> object.id == id);
    }
    public List<BanEditVersion> getVersionsSorted() {
        versions.sort((o1, o2) -> o1.time > o2.time?1:-1);
        return versions;
    }
    public BanType getBanType() {
        return banType;
    }
    @Override
    public String getReason() {
        final long[] lastTime = new long[1];
        final String[] reason = new String[1];
        if(versions.size() > 0){
            GeneralUtil.iterateAcceptedForEach(getVersions(), object -> object instanceof ReasonBanEdit && lastTime[0] < object.time,object -> {
                lastTime[0] = object.time;
                reason[0] = ((ReasonBanEdit)object).reason;
            });
            if(reason[0] != null) return ChatColor.translateAlternateColorCodes('&',reason[0]);
        }
        return super.getReason();
    }
    @Override
    public String getMessage() {
        final long[] lastTime = new long[1];
        final String[] message = new String[1];
        if(versions.size() > 0){
            GeneralUtil.iterateAcceptedForEach(getVersions(), object -> object instanceof MessageBanEdit && lastTime[0] < object.time,object -> {
                lastTime[0] = object.time;
                message[0] = ((MessageBanEdit)object).localMessage;
            });
            if(message[0] != null) return message[0];
        }
        return super.getMessage();
    }
    @Override
    public HistoryPoints getPoints() {
        final long[] lastTime = new long[1];
        final HistoryPoints[] points = new HistoryPoints[1];
        if(versions.size() > 0){
            GeneralUtil.iterateAcceptedForEach(getVersions(), object -> object instanceof PointsBanEdit && lastTime[0] < object.time,object -> {
                lastTime[0] = object.time;
                points[0] = ((PointsBanEdit)object).points;
            });
            if(points[0] != null) return points[0];
        }
        return super.getPoints();
    }

    public String getFirstReason(){
        return this.reason;
    }
    public String getFirstMessage(){
        return this.message;
    }
    public long getFirstTomeOut(){
        return this.timeOut;
    }
    public ReasonBanEdit setReason(String reason){
        return setReason(reason,"",(UUID)null);
    }
    public ReasonBanEdit setReason(String reason, String message){
        return setReason(reason,message,(UUID)null);
    }
    public ReasonBanEdit setReason(String reason, String message,UUID staff){
        return setReason(reason, message,staff==null?"Console":staff.toString());
    }
    public ReasonBanEdit setReason(String reason, String message, String staff){
        ReasonBanEdit edit = new ReasonBanEdit(staff, message,reason);
        versions.add(edit);
        update(new Document().append("edit",edit));
        return edit;
    }
    public MessageBanEdit setMessage(String message){
        return setMessage(message,"",(UUID)null);
    }
    public MessageBanEdit setMessage(String message,String messageForMessage){
        return setMessage(message,messageForMessage,(UUID)null);
    }
    public MessageBanEdit setMessage(String message,String messageForMessage,UUID staff){
        return setMessage(message,messageForMessage,staff==null?"Console":staff.toString());
    }
    public MessageBanEdit setMessage(String message,String messageForMessage, String staff){
        MessageBanEdit edit = new MessageBanEdit(staff,messageForMessage,message);
        versions.add(edit);
        update(new Document().append("edit",edit));
        return edit;
    }
    public TimeOutBanEdit setTimeOut(long timeOut){
        return setTimeOut(timeOut,"",(UUID)null);
    }
    public TimeOutBanEdit setTimeOut(long timeOut,String message){
        return setTimeOut(timeOut,message,(UUID)null);
    }
    public TimeOutBanEdit setTimeOut(long timeOut,String message,UUID staff){
        return setTimeOut(timeOut,message,staff==null?"Console":staff.toString());
    }
    public TimeOutBanEdit setTimeOut(long timeOut,String message, String staff){
        TimeOutBanEdit edit = new TimeOutBanEdit(staff,message,timeOut,getTimeStamp());
        versions.add(edit);
        update(new Document().append("edit",edit));
        return edit;
    }
    public PointsBanEdit setPoints(int points){
        return setPoints(points,"",(UUID)null);
    }
    public PointsBanEdit setPoints(int points,String message){
        return setPoints(points,message,(UUID)null);
    }
    public PointsBanEdit setPoints(int points,String message,UUID staff){
        return setPoints(points,message,staff==null?"Console":staff.toString());
    }
    public PointsBanEdit setPoints(int points,String message, String staff){
        PointsBanEdit edit = new PointsBanEdit(staff,message,new HistoryPoints(points,super.getPoints().getHistoryType()));
        versions.add(edit);
        update(new Document().append("edit",edit));
        return edit;
    }

    @Override
    public String getTypeName() {
        return "Ban";
    }

    @Override
    public TextComponent getListMessage() {
        String message = Messages.HISTORY_LIST_BAN_NETWORK;
        if(banType == BanType.CHAT) message = Messages.HISTORY_LIST_BAN_CHAT;
        return new TextComponent(message = replace(message,false));
    }
    public TextComponent getInfo(boolean first){
        String message = Messages.HISTORY_INFO_BAN_NETWORK;
        if(banType == BanType.CHAT) message = Messages.HISTORY_INFO_BAN_CHAT;
        TextComponent changes = new TextComponent(Messages.HISTORY_INFO_BAN_CHANGES.replace("[changeCount]",""+versions.size()));
        changes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/history "+uuid+" "+id+" list"));
        return GeneralUtil.replaceTextComponent(message = replace(message,first),"[changes]",changes);
    }


    @Override
    public TextComponent getInfoMessage() {
        return getInfo(false);
    }

    public TextComponent toMessage(){
        String message = Messages.BAN_MESSAGE_NETWORK_TEMPORARY;
        if(getBanType() == BanType.CHAT){
            message = Messages.BAN_MESSAGE_CHAT_TEMPORARY;
            if(getTimeOut() <= 0) message = Messages.BAN_MESSAGE_CHAT_PERMANENT;
        }else if(getTimeOut() <= 0) message = Messages.BAN_MESSAGE_NETWORK_PERMANENT;
        return new TextComponent(replace(message,false));
    }
    public String replace(String message, boolean first){
        return message
                .replace("[player]",getPlayer().getColoredName())
                .replace("[id]",""+getID())
                .replace("[banId]",""+getID())
                .replace("[banid]",""+getID())
                .replace("[reason]",(first?getFirstReason():getReason()))
                .replace("[reasonID]",""+getReasonID())
                .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(getTimeStamp()))
                .replace("[timeout]",BanSystem.getInstance().getConfig().dateFormat.format((first?getFirstTomeOut():getTimeOut())))
                .replace("[message]",(first?getFirstMessage():getMessage()))
                .replace("[type]",getTypeName())
                .replace("[staff]",getStaffName())
                .replace("[ip]",getIp())
                .replace("[points]",""+getPoints().getPoints())
                .replace("[pointsType]",getPoints().getHistoryType().getDisplay())
                .replace("[duration]",GeneralUtil.calculateDuration(first?getFirstDuration():getDuration()))
                .replace("[remaining]",GeneralUtil.calculateRemaining((first?getFirstRemaining():getRemaining()),false))
                .replace("[remaining-short]",GeneralUtil.calculateRemaining((first?getFirstRemaining():getRemaining()),true))
                .replace("[prefix]",Messages.PREFIX_BAN);
    }

    public abstract class BanEditVersion {

        protected int id;
        protected String staff, message;
        protected long time;

        public BanEditVersion(String staff,String message) {
            this.staff = staff;
            this.message = message;
            time = System.currentTimeMillis();
            id = versions.size()+1;
        }
        public int getID() {
            return id;
        }
        public String getStaff() {
            return staff;
        }
        public String getMessage() {
            return message;
        }
        public long getTime() {
            return time;
        }
        public String getStaffName(){
            if(staff == null) return BanSystem.getInstance().getConfig().playerColorConsole+"Console";
            try{
                return BanSystem.getInstance().getPlayerManager().getPlayer(UUID.fromString(this.staff)).getColoredName();
            }catch (Exception exception){}
            return BanSystem.getInstance().getConfig().playerColorConsole+this.staff;
        }
        public abstract String getListMessage();

        public abstract String getInfoMessage();
    }
    public class ReasonBanEdit extends BanEditVersion {

        private String reason;

        public ReasonBanEdit(String staff,String message, String reason) {
            super(staff,message);
            this.reason = reason;
        }
        public String getReason() {
            return reason;
        }
        @Override
        public String getListMessage() {
            return Messages.HISTORY_INFO_BAN_VERSION_LIST_REASON
                    .replace("[reason]",reason)
                    .replace("[message]",message)
                    .replace("[staff]",getStaffName())
                    .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(time))
                    .replace("[prefix]",Messages.PREFIX_BAN);
        }
        @Override
        public String getInfoMessage() {
            return Messages.HISTORY_INFO_BAN_VERSION_INFO_REASON
                    .replace("[reason]",reason)
                    .replace("[message]",message)
                    .replace("[staff]",getStaffName())
                    .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(time))
                    .replace("[prefix]",Messages.PREFIX_BAN);
        }
    }
    public class MessageBanEdit extends BanEditVersion {

        private String localMessage;

        public MessageBanEdit(String staff,String messageForMessage, String message) {
            super(staff,messageForMessage);
            this.localMessage = message;
        }
        public String getLocalMessage() {
            return localMessage;
        }
        @Override
        public String getListMessage() {
            return Messages.HISTORY_INFO_BAN_VERSION_LIST_MESSAGE
                    .replace("[localMessage]",localMessage)
                    .replace("[message]",message)
                    .replace("[staff]",getStaffName())
                    .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(time))
                    .replace("[prefix]",Messages.PREFIX_BAN);
        }
        @Override
        public String getInfoMessage() {
            return Messages.HISTORY_INFO_BAN_VERSION_INFO_MESSAGE
                    .replace("[localMessage]",localMessage)
                    .replace("[message]",message)
                    .replace("[staff]",getStaffName())
                    .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(time))
                    .replace("[prefix]",Messages.PREFIX_BAN);
        }
    }
    public class TimeOutBanEdit extends BanEditVersion {

        private long timeOut;
        private long firstTimeStamp;

        public TimeOutBanEdit(String staff,String message, long timeOut, long firstTimeStamp) {
            super(staff,message);
            this.timeOut = timeOut;
            this.firstTimeStamp = firstTimeStamp;
        }
        public long getTimeOut() {
            return timeOut;
        }
        public long getNewDuration(){
            return timeOut-time;
        }
        public long getDuration(){
            return timeOut-firstTimeStamp;
        }
        public long getRemaining(){
            return timeOut-System.currentTimeMillis();
        }
        @Override
        public String getListMessage() {
            return Messages.HISTORY_INFO_BAN_VERSION_LIST_TIMEOUT
                    .replace("[timeOut]",BanSystem.getInstance().getConfig().dateFormat.format(timeOut))
                    .replace("[message]",message)
                    .replace("[staff]",getStaffName())
                    .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(time))
                    .replace("[duration]",GeneralUtil.calculateDuration(getDuration()))
                    .replace("[remaining]",GeneralUtil.calculateRemaining(getRemaining(),false))
                    .replace("[remaining-short]",GeneralUtil.calculateRemaining(getRemaining(),true))
                    .replace("[prefix]",Messages.PREFIX_BAN);
        }
        @Override
        public String getInfoMessage() {
            return Messages.HISTORY_INFO_BAN_VERSION_INFO_TIMEOUT
                    .replace("[timeOut]",BanSystem.getInstance().getConfig().dateFormat.format(timeOut))
                    .replace("[message]",message)
                    .replace("[staff]",getStaffName())
                    .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(time))
                    .replace("[duration]",GeneralUtil.calculateDuration(getDuration()))
                    .replace("[remaining]",GeneralUtil.calculateRemaining(getRemaining(),false))
                    .replace("[remaining-short]",GeneralUtil.calculateRemaining(getRemaining(),true))
                    .replace("[prefix]",Messages.PREFIX_BAN);
        }
    }
    public class PointsBanEdit extends BanEditVersion {

        private HistoryPoints points;

        public PointsBanEdit(String staff,String message, HistoryPoints points) {
            super(staff,message);
            this.points = points;
        }
        public HistoryPoints getPoints() {
            return points;
        }
        @Override
        public String getListMessage() {
            return Messages.HISTORY_INFO_BAN_VERSION_LIST_POINTS
                    .replace("[message]",message)
                    .replace("[points]",""+points.getPoints())
                    .replace("[staff]",getStaffName())
                    .replace("[pointType]",points.getHistoryType().getDisplay())
                    .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(time))
                    .replace("[prefix]",Messages.PREFIX_BAN);
        }
        @Override
        public String getInfoMessage() {
            return Messages.HISTORY_INFO_BAN_VERSION_INFO_POINTS
                    .replace("[message]",message)
                    .replace("[points]",""+points.getPoints())
                    .replace("[pointType]",points.getHistoryType().getDisplay())
                    .replace("[staff]",getStaffName())
                    .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(time))
                    .replace("[prefix]",Messages.PREFIX_BAN);
        }
    }

    public static void buildAdapter(){
        RuntimeTypeAdapterFactory<Ban.BanEditVersion> adapter = RuntimeTypeAdapterFactory.of(Ban.BanEditVersion.class, "editAdapterType");
        adapter.registerSubtype(Ban.ReasonBanEdit.class,"REASON");
        adapter.registerSubtype(Ban.MessageBanEdit.class,"MESSAGE");
        adapter.registerSubtype(Ban.TimeOutBanEdit.class,"TIMEOUT");
        adapter.registerSubtype(Ban.PointsBanEdit.class,"POINTS");
        GeneralUtil.GSON_BUILDER.registerTypeAdapterFactory(adapter);
        GeneralUtil.GSON_BUILDER_NOT_PRETTY.registerTypeAdapterFactory(adapter);
        GeneralUtil.createGSON();
    }
}
