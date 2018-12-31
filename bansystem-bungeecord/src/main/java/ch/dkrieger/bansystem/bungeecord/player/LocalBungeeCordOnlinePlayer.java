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

package ch.dkrieger.bansystem.bungeecord.player;

import ch.dkrieger.bansystem.bungeecord.BungeeCordBanSystemBootstrap;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import ch.dkrieger.bansystem.lib.player.history.entry.Warn;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class LocalBungeeCordOnlinePlayer implements OnlineNetworkPlayer {

    private ProxiedPlayer player;

    public LocalBungeeCordOnlinePlayer(ProxiedPlayer player) {
        this.player = player;
    }

    @Override
    public UUID getUUID() {
        return player.getUniqueId();
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public String getProxy() {
        return BungeeCordBanSystemBootstrap.getInstance().getProxyName();
    }

    @Override
    public String getServer() {
        return player.getServer().getInfo().getName();
    }

    @Override
    public int getPing() {
        return player.getPing();
    }

    @Override
    public NetworkPlayer getPlayer() {
        return BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
    }
    @Override
    public void sendMessage(String message) {
        sendMessage(new TextComponent(message));
    }

    @Override
    public void sendMessage(TextComponent component) {
        player.sendMessage(component);
    }

    @Override
    public void connect(String server) {
        ServerInfo serverInfo = BungeeCord.getInstance().getServerInfo(server);
        if(serverInfo != null && !(player.getServer().getInfo().equals(serverInfo))) player.connect(serverInfo);
    }

    @Override
    public void executeCommand(String command) {
        if(command.startsWith("/")) command = command.substring(1);
        BungeeCord.getInstance().getPluginManager().dispatchCommand(player,command);
    }
    @Override
    public void sendBan(Ban ban) {
        if(ban.getBanType() == BanType.NETWORK) player.disconnect(ban.toMessage());
        else player.sendMessage(ban.toMessage());
    }

    @Override
    public void sendKick(Kick kick) {
        player.disconnect(kick.toMessage());
    }

    @Override
    public void sendWarn(Warn warn) {
        player.sendMessage(warn.toMessage());
    }
}
