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
import ch.dkrieger.bansystem.bukkit.BungeeCordConnection;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.NetworkPlayerUpdateCause;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.PlayerManager;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.*;

public class BukkitBungeeCordPlayerManager extends PlayerManager {

    private Map<UUID,OnlineNetworkPlayer> onlinePlayers;
    private BungeeCordConnection connection;

    public BukkitBungeeCordPlayerManager(BungeeCordConnection connection) {
        this.onlinePlayers = new HashMap<>();
        this.connection = connection;
    }
    public void insertOnlinePlayer(OnlineNetworkPlayer online){
        this.onlinePlayers.put(online.getUUID(),online);
    }
    @Override
    public OnlineNetworkPlayer getOnlinePlayer(int id) {
        NetworkPlayer player = getPlayer(id);
        if(player != null) return getOnlinePlayer(player.getUUID());
        return null;
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(UUID uuid) {
        return onlinePlayers.get(uuid);
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(String name) {
        return GeneralUtil.iterateOne(this.onlinePlayers.values(), object -> object.getName().equalsIgnoreCase(name));
    }

    @Override
    public List<OnlineNetworkPlayer> getOnlinePlayers() {
        return new ArrayList<>(this.onlinePlayers.values());
    }

    @Override
    public void removeOnlinePlayerFromCache(OnlineNetworkPlayer player) {
        this.onlinePlayers.remove(player.getUUID());
    }

    @Override
    public void removeOnlinePlayerFromCache(UUID uuid) {
        this.onlinePlayers.remove(uuid);
    }

    @Override
    public void updatePlayer(NetworkPlayer player, NetworkPlayerUpdateCause cause, Document properties) {
        System.out.println("update "+player.getColoredName());
        BukkitBanSystemBootstrap.getInstance().executePlayerUpdateEvents(player.getUUID(),cause,properties,true);
        connection.send("updatePlayer",new Document().append("uuid",player.getUUID()).append("cause",cause)
                .append("properties",properties));
    }
    public void updateAll(List<OnlineNetworkPlayer> players){
        this.onlinePlayers.clear();
        GeneralUtil.iterateForEach(players, object -> onlinePlayers.put(object.getUUID(),object));
    }
    @Override
    public void updateOnlinePlayer(OnlineNetworkPlayer player) {}

    @Override
    public int getOnlineCount() {
        return this.onlinePlayers.size();
    }
}
