package ch.dkrieger.bansystem.lib.stats;

public class PlayerStats extends Stats{

    private long reportsReceived;

    public PlayerStats() {
        this.reportsReceived = 0;
    }

    public PlayerStats(long logins, long reports, long reportsAccepted, long messages, long reportsReceived) {
        super(logins, reports, reportsAccepted, messages);
        this.reportsReceived = reportsReceived;
    }

    public long getReportsReceived() {
        return reportsReceived;
    }
}
