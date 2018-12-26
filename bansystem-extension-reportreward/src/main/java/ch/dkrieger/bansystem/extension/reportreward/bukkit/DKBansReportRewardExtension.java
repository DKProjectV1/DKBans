package ch.dkrieger.bansystem.extension.reportreward.bukkit;

import ch.dkrieger.bansystem.bukkit.event.BukkitNetworkPlayerReportsAcceptEvent;
import ch.dkrieger.bansystem.extension.reportreward.ReportRewardConfig;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.report.Report;
import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.core.manager.MessageManager;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DKBansReportRewardExtension extends JavaPlugin implements Listener {

    private ReportRewardConfig config;

    @Override
    public void onEnable() {
        this.config = new ReportRewardConfig();
    }
    @EventHandler
    public void onAcceptedReport(BukkitNetworkPlayerReportsAcceptEvent event){
        for(Report report : event.getReport()){
            Player player = Bukkit.getPlayer(report.getUUID());
            if(player != null){
                player.sendMessage(config.reportRewardMessage.replace("[prefix]", Messages.PREFIX_BAN)
                        .replace("[coin-prefix]",MessageManager.getInstance().prefix)
                        .replace("[coins]",""+config.reportRewardCoins));
                CoinPlayer coinPlayer = CoinSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
                if(coinPlayer != null) coinPlayer.addCoins(config.reportRewardCoins,"DKBansReportReward");
            }
        }
    }
}
