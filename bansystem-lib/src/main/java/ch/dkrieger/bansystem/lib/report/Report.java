package ch.dkrieger.bansystem.lib.report;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;

import java.util.UUID;

public class Report {

    private UUID uuid, staff, reporter;
    private String reason, reportedServer;
    private long timeStamp;
    private int reasonID;

    public Report(UUID uuid, UUID staff, UUID reporter, String reason, String reportedServer, long timeStamp, int reasonID) {
        this.uuid = uuid;
        this.staff = staff;
        this.reporter = reporter;
        this.reason = reason;
        this.reportedServer = reportedServer;
        this.timeStamp = timeStamp;
        this.reasonID = reasonID;
    }

    public UUID getUUID() {
        return uuid;
    }
    public UUID getStaff() {
        return staff;
    }
    public UUID getReporterUUID() {
        return reporter;
    }
    public String getReason() {
        return reason;
    }
    public String getReportedServer() {
        return reportedServer;
    }
    public long getTimeStamp() {
        return timeStamp;
    }
    public int getReasonID() {
        return reasonID;
    }
    public NetworkPlayer getPlayer(){
        return BanSystem.getInstance().getPlayerManager().getPlayer(this.uuid);
    }
    public NetworkPlayer getReporter(){
        return BanSystem.getInstance().getPlayerManager().getPlayer(this.reporter);
    }
    public void setStaff(UUID uuid){
        this.staff = staff;
    }
}
