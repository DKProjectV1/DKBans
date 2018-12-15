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

import java.util.List;

public class PlayerInfoCommand extends NetworkCommand {

    public PlayerInfoCommand() {
        super("playerinfo","","dkbans.playerinfo");
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 1){
            sender.sendMessage(Messages.PLAYER_INFO_HELP);
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
            if(GeneralUtil.equalsOne(args[1],"sessions","onlinesessions")){
                List<OnlineSession> sessions = player.getOnlineSessions();
                sender.sendMessage(Messages.PLAYER_INFO_SESSIONS_HEADER.replace("[prefix]",getPrefix()));
                for(OnlineSession session : sessions){
                    sender.sendMessage(Messages.PLAYER_INFO_SESSIONS_LIST
                            .replace("[client]",session.getClient())
                            .replace("[clientLanguage]",session.getClientLanguage())
                            .replace("[clientVersion]",session.getClientVersion())
                            .replace("[country]",session.getCountry())
                            .replace("[ip]",session.getIp())
                            .replace("[connected]",""+session.getConnected())
                            .replace("[disconnected]",""+session.getDisconnected())
                            .replace("[duration]",""+session.getDuration())
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
            sender.sendMessage(replace(Messages.PLAYER_INFO_OFFLINE
                    .replace("[server]",online.getServer())
                    .replace("[proxy]",online.getProxy())
                    ,player));
        }else sender.sendMessage(replace(Messages.PLAYER_INFO_ONLINE,player));
    }
    private String replace(String replace, NetworkPlayer player){
        return replace
                .replace("[prefix]",getPrefix())
                .replace("[player]",player.getColoredName())
                .replace("[color]",player.getColor())
                .replace("[country]",player.getCountry())
                .replace("[ip]",player.getIP())
                .replace("[firstLogin]",""+player.getFirstLogin())
                .replace("[lastLogin]",""+player.getLastLogin())
                .replace("[id]",String.valueOf(player.getID()))
                .replace("[onlineTime]",""+player.getOnlineTime())
                .replace("[logins]",""+player.getStats().getLogins())
                .replace("[messages]",""+player.getStats().getMessages())
                .replace("[reportsSent]",""+player.getStats().getReports())
                .replace("[reportsAccepted]",""+player.getStats().getReportsAccepted())
                .replace("[reportsDenies]",""+player.getStats().getReportsDenied())
                .replace("[reportsReceived]",""+player.getStats().getReportsReceived())
                .replace("[bans]",""+player.getHistory().getBanCount(BanType.NETWORK))
                .replace("[mutes]",""+player.getHistory().getBanCount(BanType.CHAT))
                .replace("[logins]",""+player.getStats().getLogins())
                .replace("[uuid]",String.valueOf(player.getUUID()));
    }

    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
