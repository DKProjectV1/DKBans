/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 08.11.19, 22:06
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

package ch.dkrieger.bansystem.extension.maintenance.bukkit;

import ch.dkrieger.bansystem.bukkit.event.BukkitDKBansSettingUpdateEvent;
import ch.dkrieger.bansystem.extension.maintenance.Maintenance;
import ch.dkrieger.bansystem.extension.maintenance.MaintenanceCommand;
import ch.dkrieger.bansystem.extension.maintenance.MaintenanceConfig;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.utils.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DKBansMaintenanceExtension extends JavaPlugin implements Listener {

    private Maintenance maintenance;
    private MaintenanceConfig config;

    @Override
    public void onEnable() {
        config = new MaintenanceConfig();
        Document data = BanSystem.getInstance().getSettingProvider().get("maintenance");
        if(data != null && data.contains("maintenance")) maintenance = data.getObject("maintenance",Maintenance.class);
        else maintenance = new Maintenance(false, System.currentTimeMillis(),"Dkrieger has hacked this server :)");
        Bukkit.getPluginManager().registerEvents(this,this);
        BanSystem.getInstance().getCommandManager().registerCommand(new MaintenanceCommand(config,maintenance));
    }

    @EventHandler(priority= EventPriority.HIGHEST)
    public void onPing(ServerListPingEvent event){
        System.out.println("Ping");
        if(maintenance.isEnabled()) event.setMotd(maintenance.replace(config.motdLine1)+"\n"+maintenance.replace(config.motdLine2));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerLoginEvent event){
        if(maintenance.isEnabled()){
            if(!maintenance.getWhitelist().contains(event.getPlayer().getUniqueId()) && !event.getPlayer().hasPermission("dkbans.maintenance.join")){
                event.disallow(PlayerLoginEvent.Result.KICK_BANNED,maintenance.replace(config.joinMessage));
            }
        }
    }

    @EventHandler
    public void onSettingUpdate(BukkitDKBansSettingUpdateEvent event){
        if(event.getName().equalsIgnoreCase("maintenance") ){
            this.maintenance = event.getSettings().getObject("maintenance",Maintenance.class);
            if(maintenance.isEnabled()){
                for(Player player : Bukkit.getOnlinePlayers()){
                    if(!maintenance.getWhitelist().contains(player.getUniqueId()) && !player.hasPermission("dkbans.maintenance.join"))
                        player.kickPlayer(maintenance.replace(config.joinMessage));
                }
            }
        }
    }
}
