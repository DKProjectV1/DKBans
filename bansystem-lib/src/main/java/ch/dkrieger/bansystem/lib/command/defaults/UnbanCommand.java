package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.reason.UnbanReason;

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
            sender.sendMessage(Messages.PLAYER_NOT_
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",args[0]));
            return;
        }
        if(!player.isBanned()){
            sender.sendMessage(Messages.PLAYER_NOT_FOUND
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",args[0]));
            return;
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
