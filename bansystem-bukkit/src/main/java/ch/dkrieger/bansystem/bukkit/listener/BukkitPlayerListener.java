/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 18.10.19, 21:00
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

package ch.dkrieger.bansystem.bukkit.listener;

import ch.dkrieger.bansystem.bukkit.BukkitBanSystemBootstrap;
import ch.dkrieger.bansystem.bukkit.event.BukkitNetworkPlayerRegisterLocalEvent;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.filter.FilterType;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.reason.BanReason;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BukkitPlayerListener implements Listener {

    private Map<UUID,lastMessage> lastMessage;
    private Map<UUID,Integer> banBlockPoints;
    private Map<UUID,Integer> currentMessageCount;

    public BukkitPlayerListener() {
        this.lastMessage = new HashMap<>();
        this.banBlockPoints = new HashMap<>();
        this.currentMessageCount = new HashMap<>();
    }
    @EventHandler
    public void onLogin(PlayerLoginEvent event){
        if(BanSystem.getInstance().getFilterManager().isBlocked(FilterType.NICKNAME,event.getPlayer().getName())){
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED,Messages.CHAT_FILTER_NICKNAME.replace("[prefix]",Messages.PREFIX_CHAT));
            return;
        }
        NetworkPlayer player = null;
        try{
            player = BanSystem.getInstance().getPlayerManager().getPlayerSave(event.getPlayer().getUniqueId());
        }catch (Exception exception){
            try{
                player = BanSystem.getInstance().getPlayerManager().getPlayerSave(event.getPlayer().getUniqueId());
            }catch (Exception exception2){
                event.disallow(PlayerLoginEvent.Result.KICK_BANNED,Messages.ERROR.replace("[prefix]",Messages.PREFIX_NETWORK));
                exception.printStackTrace();
                return;
            }
        }
        boolean newPlayer = false;
        if(player == null){
            player =  BanSystem.getInstance().getPlayerManager().createPlayer(event.getPlayer().getUniqueId()
                    ,event.getPlayer().getName(),event.getAddress().getHostAddress());
            newPlayer = true;
            Bukkit.getScheduler().runTaskAsynchronously(BukkitBanSystemBootstrap.getInstance(),()->{
                Bukkit.getPluginManager().callEvent(new BukkitNetworkPlayerRegisterLocalEvent(event.getPlayer().getUniqueId()
                        ,System.currentTimeMillis(),true));
            });
        }else{
            Ban ban = player.getBan(BanType.NETWORK);
            if(ban != null){
                event.disallow(PlayerLoginEvent.Result.KICK_BANNED,ban.toMessage().toLegacyText());
                return;
            }
        }
        if(BanSystem.getInstance().getConfig().ipBanBanOnlyNewPlayers && !(newPlayer)) return;
        if(BanSystem.getInstance().getPlayerManager().isIPBanned(event.getAddress().getHostAddress())){
            Ban ban = BanSystem.getInstance().getConfig().createAltAccountBan(player,event.getAddress().getHostAddress());
            player.ban(ban,true);
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED,ban.toMessage().toLegacyText());
            return;
        }
    }
    @EventHandler
    public void onPostLogin(PlayerJoinEvent event){
        BanSystem.getInstance().getTempSyncStats().addLogins();
        this.currentMessageCount.put(event.getPlayer().getUniqueId(),0);
        Bukkit.getScheduler().runTaskAsynchronously(BukkitBanSystemBootstrap.getInstance(),()->{
            if(BanSystem.getInstance().getConfig().onJoinChatClear) for(int i = 1; i < 120; i++) event.getPlayer().sendMessage("");
            NetworkPlayer player = BanSystem.getInstance().getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
            if(player == null) return;
            if(BanSystem.getInstance().getConfig().onJoinTeamChatInfo && event.getPlayer().hasPermission("dkbans.teamchat.receive")){
                event.getPlayer().sendMessage(Messages.STAFF_STATUS_NOW
                        .replace("[status]",(player.isTeamChatLoggedIn()?Messages.STAFF_STATUS_LOGIN:Messages.STAFF_STATUS_LOGOUT))
                        .replace("[prefix]",Messages.PREFIX_TEAMCHAT));
            }
            if(event.getPlayer().hasPermission("dkbans.report.receive")){
                if(BanSystem.getInstance().getConfig().onJoinReportInfo){
                    event.getPlayer().sendMessage(Messages.STAFF_STATUS_NOW
                            .replace("[status]",(player.isReportLoggedIn()?Messages.STAFF_STATUS_LOGIN:Messages.STAFF_STATUS_LOGOUT))
                            .replace("[prefix]",Messages.PREFIX_REPORT));
                }
                if(BanSystem.getInstance().getConfig().onJoinReportSize){
                    event.getPlayer().sendMessage(Messages.REPORT_INFO
                            .replace("[size]",""+BanSystem.getInstance().getReportManager().getOpenReports().size())
                            .replace("[prefix]",Messages.PREFIX_REPORT));
                }
            }//event.getPlayer().getLocale().getLanguage() ((CraftPlayer) player).getHandle().playerConnection.networkManager.getVersion();
            player.playerLogin(event.getPlayer().getName(),event.getPlayer().getAddress().getAddress().getHostAddress()
                    ,0,Messages.UNKNOWN,"Proxy-1"
                    ,BukkitBanSystemBootstrap.getInstance().getColor(player),event.getPlayer().hasPermission("dkbans.bypass"));
            if(event.getPlayer().hasPermission("dkbans.admin")) {
                if(BanSystem.getInstance().getUpdateChecker().hasNewVersion()) {
                    event.getPlayer().sendMessage(Messages.PREFIX_BAN + "New version available &e" + BanSystem.getInstance().getUpdateChecker().getLatestVersionString());
                }
            }
        });
    }
    @EventHandler
    public void onDisconnect(PlayerQuitEvent event){
        Bukkit.getScheduler().runTaskAsynchronously(BukkitBanSystemBootstrap.getInstance(),()->{
            NetworkPlayer player = BanSystem.getInstance().getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
            if(player == null) return;
            String server = Messages.UNKNOWN;
            World world = event.getPlayer().getWorld();
            if(world != null) server = world.getName();
            player.playerLogout(BukkitBanSystemBootstrap.getInstance().getColor(player),event.getPlayer().hasPermission("dkbans.bypass")
                    ,server,this.currentMessageCount.get(player.getUUID()));
        });
    }
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
        Ban ban = networkPlayer.getBan(BanType.CHAT);
        if(ban != null) {
            BukkitBanSystemBootstrap.getInstance().sendTextComponent(player, ban.toMessage());
            event.setCancelled(true);
        } else if(BanSystem.getInstance().getConfig().chatFirstJoinDelayEnabled) {
            int firstJoinDelay = BanSystem.getInstance().getConfig().chatFirstJoinDelay;
            if(TimeUnit.MILLISECONDS.toSeconds(networkPlayer.getFirstLogin())+firstJoinDelay > System.currentTimeMillis()) {
                event.setCancelled(true);
                player.sendMessage(Messages.CHAT_FIRST_JOIN_DELAY_CANCELLED
                        .replace("[prefix]", Messages.PREFIX_CHAT)
                        .replace("[player]", networkPlayer.getColoredName())
                        .replace("[delay]", String.valueOf(firstJoinDelay)));
            }
        }else {
            FilterType filter = null;
            if(!player.hasPermission("dkbans.bypass.chat")){
                if(BanSystem.getInstance().getConfig().chatBlockRepeat){
                    lastMessage lastMessage = this.lastMessage.get(player.getUniqueId());
                    if(lastMessage != null){
                        if(event.getMessage().equalsIgnoreCase(lastMessage.message)){
                            event.setCancelled(true);
                            player.sendMessage(Messages.CHAT_FILTER_SPAM_REPEAT.replace("[prefix]",Messages.PREFIX_CHAT));
                            return;
                        }else if(lastMessage.time+BanSystem.getInstance().getConfig().chatDelay >= System.currentTimeMillis()){
                            event.setCancelled(true);
                            player.sendMessage(Messages.CHAT_FILTER_SPAM_TOFAST.replace("[prefix]",Messages.PREFIX_CHAT));
                            return;
                        }else{
                            lastMessage.message = event.getMessage();
                            lastMessage.time = System.currentTimeMillis();
                        }
                    }else this.lastMessage.put(player.getUniqueId(),new lastMessage(event.getMessage(),System.currentTimeMillis()));
                }

                if(BanSystem.getInstance().getFilterManager().isBlocked(FilterType.MESSAGE,event.getMessage())){
                    filter = FilterType.MESSAGE;
                    event.setCancelled(true);
                    if(!checkBan(networkPlayer,BanSystem.getInstance().getConfig().chatFilterAutobanMessageBanID))
                        player.sendMessage(Messages.CHAT_FILTER_MESSAGE.replace("[prefix]",Messages.PREFIX_CHAT));
                }
                if(BanSystem.getInstance().getFilterManager().isBlocked(FilterType.PROMOTION,event.getMessage())){
                    filter = FilterType.PROMOTION;
                    event.setCancelled(true);
                    if(!checkBan(networkPlayer,BanSystem.getInstance().getConfig().chatFilterAutobanPromotionBanID))
                        player.sendMessage(Messages.CHAT_FILTER_PROMOTION.replace("[prefix]",Messages.PREFIX_CHAT));
                }
            }
            if(!event.isCancelled()){
                if(!this.currentMessageCount.containsKey(player.getUniqueId())) this.currentMessageCount.put(player.getUniqueId(),1);
                else this.currentMessageCount.put(player.getUniqueId(),this.currentMessageCount.get(player.getUniqueId())+1);
            }
            if(BanSystem.getInstance().getConfig().chatlogEnabled){
                final FilterType finalFilter = filter;
                Bukkit.getScheduler().runTaskAsynchronously(BukkitBanSystemBootstrap.getInstance(),()->{
                    BanSystem.getInstance().getPlayerManager().createChatLogEntry(player.getUniqueId(),event.getMessage()
                            ,player.getWorld().getName(),finalFilter);
                });
            }
        }
    }
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){
        event.setMessage(event.getMessage().replace("bungeecord:","").replace("minecraft:","").replace("bungeecord:",""));
        if(BanSystem.getInstance().getConfig().chatBlockPlugin && (event.getMessage().startsWith("/pl ")
                || event.getMessage().startsWith("/plugins ")
                || event.getMessage().startsWith("/plugin ")
                || event.getMessage().equalsIgnoreCase("/pl")
                || event.getMessage().equalsIgnoreCase("/plugin")
                || event.getMessage().equalsIgnoreCase("/plugins"))){
            if(!(event.getPlayer().hasPermission("dkbans.bypass.plugin"))){
                event.setCancelled(true);
                event.getPlayer().sendMessage(Messages.CHAT_PLUGIN.replace("[prefix]",Messages.PREFIX_NETWORK));
            }
            return;
        }
        if(!event.getPlayer().hasPermission("dkbans.bypass.chat") && BanSystem.getInstance().getFilterManager().isBlocked(FilterType.COMMAND,event.getMessage())){
            event.getPlayer().sendMessage(Messages.CHAT_FILTER_COMMAND.replace("[prefix]",Messages.PREFIX_CHAT));
            event.setCancelled(true);
            return;
        }
        NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
        Ban ban = networkPlayer.getBan(BanType.CHAT);
        if(ban != null && BanSystem.getInstance().getFilterManager().isBlocked(FilterType.MUTECOMMAND,event.getMessage())){
            BukkitBanSystemBootstrap.getInstance().sendTextComponent(event.getPlayer(),ban.toMessage());
            event.setCancelled(true);
        }
    }
    private boolean checkBan(NetworkPlayer player, int reasonID){
        if(this.banBlockPoints.containsKey(player.getUUID())){
            int bans = this.banBlockPoints.get(player.getUUID());
            if(bans >= BanSystem.getInstance().getConfig().chatFilterAutobanCount){
                this.banBlockPoints.remove(player.getUUID());
                BanReason reason = BanSystem.getInstance().getReasonProvider().getBanReason(reasonID);
                if(reason != null) player.ban(reason,"","ChatFilter");
                return reason!=null;
            }else this.banBlockPoints.put(player.getUUID(),bans+1);
        }else this.banBlockPoints.put(player.getUUID(),1);
        return false;
    }
    @EventHandler
    public void onTabComplete(PlayerChatTabCompleteEvent event){
        if(event.getChatMessage().startsWith("/") && BanSystem.getInstance().getConfig().tabCompleteBlockEnabled){
            String search = event.getChatMessage();
            if(search.contains(" ")) search = search.split(" ")[0].replace("/","");
            final String finalSearch = search;
            event.getTabCompletions().clear();
            GeneralUtil.iterateAcceptedForEach(BanSystem.getInstance().getConfig().tabCompleteOptions
                    ,option -> (option.getPermission() == null || event.getPlayer().hasPermission(option.getPermission())) && finalSearch.replace("/","").startsWith(option.getOption())
                    ,option -> event.getTabCompletions().add(option.getOption()));
        }
    }
    private class lastMessage {

        private String message;
        private long time;

        public lastMessage(String message, long time) {
            this.message = message;
            this.time = time;
        }
    }
}
