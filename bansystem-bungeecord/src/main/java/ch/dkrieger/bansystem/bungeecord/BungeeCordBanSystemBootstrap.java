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

package ch.dkrieger.bansystem.bungeecord;

import ch.dkrieger.bansystem.bungeecord.event.*;
import ch.dkrieger.bansystem.bungeecord.listener.PlayerListener;
import ch.dkrieger.bansystem.bungeecord.player.BungeeCordPlayerManager;
import ch.dkrieger.bansystem.bungeecord.player.cloudnet.CloudNetV2PlayerManager;
import ch.dkrieger.bansystem.bungeecord.player.cloudnet.CloudNetV3PlayerManager;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.DKBansPlatform;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.NetworkTaskManager;
import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.cloudnet.v2.CloudNetV2Network;
import ch.dkrieger.bansystem.lib.cloudnet.v3.CloudNetV3Network;
import ch.dkrieger.bansystem.lib.command.NetworkCommandManager;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.NetworkPlayerUpdateCause;
import ch.dkrieger.bansystem.lib.player.PlayerColor;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.report.Report;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import com.google.gson.reflect.TypeToken;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BungeeCordBanSystemBootstrap extends Plugin implements DKBansPlatform, NetworkTaskManager {

    private static BungeeCordBanSystemBootstrap instance;
    private BungeeCordCommandManager commandManager;
    private boolean cloudNetV2, cloudNetV3;

    private SubServerConnection subServerConnection;

    @Override
    public void onLoad() {
        instance = this;
        this.commandManager = new BungeeCordCommandManager();
        this.subServerConnection = new SubServerConnection();
        new BanSystem(this,new BungeeCordNetwork(this.subServerConnection),new BungeeCordPlayerManager(this.subServerConnection));
    }
    @Override
    public void onEnable() {
        ProxyServer.getInstance().getPluginManager().registerListener(this,new PlayerListener());
        ProxyServer.getInstance().getScheduler().schedule(this,()->{
            checkCloudNet();
            if(cloudNetV2){
                BanSystem.getInstance().setNetwork(new CloudNetV2Network() {
                    @Override
                    public void broadcastLocal(Broadcast broadcast) {sendLocalBroadcast(broadcast);}
                });
                BanSystem.getInstance().setPlayerManager(new CloudNetV2PlayerManager());
                ProxyServer.getInstance().getPluginManager().registerListener(this,(CloudNetV2PlayerManager)BanSystem.getInstance().getPlayerManager());
            }else if(cloudNetV3){
                BanSystem.getInstance().setNetwork(new CloudNetV3Network() {
                    @Override
                    public void broadcastLocal(Broadcast broadcast) {sendLocalBroadcast(broadcast);}
                });
                BanSystem.getInstance().setPlayerManager(new CloudNetV3PlayerManager());
                ProxyServer.getInstance().getPluginManager().registerListener(this,(CloudNetV3PlayerManager)BanSystem.getInstance().getPlayerManager());
            }else {
                ProxyServer.getInstance().getPluginManager().registerListener(this,this.subServerConnection);
                ProxyServer.getInstance().getPluginManager().registerListener(this,(BungeeCordPlayerManager)BanSystem.getInstance().getPlayerManager());
                this.subServerConnection.enable();
            }
        },1L, TimeUnit.SECONDS);
    }

    @Override
    public void onDisable() {
        BanSystem.getInstance().shutdown();
    }

    public SubServerConnection getSubServerConnection() {
        return subServerConnection;
    }

    @Override
    public String getPlatformName() {
        return "BungeeCord";
    }

    @Override
    public String getServerVersion() {
        return ProxyServer.getInstance().getVersion()+" | "+ProxyServer.getInstance().getGameVersion();
    }

    @Override
    public File getFolder() {
        return new File("plugins/DKBans/");
    }

    @Override
    public NetworkCommandManager getCommandManager() {
        return this.commandManager;
    }
    @Override
    public NetworkTaskManager getTaskManager() {
        return this;
    }

    @Override
    public String getColor(NetworkPlayer player) {
        ProxiedPlayer proxyPlayer = ProxyServer.getInstance().getPlayer(player.getUUID());
        String color = null;
        if(proxyPlayer != null){
            color = BanSystem.getInstance().getConfig().playerColorDefault;
            for(PlayerColor colors : BanSystem.getInstance().getConfig().playerColorColors){
                if(proxyPlayer.hasPermission(colors.getPermission())){
                    color = colors.getColor();
                    break;
                }
            }
        }
        ProxiedNetworkPlayerColorSetEvent event = new ProxiedNetworkPlayerColorSetEvent(player.getUUID(),System.currentTimeMillis(),true,player,color);
        ProxyServer.getInstance().getPluginManager().callEvent(event);
        if(event.getColor() != null) color = event.getColor();
        return color;
    }

    @Override
    public boolean checkPermissionInternally(NetworkPlayer player, String permission) {
        ProxiedPlayer proxyPlayer = ProxyServer.getInstance().getPlayer(player.getUUID());
        if(proxyPlayer != null) return proxyPlayer.hasPermission(permission);
        ProxiedNetworkPlayerOfflinePermissionCheckEvent event = new ProxiedNetworkPlayerOfflinePermissionCheckEvent(player.getUUID()
                ,System.currentTimeMillis(),true,player,permission);
        ProxyServer.getInstance().getPluginManager().callEvent(event);
        return event.hasPermission();
    }

    public void sendLocalBroadcast(Broadcast broadcast){
        if(broadcast == null) return;
        for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers()){
            if(broadcast.getPermission() == null || broadcast.getPermission().length() == 0|| player.hasPermission(broadcast.getPermission())){
                NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
                player.sendMessage(GeneralUtil.replaceTextComponent(Messages.BROADCAST_FORMAT_SEND.replace("[prefix]",Messages.PREFIX_NETWORK)
                        ,"[message]",broadcast.build(networkPlayer)));
            }
        }
    }

    public String getProxyName(){
        return "Proxy-1";
    }
    public Boolean isCloudNetV2(){
        return this.cloudNetV2;
    }
    public Boolean isCloudNetV3(){
        return this.cloudNetV3;
    }
    private void checkCloudNet(){
        Plugin plugin = ProxyServer.getInstance().getPluginManager().getPlugin("CloudNetAPI");
        if(plugin != null && plugin.getDescription() != null){
            this.cloudNetV2 = true;
            System.out.println(Messages.SYSTEM_PREFIX+"CloudNetV2 found");
            return;
        }else this.cloudNetV2 = false;
        plugin = ProxyServer.getInstance().getPluginManager().getPlugin("CloudNet-Bridge");
        if(plugin != null && plugin.getDescription() != null){
            this.cloudNetV3 = true;
            System.out.println(Messages.SYSTEM_PREFIX+"CloudNetV3 found");
            return;
        }else this.cloudNetV3 = false;
    }

    public void executePlayerUpdateEvents(UUID player, NetworkPlayerUpdateCause cause, Document properties, boolean onThisServer){
        if(cause == NetworkPlayerUpdateCause.LOGIN){
            ProxyServer.getInstance().getPluginManager().callEvent(new ProxiedNetworkPlayerLoginEvent(player,System.currentTimeMillis(),onThisServer));
        }else if(cause == NetworkPlayerUpdateCause.LOGOUT){
            List<Report> reports = properties.getObject("reports",new TypeToken<List<Report>>(){}.getType());
            List<UUID> sentStaffs = new ArrayList<>();
            GeneralUtil.iterateForEach(reports, object -> {
                ProxiedPlayer reporter = ProxyServer.getInstance().getPlayer(object.getReporterUUID());
                if(reporter != null) reporter.sendMessage(new TextComponent(Messages.REPORT_LEAVED_USER
                        .replace("[player]",object.getPlayer().getColoredName())
                        .replace("[prefix]",Messages.PREFIX_REPORT)));
                if(!sentStaffs.contains(object.getStaff())){
                    sentStaffs.add(object.getStaff());
                    ProxiedPlayer staff = ProxyServer.getInstance().getPlayer(object.getStaff());
                    if(staff != null){
                        staff.sendMessage(new TextComponent(Messages.REPORT_LEAVED_STAFF
                                .replace("[player]",object.getPlayer().getColoredName())
                                .replace("[prefix]",Messages.PREFIX_REPORT)));
                        reportExit(staff);
                    }
                }
            });
            ProxyServer.getInstance().getPluginManager().callEvent(new ProxiedNetworkPlayerLogoutEvent(player,System.currentTimeMillis(),onThisServer,reports));
        }else if(cause == NetworkPlayerUpdateCause.BAN){
            BanSystem.getInstance().getHistoryManager().clearCache();
            List<Report> reports = properties.getObject("reports",new TypeToken<List<Report>>(){}.getType());
            ProxyServer.getInstance().getPluginManager().callEvent(new ProxiedNetworkPlayerBanEvent(player
                    ,System.currentTimeMillis(),onThisServer,properties.getObject("ban", Ban.class)));
            if(reports.size() > 0){
                List<UUID> sentStaffs = new ArrayList<>();
                GeneralUtil.iterateForEach(reports, object -> {
                    ProxiedPlayer reporter = ProxyServer.getInstance().getPlayer(object.getReporterUUID());
                    if(reporter != null) reporter.sendMessage(new TextComponent(Messages.REPORT_ACCEPTED
                            .replace("[player]",object.getPlayer().getColoredName())
                            .replace("[prefix]",Messages.PREFIX_REPORT)));
                    if(!sentStaffs.contains(object.getStaff())){
                        sentStaffs.add(object.getStaff());
                        ProxiedPlayer staff = ProxyServer.getInstance().getPlayer(object.getStaff());
                        if(staff != null) reportExit(staff);
                    }
                });
                ProxyServer.getInstance().getPluginManager().callEvent(new ProxiedNetworkPlayerReportsAcceptEvent(player
                        ,System.currentTimeMillis(),onThisServer,reports));
            }
        }else if(cause == NetworkPlayerUpdateCause.KICK){
            ProxyServer.getInstance().getPluginManager().callEvent(new ProxiedNetworkPlayerKickEvent(player,System.currentTimeMillis(),onThisServer));
        }else if(cause == NetworkPlayerUpdateCause.WARN){
            ProxyServer.getInstance().getPluginManager().callEvent(new ProxiedNetworkPlayerWarnEvent(player,System.currentTimeMillis(),onThisServer));
        }else if(cause == NetworkPlayerUpdateCause.UNBAN){
            ProxyServer.getInstance().getPluginManager().callEvent(new ProxiedNetworkPlayerUnbanEvent(player,System.currentTimeMillis(),onThisServer));
        }else if(cause == NetworkPlayerUpdateCause.REPORTSEND){
            Report report = properties.getObject("report",Report.class);
            for(ProxiedPlayer players : ProxyServer.getInstance().getPlayers()){
                if(players.hasPermission("dkbans.report.receive")){
                    NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(players.getUniqueId());
                    if(networkPlayer != null && networkPlayer.isReportLoggedIn()) players.sendMessage(report.toMessage());
                }
            }
            ProxyServer.getInstance().getPluginManager().callEvent(new ProxiedNetworkPlayerReportEvent(player
                    ,System.currentTimeMillis(),onThisServer,report));
            BanSystem.getInstance().getReportManager().getReports().add(report);
        }else if(cause == NetworkPlayerUpdateCause.REPORTPROCESS){
            BanSystem.getInstance().getReportManager().clearCachedReports();
            ProxyServer.getInstance().getPluginManager().callEvent(new ProxiedNetworkPlayerReportsProcessEvent(player
                    ,System.currentTimeMillis(),onThisServer,properties.getObject("staff", UUID.class)));
        }else if(cause == NetworkPlayerUpdateCause.REPORTDENY){
            List<Report> reports = properties.getObject("reports",new TypeToken<List<Report>>(){}.getType());
            List<UUID> sentStaffs = new ArrayList<>();
            GeneralUtil.iterateForEach(reports, object -> {
                ProxiedPlayer reporter = ProxyServer.getInstance().getPlayer(object.getReporterUUID());
                if(reporter != null) reporter.sendMessage(new TextComponent(Messages.REPORT_DENIED_USER
                        .replace("[player]",object.getPlayer().getColoredName())
                        .replace("[prefix]",Messages.PREFIX_REPORT)));
                if(!sentStaffs.contains(object.getStaff())){
                    sentStaffs.add(object.getStaff());
                    ProxiedPlayer staff = ProxyServer.getInstance().getPlayer(object.getStaff());
                    if(staff != null){
                        reportExit(staff);
                    }
                }
            });
            BanSystem.getInstance().getReportManager().clearCachedReports();
            ProxyServer.getInstance().getPluginManager().callEvent(new ProxiedNetworkPlayerReportsDenyEvent(player
                    ,System.currentTimeMillis(),onThisServer,reports));
        }
        ProxyServer.getInstance().getPluginManager().callEvent(new ProxiedNetworkPlayerUpdateEvent(player,System.currentTimeMillis(),onThisServer,cause));
    }
    private void reportExit(ProxiedPlayer player){
        if(BanSystem.getInstance().getConfig().reportAutoCommandExecuteOnProxy){
            for(String command :  BanSystem.getInstance().getConfig().reportAutoCommandEnter) {
                ProxyServer.getInstance().getPluginManager().dispatchCommand(player,command);
            }
        }else{
            for(String command :  BanSystem.getInstance().getConfig().reportAutoCommandEnter){
                if(!command.startsWith("/")) command = "/"+command;
                player.chat(command);
            }
        }
    }

    @Override
    public void runTaskAsync(Runnable runnable) {
        ProxyServer.getInstance().getScheduler().runAsync(this,runnable);
    }

    @Override
    public void runTaskLater(Runnable runnable, Long delay, TimeUnit unit) {
        ProxyServer.getInstance().getScheduler().schedule(this,runnable,delay,unit);
    }

    @Override
    public void scheduleTask(Runnable runnable, Long repeat, TimeUnit unit) {
        ProxyServer.getInstance().getScheduler().schedule(this,runnable,repeat,repeat,unit);
    }


    public static BungeeCordBanSystemBootstrap getInstance() {
        return instance;
    }
}
