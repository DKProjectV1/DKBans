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
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.HistoryPoints;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import ch.dkrieger.bansystem.lib.utils.RuntimeTypeAdapterFactory;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class HistoryEntry {

    public static Map<String,Class<? extends HistoryEntry> > GSONTYPEADAPTER = new HashMap<>();

    static {
        GSONTYPEADAPTER.put("BAN",Ban.class);
        GSONTYPEADAPTER.put("KICK",Kick.class);
        GSONTYPEADAPTER.put("UNBAN",Unban.class);
        GSONTYPEADAPTER.put("WARN",Warn.class);
        buildAdapter();
    }

    protected UUID uuid;
    protected String ip, reason, message;
    protected long timeStamp;
    protected int id, reasonID;
    protected String staff;
    protected HistoryPoints historyPoints;
    protected Document properties;

    public HistoryEntry(UUID uuid, String ip, String reason, String message, long timeStamp, int id,HistoryPoints points, int reasonID, String staff, Document properties) {
        this.uuid = uuid;
        this.ip = ip;
        this.reason = reason;
        this.message = message;
        this.timeStamp = timeStamp;
        this.id = id;
        this.historyPoints = points;
        this.reasonID = reasonID;
        this.staff = staff;
        this.properties = properties;
    }
    public UUID getUUID() {
        return uuid;
    }

    public String getIp() {
        return ip;
    }

    public String getReason() {
        return ChatColor.translateAlternateColorCodes('&',reason);
    }
    public String getRawReason() {
        return reason;
    }
    public String getMessage() {
        return message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public HistoryPoints getPoints() {
        if(historyPoints == null) this.historyPoints = new HistoryPoints(0, BanType.NETWORK);
        return historyPoints;
    }

    public int getID() {
        return id;
    }

    public int getReasonID() {
        return reasonID;
    }

    public String getStaff() {
        return staff;
    }
    public NetworkPlayer getStaffAsPlayer(){
        try{
            return BanSystem.getInstance().getPlayerManager().getPlayer(UUID.fromString(this.staff));
        }catch (Exception exception){}
        return null;
    }
    public String getStaffName(){
        if(staff == null) return BanSystem.getInstance().getConfig().playerColorConsole+"Console";
        try{
            return BanSystem.getInstance().getPlayerManager().getPlayer(UUID.fromString(this.staff)).getColoredName();
        }catch (Exception exception){}
        return BanSystem.getInstance().getConfig().playerColorConsole+this.staff;
    }
    public Document getProperties() {
        return properties;
    }

    public NetworkPlayer getPlayer(){
        return BanSystem.getInstance().getPlayerManager().getPlayer(this.uuid);
    }

    @SuppressWarnings("This is only for databse insert functions")
    public void setID(int id) {
        this.id = id;
    }

    public void update(){

    }

    public abstract String getTypeName();

    public abstract TextComponent getListMessage();

    public abstract TextComponent getInfoMessage();



    public static void buildAdapter(){
        RuntimeTypeAdapterFactory<HistoryEntry> adapter = RuntimeTypeAdapterFactory.of(HistoryEntry.class, "historyAdapterType");
        for(Map.Entry<String,Class<? extends HistoryEntry>> entry : GSONTYPEADAPTER.entrySet()) adapter.registerSubtype(entry.getValue(),entry.getKey());
        GeneralUtil.GSON_BUILDER.registerTypeAdapterFactory(adapter);
        GeneralUtil.GSON_BUILDER_NOT_PRETTY.registerTypeAdapterFactory(adapter);
        GeneralUtil.createGSON();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        return obj instanceof HistoryEntry && ((HistoryEntry) obj).getID() == id;
    }
}
