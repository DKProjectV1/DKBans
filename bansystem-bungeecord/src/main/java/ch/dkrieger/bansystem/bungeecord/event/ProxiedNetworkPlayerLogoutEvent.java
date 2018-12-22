package ch.dkrieger.bansystem.bungeecord.event;

import ch.dkrieger.bansystem.lib.report.Report;

import java.util.List;
import java.util.UUID;

public class ProxiedNetworkPlayerLogoutEvent extends ProxiedDKBansEvent{

    private final List<Report> reports;

    public ProxiedNetworkPlayerLogoutEvent(UUID uuid, long timeStamp, boolean onThisServer, List<Report> reports) {
        super(uuid, timeStamp,onThisServer);
        this.reports = reports;
    }
    public List<Report> getReports() {
        return reports;
    }
}
