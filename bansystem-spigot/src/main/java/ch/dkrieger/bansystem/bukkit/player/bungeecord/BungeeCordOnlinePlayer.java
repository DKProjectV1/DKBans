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

package ch.dkrieger.bansystem.bukkit.player.bungeecord;

import ch.dkrieger.bansystem.bukkit.BukkitBanSystemBootstrap;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import ch.dkrieger.bansystem.lib.utils.Document;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

public class BungeeCordOnlinePlayer implements OnlineNetworkPlayer {

    private UUID uuid;
    private String name, server, proxy;

    public BungeeCordOnlinePlayer(UUID uuid, String name, String server, String proxy) {
        this.uuid = uuid;
        this.name = name;
        this.server = server;
        this.proxy = proxy;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public String getProxy() {
        return proxy;
    }
    @Override
    public String getServer() {
        return server;
    }
    @Override
    public int getPing() {
        return -1;
    }

    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public NetworkPlayer getPlayer() {
        return BanSystem.getInstance().getPlayerManager().getPlayer(uuid);
    }
    @Override
    public void sendMessage(String message) {
        sendMessage(new TextComponent(message));
    }
    @Override
    public void sendMessage(TextComponent component) {
        BukkitBanSystemBootstrap.getInstance().getBungeeCordConnection().send("sendMesssage"
                ,new Document().append("uuid",uuid).append("message",component));
    }
    @Override
    public void connect(String server) {
        BukkitBanSystemBootstrap.getInstance().getBungeeCordConnection().send("connect"
                ,new Document().append("uuid",uuid).append("server",server));
    }
    @Override
    public void executeCommand(String command) {
        BukkitBanSystemBootstrap.getInstance().getBungeeCordConnection().send("executeCommand"
                ,new Document().append("uuid",uuid).append("command",command));
    }
    @Override
    public void sendBan(Ban ban) {
        BukkitBanSystemBootstrap.getInstance().getBungeeCordConnection().send("sendBan"
                ,new Document().append("uuid",uuid).append("ban",ban));
    }
    @Override
    public void kick(Kick kick) {
        BukkitBanSystemBootstrap.getInstance().getBungeeCordConnection().send("kick"
                ,new Document().append("uuid",uuid).append("kick",kick));
    }
}
