package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.config.mode.BanMode;
import ch.dkrieger.bansystem.lib.config.mode.UnbanMode;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.Unban;
import ch.dkrieger.bansystem.lib.reason.ReportReason;
import ch.dkrieger.bansystem.lib.reason.UnbanReason;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

public class UnbanCommand extends NetworkCommand {

    private UnbanMode unbanMode;

    public UnbanCommand() {
        super("unban","","dkperms.unban","","unmute");
        setPrefix(Messages.PREFIX_BAN);
        this.unbanMode = BanSystem.getInstance().getConfig().unbanMode;
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        int messageStart = 1;
        if(args.length < 1) {//unban dkrieger
            sendReasons(sender);
            return;
        }
        UnbanReason reason = null;
        if(unbanMode != UnbanMode.SELF){
            if(args.length >= 2) reason = BanSystem.getInstance().getReasonProvider().searchUnbanReason(args[1]);
            if(reason == null){
                sendReasons(sender);
                return;
            }
            messageStart++;
        }
        NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
        if(player == null){
            sender.sendMessage(Messages.PLAYER_NOT_FOUND
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",args[0]));
            return;
        }
        if(!player.isBanned()){
            sender.sendMessage(Messages.PLAYER_NOT_BANNED
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",args[0]));
            return;
        }
        BanType type = null;
        if((unbanMode == UnbanMode.SELF && args.length >= 2) || (unbanMode == UnbanMode.TEMPLATE && args.length >= 3)){//unban dkrieger 1 network das war ein test
            type = BanType.parse(args[messageStart].toUpperCase());
            messageStart++;
        }
        String message = "";
        for(int i = messageStart;i < args.length;i++) message += args[i]+" ";
        if(type == null && player.isBanned(BanType.NETWORK) && player.isBanned(BanType.CHAT)){
            sender.sendMessage(Messages.PLAYER_HAS_MOREBANS_HEADER
                    .replace("[player]",player.getColoredName())
                            .replace("[prefix]",getPrefix()));
            TextComponent network = new TextComponent(Messages.PLAYER_HAS_MOREBANS_NETWORK
                    .replace("[prefix]",getPrefix())
                    .replace("[duration]",GeneralUtil.calculateDuration(player.getBan(BanType.NETWORK).getDuration()))
                    .replace("[remaining]",GeneralUtil.calculateRemaining(player.getBan(BanType.NETWORK).getDuration(),false))
                    .replace("[remaining-short]",GeneralUtil.calculateRemaining(player.getBan(BanType.NETWORK).getDuration(),true))
                    .replace("[id]",""+player.getBan(BanType.NETWORK).getID())
                    .replace("[reason]",player.getBan(BanType.NETWORK).getReason())
                    .replace("[type]",player.getBan(BanType.NETWORK).getTypeName())
                    .replace("[points]",""+player.getBan(BanType.NETWORK).getPoints())
                    .replace("[message]",player.getBan(BanType.NETWORK).getMessage())
                    .replace("[date]",""+player.getBan(BanType.NETWORK).getTimeStamp())
                    .replace("[ip]",player.getBan(BanType.NETWORK).getIp())
                    .replace("[staff]",player.getBan(BanType.NETWORK).getStaffName())
                    .replace("[player]",player.getColoredName()));
            network.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/unban "+args[0]+" NETWORK "+message));
            TextComponent chat = new TextComponent(Messages.PLAYER_HAS_MOREBANS_CHAT
                    .replace("[prefix]",getPrefix())
                    .replace("[duration]",GeneralUtil.calculateDuration(player.getBan(BanType.CHAT).getDuration()))
                    .replace("[remaining]",GeneralUtil.calculateRemaining(player.getBan(BanType.CHAT).getDuration(),false))
                    .replace("[remaining-short]",GeneralUtil.calculateRemaining(player.getBan(BanType.CHAT).getDuration(),true))
                    .replace("[id]",""+player.getBan(BanType.CHAT).getID())
                    .replace("[reason]",player.getBan(BanType.CHAT).getReason())
                    .replace("[type]",player.getBan(BanType.CHAT).getTypeName())
                    .replace("[points]",""+player.getBan(BanType.CHAT).getPoints())
                    .replace("[message]",player.getBan(BanType.CHAT).getMessage())
                    .replace("[date]",""+player.getBan(BanType.CHAT).getTimeStamp())
                    .replace("[ip]",player.getBan(BanType.CHAT).getIp())
                    .replace("[staff]",player.getBan(BanType.CHAT).getStaffName())
                    .replace("[player]",player.getColoredName()));
            chat.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/unban "+args[0]+" CHAT "+message));
            sender.sendMessage(network);
            sender.sendMessage(chat);
            return;
        }else if(!player.isBanned(type)){
            sender.sendMessage(Messages.PLAYER_NOT_BANNED
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",args[0]));
            return;
        }
        if(!sender.hasPermission("dkbans.unban.all")){
            Ban ban = null;
            if(type != null) ban = player.getBan(type);
            else ban = player.getBan((player.isBanned(BanType.NETWORK))?BanType.NETWORK:BanType.CHAT);
            if(!ban.getStaff().equals(sender.getUUID().toString())){
                sender.sendMessage(Messages.UNBAN_NOTALLOWED
                        .replace("[player]",player.getColoredName())
                        .replace("[prefix]",getPrefix()));
                return;
            }
        }

        if((type == null || type == BanType.NETWORK) &&  player.isBanned(BanType.NETWORK)){
            Unban unban;
            if(this.unbanMode == UnbanMode.SELF) unban = player.unban(BanType.NETWORK,message,sender.getUUID());
            else unban = player.unban(BanType.NETWORK,reason,message,sender.getUUID());
            sender.sendMessage(Messages.PLAYER_UNBANNED
                    .replace("[prefix]",getPrefix())
                    .replace("[reason]",unban.getReason())
                    .replace("[message]",unban.getMessage())
                    .replace("[staff]",unban.getStaffName())
                    .replace("[id]",""+unban.getID())
                    .replace("[points]",""+unban.getPoints())
                    .replace("[player]",args[0]));
        }else{
            Unban unban;
            if(this.unbanMode == UnbanMode.SELF) unban = player.unban(BanType.CHAT,message,sender.getUUID());
            else unban = player.unban(BanType.CHAT,reason,message,sender.getUUID());
            sender.sendMessage(Messages.PLAYER_UNMUTED
                    .replace("[prefix]",getPrefix())
                    .replace("[reason]",unban.getReason())
                    .replace("[message]",unban.getMessage())
                    .replace("[staff]",unban.getStaffName())
                    .replace("[id]",""+unban.getID())
                    .replace("[points]",""+unban.getPoints())
                    .replace("[player]",args[0]));
        }
    }
    private void sendReasons(NetworkCommandSender sender){
        if(BanSystem.getInstance().getConfig().unbanMode != UnbanMode.SELF) {
            sender.sendMessage(Messages.UNBAN_HELP_HEADER);
            for(UnbanReason reason : BanSystem.getInstance().getReasonProvider().getUnbanReasons()){
                if(!sender.hasPermission(reason.getPermission())) continue;
                sender.sendMessage(Messages.UNBAN_HELP_REASON
                        .replace("[prefix]",getPrefix())
                        .replace("[id]",""+reason.getID())
                        .replace("[name]",reason.getDisplay())
                        .replace("[maxPoints]",""+reason.getMaxPoints())
                        .replace("[maxDuration]",reason.getMaxDuration().getFormatedTime(true))
                        .replace("[points]",""+reason.getPoints()));
            }
        }
        sender.sendMessage(Messages.UNBAN_HELP_HELP);
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        if(args.length == 1) return GeneralUtil.calculateTabComplete(args[0],sender.getName(),BanSystem.getInstance().getNetwork().getPlayersOnServer(sender.getServer()));
        return null;
    }
}
