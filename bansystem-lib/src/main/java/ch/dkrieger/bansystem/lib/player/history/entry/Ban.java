package ch.dkrieger.bansystem.lib.player.history.entry;

import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.utils.Document;

import java.util.UUID;

public class Ban extends HistoryEntry {

    private long timeOut;
    private BanType banType;

    public Ban(UUID uuid, String ip, String reason, String message, long timeStamp, int id, int points, int reasonID, String staff, Document properties, long timeOut, BanType banType) {
        super(uuid, ip, reason, message, timeStamp, id, points, reasonID, staff, properties);
        this.timeOut = timeOut;
        this.banType = banType;
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

    @Override
    public String getTypeName() {
        return "Ban";
    }
}
