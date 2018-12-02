package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Arrays;
import java.util.List;

public class JumptoCommand extends NetworkCommand {

    public JumptoCommand() {
        super("jumpto","Jump to","dkbans.jumpto");
        getAliases().add("goto");
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 1){
            sender.sendMessage(Messages.JUMPTO_HELP
                    .replace("[prefix]",getPrefix()));
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
        if(online.getServer() == null){
            sender.sendMessage(Messages.SERVER_NOT_FOUND
                    .replace("[prefix]",getPrefix()));
            return;
        }
        if(sender.getAsOnlineNetworkPlayer().getServer().equalsIgnoreCase(online.getServer())){
            sender.sendMessage(Messages.SERVER_ALREADY
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",player.getColoredName())
                    .replace("[server]",online.getServer()));
            return;
        }
        sender.sendMessage(Messages.SERVER_CONNECTING);
        sender.getAsOnlineNetworkPlayer().connect(online.getServer());
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
