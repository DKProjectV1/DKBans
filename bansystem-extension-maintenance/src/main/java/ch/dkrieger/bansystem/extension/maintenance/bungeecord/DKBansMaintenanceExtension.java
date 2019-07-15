/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 15.07.19 11:31
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

package ch.dkrieger.bansystem.extension.maintenance.bungeecord;

import ch.dkrieger.bansystem.bungeecord.event.ProxiedDKBansSettingUpdateEvent;
import ch.dkrieger.bansystem.extension.maintenance.Maintenance;
import ch.dkrieger.bansystem.extension.maintenance.MaintenanceCommand;
import ch.dkrieger.bansystem.extension.maintenance.MaintenanceConfig;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.utils.Document;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PermissionCheckEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.score.Scoreboard;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DKBansMaintenanceExtension extends Plugin implements Listener {

    private Maintenance maintenance;
    private MaintenanceConfig config;
    private ServerPing pingResponse;

    @Override
    public void onEnable() {
        config = new MaintenanceConfig();
        Document data = BanSystem.getInstance().getSettingProvider().get("maintenance");
        if(data != null && data.contains("maintenance")) maintenance = data.getObject("maintenance",Maintenance.class);
        else maintenance = new Maintenance(false, System.currentTimeMillis(),"Dkrieger has hacked this server :)");
        buildResponse();
        ProxyServer.getInstance().getPluginManager().registerListener(this,this);
        BanSystem.getInstance().getCommandManager().registerCommand(new MaintenanceCommand(config,maintenance));
        ProxyServer.getInstance().getScheduler().schedule(this,this::buildResponse,0L,5L, TimeUnit.SECONDS);
    }
    @EventHandler(priority=90)
    public void onPing(ProxyPingEvent event){
        if(maintenance.isEnabled()) event.setResponse(pingResponse);
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(LoginEvent event){
        if(maintenance.isEnabled()){
            if(!maintenance.getWhitelist().contains(event.getConnection().getUniqueId())){
                PermissionCheckEvent checkEvent = ProxyServer.getInstance().getPluginManager().callEvent(new PermissionCheckEvent(
                        new ProxiedSimplePermissionCheckPlayer(event.getConnection()), "dkbans.maintenance.join", false));
                if(!checkEvent.hasPermission()){
                    event.setCancelled(true);
                    event.setCancelReason(new TextComponent(maintenance.replace(config.joinMessage)));
                }
            }
        }
    }
    @EventHandler
    public void onSettingUpdate(ProxiedDKBansSettingUpdateEvent event){
        if(event.getName().equalsIgnoreCase("maintenance")){
            this.maintenance = event.getSettings().getObject("maintenance",Maintenance.class);
            buildResponse();
            if(maintenance.isEnabled()){
                for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
                    if(!maintenance.getWhitelist().contains(player.getUniqueId()) && !player.hasPermission("dkbans.maintenance.join"))
                        player.disconnect(new TextComponent(maintenance.replace(config.joinMessage)));
                }
            }
        }
    }
    private void buildResponse() {
        if(this.pingResponse == null){
            this.pingResponse = new ServerPing();
            File serverIcon = new File("maintenance-server-icon.png");
            if(!serverIcon.exists()) serverIcon = new File("server-icon.png");
            if(serverIcon.exists()){
                try {
                    this.pingResponse.setFavicon(Favicon.create(ImageIO.read(serverIcon)));
                } catch (IOException exception) {
                    System.out.println("Could not load server-icon.png ("+exception.getMessage()+")");
                }
            }
        }
        pingResponse.setVersion(new ServerPing.Protocol(maintenance.replace(config.versionInfo),-1));
        pingResponse.setDescriptionComponent(new TextComponent(maintenance.replace(config.motdLine1)+"\n"+maintenance.replace(config.motdLine2)));

        List<ServerPing.PlayerInfo> playerInfo = new ArrayList<>();
        for(String info : config.playerInfo) playerInfo.add(new ServerPing.PlayerInfo(maintenance.replace(info),""));

        pingResponse.setPlayers(new ServerPing.Players(0,0,playerInfo.toArray(new ServerPing.PlayerInfo[0])));
    }
    private class ProxiedSimplePermissionCheckPlayer implements ProxiedPlayer{

        private PendingConnection connection;

        public ProxiedSimplePermissionCheckPlayer(PendingConnection connection) {
            this.connection = connection;
        }

        @Override
        public String getDisplayName() {
            return connection.getName();
        }
        @Override
        public void setDisplayName(String s) {}

        @Override
        public void sendMessage(ChatMessageType chatMessageType, BaseComponent... baseComponents) {}

        @Override
        public void sendMessage(ChatMessageType chatMessageType, BaseComponent baseComponent) {}

        @Override
        public void connect(ServerInfo serverInfo) {}

        @Override
        public void connect(ServerInfo serverInfo, ServerConnectEvent.Reason reason) {}

        @Override
        public void connect(ServerInfo serverInfo, Callback<Boolean> callback) {}

        @Override
        public void connect(ServerInfo serverInfo, Callback<Boolean> callback, ServerConnectEvent.Reason reason) { }

        @Override
        public void connect(ServerConnectRequest serverConnectRequest) { }

        @Override
        public Server getServer() {
            return null;
        }

        @Override
        public int getPing() {
            return -1;
        }

        @Override
        public void sendData(String s, byte[] bytes) {}

        @Override
        public PendingConnection getPendingConnection() {
            return connection;
        }

        @Override
        public void chat(String s) {}

        @Override
        public ServerInfo getReconnectServer() {
            return null;
        }

        @Override
        public void setReconnectServer(ServerInfo serverInfo) {}

        @Override
        public String getUUID() {
            return connection.getUUID();
        }

        @Override
        public UUID getUniqueId() {
            return connection.getUniqueId();
        }

        @Override
        public Locale getLocale() {
            return null;
        }

        @Override
        public byte getViewDistance() {
            return 0;
        }

        @Override
        public ChatMode getChatMode() {
            return null;
        }

        @Override
        public boolean hasChatColors() {
            return false;
        }

        @Override
        public SkinConfiguration getSkinParts() {
            return null;
        }

        @Override
        public MainHand getMainHand() {
            return null;
        }

        @Override
        public void setTabHeader(BaseComponent baseComponent, BaseComponent baseComponent1) {}

        @Override
        public void setTabHeader(BaseComponent[] baseComponents, BaseComponent[] baseComponents1) {}

        @Override
        public void resetTabHeader() {}

        @Override
        public void sendTitle(Title title) {}

        @Override
        public boolean isForgeUser() {
            return false;
        }

        @Override
        public Map<String, String> getModList() {
            return null;
        }

        @Override
        public Scoreboard getScoreboard() {
            return null;
        }

        @Override
        public String getName() {
            return connection.getName();
        }

        @Override
        public void sendMessage(String s) {}

        @Override
        public void sendMessages(String... strings) {}

        @Override
        public void sendMessage(BaseComponent... baseComponents) {}

        @Override
        public void sendMessage(BaseComponent baseComponent) {}

        @Override
        public Collection<String> getGroups() {
            return new ArrayList<>();
        }

        @Override
        public void addGroups(String... strings) {}

        @Override
        public void removeGroups(String... strings) {}

        @Override
        public boolean hasPermission(String s) {
            return false;
        }

        @Override
        public void setPermission(String s, boolean b) {}

        @Override
        public Collection<String> getPermissions() {
            return new ArrayList<>();
        }

        @Override
        public InetSocketAddress getAddress() {
            return connection.getAddress();
        }

        @Override
        public void disconnect(String s) {
            connection.disconnect(s);
        }

        @Override
        public void disconnect(BaseComponent... baseComponents) {
            connection.disconnect(baseComponents);
        }

        @Override
        public void disconnect(BaseComponent baseComponent) {
            connection.disconnect(baseComponent);
        }

        @Override
        public boolean isConnected() {
            return true;
        }
        @Override
        public Unsafe unsafe() {
            return connection.unsafe();
        }
    }
}
