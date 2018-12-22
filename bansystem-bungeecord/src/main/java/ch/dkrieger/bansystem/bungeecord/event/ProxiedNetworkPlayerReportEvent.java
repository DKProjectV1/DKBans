package ch.dkrieger.bansystem.bungeecord.event;

import ch.dkrieger.bansystem.lib.report.Report;

import java.util.List;
import java.util.UUID;

public class ProxiedNetworkPlayerReportEvent extends ProxiedDKBansEvent{

    private final Report reports;

    public ProxiedNetworkPlayerReportEvent(UUID uuid, long timeStamp,boolean onThisServer,Report report) {
        super(uuid, timeStamp,onThisServer);
        this.reports = report;
    }
    public Report getReport() {
        return reports;
    }
}
