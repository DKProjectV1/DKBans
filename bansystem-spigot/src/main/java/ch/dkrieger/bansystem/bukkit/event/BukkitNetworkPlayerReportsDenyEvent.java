package ch.dkrieger.bansystem.bukkit.event;

import ch.dkrieger.bansystem.lib.report.Report;

import java.util.List;
import java.util.UUID;

public class BukkitNetworkPlayerReportsDenyEvent extends BukkitNetworkPlayerEvent {

    private final List<Report> reports;

    public BukkitNetworkPlayerReportsDenyEvent(UUID uuid, long timeStamp, boolean onThisServer, List<Report> reports) {
        super(uuid, timeStamp,onThisServer);
        this.reports = reports;
    }
    public List<Report> getReport() {
        return reports;
    }
}
