package ch.dkrieger.bansystem.lib.stats;

public class PlayerStats extends NetworkStats {

    private long reportsReceived;

    public PlayerStats() {
        this.reportsReceived = 0;
    }

    public PlayerStats(long logins, long reports, long reportsAccepted, long messages, long bans, long mutes, long unbans, long kicks, long reportsReceived) {
        super(logins, reports, reportsAccepted, messages, bans, mutes, unbans, kicks);
        this.reportsReceived = reportsReceived;
    }

    public long getReportsReceived() {
        return reportsReceived;
    }
    public void addReportsReceived(){
        this.reportsReceived++;
    }
}
