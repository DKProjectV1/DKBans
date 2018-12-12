package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.History;
import ch.dkrieger.bansystem.lib.player.history.value.HistoryValue;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.List;

public class ResetHistoryCommand extends NetworkCommand {

    public ResetHistoryCommand() {
        super("resethistory");
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 1){
            sender.sendMessage(Messages.HISTORY_HELP.replace("[prefix]",getPrefix()));
            return;
        }
        NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
        if(player == null){
            sender.sendMessage(Messages.PLAYER_NOT_FOUND
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",args[0]));
            return;
        }
        History history = player.getHistory();
        if(history == null || history.size() == 0){
            sender.sendMessage(Messages.HISTORY_NOTFOUND
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",player.getColoredName()));
            return;
        }
        if(args.length > 1 && GeneralUtil.isNumber(args[1])) {
            HistoryValue value = history.getValue(Integer.valueOf(args[1]));
            if(value == null){
                sender.sendMessage(Messages.HISTORY_NOTFOUND
                        .replace("[prefix]",getPrefix())
                        .replace("[player]",player.getColoredName()));
                return;
            }
            player.resetHistory(value.getID());
            sender.sendMessage(Messages.HISTORY_RESET_ONE
                    .replace("[prefix]",getPrefix())
                    .replace("[id]",""+value.getID())
                    .replace("[player]",player.getColoredName()));
            return;
        }
        player.resetHistory();
        sender.sendMessage(Messages.HISTORY_RESET_ALL
                .replace("[prefix]",getPrefix())
                .replace("[player]",player.getColoredName()));
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
