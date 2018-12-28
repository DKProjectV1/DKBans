package ch.dkrieger.bansystem.lib.reason;

import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.utils.Duration;

import java.util.concurrent.TimeUnit;

public class BanReasonEntry {

    private BanType type;
    private Duration duration;

    public BanReasonEntry(BanType type, Duration duration) {
        this.type = type;
        this.duration = duration;
    }
    public BanReasonEntry(BanType type, long time, TimeUnit unit){
        this.type = type;
        this.duration = new Duration(time,unit);
    }

    public BanType getType() {
        return type;
    }

    public Duration getDuration() {
        return duration;
    }
}
