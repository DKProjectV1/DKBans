package ch.dkrieger.bansystem.lib.player.history.entry;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.utils.Document;

import java.util.UUID;

public abstract class HistoryEntry {

    private UUID uuid;
    private String ip, reason, message;
    private long timeStamp;
    private int id, points, reasonID;
    private String staff;
    private Document properties;

    public HistoryEntry(UUID uuid, String ip, String reason, String message, long timeStamp, int id, int points, int reasonID, String staff, Document properties) {
        this.uuid = uuid;
        this.ip = ip;
        this.reason = reason;
        this.message = message;
        this.timeStamp = timeStamp;
        this.id = id;
        this.points = points;
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
        return reason;
    }

    public String getMessage() {
        return message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getPoints() {
        return points;
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
    public String getStaffName(){
        if(staff == null) return "Console";
        try{
            return BanSystem.getInstance().getPlayerManager().getPlayer(UUID.fromString(this.staff)).getColoredName();
        }catch (Exception exception){}
        return this.staff;
    }
    public Document getProperties() {
        return properties;
    }

    @SuppressWarnings("This is only for databse insert functions")
    public void setID(int id) {
        this.id = id;
    }

    public abstract String getTypeName();
}
