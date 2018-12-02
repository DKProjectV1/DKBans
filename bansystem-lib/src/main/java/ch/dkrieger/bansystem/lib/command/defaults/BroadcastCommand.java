package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Arrays;
import java.util.List;

public class BroadcastCommand extends NetworkCommand {

    public BroadcastCommand() {
        super("broadcast","Broadcast","dkbans.broadcast");
        getAliases().addAll(Arrays.asList("bc","alert","rundruf","toall"));
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 1){
            sender.sendMessage(Messages.BROADCAST_HELP
                    .replace("[prefix]",getPrefix()));
            return;
        }
        TextComponent component = new TextComponent();
        String message = "";
        for(int i = 0; i < args.length;i++)  message += args[0]+" ";
        BanSystem.getInstance().getNetwork().broadcast(message);
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
