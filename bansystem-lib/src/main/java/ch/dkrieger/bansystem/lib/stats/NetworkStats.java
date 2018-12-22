package ch.dkrieger.bansystem.lib.stats;

public class NetworkStats extends Stats{

    private long bans,mutes, unbans, kicks,loadTime;

    public NetworkStats() {
        this(0,0,0,0,0,0,0,0,0);
    }

    public NetworkStats(long logins, long reports, long reportsAccepted, long messages, long bans, long mutes, long unbans, long kicks, long loadTime) {
        super(logins, reports, reportsAccepted, messages);
        this.bans = bans;
        this.mutes = mutes;
        this.unbans = unbans;
        this.kicks = kicks;
        this.loadTime = loadTime;
    }

    public long getBans() {
        return bans;
    }

    public long getMutes() {
        return mutes;
    }

    public long getUnbans() {
        return unbans;
    }

    public long getKicks() {
        return kicks;
    }
    public long getLoadTime() {
        return loadTime;
    }
}
