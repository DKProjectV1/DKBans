/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 31.12.18 11:47
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

package ch.dkrieger.bansystem.extension.reportconnect.bukkit;

import ch.dkrieger.bansystem.bukkit.event.BukkitOnlineNetworkPlayerUpdateEvent;
import ch.dkrieger.bansystem.extension.reportconnect.DKBansReportConnectConfig;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.report.Report;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class DKBansReportConnectExtension extends JavaPlugin implements Listener {

    private DKBansReportConnectConfig config;

    @Override
    public void onEnable() {
        config = new DKBansReportConnectConfig();
        Bukkit.getPluginManager().registerEvents(this,this);
    }
    @EventHandler
    public void onUpdate(BukkitOnlineNetworkPlayerUpdateEvent event){
        NetworkPlayer player = event.getPlayer();
        OnlineNetworkPlayer online = event.getOnlinePlayer();
        List<UUID> staffs = new ArrayList<>();
        for(Report report : player.getProcessingReports()){
            if(!staffs.contains(report.getStaff())){
                staffs.add(report.getStaff());
                Player staff = Bukkit.getPlayer(report.getStaff());
                if(staff!=null && !(staff.getWorld().getName().equals(online.getServer()))){
                    staff.sendMessage(config.message
                            .replace("[player]",player.getColoredName())
                            .replace("[prefix]", Messages.PREFIX_REPORT));
                    staff.teleport(Bukkit.getWorld(online.getServer()).getSpawnLocation());
                    BanSystem.getInstance().getPlatform().getTaskManager().runTaskLater(()->{
                        for(String command :  config.commadnsOnConnect){
                            if(!command.startsWith("/")) command = "/"+command;
                            staff.chat(command.replace("[player]",player.getName()));
                        }
                    },500L, TimeUnit.MILLISECONDS);
                }
            }
        }
    }
}
