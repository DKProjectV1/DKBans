package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.value.Ban;
import ch.dkrieger.bansystem.lib.player.history.value.Unban;
import ch.dkrieger.bansystem.lib.reason.UnbanReason;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.List;

public class UnbanCommand extends NetworkCommand {

    public UnbanCommand(String name) {
        super(name);
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 1){
            sendReasons(sender);
            return;
        }
        NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
        if(player != null){
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
        if(args.length > 2){
            type = BanType.parse(args[2]);
            return;
        }
        if(type != null && player.isBanned(BanType.NETWORK) && player.isBanned(BanType.CHAT)){
            sender.sendMessage(Messages.PLAYER_HAS_MOREBANS_HEADER
                    .replace("[player]",player.getColoredName())
                            .replace("[prefix]",getPrefix()));
            sender.sendMessage(Messages.PLAYER_HAS_MOREBANS_NETWORK
                    .replace("[prefix]",getPrefix())
                    .replace("[remaining]", GeneralUtil.calculateTime(player.getBan(BanType.NETWORK).getRemaining(),true))
                    .replace("[duration]",GeneralUtil.calculateTime(player.getBan(BanType.NETWORK).getDuration(),true))
                    .replace("[id]",""+player.getBan(BanType.NETWORK).getID())
                    .replace("[reason]",player.getBan(BanType.NETWORK).getReason())
                    .replace("[type]",player.getBan(BanType.NETWORK).getTypeName())
                    .replace("[points]",""+player.getBan(BanType.NETWORK).getPoints())
                    .replace("[message]",player.getBan(BanType.NETWORK).getMessage())
                    .replace("[date]",""+player.getBan(BanType.NETWORK).getTimeStamp())
                    .replace("[ip]",player.getBan(BanType.NETWORK).getIp())
                    .replace("[staff]",player.getBan(BanType.NETWORK).getStaffName())
                    .replace("[player]",player.getColoredName()));
            sender.sendMessage(Messages.PLAYER_HAS_MOREBANS_CHAT
                    .replace("[prefix]",getPrefix())
                    .replace("[remaining]", GeneralUtil.calculateTime(player.getBan(BanType.CHAT).getRemaining(),true))
                    .replace("[duration]",GeneralUtil.calculateTime(player.getBan(BanType.CHAT).getDuration(),true))
                    .replace("[id]",""+player.getBan(BanType.CHAT).getID())
                    .replace("[reason]",player.getBan(BanType.CHAT).getReason())
                    .replace("[type]",player.getBan(BanType.CHAT).getTypeName())
                    .replace("[points]",""+player.getBan(BanType.CHAT).getPoints())
                    .replace("[message]",player.getBan(BanType.CHAT).getMessage())
                    .replace("[date]",""+player.getBan(BanType.CHAT).getTimeStamp())
                    .replace("[ip]",player.getBan(BanType.CHAT).getIp())
                    .replace("[staff]",player.getBan(BanType.CHAT).getStaffName())
                    .replace("[player]",player.getColoredName()));
            return;
        }
        if(player.isBanned(BanType.NETWORK)){
            Unban unban = player.unban(BanType.NETWORK);
            sender.sendMessage(Messages.PLAYER_UNBANNED
                    .replace("[prefix]",getPrefix())
                    .replace("[reason]",unban.getReason())
                    .replace("[message]",unban.getMessage())
                    .replace("[staff]",unban.getStaffName())
                    .replace("[id]",""+unban.getID())
                    .replace("[points]",""+unban.getPoints())
                    .replace("[player]",args[0]));
        }else{
            Unban unban = player.unban(BanType.NETWORK);
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
        sender.sendMessage(Messages.UNBAN_HELP_HEADER);
        for(UnbanReason reason : BanSystem.getInstance().getReasonProvider().getUnbanReasons()){
            if(!sender.hasPermission(reason.getPermission())) continue;
            sender.sendMessage(Messages.REASON_HELP
                    .replace("[prefix]",getPrefix())
                    .replace("[id]",""+reason.getID())
                    .replace("[name]",reason.getDisplay())
                    .replace("[maxPoints]",""+reason.getMaxPoints())
                    .replace("[maxDuration]",reason.getMaxDuration().getFormatedTime(true))
                    .replace("[points]",""+reason.getPoints()));
        }
        sender.sendMessage(Messages.UNBAN_HELP_HELP);
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
