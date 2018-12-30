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

package ch.dkrieger.bansystem.bukkit.player.cloudnet;

import ch.dkrieger.bansystem.bukkit.BukkitBanSystemBootstrap;
import ch.dkrieger.bansystem.bukkit.event.BukkitOnlineNetworkPlayerUpdateEvent;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.JoinMe;
import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.cloudnet.v2.CloudNetV2Network;
import ch.dkrieger.bansystem.lib.cloudnet.v2.CloudNetV2OnlinePlayer;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.NetworkPlayerUpdateCause;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.PlayerManager;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import ch.dkrieger.bansystem.lib.utils.Document;
import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.bridge.event.bukkit.BukkitPlayerUpdateEvent;
import de.dytanic.cloudnet.bridge.event.bukkit.BukkitSubChannelMessageEvent;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class CloudNetV2PlayerManager extends PlayerManager implements Listener {

    private Map<UUID, CloudNetV2OnlinePlayer> players;

    public CloudNetV2PlayerManager() {
        this.players = new HashMap<>();
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(int id) {
        NetworkPlayer networkPlayer = getPlayer(id);
        if(networkPlayer == null) return null;
        return getOnlinePlayer(networkPlayer.getUUID());
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(UUID uuid) {
        return getOnlinePlayer(CloudAPI.getInstance().getOnlinePlayer(uuid));
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(String name) {
        NetworkPlayer networkPlayer = getPlayer(name);
        if(networkPlayer == null) return null;
        return getOnlinePlayer(networkPlayer.getUUID());
    }

    @Override
    public List<OnlineNetworkPlayer> getOnlinePlayers() {
        List<OnlineNetworkPlayer> players = new ArrayList<>();
        for(CloudPlayer player : CloudAPI.getInstance().getOnlinePlayers()) players.add(getOnlinePlayer(player));
        return players;
    }

    @Override
    public void removeOnlinePlayerFromCache(OnlineNetworkPlayer player) {
        this.players.remove(player.getUUID());
    }

    @Override
    public void removeOnlinePlayerFromCache(UUID uuid) {
        this.players.remove(uuid);
    }

    @Override
    public void updatePlayer(NetworkPlayer player, NetworkPlayerUpdateCause cause, Document properties) {
        CloudAPI.getInstance().sendCustomSubProxyMessage("DKBans","updatePlayer",new de.dytanic.cloudnet.lib.utility.document.Document()
                .append("uuid",player.getUUID()).append("sender",CloudAPI.getInstance().getServerId()).append("cause",cause).append("properties",properties));
        CloudAPI.getInstance().sendCustomSubServerMessage("DKBans","updatePlayer",new de.dytanic.cloudnet.lib.utility.document.Document()
                .append("uuid",player.getUUID()).append("sender",CloudAPI.getInstance().getServerId()).append("cause",cause).append("properties",properties));
        BukkitBanSystemBootstrap.getInstance().executePlayerUpdateEvents(player.getUUID(),cause,properties,false);
    }

    @Override
    public void updateOnlinePlayer(OnlineNetworkPlayer player) {}
    public OnlineNetworkPlayer getOnlinePlayer(CloudPlayer player){
        if(player != null){
            CloudNetV2OnlinePlayer online = this.players.get(player.getUniqueId());
            if(online == null){
                online = new CloudNetV2OnlinePlayer(player);
                this.players.put(player.getUniqueId(),online);
            }else online.setCloudPlayer(player);
            return online;
        }
        return null;
    }
    @Override
    public int getOnlineCount() {
        return CloudAPI.getInstance().getOnlineCount();
    }
    @EventHandler
    public void onMessageReveice(BukkitSubChannelMessageEvent event){
        if(event.getChannel().equalsIgnoreCase("DKBans")){
            if(event.getMessage().equalsIgnoreCase("updatePlayer") &&
                    !CloudAPI.getInstance().getServerId().equalsIgnoreCase(event.getDocument().getString("sender"))){
                UUID uuid = event.getDocument().getObject("uuid",UUID.class);
                BanSystem.getInstance().getPlayerManager().removePlayerFromCache(uuid);
                BukkitBanSystemBootstrap.getInstance().executePlayerUpdateEvents(uuid
                        ,event.getDocument().getObject("cause", NetworkPlayerUpdateCause.class)
                        ,event.getDocument().getObject("properties",Document.class),false);
            }else if(event.getMessage().equalsIgnoreCase("pingResult")){
                UUID uuid = event.getDocument().getObject("uuid",UUID.class);
                CloudNetV2OnlinePlayer.PINGGETTER.put(uuid,event.getDocument().getInt("ping"));
            }else if(event.getMessage().equalsIgnoreCase("reloadFilter")){
                BanSystem.getInstance().getFilterManager().reloadLocal();
            }else if(event.getMessage().equalsIgnoreCase("reloadBroadcast")){
                BanSystem.getInstance().getBroadcastManager().reloadLocal();
            }
        }
    }
    @EventHandler
    public void onPlayerUpdate(BukkitPlayerUpdateEvent event){
        Bukkit.getPluginManager().callEvent(new BukkitOnlineNetworkPlayerUpdateEvent(event.getCloudPlayer().getUniqueId()
                ,System.currentTimeMillis(),false));
    }
}
