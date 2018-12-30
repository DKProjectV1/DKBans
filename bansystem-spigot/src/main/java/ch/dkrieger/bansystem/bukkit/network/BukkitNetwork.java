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

package ch.dkrieger.bansystem.bukkit.network;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.DKNetwork;
import ch.dkrieger.bansystem.lib.JoinMe;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import ch.dkrieger.bansystem.bukkit.BukkitBanSystemBootstrap;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class BukkitNetwork implements DKNetwork {

    private Map<UUID,JoinMe> joinmes;

    public BukkitNetwork() {
        joinmes = new HashMap<>();
    }

    @Override
    public JoinMe getJoinMe(OnlineNetworkPlayer player) {
        return getJoinMe(player.getUUID());
    }

    @Override
    public JoinMe getJoinMe(NetworkPlayer player) {
        return getJoinMe(player.getUUID());
    }

    @Override
    public JoinMe getJoinMe(UUID uuid) {
        return joinmes.get(uuid);
    }

    @Override
    public List<String> getPlayersOnServer(String server) {
        List<String> result = new ArrayList<>();
        for(Player player : Bukkit.getOnlinePlayers()) result.add(player.getName());
        return result;
    }

    @Override
    public void broadcast(String message) {
        Bukkit.broadcastMessage(message);
    }

    @Override
    public void broadcast(TextComponent component) {
        for(Player player : Bukkit.getOnlinePlayers()) BukkitBanSystemBootstrap.getInstance().sendTextComponent(player,component);
    }

    @Override
    public void broadcast(Broadcast broadcast) {
        if(broadcast == null) return;
        for(Player player : Bukkit.getOnlinePlayers()){
            if(broadcast.getPermission() == null || broadcast.getPermission().length() == 0|| player.hasPermission(broadcast.getPermission())) {
                NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
                BukkitBanSystemBootstrap.getInstance().sendTextComponent(player,GeneralUtil.replaceTextComponent(Messages.BROADCAST_FORMAT_SEND.replace("[prefix]",Messages.PREFIX_NETWORK)
                        ,"[message]",broadcast.build(networkPlayer)));
            }
        }
    }

    @Override
    public void broadcastLocal(Broadcast broadcast) {
        broadcast(broadcast);
    }

    @Override
    public void sendJoinMe(JoinMe joinMe) {
        this.joinmes.put(joinMe.getUUID(),joinMe);
        List<TextComponent> components = joinMe.create();
        for(Player player : Bukkit.getOnlinePlayers()){
            for(TextComponent component : components) BukkitBanSystemBootstrap.getInstance().sendTextComponent(player,component);
        }
    }

    @Override
    public void sendTeamMessage(String message) {
        sendTeamMessage(message,false);
    }

    @Override
    public void sendTeamMessage(String message, boolean onlyLogin) {
        sendTeamMessage(new TextComponent(message),onlyLogin);
    }

    @Override
    public void sendTeamMessage(TextComponent component) {
        sendTeamMessage(component,false);
    }

    @Override
    public void sendTeamMessage(TextComponent component, boolean onlyLogin) {
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player.hasPermission("dkbans.team")){
                NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
                if(networkPlayer != null && (!onlyLogin || networkPlayer.isTeamChatLoggedIn())) BukkitBanSystemBootstrap.getInstance().sendTextComponent(player,component);
            }
        }
    }

    @Override
    public void reloadFilter() {
        BanSystem.getInstance().getFilterManager().reloadLocal();
    }

    @Override
    public void reloadBroadcast() {
        BanSystem.getInstance().getBroadcastManager().reloadLocal();
    }
}
