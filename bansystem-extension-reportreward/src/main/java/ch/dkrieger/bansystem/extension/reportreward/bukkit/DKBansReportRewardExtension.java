/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 30.12.18 14:39
 * @Website https://github.com/DevKrieger/DKBans
 *
 * The DKBans Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package ch.dkrieger.bansystem.extension.reportreward.bukkit;

import ch.dkrieger.bansystem.bukkit.event.BukkitNetworkPlayerReportsAcceptEvent;
import ch.dkrieger.bansystem.extension.reportreward.ReportRewardConfig;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.report.Report;
import ch.dkrieger.coinsystem.core.CoinSystem;
import ch.dkrieger.coinsystem.core.manager.MessageManager;
import ch.dkrieger.coinsystem.core.player.CoinPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class DKBansReportRewardExtension extends JavaPlugin implements Listener {

    private ReportRewardConfig config;

    @Override
    public void onEnable() {
        this.config = new ReportRewardConfig();
        Bukkit.getPluginManager().registerEvents(this,this);
    }
    @EventHandler
    public void onAcceptedReport(BukkitNetworkPlayerReportsAcceptEvent event){
        for(Report report : event.getReport()){
            Player player = Bukkit.getPlayer(report.getReporterUUID());
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
