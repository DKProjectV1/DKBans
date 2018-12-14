package ch.dkrieger.bansystem.lib.player.history.value;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;

import java.util.UUID;

public abstract class HistoryEntry {

    private String ip, reason, message;
    private long timeStamp;
    private int id, points, reasonID;
    private UUID staff;

    public HistoryEntry(String ip, String reason, String message, long timeStamp, int points, int reasonID, UUID staff) {
        this.ip = ip;
        this.reason = reason;
        this.message = message;
        this.timeStamp = timeStamp;
        this.points = points;
        this.reasonID = reasonID;
        this.staff = staff;
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

    public UUID getStaff() {
        return staff;
    }
    public String getStaffName(){
        if(staff == null) return "Console";
        else return BanSystem.getInstance().getPlayerManager().getPlayer(this.staff).getColoredName();
    }

    public abstract String getTypeName();
}
