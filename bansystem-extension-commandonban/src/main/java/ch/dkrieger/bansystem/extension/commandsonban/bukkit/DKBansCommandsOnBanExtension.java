/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 06.11.19, 20:31
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

package ch.dkrieger.bansystem.extension.commandsonban.bukkit;

import ch.dkrieger.bansystem.bukkit.event.BukkitNetworkPlayerBanEvent;
import ch.dkrieger.bansystem.extension.commandsonban.CommandsOnBanConfig;
import net.md_5.bungee.api.ProxyServer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class DKBansCommandsOnBanExtension extends JavaPlugin implements Listener {

    private CommandsOnBanConfig config;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this,this);
        this.config = new CommandsOnBanConfig();
    }
    
    @EventHandler
    public void onBan(BukkitNetworkPlayerBanEvent event){
        if(!config.executeOnAllServers && !event.isOnThisServer()) return;
        config.commands.forEach(command -> ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole()
                ,event.getBan().replace(command.replace("[player]",event.getPlayer().getName()),true)));
    }
}
