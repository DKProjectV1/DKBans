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

package ch.dkrieger.bansystem.extension.commandsonban.bungeecord;

import ch.dkrieger.bansystem.bungeecord.event.ProxiedNetworkPlayerBanEvent;
import ch.dkrieger.bansystem.bungeecord.event.ProxiedNetworkPlayerKickEvent;
import ch.dkrieger.bansystem.bungeecord.event.ProxiedNetworkPlayerUnbanEvent;
import ch.dkrieger.bansystem.bungeecord.event.ProxiedNetworkPlayerWarnEvent;
import ch.dkrieger.bansystem.extension.commandsonban.CommandsOnBanConfig;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import ch.dkrieger.bansystem.lib.player.history.entry.Unban;
import ch.dkrieger.bansystem.lib.player.history.entry.Warn;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.function.Consumer;

public class DKBansCommandsOnBanExtension extends Plugin implements Listener {

    private CommandsOnBanConfig config;

    @Override
    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerListener(this,this);
        this.config = new CommandsOnBanConfig();
    }

    @EventHandler
    public void onBan(ProxiedNetworkPlayerBanEvent event){
        if(!config.executeOnAllServers && !event.isOnThisServer()) return;
        config.commands.forEach(command -> ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole()
                ,event.getBan().replace(command.replace("[player]",event.getPlayer().getName()),true)));
    }
}
