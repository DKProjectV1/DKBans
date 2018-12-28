package ch.dkrieger.bansystem.lib.stats;

public class NetworkStats {

    private long logins, reports, reportsAccepted, messages, bans,mutes, unbans, kicks;

    public NetworkStats(){
        this(0,0,0,0,0,0,0,0);
    }

    public NetworkStats(long logins, long reports, long reportsAccepted, long messages, long bans, long mutes, long unbans, long kicks) {
        this.logins = logins;
        this.reports = reports;
        this.reportsAccepted = reportsAccepted;
        this.messages = messages;
        this.bans = bans;
        this.mutes = mutes;
        this.unbans = unbans;
        this.kicks = kicks;
    }

    public long getLogins() {
        return logins;
    }

    public long getReports() {
        return reports;
    }

    public long getReportsAccepted() {
        return reportsAccepted;
    }
    public long getReportsDenied() {
        return reports-reportsAccepted;
    }

    public long getMessages() {
        return messages;
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

    public void addLogins(){
        logins++;
    }
    public void addReports(){
        reports++;
    }
    public void addReportsAccepted(){
        reportsAccepted++;
    }
    public void addMessages(){
        messages++;
    }

    public void setLogins(long logins) {
        this.logins = logins;
    }

    public void setReports(long reports) {
        this.reports = reports;
    }

    public void setReportsAccepted(long reportsAccepted) {
        this.reportsAccepted = reportsAccepted;
    }

    public void setMessages(long messages) {
        this.messages = messages;
    }

    public void addBans(){
        bans++;
    }
    public void addMutes(){
        mutes++;
    }
    public void addUnbans(){
        unbans++;
    }
    public void addKicks(){
        kicks++;
    }
}
