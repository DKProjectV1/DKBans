package ch.dkrieger.bansystem.bungeecord.listener;

import ch.dkrieger.bansystem.bungeecord.BungeeCordBanSystemBootstrap;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.filter.FilterType;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.value.Ban;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import ch.dkrieger.bansystem.lib.utils.TabCompleteOption;
import de.dytanic.cloudnet.bridge.CloudProxy;
import de.dytanic.cloudnet.bridge.event.bukkit.BukkitPlayerDisconnectEvent;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerListener implements Listener {

    @EventHandler
    public void onLogin(LoginEvent event){
        CloudNetFix(event.getConnection().getUniqueId());
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
            player =  BanSystem.getInstance().getPlayerManager().createPlayer(event.getConnection().getUniqueId(),event.getConnection().getName());
        }else{
            //update player info
            Ban ban = player.getBan(BanType.NETWORK);
            if(ban != null){
                event.setCancelled(true);
                String message = Messages.BAN_MESSAGE_NETWORK_TEMPORARY;
                if(ban.getTimeOut() <= 0) message = Messages.BAN_MESSAGE_NETWORK_PERMANENT;
                event.setCancelReason(new TextComponent(message
                        .replace("[id]",ban.getID()+"")
                        .replace("[staff]",ban.getStaffName())
                        .replace("[reason]",ban.getReason())
                        .replace("[message]",ban.getMessage())
                        .replace("[remaining]",GeneralUtil.calculateTime(ban.getRemaining(),false))
                        .replace("[remaining-short]", GeneralUtil.calculateTime(ban.getRemaining(),true))
                        .replace("[time]",""+ban.getTimeStamp())
                        .replace("[prefix]",Messages.PREFIX_NETWORK)));
            }
        }

    }
    @EventHandler
    public void onPostLogin(PostLoginEvent event){
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
                            .replace("[status]",(player.isReportLogin()?Messages.STAFF_STATUS_LOGIN:Messages.STAFF_STATUS_LOGOUT))
                            .replace("[prefix]",Messages.PREFIX_TEAMCHAT)));
                }
                if(BanSystem.getInstance().getConfig().onJoinReportSize){
                    event.getPlayer().sendMessage(new TextComponent(Messages.REPORT_INFO
                            .replace("[size]",""+BanSystem.getInstance().getReportManager().getOpenReports().size())
                            .replace("[prefix]",Messages.PREFIX_REPORT)));
                }
            }
        });
    }
    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event){
        BungeeCord.getInstance().getScheduler().runAsync(BungeeCordBanSystemBootstrap.getInstance(),()->{
            NetworkPlayer player = BanSystem.getInstance().getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
            if(player == null) return;
            //update player info
        });
    }
    @EventHandler
    public void onChat(ChatEvent event){
        if(!(event.getSender() instanceof  ProxiedPlayer)) return;
        ProxiedPlayer player = (ProxiedPlayer)event.getSender();
        NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
        if(event.isCommand()){
            event.setMessage(event.getMessage().replace("bukkit:","").replace("minecraft:","").replace("spigot:",""));
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
            if(BanSystem.getInstance().getFilterManager().isBlocked(FilterType.COMMAND,event.getMessage())){
                player.sendMessage(new TextComponent(Messages.CHAT_FILTER_COMMAND.replace("[prefix]",Messages.PREFIX_CHAT)));
                return;
            }
        }
        Ban ban = networkPlayer.getBan(BanType.CHAT);
        if(ban != null){
            if(event.isCommand() && !(BanSystem.getInstance().getFilterManager().isBlocked(FilterType.MUTECOMMAND,event.getMessage()))) return;
            String message = Messages.BAN_MESSAGE_CHAT_TEMPORARY;
            if(ban.getTimeOut() <= 0) message = Messages.BAN_MESSAGE_CHAT_PERMANENT;
            player.sendMessage(new TextComponent(message
                    .replace("[id]",ban.getID()+"")
                    .replace("[staff]",ban.getStaffName())
                    .replace("[reason]",ban.getReason())
                    .replace("[message]",ban.getMessage())
                    .replace("[remaining]",GeneralUtil.calculateTime(ban.getRemaining(),false))
                    .replace("[remaining-short]", GeneralUtil.calculateTime(ban.getRemaining(),true))
                    .replace("[time]",""+ban.getTimeStamp())
                    .replace("[prefix]",Messages.PREFIX_NETWORK)));
            event.setCancelled(true);
        }else if(!event.isCommand()){
            if(BanSystem.getInstance().getFilterManager().isBlocked(FilterType.MESSAGE,event.getMessage())){
                player.sendMessage(new TextComponent(Messages.CHAT_FILTER_MESSAGE.replace("[prefix]",Messages.PREFIX_CHAT)));
                return;
            }
            if(BanSystem.getInstance().getFilterManager().isBlocked(FilterType.PROMOTION,event.getMessage())){
                player.sendMessage(new TextComponent(Messages.CHAT_FILTER_PROMOTION.replace("[prefix]",Messages.PREFIX_CHAT)));
                return;
            }
        }
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent event){
        if(event.getCursor().startsWith("/") && BanSystem.getInstance().getConfig().tabCompleteBlockEnabled){
            if(event.getSender() instanceof ProxiedPlayer){
                ProxiedPlayer player = (ProxiedPlayer)event.getSender();
                event.getSuggestions().clear();
                GeneralUtil.iterateAcceptedForEach(BanSystem.getInstance().getConfig().tabCompleteOptions
                        ,option -> (option.getPermission() == null || player.hasPermission(option.getPermission())) && event.getCursor().startsWith(option.getOption())
                        ,option -> event.getSuggestions().add(option.getOption()));
            }
        }
    }
    private void CloudNetFix(final UUID uuid){
        //if(!BanSystem.getInstance().cloudnet) return;
        ProxyServer.getInstance().getScheduler().schedule(CloudProxy.getInstance().getPlugin(), () -> {
            ProxiedPlayer player = BungeeCord.getInstance().getPlayer(uuid);
            //if(player == null) CloudNetExtension.getInstance().unregisterPlayer(uuid);
        }, 550L, TimeUnit.MILLISECONDS);
    }
}
