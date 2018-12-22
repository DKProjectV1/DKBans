package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;

import java.util.List;

public class PingCommand extends NetworkCommand {

    public PingCommand() {
        super("ping");
        setPrefix(Messages.PREFIX_NETWORK);
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 1 || !(sender.hasPermission("dkbans.ping.other"))){
            sender.sendMessage(Messages.PING_SELF
                    .replace("[prefix]",getPrefix())
                    .replace("[ping]",String.valueOf(sender.getAsOnlineNetworkPlayer().getPing())));
            return;
        }
        NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
        if(player == null){
            sender.sendMessage(Messages.PLAYER_NOT_FOUND
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",args[0]));
            return;
        }
        OnlineNetworkPlayer online = player.getOnlinePlayer();
        if(online == null){
            sender.sendMessage(Messages.PLAYER_NOT_ONLINE
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",player.getColoredName()));
            return;
        }
        sender.sendMessage(Messages.PING_OTHER
                .replace("[prefix]",getPrefix())
                .replace("[player]",player.getColoredName())
                .replace("[ping]",String.valueOf(online.getPing())));
        return;
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
