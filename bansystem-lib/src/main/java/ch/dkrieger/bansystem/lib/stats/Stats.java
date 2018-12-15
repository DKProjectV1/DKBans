package ch.dkrieger.bansystem.lib.stats;

public class Stats {

    //all
    private int logins, reports, reportsAccepted, messages;

    public int getLogins() {
        return logins;
    }

    public int getReports() {
        return reports;
    }

    public int getReportsAccepted() {
        return reportsAccepted;
    }
    public int getReportsDenied() {
        return reports-reportsAccepted;
    }

    public int getMessages() {
        return messages;
    }
}
