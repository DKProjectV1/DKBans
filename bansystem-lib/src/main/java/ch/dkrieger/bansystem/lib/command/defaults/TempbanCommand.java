package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TempbanCommand extends NetworkCommand {

    public TempbanCommand() {
        super("tempban","","dkbans.ban.temp.ban","","tban");
        setPrefix(Messages.PREFIX_BAN);
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 3 || !(GeneralUtil.isNumber(args[2]))){
            sender.sendMessage(Messages.TEMPBAN_HELP.replace("[prefix]",getPrefix()));
            return;
        }
        NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
        if(player != null){
            sender.sendMessage(Messages.PLAYER_NOT_FOUND
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",args[0]));
            return;
        }
        if(player.hasBypass() && !(sender.hasPermission("dkbans.bypass.ignore"))){
            sender.sendMessage(Messages.BAN_BYPASS
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",player.getColoredName()));
            return;
        }
        String unit = "days";
        if(args.length > 3) unit = args[3];
        long millis = GeneralUtil.convertToMillis(Long.valueOf(args[2]),unit);
        if(player.isBanned(BanType.NETWORK)){
            sender.sendMessage(Messages.PLAYER_ALREADY_BANNED
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",player.getColoredName()));
            return;
        }
        Ban ban = player.ban(BanType.NETWORK,millis,TimeUnit.MILLISECONDS,args[1],-1,sender.getUUID());
        sender.sendMessage(Messages.BAN_SUCCESS
                .replace("[prefix]",getPrefix())
                .replace("[player]",player.getColoredName())
                .replace("[type]",ban.getBanType().getDisplay())
                .replace("[reason]",ban.getReason())
                .replace("[points]",String.valueOf(ban.getPoints()))
                .replace("[staff]",ban.getStaffName())
                .replace("[reasonID]",String.valueOf(ban.getReasonID()))
                .replace("[ip]",ban.getIp())
                .replace("[duration]",GeneralUtil.calculateDuration(ban.getDuration()))
                .replace("[remaining]",GeneralUtil.calculateRemaining(ban.getDuration(),false))
                .replace("[remaining-short]",GeneralUtil.calculateRemaining(ban.getDuration(),true)));
    }

    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
