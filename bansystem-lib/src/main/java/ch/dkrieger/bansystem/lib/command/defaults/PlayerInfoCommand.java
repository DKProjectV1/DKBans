package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineSession;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayerInfoCommand extends NetworkCommand {

    public PlayerInfoCommand() {
        super("playerinfo","","dkbans.playerinfo");
        getAliases().add("pinfo");
        setPrefix(Messages.PREFIX_BAN);
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 1){
            sender.sendMessage(Messages.PLAYER_INFO_HELP.replace("[prefix]",getPrefix()));
            return;
        }
        NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
        if(player == null){
            sender.sendMessage(Messages.PLAYER_NOT_FOUND
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",args[0]));
            return;
        }
        if(args.length >= 2){
            if(BanSystem.getInstance().getConfig().playerOnlineSessionSaving && GeneralUtil.equalsOne(args[1],"sessions","onlinesessions","session")){
                List<OnlineSession> sessions = player.getOnlineSessions();
                sender.sendMessage(Messages.PLAYER_INFO_SESSIONS_HEADER
                        .replace("[player]",player.getColoredName())
                        .replace("[prefix]",getPrefix()));
                for(OnlineSession session : sessions){
                    sender.sendMessage(Messages.PLAYER_INFO_SESSIONS_LIST
                            .replace("[clientLanguage]",session.getClientLanguage())
                            .replace("[clientVersion]",""+session.getClientVersion())
                            .replace("[country]",session.getCountry())
                            .replace("[ip]",session.getIp())
                            .replace("[connected]",BanSystem.getInstance().getConfig().dateFormat.format(session.getConnected()))
                            .replace("[disconnected]",BanSystem.getInstance().getConfig().dateFormat.format(session.getDisconnected()))
                            .replace("[duration]",""+GeneralUtil.calculateRemaining(session.getDuration(),true))
                            .replace("[prefix]",getPrefix()));
                }
                return;
            }else if(GeneralUtil.equalsOne(args[1],"history")){//playerinfo dkrieger history asd
                sender.executeCommand("/history "+args[0]+(args.length>=3?" "+args[2]:""));
                return;
            }
        }
        OnlineNetworkPlayer online = player.getOnlinePlayer();
        if(online != null){
            sender.sendMessage(replace(Messages.PLAYER_INFO_ONLINE
                    .replace("[server]",online.getServer())
                    .replace("[proxy]",online.getProxy())
                    ,player));
        }else sender.sendMessage(replace(Messages.PLAYER_INFO_OFFLINE,player));
    }
    private TextComponent replace(String replace, NetworkPlayer player){
        TextComponent history = new TextComponent(Messages.PLAYER_INFO_HISTORY);
        history.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/history "+player.getUUID()));

        TextComponent sessions = new TextComponent(Messages.PLAYER_INFO_SESSIONS);
        sessions.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/playerinfo "+player.getUUID()+" sessions"));

        TextComponent ip = new TextComponent(Messages.PLAYER_INFO_IPS);
        ip.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/ipinfo "+player.getUUID()));

        TextComponent component = GeneralUtil.replaceTextComponent(replace
                .replace("[prefix]",getPrefix())
                .replace("[player]",player.getColoredName())
                .replace("[color]",player.getColor())
                .replace("[country]",player.getCountry())
                .replace("[ip]",player.getIP())
                .replace("[firstLogin]",BanSystem.getInstance().getConfig().dateFormat.format(player.getFirstLogin()))
                .replace("[lastLogin]",BanSystem.getInstance().getConfig().dateFormat.format(player.getLastLogin()))
                .replace("[id]",String.valueOf(player.getID()))
                .replace("[onlineTime-short]",""+GeneralUtil.calculateRemaining(player.getOnlineTime(true),true))
                .replace("[onlineTime]",""+GeneralUtil.calculateRemaining(player.getOnlineTime(true),false))
                .replace("[messages]",""+player.getStats().getMessages())
                .replace("[reportsSent]",""+player.getStats().getReports())
                .replace("[reportsAccepted]",""+player.getStats().getReportsAccepted())
                .replace("[reportsDenies]",""+player.getStats().getReportsDenied())
                .replace("[reportsReceived]",""+player.getStats().getReportsReceived())
                .replace("[bans]",""+player.getHistory().getBanCount(BanType.NETWORK))
                .replace("[mutes]",""+player.getHistory().getBanCount(BanType.CHAT))
                .replace("[logins]",""+player.getStats().getLogins())
                .replace("[uuid]",String.valueOf(player.getUUID())),"[history]",history);
        component = GeneralUtil.replaceTextComponent(component,"[sessions]",sessions);
        component = GeneralUtil.replaceTextComponent(component,"[ips]",ip);
        return component;
    }

    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        if(args.length == 1) return GeneralUtil.calculateTabComplete(args[0],sender.getName(),BanSystem.getInstance().getNetwork().getPlayersOnServer(sender.getServer()));
        return null;
    }
}
