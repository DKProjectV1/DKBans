/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 14.03.19 19:43
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

package ch.dkrieger.bansystem.bukkit.player.bukkit;

import ch.dkrieger.bansystem.bukkit.BukkitBanSystemBootstrap;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import ch.dkrieger.bansystem.lib.player.history.entry.Warn;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BukkitOnlinePlayer implements OnlineNetworkPlayer {

    private Player player;

    public BukkitOnlinePlayer(Player player) {
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
        return "Proxy-1";
    }

    @Override
    public String getServer() {
        return player.getWorld().getName();
    }

    @Override
    public int getPing() {
        try {
            Object playerHandle = player.getClass().getMethod("getHandle").invoke(player);
            return (int)playerHandle.getClass().getField( "ping").get(playerHandle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public NetworkPlayer getPlayer() {
        return BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public void sendMessage(TextComponent component) {
        BukkitBanSystemBootstrap.getInstance().sendTextComponent(player,component);
    }

    @Override
    public void connect(String server) {
        World world = Bukkit.getWorld(server);
        if(world != null) player.teleport(world.getSpawnLocation());
    }

    @Override
    public void executeCommand(String command) {
        Bukkit.dispatchCommand(player,command);
    }

    @Override
    public void sendBan(Ban ban) {
        if(ban.getBanType() == BanType.NETWORK) kick(ban.toMessage());
        else BukkitBanSystemBootstrap.getInstance().sendTextComponent(player,ban.toMessage());
    }

    @Override
    public void sendKick(Kick kick) {
        kick(kick.toMessage());
    }
    @Override
    public void sendWarn(Warn warn) {
        if(warn.isKick()) player.kickPlayer(warn.toKickMessage().toLegacyText());
        else BukkitBanSystemBootstrap.getInstance().sendTextComponent(player,warn.toChatMessage());
    }
    public void kick(TextComponent component){
       Bukkit.getScheduler().runTask(BukkitBanSystemBootstrap.getInstance(),()->{ player.kickPlayer(component.toLegacyText());});
    }
}
