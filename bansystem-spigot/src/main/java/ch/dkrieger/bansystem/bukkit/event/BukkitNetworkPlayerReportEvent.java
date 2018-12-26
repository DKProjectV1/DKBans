package ch.dkrieger.bansystem.bukkit.event;

import ch.dkrieger.bansystem.lib.report.Report;

import java.util.UUID;

public class BukkitNetworkPlayerReportEvent extends BukkitNetworkPlayerEvent {

    private final Report reports;

    public BukkitNetworkPlayerReportEvent(UUID uuid, long timeStamp, boolean onThisServer, Report report) {
        super(uuid, timeStamp,onThisServer);
        this.reports = report;
    }
    public Report getReport() {
        return reports;
    }
}
