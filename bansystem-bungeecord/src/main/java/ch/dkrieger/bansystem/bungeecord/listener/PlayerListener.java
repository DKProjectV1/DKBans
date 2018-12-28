package ch.dkrieger.bansystem.bungeecord.listener;

import ch.dkrieger.bansystem.bungeecord.BungeeCordBanSystemBootstrap;
import ch.dkrieger.bansystem.bungeecord.CloudNetExtension;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.filter.FilterType;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.reason.BanReason;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerListener implements Listener {

    private Map<UUID,lastMessage> lastMessage;
    private Map<UUID,Integer> banPoints;
    private Map<UUID,Integer> currentMessageCount;

    public PlayerListener() {
        this.lastMessage = new HashMap<>();
        this.banPoints = new HashMap<>();
        this.currentMessageCount = new HashMap<>();
    }

    @EventHandler
    public void onLogin(LoginEvent event){
        if(BungeeCordBanSystemBootstrap.getInstance().isCloudNetV2()) CloudNetExtension.loginFix(event.getConnection().getUniqueId());
        if(BungeeCord.getInstance().getConfig().isOnlineMode() && !(event.getConnection().isOnlineMode())) return;
        if(BanSystem.getInstance().getFilterManager().isBlocked(FilterType.NICKNAME,event.getConnection().getName())){
            event.setCancelled(true);
            event.setCancelReason(new TextComponent(Messages.CHAT_FILTER_NICKNAME.replace("[prefix]",Messages.PREFIX_CHAT)));
            return;
        }
        NetworkPlayer player = null;
        try{
            player = BanSystem.getInstance().getPlayerManager().getPlayerSave(event.getConnection().getUniqueId());
        }catch (Exception exception){
            try{
                player = BanSystem.getInstance().getPlayerManager().getPlayerSave(event.getConnection().getUniqueId());
            }catch (Exception exception2){
                event.setCancelled(true);
                event.setCancelReason(new TextComponent(Messages.ERROR.replace("[prefix]",Messages.PREFIX_NETWORK)));
                exception.printStackTrace();
                return;
            }
        }
        if(player == null){
            player =  BanSystem.getInstance().getPlayerManager().createPlayer(event.getConnection().getUniqueId()
                    ,event.getConnection().getName(),event.getConnection().getAddress().getAddress().getHostAddress());
        }else{
            Ban ban = player.getBan(BanType.NETWORK);
            if(ban != null){
                event.setCancelled(true);
                event.setCancelReason(ban.toMessage());
            }
        }
    }
    @EventHandler
    public void onPostLogin(PostLoginEvent event){
        BanSystem.getInstance().getTempSyncStats().addLogins();
        this.currentMessageCount.put(event.getPlayer().getUniqueId(),0);
        BungeeCord.getInstance().getScheduler().runAsync(BungeeCordBanSystemBootstrap.getInstance(),()->{
            if(BanSystem.getInstance().getConfig().onJoinChatClear) for(int i = 1; i < 120; i++) event.getPlayer().sendMessage(new TextComponent(""));
            NetworkPlayer player = BanSystem.getInstance().getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
            if(player == null) return;
            if(BanSystem.getInstance().getConfig().onJoinTeamChatInfo && event.getPlayer().hasPermission("dkbans.teamchat.receive")){
                event.getPlayer().sendMessage(new TextComponent(Messages.STAFF_STATUS_NOW
                        .replace("[status]",(player.isTeamChatLoggedIn()?Messages.STAFF_STATUS_LOGIN:Messages.STAFF_STATUS_LOGOUT))
                        .replace("[prefix]",Messages.PREFIX_TEAMCHAT)));
            }
            if(event.getPlayer().hasPermission("dkbans.report.receive")){
                if(BanSystem.getInstance().getConfig().onJoinReportInfo){
                    event.getPlayer().sendMessage(new TextComponent(Messages.STAFF_STATUS_NOW
                            .replace("[status]",(player.isReportLoggedIn()?Messages.STAFF_STATUS_LOGIN:Messages.STAFF_STATUS_LOGOUT))
                            .replace("[prefix]",Messages.PREFIX_REPORT)));
                }
                if(BanSystem.getInstance().getConfig().onJoinReportSize){
                    TextComponent component = new TextComponent(Messages.REPORT_INFO
                            .replace("[size]",""+BanSystem.getInstance().getReportManager().getOpenReports().size())
                            .replace("[prefix]",Messages.PREFIX_REPORT));
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/reports"));
                    event.getPlayer().sendMessage(component);
                }
            }//event.getPlayer().getLocale().getLanguage()
            player.playerLogin(event.getPlayer().getName(),event.getPlayer().getAddress().getAddress().getHostAddress()
                    ,event.getPlayer().getPendingConnection().getVersion(),"Unknown",BungeeCordBanSystemBootstrap.getInstance().getProxyName()
                    ,BungeeCordBanSystemBootstrap.getInstance().getColor(player),event.getPlayer().hasPermission("dkbans.bypass"));
        });
    }
    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event){
        BungeeCord.getInstance().getScheduler().runAsync(BungeeCordBanSystemBootstrap.getInstance(),()->{
            NetworkPlayer player = BanSystem.getInstance().getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
            if(player == null) return;
            String server = "Unknown";
            Server serverInfo = event.getPlayer().getServer();
            if(serverInfo != null) server = serverInfo.getInfo().getName();
            player.playerLogout(BungeeCordBanSystemBootstrap.getInstance().getColor(player),event.getPlayer().hasPermission("dkbans.bypass")
            ,server,this.currentMessageCount.get(player.getUUID()));
        });
    }
    @EventHandler
    public void onChat(ChatEvent event){
        if(!(event.getSender() instanceof ProxiedPlayer)) return;
        ProxiedPlayer player = (ProxiedPlayer)event.getSender();
        NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
        if(event.isCommand()){
            event.setMessage(event.getMessage().replace("bungeecord:","").replace("minecraft:","").replace("bungeecord:",""));
            if(BanSystem.getInstance().getConfig().chatBlockPlugin){
                if(event.getMessage().startsWith("/pl ")
                        || event.getMessage().startsWith("/plugins ")
                        || event.getMessage().startsWith("/plugin ")
                        || event.getMessage().equalsIgnoreCase("/pl") ||
                        event.getMessage().equalsIgnoreCase("/plugin")
                        || event.getMessage().equalsIgnoreCase("/plugins")){
                    if(!(player.hasPermission("dkbans.bypass.plugin"))){
                        event.setCancelled(true);
                        player.sendMessage(new TextComponent(Messages.CHAT_PLUGIN.replace("[prefix]",Messages.PREFIX_NETWORK)));
                    }
                    return;
                }
            }
            if(!player.hasPermission("dkbans.bypass.chat") && BanSystem.getInstance().getFilterManager().isBlocked(FilterType.COMMAND,event.getMessage())){
                player.sendMessage(new TextComponent(Messages.CHAT_FILTER_COMMAND.replace("[prefix]",Messages.PREFIX_CHAT)));
                event.setCancelled(true);
                return;
            }
        }
        Ban ban = networkPlayer.getBan(BanType.CHAT);
        if(ban != null){
            if(event.isCommand() && !(BanSystem.getInstance().getFilterManager().isBlocked(FilterType.MUTECOMMAND,event.getMessage()))) return;
            player.sendMessage(ban.toMessage());
            event.setCancelled(true);
        }else if(!event.isCommand()){
            lastMessage lastMessage = this.lastMessage.get(player.getUniqueId());
            if(lastMessage != null){
                if(event.getMessage().equalsIgnoreCase(lastMessage.message)){
                    event.setCancelled(true);
                    player.sendMessage(new TextComponent(Messages.CHAT_FILTER_SPAM_REPEAT.replace("[prefix]",Messages.PREFIX_CHAT)));
                    return;
                }else if(lastMessage.time+BanSystem.getInstance().getConfig().chatDelay >= System.currentTimeMillis()){
                    event.setCancelled(true);
                    player.sendMessage(new TextComponent(Messages.CHAT_FILTER_SPAM_TOFAST.replace("[prefix]",Messages.PREFIX_CHAT)));
                    return;
                }else{
                    lastMessage.message = event.getMessage();
                    lastMessage.time = System.currentTimeMillis();
                }
            }else this.lastMessage.put(player.getUniqueId(),new lastMessage(event.getMessage(),System.currentTimeMillis()));
            FilterType filter = null;
            if(!player.hasPermission("dkbans.bypass.chat")){
                if(BanSystem.getInstance().getFilterManager().isBlocked(FilterType.MESSAGE,event.getMessage())){
                    filter = FilterType.MESSAGE;
                    event.setCancelled(true);
                    if(!checkBan(networkPlayer,BanSystem.getInstance().getConfig().chatFilterAutobanMessageBanID))
                        player.sendMessage(new TextComponent(Messages.CHAT_FILTER_MESSAGE.replace("[prefix]",Messages.PREFIX_CHAT)));
                }
                if(BanSystem.getInstance().getFilterManager().isBlocked(FilterType.PROMOTION,event.getMessage())){
                    filter = FilterType.PROMOTION;
                    event.setCancelled(true);
                    if(!checkBan(networkPlayer,BanSystem.getInstance().getConfig().chatFilterAutobanPromotionBanID))
                        player.sendMessage(new TextComponent(Messages.CHAT_FILTER_PROMOTION.replace("[prefix]",Messages.PREFIX_CHAT)));
                }
            }
            if(!event.isCancelled()){
                if(!this.currentMessageCount.containsKey(player.getUniqueId())) this.currentMessageCount.put(player.getUniqueId(),1);
                else this.currentMessageCount.put(player.getUniqueId(),this.currentMessageCount.get(player.getUniqueId())+1);
                BanSystem.getInstance().getTempSyncStats().addMessages();
            }
            if(BanSystem.getInstance().getConfig().chatlogEnabled){
                final FilterType finalFilter = filter;
                BungeeCord.getInstance().getScheduler().runAsync(BungeeCordBanSystemBootstrap.getInstance(),()->{
                    BanSystem.getInstance().getPlayerManager().createChatLogEntry(player.getUniqueId(),event.getMessage()
                            ,player.getServer().getInfo().getName(),finalFilter);
                });
            }
        }
    }
    private boolean checkBan(NetworkPlayer player, int reasonID){
        if(this.banPoints.containsKey(player.getUUID())){
            int bans = this.banPoints.get(player.getUUID());
            if(bans >= BanSystem.getInstance().getConfig().chatFilterAutobanCount){
                this.banPoints.remove(player.getUUID());
                BanReason reason = BanSystem.getInstance().getReasonProvider().getBanReason(reasonID);
                if(reason != null) player.ban(reason,"","ChatFilter");
                return reason!=null;
            }else this.banPoints.put(player.getUUID(),bans+1);
        }else this.banPoints.put(player.getUUID(),1);
        return false;
    }
    @EventHandler
    public void onTabComplete(TabCompleteEvent event){
        if(!(event.getSender() instanceof ProxiedPlayer)) return;
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if(event.getCursor().startsWith("/") && !event.getCursor().contains(" ") && BanSystem.getInstance().getConfig().tabCompleteBlockEnabled){
            final String search = event.getCursor();
            if(!player.hasPermission("dkbans.bypass.tabcomplete")) event.getSuggestions().clear();
            GeneralUtil.iterateAcceptedForEach(BanSystem.getInstance().getConfig().tabCompleteOptions
                    ,option -> (option.getPermission() == null || player.hasPermission(option.getPermission())) && option.getOption().startsWith(search)
                    ,option -> event.getSuggestions().add(option.getOption()));
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
