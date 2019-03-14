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

package ch.dkrieger.bansystem.bungeecord.player.cloudnet;

import ch.dkrieger.bansystem.bungeecord.BungeeCordBanSystemBootstrap;
import ch.dkrieger.bansystem.bungeecord.event.ProxiedDKBansSettingUpdateEvent;
import ch.dkrieger.bansystem.bungeecord.event.ProxiedOnlineNetworkPlayerUpdateEvent;
import ch.dkrieger.bansystem.bungeecord.player.LocalBungeeCordOnlinePlayer;
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
import ch.dkrieger.bansystem.lib.player.history.entry.Warn;
import ch.dkrieger.bansystem.lib.utils.Document;
import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.bridge.event.proxied.ProxiedPlayerUpdateEvent;
import de.dytanic.cloudnet.bridge.event.proxied.ProxiedSubChannelMessageEvent;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.*;

public class CloudNetV2PlayerManager extends PlayerManager implements Listener {

    private Map<ProxiedPlayer,LocalBungeeCordOnlinePlayer> localPlayers;
    private Map<UUID, CloudNetV2OnlinePlayer> externalPlayers;

    public CloudNetV2PlayerManager() {
        this.localPlayers = new HashMap<>();
        this.externalPlayers = new HashMap<>();
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
        if(player == null) return getOnlinePlayer(CloudAPI.getInstance().getOnlinePlayer(uuid));
        return player;
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(String name) {
        OnlineNetworkPlayer player = getOnlinePlayer(ProxyServer.getInstance().getPlayer(name));
        if(player == null){
            NetworkPlayer networkPlayer = getPlayer(name);
            if(networkPlayer == null) return null;
            return getOnlinePlayer(CloudAPI.getInstance().getOnlinePlayer(networkPlayer.getUUID()));
        }
        return player;
    }

    @Override
    public List<OnlineNetworkPlayer> getOnlinePlayers() {
        List<OnlineNetworkPlayer> players = new ArrayList<>();
        for(CloudPlayer player : CloudAPI.getInstance().getOnlinePlayers()) players.add(getOnlinePlayer(player));
        return players;
    }
    public OnlineNetworkPlayer getOnlinePlayer(CloudPlayer player){
        if(player != null){
            CloudNetV2OnlinePlayer online = this.externalPlayers.get(player.getUniqueId());
            if(online == null){
                online = new CloudNetV2OnlinePlayer(player);
                this.externalPlayers.put(player.getUniqueId(),online);
            }else online.setCloudPlayer(player);
            return online;
        }
        return null;
    }
    public OnlineNetworkPlayer getOnlinePlayer(ProxiedPlayer player){
        if(player != null){
            LocalBungeeCordOnlinePlayer online = this.localPlayers.get(player);
            if(online == null){
                online = new LocalBungeeCordOnlinePlayer(player);
                this.localPlayers.put(player,online);
            }
            return online;
        }
        return null;
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
        CloudAPI.getInstance().sendCustomSubProxyMessage("DKBans","updatePlayer",new de.dytanic.cloudnet.lib.utility.document.Document()
                .append("uuid",player.getUUID()).append("sender",CloudAPI.getInstance().getServerId()).append("cause",cause).append("properties",properties));
        CloudAPI.getInstance().sendCustomSubServerMessage("DKBans","updatePlayer",new de.dytanic.cloudnet.lib.utility.document.Document()
                .append("uuid",player.getUUID()).append("sender",CloudAPI.getInstance().getServerId()).append("cause",cause).append("properties",properties));
        BungeeCordBanSystemBootstrap.getInstance().executePlayerUpdateEvents(player.getUUID(),cause,properties,true);
    }

    @Override
    public void updateOnlinePlayer(OnlineNetworkPlayer player) {}

    @Override
    public int getOnlineCount() {
        return CloudAPI.getInstance().getOnlineCount();
    }
    @EventHandler
    public void onMessageReceive(ProxiedSubChannelMessageEvent event){
        if(event.getChannel().equalsIgnoreCase("DKBans")){
            if(event.getMessage().equalsIgnoreCase("updatePlayer") &&
                    !CloudAPI.getInstance().getServerId().equalsIgnoreCase(event.getDocument().getString("sender"))){
                UUID uuid = event.getDocument().getObject("uuid",UUID.class);
                BanSystem.getInstance().getPlayerManager().removePlayerFromCache(uuid);
                BungeeCordBanSystemBootstrap.getInstance().executePlayerUpdateEvents(uuid
                        ,event.getDocument().getObject("cause", NetworkPlayerUpdateCause.class)
                        ,event.getDocument().getObject("properties",Document.class),false);
            }else if(event.getMessage().equalsIgnoreCase("sendMesssage")){
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getDocument().getObject("uuid",UUID.class));
                if(player != null) player.sendMessage(event.getDocument().getObject("message", TextComponent.class));
            }else if(event.getMessage().equalsIgnoreCase("getPing")){
                UUID uuid = event.getDocument().getObject("uuid",UUID.class);
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
                if(player != null){
                    int ping = player.getPing();
                    CloudAPI.getInstance().sendCustomSubProxyMessage("DKBans","pingResult",new de.dytanic.cloudnet.lib.utility.document.Document()
                            .append("uuid",uuid).append("ping",ping));
                    CloudAPI.getInstance().sendCustomSubServerMessage("DKBans","pingResult",new de.dytanic.cloudnet.lib.utility.document.Document()
                            .append("uuid",uuid).append("ping",ping));
                }
            }else if(event.getMessage().equalsIgnoreCase("pingResult")){
                UUID uuid = event.getDocument().getObject("uuid",UUID.class);
                CloudNetV2OnlinePlayer.PINGGETTER.put(uuid,event.getDocument().getInt("ping"));
            }else if(event.getMessage().equalsIgnoreCase("connect")){
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getDocument().getObject("uuid",UUID.class));
                if(player != null){
                    ServerInfo server = ProxyServer.getInstance().getServerInfo("server");
                    if(server != null && player.getServer().getInfo() != server) player.connect(server);
                }
            }else if(event.getMessage().equalsIgnoreCase("executeCommand")){
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getDocument().getObject("uuid",UUID.class));
                if(player != null) ProxyServer.getInstance().getPluginManager().dispatchCommand(player,event.getDocument().getString("command"));
            }else if(event.getMessage().equalsIgnoreCase("kick")){
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getDocument().getObject("uuid",UUID.class));
                if(player != null){
                    Kick kick = event.getDocument().getObject("kick", Kick.class);
                    player.disconnect(kick.toMessage());
                }
            }else if(event.getMessage().equalsIgnoreCase("sendBan")){
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getDocument().getObject("uuid",UUID.class));
                if(player != null){
                    Ban ban = event.getDocument().getObject("ban", Ban.class);
                    if(ban.getBanType() == BanType.NETWORK) player.disconnect(ban.toMessage());
                    else player.sendMessage(ban.toMessage());
                }
            }else if(event.getMessage().equalsIgnoreCase("broadcast")){
                if(event.getDocument().contains("message")){
                    ProxyServer.getInstance().broadcast(event.getDocument().getObject("message",TextComponent.class));
                }else BanSystem.getInstance().getNetwork().broadcastLocal(event.getDocument().getObject("broadcast", Broadcast.class));
            }else if(event.getMessage().equalsIgnoreCase("sendJoinMe")){
                JoinMe joinme = event.getDocument().getObject("joinme", JoinMe.class);
                ((CloudNetV2Network)BanSystem.getInstance().getNetwork()).insertJoinMe(joinme);
                List<TextComponent> components = joinme.create();
                for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
                    for(TextComponent component : components) player.sendMessage(component);
                }
            }else if(event.getMessage().equalsIgnoreCase("sendTeamMessage")){
                for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
                    if(player.hasPermission("dkbans.team")){
                        NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
                        if(networkPlayer != null){
                            if(event.getDocument().getBoolean("onlyLogin") && !(networkPlayer.isTeamChatLoggedIn())) return;
                            player.sendMessage(event.getDocument().getObject("message",TextComponent.class));
                        }
                    }
                }
            }else if(event.getMessage().equalsIgnoreCase("reloadFilter")){
                BanSystem.getInstance().getFilterManager().reloadLocal();
            }else if(event.getMessage().equalsIgnoreCase("reloadBroadcast")){
                BanSystem.getInstance().getBroadcastManager().reloadLocal();
            }else if(event.getMessage().equalsIgnoreCase("warn")){
                ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getDocument().getObject("uuid",UUID.class));
                if(player != null){
                    Warn warn = event.getDocument().getObject("warn",Warn.class);
                    if(warn.isKick()) player.disconnect(warn.toKickMessage());
                    else player.sendMessage(warn.toChatMessage());
                }
            }else if(event.getMessage().equalsIgnoreCase("syncSetting")){
                BanSystem.getInstance().getSettingProvider().removeFromCache(event.getDocument().getString("name"));
                ProxyServer.getInstance().getPluginManager().callEvent(new ProxiedDKBansSettingUpdateEvent(event.getDocument().getString("name")
                        ,System.currentTimeMillis(),false));
            }
        }
    }
    @EventHandler
    public void onPlayerUpdate(ProxiedPlayerUpdateEvent event){
        ProxyServer.getInstance().getPluginManager().callEvent(new ProxiedOnlineNetworkPlayerUpdateEvent(event.getCloudPlayer().getUniqueId()
                ,System.currentTimeMillis(),false));
    }
}
