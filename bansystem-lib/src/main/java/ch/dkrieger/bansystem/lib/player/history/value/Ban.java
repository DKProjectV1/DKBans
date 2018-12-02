package ch.dkrieger.bansystem.lib.player.history.value;

import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.utils.Document;

public class Ban extends HistoryValue{

    private long timeOut;
    private BanType banType;
    private Document properties;

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
