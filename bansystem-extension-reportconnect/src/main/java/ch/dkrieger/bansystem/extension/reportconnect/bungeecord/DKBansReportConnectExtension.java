/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 31.12.18 10:35
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

package ch.dkrieger.bansystem.extension.reportconnect.bungeecord;

import ch.dkrieger.bansystem.bungeecord.event.ProxiedOnlineNetworkPlayerUpdateEvent;
import ch.dkrieger.bansystem.extension.reportconnect.DKBansReportConnectConfig;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.report.Report;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class DKBansReportConnectExtension extends Plugin implements Listener {

    private DKBansReportConnectConfig config;

    @Override
    public void onEnable() {
        config = new DKBansReportConnectConfig();
        BungeeCord.getInstance().getPluginManager().registerListener(this,this);
    }
    @EventHandler
    public void onUpdate(ProxiedOnlineNetworkPlayerUpdateEvent event){
        NetworkPlayer player = event.getPlayer();
        OnlineNetworkPlayer online = event.getOnlinePlayer();
        List<UUID> staffs = new ArrayList<>();
        for(Report report : player.getProcessingReports()){
            if(!staffs.contains(report.getStaff())){
                staffs.add(report.getStaff());
                ProxiedPlayer staff = BungeeCord.getInstance().getPlayer(report.getStaff());
                if(staff!=null && !(staff.getServer().getInfo().getName().equals(online.getServer()))){
                    staff.sendMessage(new TextComponent(config.message
                            .replace("[player]",player.getColoredName())
                            .replace("[prefix]", Messages.PREFIX_REPORT)));
                    staff.connect(BungeeCord.getInstance().getServerInfo(online.getServer()));
                    BanSystem.getInstance().getPlatform().getTaskManager().runTaskLater(()->{
                        for(String command :  BanSystem.getInstance().getConfig().reportAutoCommandEnter)
                            staff.chat(command.replace("[player]",player.getName()));
                    },500L, TimeUnit.MILLISECONDS);
                }
            }
        }
    }
}
