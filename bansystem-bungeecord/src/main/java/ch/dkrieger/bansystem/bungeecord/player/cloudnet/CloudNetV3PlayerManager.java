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

package ch.dkrieger.bansystem.bungeecord.player.cloudnet;

import ch.dkrieger.bansystem.bungeecord.BungeeCordBanSystemBootstrap;
import ch.dkrieger.bansystem.bungeecord.event.ProxiedOnlineNetworkPlayerUpdateEvent;
import ch.dkrieger.bansystem.bungeecord.player.BungeeCordPlayerManager;
import ch.dkrieger.bansystem.bungeecord.player.LocalBungeeCordOnlinePlayer;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.JoinMe;
import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.cloudnet.v2.CloudNetV2Network;
import ch.dkrieger.bansystem.lib.cloudnet.v3.CloudNetV3Network;
import ch.dkrieger.bansystem.lib.cloudnet.v3.CloudNetV3OnlinePlayer;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.NetworkPlayerUpdateCause;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.PlayerManager;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import ch.dkrieger.bansystem.lib.player.history.entry.Warn;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import com.google.gson.reflect.TypeToken;
import de.dytanic.cloudnet.ext.bridge.bungee.event.*;
import de.dytanic.cloudnet.wrapper.Wrapper;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.*;

public class CloudNetV3PlayerManager extends PlayerManager implements Listener {

    private Map<ProxiedPlayer,LocalBungeeCordOnlinePlayer> localPlayers;
    private Map<UUID,CloudNetV3OnlinePlayer> externalPlayers;

    public CloudNetV3PlayerManager() {
        this.localPlayers = new HashMap<>();
        this.externalPlayers = new HashMap<>();
        Wrapper.getInstance().sendChannelMessage("DKBans","getOnlinePlayers",new de.dytanic.cloudnet.common.document.Document());
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(int id) {
        NetworkPlayer networkPlayer = getPlayer(id);
        if(networkPlayer == null) return null;
        return getOnlinePlayer(networkPlayer.getUUID());
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(UUID uuid) {
        OnlineNetworkPlayer player = getOnlinePlayer(ProxyServer.getInstance().getPlayer(uuid));
        if(player == null) return externalPlayers.get(uuid);
        return player;
    }
    @Override
    public OnlineNetworkPlayer getOnlinePlayer(String name) {
        OnlineNetworkPlayer player = getOnlinePlayer(ProxyServer.getInstance().getPlayer(name));
        if(player == null) return GeneralUtil.iterateOne(externalPlayers.values(), object -> object.getName().equalsIgnoreCase(name));
        return player;
    }
    public OnlineNetworkPlayer getOnlinePlayer(ProxiedPlayer player){
        if(player == null) return null;
        LocalBungeeCordOnlinePlayer online = this.localPlayers.get(player);
        if(online == null){
            online = new LocalBungeeCordOnlinePlayer(player);
            this.localPlayers.put(player,online);
        }
        return online;
    }

    @Override
    public List<OnlineNetworkPlayer> getOnlinePlayers() {
        List<OnlineNetworkPlayer> players = new ArrayList<>();
        for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) players.add(getOnlinePlayer(player));
        players.addAll(this.externalPlayers.values());
        return players;
    }

    @Override
    public void removeOnlinePlayerFromCache(OnlineNetworkPlayer player) {
        this.externalPlayers.remove(player.getUUID());
    }

    @Override
    public void removeOnlinePlayerFromCache(UUID uuid) {
        this.externalPlayers.remove(uuid);
    }

    @Override
    public void updatePlayer(NetworkPlayer player, NetworkPlayerUpdateCause cause, Document properties) {
        de.dytanic.cloudnet.common.document.Document data = de.dytanic.cloudnet.common.document.Document.newDocument(properties.toJson());
        Wrapper.getInstance().sendChannelMessage("DKBans","updatePlayer",data
                .append("uuid",player.getUUID()).append("sender",Wrapper.getInstance().getServiceId().getName()).append("cause",cause));
        BungeeCordBanSystemBootstrap.getInstance().executePlayerUpdateEvents(player.getUUID(),cause,properties,true);
    }

    @Override
    public void updateOnlinePlayer(OnlineNetworkPlayer player) {}

    @Override
    public int getOnlineCount() {
        return this.localPlayers.size()+this.externalPlayers.size();
    }
    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event){
        ProxyServer.getInstance().getPluginManager().callEvent(new ProxiedOnlineNetworkPlayerUpdateEvent(
                event.getPlayer().getUniqueId(),System.currentTimeMillis(),true));
    }
    @EventHandler
    public void onServerSwitch(BungeeBridgeProxyPlayerServerSwitchEvent event){
        if(ProxyServer.getInstance().getPlayer(event.getNetworkConnectionInfo().getUniqueId()) != null) return;
        CloudNetV3OnlinePlayer player = externalPlayers.get(event.getNetworkConnectionInfo().getUniqueId());
        if(player == null){
            player = new CloudNetV3OnlinePlayer(event.getNetworkConnectionInfo().getUniqueId(),event.getNetworkConnectionInfo().getName()
                    ,event.getNetworkServiceInfo().getServerName(),"Unknown");
            this.externalPlayers.put(player.getUUID(),player);
        }else player.setServer(event.getNetworkServiceInfo().getServerName());
        ProxyServer.getInstance().getPluginManager().callEvent(new ProxiedOnlineNetworkPlayerUpdateEvent(player.getUUID(),System.currentTimeMillis(),false));
    }
    @EventHandler
    public void onDisconnect(BungeeBridgeProxyPlayerDisconnectEvent event){
        this.externalPlayers.remove(event.getNetworkConnectionInfo().getUniqueId());
    }
    @EventHandler
    public void onMessageReceive(BungeeChannelMessageReceiveEvent event){
        if(event.getChannel().equalsIgnoreCase("DKBans")){
            if(event.getMessage().equalsIgnoreCase("updatePlayer") &&
                    !Wrapper.getInstance().getServiceId().getName().equalsIgnoreCase(event.getData().getString("sender"))){
                UUID uuid = event.getData().get("uuid",UUID.class);
                BanSystem.getInstance().getPlayerManager().removePlayerFromCache(uuid);
                BungeeCordBanSystemBootstrap.getInstance().executePlayerUpdateEvents(uuid
                        ,event.getData().get("cause", NetworkPlayerUpdateCause.class)
                        ,Document.loadData(event.getData().toJson()),false);
            }else if(event.getMessage().equalsIgnoreCase("sendMesssage")){
                ProxiedPlayer player =ProxyServer.getInstance().getPlayer(event.getData().get("uuid",UUID.class));
                if(player != null) player.sendMessage(event.getData().get("message", TextComponent.class));
            }else if(event.getMessage().equalsIgnoreCase("getPing")){
                UUID uuid = event.getData().get("uuid",UUID.class);
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
                if(player != null){
                    int ping = player.getPing();
                    Wrapper.getInstance().sendChannelMessage("DKBans","pingResult",new de.dytanic.cloudnet.common.document.Document()
                            .append("uuid",uuid).append("ping",ping));
                }
            }else if(event.getMessage().equalsIgnoreCase("pingResult")){
                UUID uuid = event.getData().get("uuid",UUID.class);
                CloudNetV3OnlinePlayer.PINGGETTER.put(uuid,event.getData().getInt("ping"));
            }else if(event.getMessage().equalsIgnoreCase("connect")){
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getData().get("uuid",UUID.class));
                if(player != null){
                    ServerInfo server = ProxyServer.getInstance().getServerInfo("server");
                    if(server != null && player.getServer().getInfo() != server) player.connect(server);
                }
            }else if(event.getMessage().equalsIgnoreCase("executeCommand")){
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getData().get("uuid",UUID.class));
                if(player != null) ProxyServer.getInstance().getPluginManager().dispatchCommand(player,event.getData().getString("command"));
            }else if(event.getMessage().equalsIgnoreCase("kick")){
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getData().get("uuid",UUID.class));
                if(player != null){
                    Kick kick = event.getData().get("kick", Kick.class);
                    player.disconnect(kick.toMessage());
                }
            }else if(event.getMessage().equalsIgnoreCase("warn")){
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getData().get("uuid",UUID.class));
                if(player != null){
                    Warn warn = event.getData().get("warn",Warn.class);
                    player.disconnect(warn.toMessage());
                }
            }else if(event.getMessage().equalsIgnoreCase("sendBan")){
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getData().get("uuid",UUID.class));
                if(player != null){
                    Ban ban = event.getData().get("ban", Ban.class);
                    if(ban.getBanType() == BanType.NETWORK) player.disconnect(ban.toMessage());
                    else player.sendMessage(ban.toMessage());
                }
            }else if(event.getMessage().equalsIgnoreCase("broadcast")){
                if(event.getData().contains("message")){
                    ProxyServer.getInstance().broadcast(event.getData().get("message",TextComponent.class));
                }else BanSystem.getInstance().getNetwork().broadcastLocal(event.getData().get("broadcast", Broadcast.class));
            }else if(event.getMessage().equalsIgnoreCase("sendJoinMe")){
                JoinMe joinme = event.getData().get("joinme", JoinMe.class);
                ((CloudNetV3Network)BanSystem.getInstance().getNetwork()).insertJoinMe(joinme);
                List<TextComponent> components = joinme.create();
                for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
                    for(TextComponent component : components) player.sendMessage(component);
                }
            }else if(event.getMessage().equalsIgnoreCase("sendTeamMessage")){
                for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
                    if(player.hasPermission("dkbans.team")){
                        NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
                        if(networkPlayer != null && (!event.getData().getBoolean("onlyLogin") || networkPlayer.isTeamChatLoggedIn()))
                            player.sendMessage(event.getData().get("message",TextComponent.class));
                    }
                }
            }else if(event.getMessage().equalsIgnoreCase("reloadFilter")){
                BanSystem.getInstance().getFilterManager().reloadLocal();
            }else if(event.getMessage().equalsIgnoreCase("reloadBroadcast")){
                BanSystem.getInstance().getBroadcastManager().reloadLocal();
            }else if(event.getMessage().equalsIgnoreCase("getOnlinePlayers")){
                List<BungeeCordPlayerManager.PlayerUpdateObject> players = new ArrayList<>();
                for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
                    ServerInfo server = player.getServer().getInfo();
                    players.add(new BungeeCordPlayerManager.PlayerUpdateObject(player.getUniqueId(),player.getName()
                            ,server==null?"Unknown":server.getName(),Wrapper.getInstance().getServiceId().getName()));
                }
                Wrapper.getInstance().sendChannelMessage("DKBans","syncOnlinePlayers"
                        ,new de.dytanic.cloudnet.common.document.Document().append("players",players));
            }else if(event.getMessage().equalsIgnoreCase("syncOnlinePlayers")){
                List<CloudNetV3OnlinePlayer> players = event.getData().get("players"
                        ,new TypeToken<List<CloudNetV3OnlinePlayer>>(){}.getType());
                for(CloudNetV3OnlinePlayer player : players) this.externalPlayers.put(player.getUUID(),player);
            }
        }
    }
}
