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
}
