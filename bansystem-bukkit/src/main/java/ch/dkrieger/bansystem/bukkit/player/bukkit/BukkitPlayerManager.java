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

package ch.dkrieger.bansystem.bukkit.player.bukkit;

import ch.dkrieger.bansystem.bukkit.BukkitBanSystemBootstrap;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.NetworkPlayerUpdateCause;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.PlayerManager;
import ch.dkrieger.bansystem.lib.utils.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class BukkitPlayerManager extends PlayerManager {

    public Map<Player,OnlineNetworkPlayer> players;

    public BukkitPlayerManager() {
        this.players = new LinkedHashMap<>();
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(int id) {
        NetworkPlayer player = getPlayer(id);
        if(player != null) return getOnlinePlayer(player.getUUID());
        return null;
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(UUID uuid) {
        return getPlayer(Bukkit.getPlayer(uuid));
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(String name) {
        return getPlayer(Bukkit.getPlayer(name));
    }

    @Override
    public List<OnlineNetworkPlayer> getOnlinePlayers() {
        List<OnlineNetworkPlayer> players = new ArrayList<>();
        for(Player player : Bukkit.getOnlinePlayers()) players.add(getPlayer(player));
        return players;
    }
    public OnlineNetworkPlayer getPlayer(Player player){
        if(player == null) return null;
        OnlineNetworkPlayer onlinePlayer = players.get(player);
        if(onlinePlayer == null){
            onlinePlayer = new BukkitOnlinePlayer(player);
            this.players.put(player,onlinePlayer);
        }
        return onlinePlayer;
    }
    @Override
    public void removeOnlinePlayerFromCache(OnlineNetworkPlayer player) {}

    @Override
    public void removeOnlinePlayerFromCache(UUID uuid) {}

    @Override
    public void updatePlayer(NetworkPlayer player, NetworkPlayerUpdateCause cause, Document properties) {
        BukkitBanSystemBootstrap.getInstance().executePlayerUpdateEvents(player.getUUID(),cause,properties,true);
    }
    @Override
    public void updateOnlinePlayer(OnlineNetworkPlayer player) {}
    @Override
    public int getOnlineCount() {
        return Bukkit.getOnlinePlayers().size();
    }
}
