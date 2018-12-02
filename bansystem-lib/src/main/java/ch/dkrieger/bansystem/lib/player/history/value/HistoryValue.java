package ch.dkrieger.bansystem.lib.player.history.value;

import java.util.UUID;

public class HistoryValue {

    private String ip, reason, message;
    private long timeStamp;
    private int points, reasonID;
    private UUID staff;

    public String getIp() {
        return ip;
    }

    public String getReason() {
        return reason;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public int getPoints() {
        return points;
    }

    public int getReasonID() {
        return reasonID;
    }

    public UUID getStaff() {
        return staff;
    }
    public String getStaffName(){
        //return name
    }
}
