package ch.dkrieger.bansystem.lib.stats;

public class Stats {

    //all
    private long logins, reports, reportsAccepted, messages;

    public Stats(){
        this(0,0,0,0);
    }
    public Stats(long logins, long reports, long reportsAccepted, long messages) {
        this.logins = logins;
        this.reports = reports;
        this.reportsAccepted = reportsAccepted;
        this.messages = messages;
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

    public void addLogins(){
        logins++;
    }
    public void addReports(){
        reports++;
    }
    public void addReportsAccepted(){
        logins++;
    }
    public void addReportsDenied(){
        logins++;
    }
    public void addMessages(){
        logins++;
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
}
