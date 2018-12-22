package ch.dkrieger.bansystem.lib.reason;

import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.report.Report;

import java.util.List;
import java.util.UUID;

public class ReportReason extends KickReason{

    int forBan;

    public ReportReason(int id, int points, String name, String display, String permission, boolean hidden, List<String> aliases, int forban) {
        super(id, points, name, display, permission, hidden, aliases);
        this.forBan = forban;
    }

    public int getForBan() {
        return forBan;
    }

    public Report toReport(NetworkPlayer player, UUID reporter, String message, String server){
        return new Report(player.getUUID(),null,reporter,getDisplay(),message,server,System.currentTimeMillis(),getID());
    }
}
