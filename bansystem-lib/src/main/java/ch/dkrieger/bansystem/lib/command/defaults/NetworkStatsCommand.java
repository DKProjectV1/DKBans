package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.stats.NetworkStats;

import java.util.List;

public class NetworkStatsCommand extends NetworkCommand {

    public NetworkStatsCommand() {
        super("networkstats","","dkbans.networkstats");
        setPrefix(Messages.PREFIX_NETWORK);
    }

    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        NetworkStats stats = BanSystem.getInstance().getNetworkStats();
        NetworkStats syncStats = BanSystem.getInstance().getTempSyncStats();
        sender.sendMessage(Messages.NETWORK_STATS
                .replace("[registeredPlayers]",""+BanSystem.getInstance().getPlayerManager().getRegisteredCount())
                .replace("[onlinePlayers]",""+BanSystem.getInstance().getPlayerManager().getOnlineCount())
                .replace("[bans]",""+(stats.getBans()+syncStats.getBans()))
                .replace("[mutes]",""+(stats.getMutes()+syncStats.getMutes()))
                .replace("[unbans]",""+(stats.getUnbans()+syncStats.getUnbans()))
                .replace("[kicks]",""+(stats.getKicks()+syncStats.getKicks()))
                .replace("[reports]",""+(stats.getReports()+syncStats.getReports()))
                .replace("[reportsAccepted]",""+(stats.getReportsAccepted()+syncStats.getReportsAccepted()))
                .replace("[reportsDenied]",""+(stats.getReportsDenied()+syncStats.getReportsDenied()))
                .replace("[logins]",""+(stats.getLogins()+syncStats.getLogins()))
                .replace("[messages]",""+(stats.getMessages()+syncStats.getMessages()))
                .replace("[prefix]",getPrefix()));
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
