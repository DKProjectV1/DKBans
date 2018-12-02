package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;

import java.util.List;

public class PlayerInfoCommand extends NetworkCommand {

    public PlayerInfoCommand() {
        super("playerinfo","","dkbans.playerinfo");
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length > 1){
            sender.sendMessage(Messages.PLAYER_INFO_HELP);
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
        if(online != null){

        }else sender.sendMessage(replace(Messages.PLAYER_INFO_ONLINE,player));
    }
    private String replace(String replace, NetworkPlayer player){
        return replace
                .replace("[prefix]",getPrefix())
                .replace("[player]",player.getColoredName())
                .replace("[id]",String.valueOf(player.getID()))
                .replace("[uuid]",String.valueOf(player.getUUID()));
    }

    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
