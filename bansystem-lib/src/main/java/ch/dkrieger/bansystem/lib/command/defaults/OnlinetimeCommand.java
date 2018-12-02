package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.List;

public class OnlinetimeCommand extends NetworkCommand {

    public OnlinetimeCommand() {
        super("onlinetime", "","dkbans.onlinetime","","otime");
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        NetworkPlayer player = sender.getAsNetworkPlayer();
        sender.sendMessage(Messages.ONLINE_TIME
                .replace("[player]",player.getColoredName())
                .replace("[time]", GeneralUtil.calculateTime(player.getOnlineTime(),false))
                .replace("[prefix]",getPrefix()));
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
