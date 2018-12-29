package ch.dkrieger.bansystem.extension.reportreward.bungeecord;

import ch.dkrieger.bansystem.bungeecord.event.ProxiedNetworkPlayerReportsAcceptEvent;
import ch.dkrieger.bansystem.extension.reportreward.ReportRewardConfig;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.report.Report;
import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.core.manager.MessageManager;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class DKBansReportRewardExtension extends Plugin implements Listener {

    private ReportRewardConfig config;

    @Override
    public void onEnable() {
        this.config = new ReportRewardConfig();
        BungeeCord.getInstance().getPluginManager().registerListener(this,this);
    }
    @EventHandler
    public void onAcceptedReport(ProxiedNetworkPlayerReportsAcceptEvent event){
        for(Report report : event.getReport()){
            ProxiedPlayer player = BungeeCord.getInstance().getPlayer(report.getReporterUUID());
            if(player != null){
                player.sendMessage(new TextComponent(config.reportRewardMessage.replace("[prefix]", Messages.PREFIX_BAN)
                        .replace("[coin-prefix]",MessageManager.getInstance().prefix)
                        .replace("[coins]",""+config.reportRewardCoins)));
                CoinPlayer coinPlayer = CoinSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
                if(coinPlayer != null) coinPlayer.addCoins(config.reportRewardCoins,"DKBansReportReward");
            }
        }
    }
}
