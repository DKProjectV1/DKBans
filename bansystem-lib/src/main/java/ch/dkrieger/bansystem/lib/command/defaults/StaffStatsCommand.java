package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;

import java.util.List;

public class StaffStatsCommand extends NetworkCommand {

    public StaffStatsCommand() {
        super("staffstats","","dkbans.staffstats");
    }

    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 1){
            sender.sendMessage(Messages.STAFFSTATS_HELP.replace("[prefix]",getPrefix()));
            return;
        }
        NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
        if(player == null){
            sender.sendMessage(Messages.PLAYER_NOT_FOUND
                    .replace("[player]",args[0])
                    .replace("[prefix]",getPrefix()));
            return;
        }
        sender.sendMessage(Messages.STAFFSTATS_INFO
                .replace("[player]",player.getColoredName())
                .replace("[bans]",String.valueOf(player.getStats().getBans()))
                .replace("[mutes]",String.valueOf(player.getStats().getMutes()))
                .replace("[kicks]",String.valueOf(player.getStats().getKicks()))
                .replace("[unbans]",String.valueOf(player.getStats().getUnbans()))
                .replace("[prefix]",getPrefix()));
    }

    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
