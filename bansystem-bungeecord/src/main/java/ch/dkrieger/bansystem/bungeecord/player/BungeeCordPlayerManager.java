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
import ch.dkrieger.bansystem.bungeecord.SubServerConnection;
import ch.dkrieger.bansystem.bungeecord.event.ProxiedOnlineNetworkPlayerUpdateEvent;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.NetworkPlayerUpdateCause;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.PlayerManager;
import ch.dkrieger.bansystem.lib.utils.Document;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class BungeeCordPlayerManager extends PlayerManager implements Listener {

    private Map<ProxiedPlayer,LocalBungeeCordOnlinePlayer> onlinePlayers;
    private SubServerConnection connection;

    public BungeeCordPlayerManager(SubServerConnection connection) {
        this.onlinePlayers = new HashMap<>();
        this.connection = connection;
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(int id) {
        NetworkPlayer player = getPlayer(id);
        if(player != null) return getOnlinePlayer(player.getUUID());
        return null;
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(UUID uuid) {
        return getOnlinePlayer(ProxyServer.getInstance().getPlayer(uuid));
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(String name) {
        return getOnlinePlayer(ProxyServer.getInstance().getPlayer(name));
    }
    public OnlineNetworkPlayer getOnlinePlayer(ProxiedPlayer player){
        if(player != null){
            LocalBungeeCordOnlinePlayer online = this.onlinePlayers.get(player);
            if(online == null){
                online = new LocalBungeeCordOnlinePlayer(player);
                this.onlinePlayers.put(player,online);
            }
            return online;
        }
        return null;
    }

    @Override
    public List<OnlineNetworkPlayer> getOnlinePlayers() {
        List<OnlineNetworkPlayer> players = new ArrayList<>();
        for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) players.add(getOnlinePlayer(player));
        return players;
    }

    @Override
    public void updatePlayer(NetworkPlayer player, NetworkPlayerUpdateCause cause, Document properties) {
        connection.sendToAll("updatePlayer",new Document().append("uuid",player.getUUID()).append("cause",cause)
                .append("properties",properties));
        BungeeCordBanSystemBootstrap.getInstance().executePlayerUpdateEvents(player.getUUID(),cause,properties,true);
    }

    @Override
    public void updateOnlinePlayer(OnlineNetworkPlayer player) {

    }

    @Override
    public void removeOnlinePlayerFromCache(OnlineNetworkPlayer player) {
        removeOnlinePlayerFromCache(player.getUUID());
    }

    @Override
    public void removeOnlinePlayerFromCache(UUID uuid) {
        this.onlinePlayers.remove(ProxyServer.getInstance().getPlayer(uuid));
    }

    @Override
    public int getOnlineCount() {
        return ProxyServer.getInstance().getOnlineCount();
    }
    public void sendOnlinePlayers(ServerInfo sendServer){
        List<PlayerUpdateObject> players = new ArrayList<>();
        for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
            Server server = player.getServer();
            players.add(new PlayerUpdateObject(player.getUniqueId(),player.getName(),server==null?"Unknown":server.getInfo().getName(),"Proxy-1"));
        }
        connection.send(sendServer,"updateOnlinePlayers",new Document().append("players",players));
    }

    @EventHandler
    public void onPlayerServerConnected(ServerConnectedEvent event){
        ProxyServer.getInstance().getScheduler().schedule(BungeeCordBanSystemBootstrap.getInstance(),()->{
            if(event.getServer().getInfo().getPlayers().size() <= 1) sendOnlinePlayers(event.getServer().getInfo());
            connection.sendToAll("updateOnlinePlayer",new Document().append("uuid",event.getPlayer().getUniqueId())
                    .append("name",event.getPlayer().getName())
                    .append("server",event.getServer().getInfo().getName()));
            ProxyServer.getInstance().getPluginManager().callEvent(new ProxiedOnlineNetworkPlayerUpdateEvent(event.getPlayer().getUniqueId()
                    ,System.currentTimeMillis(),true));
        },300, TimeUnit.MILLISECONDS);
    }
    public static class PlayerUpdateObject {

        private UUID uuid;
        private String name, server, proxy;

        public PlayerUpdateObject(UUID uuid, String name, String server, String proxy) {
            this.uuid = uuid;
            this.name = name;
            this.server = server;
            this.proxy = proxy;
        }
    }
}
