package ch.dkrieger.bansystem.lib.player.history.value;

import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.utils.Document;

import java.util.UUID;

public class Ban extends HistoryValue{

    private long timeOut;
    private BanType banType;
    private Document properties;

    public Ban(String ip, String reason, String message, long timeStamp, int points, int reasonID, UUID staff, long timeOut, BanType banType, Document properties) {
        super(ip, reason, message, timeStamp, points, reasonID, staff);
        this.timeOut = timeOut;
        this.banType = banType;
        this.properties = properties;
    }

    public long getTimeOut() {
        return timeOut;
    }
    public long getDuration(){
        return getTimeStamp()-timeOut;
    }
    public long getRemaining(){
        return System.currentTimeMillis()-timeOut;
    }
    public BanType getBanType() {
        return banType;
    }
    public Document getProperties() {
        return properties;
    }
}
