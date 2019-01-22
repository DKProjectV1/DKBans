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

package ch.dkrieger.bansystem.bukkit;

import ch.dkrieger.bansystem.bukkit.event.*;
import ch.dkrieger.bansystem.bukkit.hook.PlaceHolderApiHook;
import ch.dkrieger.bansystem.bukkit.listener.BukkitPlayerListener;
import ch.dkrieger.bansystem.bukkit.network.BukkitBungeeCordNetwork;
import ch.dkrieger.bansystem.bukkit.network.BukkitNetwork;
import ch.dkrieger.bansystem.bukkit.player.bukkit.BukkitPlayerManager;
import ch.dkrieger.bansystem.bukkit.player.bungeecord.BukkitBungeeCordPlayerManager;
import ch.dkrieger.bansystem.bukkit.player.cloudnet.CloudNetV2PlayerManager;
import ch.dkrieger.bansystem.bukkit.player.cloudnet.CloudNetV3PlayerManager;
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
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static ch.dkrieger.bansystem.bukkit.utils.Reflection.*;

public class BukkitBanSystemBootstrap extends JavaPlugin implements DKBansPlatform, NetworkTaskManager {

    private static BukkitBanSystemBootstrap instance;
    private BukkitCommandManager commandManager;
    private List<WaitingRunnable> waitingRunnables;
    private BungeeCordConnection bungeeCordConnection;
    private boolean cloudNetV2, cloudNetV3;

    @Override
    public void onLoad() {
        instance = this;
        this.commandManager = new BukkitCommandManager();
        this.waitingRunnables = new LinkedList<>();
        this.bungeeCordConnection = new BungeeCordConnection();

        new BanSystem(this,new BukkitNetwork(),new BukkitPlayerManager());
    }

    @Override
    public void onDisable() {
        BanSystem.getInstance().shutdown();
    }

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this,()->{
            checkCloudNet();
            if(cloudNetV2){
                BanSystem.getInstance().setNetwork(new CloudNetV2Network() {
                    @Override
                    public void broadcastLocal(Broadcast broadcast) {sendLocalBroadcast(broadcast);}
                });
                BanSystem.getInstance().setPlayerManager(new CloudNetV2PlayerManager());
                Bukkit.getPluginManager().registerEvents((CloudNetV2PlayerManager) BanSystem.getInstance().getPlayerManager(),this);
            }else if(cloudNetV3){
                BanSystem.getInstance().setNetwork((new CloudNetV3Network() {
                    @Override
                    public void broadcastLocal(Broadcast broadcast) {sendLocalBroadcast(broadcast);}
                }));
                BanSystem.getInstance().setPlayerManager(new CloudNetV3PlayerManager());
                Bukkit.getPluginManager().registerEvents((CloudNetV3PlayerManager) BanSystem.getInstance().getPlayerManager(),this);
            }else if(BanSystem.getInstance().getConfig().bungeecord){
                this.bungeeCordConnection.enable();
                BanSystem.getInstance().setPlayerManager(new BukkitBungeeCordPlayerManager(this.bungeeCordConnection));
                BanSystem.getInstance().setNetwork(new BukkitBungeeCordNetwork(this.bungeeCordConnection));
            }else{
                Bukkit.getPluginManager().registerEvents(new BukkitPlayerListener(),this);
            }

            Plugin placeHolderAPI = getServer().getPluginManager().getPlugin("PlaceholderAPI");
            if(placeHolderAPI != null && placeHolderAPI.getDescription() != null) new PlaceHolderApiHook().hook();

            for(WaitingRunnable runnable : this.waitingRunnables){
                if(runnable.type == WaitingRunnableType.ASYNC){
                    Bukkit.getScheduler().runTaskAsynchronously(this,runnable.runnable);
                }else if(runnable.type == WaitingRunnableType.LATER){
                    Bukkit.getScheduler().runTaskLater(this,runnable.runnable,runnable.ticks);
                }else if(runnable.type == WaitingRunnableType.SCHEDULE){
                    Bukkit.getScheduler().runTaskTimer(this,runnable.runnable,0L,runnable.ticks);
                }
            }
        },12);
    }

    public boolean isCloudNetV2() {
        return cloudNetV2;
    }

    public boolean isCloudNetV3() {
        return cloudNetV3;
    }

    public BungeeCordConnection getBungeeCordConnection() {
        return bungeeCordConnection;
    }

    public String getPlatformName() {
        return "bukkit";
    }

    public String getServerVersion() {
        return Bukkit.getBukkitVersion()+" | "+Bukkit.getVersion();
    }

    public File getFolder() {
        return new File("plugins/DKBans/");
    }

    public NetworkCommandManager getCommandManager() {
        return commandManager;
    }

    public NetworkTaskManager getTaskManager() {
        return this;
    }

    public String getColor(NetworkPlayer player) {
        Player bukkitPlayer =  Bukkit.getPlayer(player.getUUID());
        String color = null;
        if(bukkitPlayer != null){
            color = BanSystem.getInstance().getConfig().playerColorDefault;
            for(PlayerColor colors : BanSystem.getInstance().getConfig().playerColorColors){
                if(bukkitPlayer.hasPermission(colors.getPermission())){
                    color = colors.getColor();
                    break;
                }
            }
        }
        BukkitNetworkPlayerColorSetEvent event = new BukkitNetworkPlayerColorSetEvent(null,System.currentTimeMillis(),true,player,color);
        Bukkit.getPluginManager().callEvent(event);
        if(event.getColor() != null) color = event.getColor();
        return color;
    }

    @Override
    public boolean checkPermissionInternally(NetworkPlayer player, String permission) {
        Player bukkitPlayer = Bukkit.getPlayer(player.getUUID());
        if(bukkitPlayer != null) return bukkitPlayer.hasPermission(permission);
        BukkitNetworkPlayerOfflinePermissionCheckEvent event = new BukkitNetworkPlayerOfflinePermissionCheckEvent(player.getUUID()
                ,System.currentTimeMillis(),true,player,permission);
        Bukkit.getPluginManager().callEvent(event);
        return event.hasPermission();
    }

    public void runTaskAsync(Runnable runnable) {
        if(isEnabled()) Bukkit.getScheduler().runTaskAsynchronously(this,runnable);
        else this.waitingRunnables.add(new WaitingRunnable(runnable,0L,WaitingRunnableType.ASYNC));
    }

    public void runTaskLater(Runnable runnable, Long delay, TimeUnit unit) {
       if(isEnabled()) Bukkit.getScheduler().runTaskLater(this,runnable,unit.toSeconds(delay)*20);
       else this.waitingRunnables.add(new WaitingRunnable(runnable,unit.toSeconds(delay)*20,WaitingRunnableType.LATER));
    }

    public void scheduleTask(Runnable runnable, Long repeat, TimeUnit unit) {
        if(isEnabled()) Bukkit.getScheduler().runTaskTimerAsynchronously(this,runnable,unit.toSeconds(repeat)*20,unit.toSeconds(repeat)*20);
        else this.waitingRunnables.add(new WaitingRunnable(runnable,unit.toSeconds(repeat)*20,WaitingRunnableType.SCHEDULE));
    }
    private void checkCloudNet(){
        Plugin plugin = getServer().getPluginManager().getPlugin("CloudNetAPI");
        if(plugin != null && plugin.getDescription() != null){
            this.cloudNetV2 = true;
            System.out.println(Messages.SYSTEM_PREFIX+"CloudNetV2 found");
            return;
        }
        this.cloudNetV2 = false;
        plugin = getServer().getPluginManager().getPlugin("CloudNet-Bridge");
        if(plugin != null && plugin.getDescription() != null){
            this.cloudNetV3 = true;
            System.out.println(Messages.SYSTEM_PREFIX+"CloudNetV3 found");
            return;
        }
        this.cloudNetV3 = false;
    }
    public void sendLocalBroadcast(Broadcast broadcast){
        if(broadcast == null) return;
        for(Player player : Bukkit.getOnlinePlayers()){
            if(broadcast.getPermission() == null || broadcast.getPermission().length() == 0|| player.hasPermission(broadcast.getPermission())) {
                NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
                BukkitBanSystemBootstrap.getInstance().sendTextComponent(player, GeneralUtil.replaceTextComponent(Messages.BROADCAST_FORMAT_SEND.replace("[prefix]",Messages.PREFIX_NETWORK)
                        ,"[message]",broadcast.build(networkPlayer)));
            }
        }
    }

    public void sendTextComponent(Player player, TextComponent component){
        try{
            Class<?> IChatBaseComponent = getMinecraftClass("IChatBaseComponent");
            Class<?> ChatSerializer = null;
            if(getVersion().equalsIgnoreCase("v1_8_R1")) ChatSerializer = getMinecraftClass("ChatSerializer");
            else ChatSerializer = getMinecraftClass(IChatBaseComponent,"ChatSerializer");
            Method a = ChatSerializer.getMethod("a", String.class);
            Object componentO = a.invoke(null, ComponentSerializer.toString(component));
            Constructor< ? > constructor = reflectNMSClazz("PacketPlayOutChat").getConstructor();
            Object packet = constructor.newInstance();
            setField(packet,"a",componentO);
            if(getField(packet.getClass(),"b").getType() == Byte.TYPE){
                setField(packet,"b",(byte)0);
            }else{
                Method typeA = getMinecraftClass("ChatMessageType").getMethod("a", byte.class);
                Object type = typeA.invoke(null, (byte)0);
                setField(packet,"b",type);
            }
            sendPacket(player,packet);
        }catch (Exception exception){
            player.sendMessage(component.toLegacyText());
        }
    }
    public void executePlayerUpdateEvents(UUID player, NetworkPlayerUpdateCause cause, Document properties, boolean onThisServer){
        if(cause == NetworkPlayerUpdateCause.LOGIN){
            Bukkit.getPluginManager().callEvent(new BukkitNetworkPlayerLoginEvent(player,System.currentTimeMillis(),onThisServer));
        }else if(cause == NetworkPlayerUpdateCause.LOGOUT){
            List<Report> reports = properties.getObject("reports",new TypeToken<List<Report>>(){}.getType());
            List<UUID> sentStaffs = new ArrayList<>();
            if(!BanSystem.getInstance().getConfig().bungeecord){
                GeneralUtil.iterateForEach(reports, object -> {
                    Player reporter = Bukkit.getPlayer(object.getReporterUUID());
                    if(reporter != null) sendTextComponent(reporter,new TextComponent(Messages.REPORT_LEAVED_USER
                            .replace("[player]",object.getPlayer().getColoredName())
                            .replace("[prefix]",Messages.PREFIX_REPORT)));
                    if(!sentStaffs.contains(object.getStaff())){
                        sentStaffs.add(object.getStaff());
                        Player staff = Bukkit.getPlayer(object.getStaff());
                        if(staff != null){
                            sendTextComponent(staff,new TextComponent(Messages.REPORT_LEAVED_STAFF
                                    .replace("[player]",object.getPlayer().getColoredName())
                                    .replace("[prefix]",Messages.PREFIX_REPORT)));
                            reportExit(staff);
                        }
                    }
                });
            }
            Bukkit.getPluginManager().callEvent(new BukkitNetworkPlayerLogoutEvent(player,System.currentTimeMillis(),onThisServer,reports));
        }else if(cause == NetworkPlayerUpdateCause.BAN){
            BanSystem.getInstance().getHistoryManager().clearCache();
            List<Report> reports = properties.getObject("reports",new TypeToken<List<Report>>(){}.getType());
            Bukkit.getPluginManager().callEvent(new BukkitNetworkPlayerBanEvent(player
                    ,System.currentTimeMillis(),onThisServer,properties.getObject("ban", Ban.class)));
            if(!BanSystem.getInstance().getConfig().bungeecord){
                if(reports.size() > 0){
                    List<UUID> sentStaffs = new ArrayList<>();
                    GeneralUtil.iterateForEach(reports, object -> {
                        Player reporter = Bukkit.getPlayer(object.getReporterUUID());
                        if(reporter != null) sendTextComponent(reporter,new TextComponent(Messages.REPORT_ACCEPTED
                                .replace("[player]",object.getPlayer().getColoredName())
                                .replace("[prefix]",Messages.PREFIX_REPORT)));
                        if(!sentStaffs.contains(object.getStaff())){
                            sentStaffs.add(object.getStaff());
                            Player staff = Bukkit.getPlayer(object.getStaff());
                            if(staff != null) reportExit(staff);
                        }
                    });
                    Bukkit.getPluginManager().callEvent(new BukkitNetworkPlayerReportsAcceptEvent(player
                            ,System.currentTimeMillis(),onThisServer,reports));
                }
            }
        }else if(cause == NetworkPlayerUpdateCause.KICK){
            Bukkit.getPluginManager().callEvent(new BukkitNetworkPlayerKickEvent(player,System.currentTimeMillis(),onThisServer));
        }else if(cause == NetworkPlayerUpdateCause.WARN){
            Bukkit.getPluginManager().callEvent(new BukkitNetworkPlayerWarnEvent(player,System.currentTimeMillis(),onThisServer));
        }else if(cause == NetworkPlayerUpdateCause.UNBAN){
            Bukkit.getPluginManager().callEvent(new BukkitNetworkPlayerUnbanEvent(player,System.currentTimeMillis(),onThisServer));
        }else if(cause == NetworkPlayerUpdateCause.REPORTSEND){
            Report report = properties.getObject("report",Report.class);
            if(!BanSystem.getInstance().getConfig().bungeecord){
                for(Player players : Bukkit.getOnlinePlayers()){
                    if(players.hasPermission("dkbans.report.receive")){
                        NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(players.getUniqueId());
                        if(networkPlayer != null && networkPlayer.isReportLoggedIn()) sendTextComponent(players,report.toMessage());
                    }
                }
            }
            Bukkit.getPluginManager().callEvent(new BukkitNetworkPlayerReportEvent(player
                    ,System.currentTimeMillis(),onThisServer,report));
            BanSystem.getInstance().getReportManager().getReports().add(report);
        }else if(cause == NetworkPlayerUpdateCause.REPORTPROCESS){
            BanSystem.getInstance().getReportManager().clearCachedReports();
            Bukkit.getPluginManager().callEvent(new BukkitNetworkPlayerReportsProcessEvent(player
                    ,System.currentTimeMillis(),onThisServer,properties.getObject("staff", UUID.class)));
        }else if(cause == NetworkPlayerUpdateCause.REPORTDENY){
            List<Report> reports = properties.getObject("reports",new TypeToken<List<Report>>(){}.getType());
            List<UUID> sentStaffs = new ArrayList<>();
            if(!BanSystem.getInstance().getConfig().bungeecord){
                GeneralUtil.iterateForEach(reports, object -> {
                    Player reporter = Bukkit.getPlayer(object.getReporterUUID());
                    if(reporter != null) sendTextComponent(reporter,new TextComponent(Messages.REPORT_DENIED_USER
                            .replace("[player]",object.getPlayer().getColoredName())
                            .replace("[prefix]",Messages.PREFIX_REPORT)));
                    if(!sentStaffs.contains(object.getStaff())){
                        sentStaffs.add(object.getStaff());
                        Player staff = Bukkit.getPlayer(object.getStaff());
                        if(staff != null){
                            reportExit(staff);
                        }
                    }
                });
            }
            BanSystem.getInstance().getReportManager().clearCachedReports();
            Bukkit.getPluginManager().callEvent(new BukkitNetworkPlayerReportsDenyEvent(player
                    ,System.currentTimeMillis(),onThisServer,reports));
        }else if(cause == NetworkPlayerUpdateCause.HISTORYUPDATE) {
            BanSystem.getInstance().getHistoryManager().clearCache();
            Bukkit.getPluginManager().callEvent(new BukkitNetworkPlayerHistoryUpdateEvent(player, System.currentTimeMillis(), onThisServer));
        }
        Bukkit.getPluginManager().callEvent(new BukkitNetworkPlayerUpdateEvent(player,System.currentTimeMillis(),onThisServer,cause));
    }
    private void reportExit(Player player){
        for(String command :  BanSystem.getInstance().getConfig().reportAutoCommandEnter) Bukkit.dispatchCommand(player,command);
        BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId()).setWatchingReportedPlayer(null);
    }
    
    public static BukkitBanSystemBootstrap getInstance() {
        return instance;
    }
    private class WaitingRunnable {

        private Runnable runnable;
        private Long ticks;
        private WaitingRunnableType type;

        public WaitingRunnable(Runnable runnable, Long ticks, WaitingRunnableType type) {
            this.runnable = runnable;
            this.ticks = ticks;
            this.type = type;
        }
    }
    private enum WaitingRunnableType {
        ASYNC(),
        LATER(),
        SCHEDULE();
    }
}
