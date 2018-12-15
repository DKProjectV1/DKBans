package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import ch.dkrieger.bansystem.lib.reason.KickReason;

import java.util.List;

public class KickCommand extends NetworkCommand {


    public KickCommand() {
        super("kick","Kick a player","dkbans.kick","<player> <reason>","gkick","globalkick");
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 1){
            sendReasons(sender);
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
        KickReason reason = BanSystem.getInstance().getReasonProvider().searchKickReason(args[1]);
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

        if(player.hasBypass() && !(sender.hasPermission("dkbans.bypass.ignore"))){
            sender.sendMessage(Messages.KICK_BYPASS
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",player.getColoredName()));
            return;
        }
        Kick kick = player.kick(reason);
        sender.sendMessage(Messages.KICK_SUCCESS
                .replace("[prefix]",getPrefix())
                .replace("[server]",kick.getServer())
                .replace("[reason]",kick.getReason())
                .replace("[reasonID]",""+kick.getReasonID())
                .replace("[player]",player.getColoredName()));
    }
    private void sendReasons(NetworkCommandSender sender){
        sender.sendMessage(Messages.KICK_HELP_HEADER);
        for(KickReason reason : BanSystem.getInstance().getReasonProvider().getKickReasons()){
            if(!sender.hasPermission(reason.getPermission())) continue;
            sender.sendMessage(Messages.REASON_HELP
                    .replace("[prefix]",getPrefix())
                    .replace("[id]",""+reason.getID())
                    .replace("[name]",reason.getDisplay())
                    .replace("[points]",""+reason.getPoints()));
        }
        sender.sendMessage(Messages.KICK_HELP_HELP);
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
