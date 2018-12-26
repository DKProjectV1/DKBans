package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.config.mode.KickMode;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import ch.dkrieger.bansystem.lib.reason.KickReason;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.List;

public class KickCommand extends NetworkCommand {


    public KickCommand() {
        super("kick","Kick a player","dkbans.kick","<player> <reason>","gkick","globalkick");
        setPrefix(Messages.PREFIX_BAN);
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 2){
            sendReasons(sender);
            return;
        }//kick dkrieger 1
        if(sender.getName().equalsIgnoreCase(args[0])){
            sender.sendMessage(Messages.KICK_SELF.replace("[prefix]",getPrefix()));
            return;
        }
        NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
        if(player == null){
            sender.sendMessage(Messages.PLAYER_NOT_FOUND
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",args[0]));
            return;
        }
        if(!player.isOnline()){
            sender.sendMessage(Messages.PLAYER_NOT_ONLINE
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",player.getColoredName()));
            return;
        }
        KickReason reason = null;
        if(BanSystem.getInstance().getConfig().kickMode == KickMode.TEMPLATE){
            reason = BanSystem.getInstance().getReasonProvider().searchKickReason(args[1]);
            if(reason == null){
                sendReasons(sender);
                return;
            }
            if(!sender.hasPermission(reason.getPermission())){
                sender.sendMessage(Messages.REASON_NO_PERMISSION
                        .replace("[prefix]",getPrefix())
                        .replace("[reason]",reason.getDisplay()));
                return;
            }
        }
        if(player.hasBypass() && !(sender.hasPermission("dkbans.bypass.ignore"))){
            sender.sendMessage(Messages.KICK_BYPASS
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",player.getColoredName()));
            return;
        }
        String message = "";
        for(int i = 2;i < args.length;i++) message += args[i]+" ";

        Kick kick = null;
        if(BanSystem.getInstance().getConfig().kickMode == KickMode.TEMPLATE) kick = player.kick(reason,message,sender.getUUID());
        else kick = player.kick(args[1],message,sender.getUUID());

        sender.sendMessage(Messages.KICK_SUCCESS
                .replace("[prefix]",getPrefix())
                .replace("[server]",kick.getServer())
                .replace("[reason]",kick.getReason())
                .replace("[reasonID]",""+kick.getReasonID())
                .replace("[player]",player.getColoredName()));
    }
    private void sendReasons(NetworkCommandSender sender){
        if(BanSystem.getInstance().getConfig().kickMode == KickMode.TEMPLATE) {
            sender.sendMessage(Messages.KICK_HELP_HEADER.replace("[prefix]",getPrefix()));
            for (KickReason reason : BanSystem.getInstance().getReasonProvider().getKickReasons()) {
                if (!sender.hasPermission(reason.getPermission())) continue;
                sender.sendMessage(Messages.KICK_HELP_REASON
                        .replace("[prefix]", getPrefix())
                        .replace("[id]", "" + reason.getID())
                        .replace("[reason]", reason.getDisplay())
                        .replace("[name]", reason.getDisplay())
                        .replace("[points]", "" + reason.getPoints()));
            }
        }
        sender.sendMessage(Messages.KICK_HELP_HELP.replace("[prefix]",getPrefix()));
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        if(args.length == 1) return GeneralUtil.calculateTabComplete(args[0],sender.getName(),BanSystem.getInstance().getNetwork().getPlayersOnServer(sender.getServer()));
        return null;
    }
}
