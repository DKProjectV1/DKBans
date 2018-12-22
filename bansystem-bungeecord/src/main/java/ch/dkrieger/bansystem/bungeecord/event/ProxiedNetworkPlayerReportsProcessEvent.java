package ch.dkrieger.bansystem.bungeecord.event;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.report.Report;

import java.util.List;
import java.util.UUID;

public class ProxiedNetworkPlayerReportsProcessEvent extends ProxiedDKBansEvent{

    private final UUID staff;

    public ProxiedNetworkPlayerReportsProcessEvent(UUID uuid, long timeStamp, boolean onThisServer,UUID staff) {
        super(uuid, timeStamp,onThisServer);
        this.staff = staff;
    }
    public UUID getStaffUUID(){
        return staff;
    }
    public NetworkPlayer getStaff(){
        return BanSystem.getInstance().getPlayerManager().getPlayer(this.staff);
    }
    public List<Report> getReport() {
        return getPlayer().getReports();
    }
}
