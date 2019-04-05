/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 05.04.19 22:47
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

package ch.dkrieger.bansystem.bungeecord;

import ch.dkrieger.bansystem.bungeecord.event.ProxiedDKBansSettingUpdateEvent;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.DKNetwork;
import ch.dkrieger.bansystem.lib.JoinMe;
import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;

public class BungeeCordNetwork implements DKNetwork {

    private Map<UUID,JoinMe> joinme;
    private SubServerConnection connection;

    public BungeeCordNetwork(SubServerConnection connection) {
        this.connection = connection;
        this.joinme = new LinkedHashMap<>();
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
        return this.joinme.get(uuid);
    }
    @Override
    public void broadcast(String message) {
        broadcast(new TextComponent(GeneralUtil.buildNextLineColor(ChatColor.translateAlternateColorCodes('&',message))));
    }
    @Override
    public void broadcast(TextComponent component) {
        ProxyServer.getInstance().broadcast(component);
    }

    @Override
    public void broadcast(Broadcast broadcast) {
        broadcastLocal(broadcast);
    }

    @Override
    public void broadcastLocal(Broadcast broadcast) {
        BungeeCordBanSystemBootstrap.getInstance().sendLocalBroadcast(broadcast);
    }

    @Override
    public void sendJoinMe(JoinMe joinMe) {
        this.joinme.put(joinMe.getUUID(),joinMe);
        List<TextComponent> components = joinMe.create();
        for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
            for(TextComponent component : components) player.sendMessage(component);
        }
    }
    @Override
    public void sendTeamMessage(String message) {
        sendTeamMessage(new TextComponent(message));
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
        for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
            if(player.hasPermission("dkbans.team")){//set right permission
                NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
                if(networkPlayer != null && (!onlyLogin || networkPlayer.isTeamChatLoggedIn())) player.sendMessage(component);
            }
        }
    }
    @Override
    public List<String> getPlayersOnServer(String server) {
        ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(server);
        List<String> players = new ArrayList<>();
        if(serverInfo != null) for(ProxiedPlayer player : serverInfo.getPlayers()) players.add(player.getName());
        return players;
    }

    @Override
    public List<String> getGroupServers(String group) {
        List<String> servers = new LinkedList<>();
        for(ServerInfo server : ProxyServer.getInstance().getServers().values()){
            int cid = 0, creplace = 0;
            for(char c : server.getName().toCharArray()){
                cid++;
                if(c == BanSystem.getInstance().getConfig().serverGroupSplit) creplace = cid-1;
            }
            if(group.equalsIgnoreCase(creplace>0?server.getName().substring(0,creplace):server.getName())) servers.add(server.getName());
        }
        return servers;
    }

    @Override
    public void reloadFilter() {
        BanSystem.getInstance().getFilterManager().reloadLocal();
        connection.sendToAll("reloadFilter",new Document());
    }
    @Override
    public void reloadBroadcast() {
        BanSystem.getInstance().getBroadcastManager().reloadLocal();
        connection.sendToAll("reloadBroadcast",new Document());
    }

    @Override
    public void syncSetting(String name) {
        ProxyServer.getInstance().getPluginManager().callEvent(new ProxiedDKBansSettingUpdateEvent(
                name,System.currentTimeMillis(),false));
        connection.sendToAll("syncSetting",new Document().append("name",name));
    }
}
