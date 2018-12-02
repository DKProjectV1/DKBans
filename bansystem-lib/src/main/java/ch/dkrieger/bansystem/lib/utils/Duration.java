package ch.dkrieger.bansystem.lib.utils;

import java.util.concurrent.TimeUnit;

public class Duration {

    private long time;
    private TimeUnit unit;

    public Duration(long time, TimeUnit unit) {
        this.time = time;
        this.unit = unit;
    }
    public long getTime() {
        return time;
    }
    public TimeUnit getUnit() {
        return unit;
    }
    public long getMillisTime(){
        return this.unit.toMillis(this.time);
    }
}
