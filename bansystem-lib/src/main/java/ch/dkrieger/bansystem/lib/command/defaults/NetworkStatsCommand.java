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
        sender.sendMessage(Messages.NETWORK_STATS
                .replace("[registeredPlayers]",""+BanSystem.getInstance().getPlayerManager().getRegisteredCount())
                .replace("[onlinePlayers]",""+BanSystem.getInstance().getPlayerManager().getOnlineCount())
                .replace("[bans]",""+stats.getBans())
                .replace("[mutes]",""+stats.getMutes())
                .replace("[unbans]",""+stats.getUnbans())
                .replace("[kicks]",""+stats.getKicks())
                .replace("[reports]",""+stats.getReports())
                .replace("[currentBans]","//Implement")
                .replace("[currentMutes]","//Implement")
                .replace("[reportsAccepted]",""+stats.getReportsAccepted())
                .replace("[reportsDenied]",""+stats.getReportsDenied())
                .replace("[countries]",""+BanSystem.getInstance().getPlayerManager().getCountryCount())
                .replace("[messages]",""+stats.getMessages())
                .replace("[prefix]",getPrefix()));
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
