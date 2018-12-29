package ch.dkrieger.bansystem.extension.consolecommand.commands;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;

import java.util.List;

public class ConsoleUnbanCommand extends NetworkCommand {

    public ConsoleUnbanCommand() {
        super("cunban");
    }

    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(sender.getUUID() == null){
            if(args.length >= 3){
                NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
                if(player == null){
                    System.out.println(Messages.SYSTEM_PREFIX+"This player was not found.");
                    return;
                }
                BanType type;
                try{
                    type = BanType.valueOf(args[1].toUpperCase());
                }catch (Exception exception){
                    System.out.println(Messages.SYSTEM_PREFIX+"Invalid ban type, use Chat or Network.");
                    return;
                }
                String message = "";
                for(int i = 4;i < args.length;i++) message += args[i]+" ";
                player.unban(type,args[2],message,args[3]);
                System.out.println(Messages.SYSTEM_PREFIX+player.getName()+" was unbanned");
            }else{
                System.out.println(Messages.SYSTEM_PREFIX+"This is a simple addon for banning, kicking or unbanning a " +
                        "player from the console with a special staff member (Example: AntiCheat)\n\n\t-> /cUnban <player> <reason> <staffName> <message>" +
                        "\n\nAs reason you can use a id or a custom reason\n\n\t-> Do not forgot syncing all reasons in you network (Config files).");
            }
        }else sender.sendMessage(Messages.CHAT_FILTER_COMMAND.replace("[prefix]",Messages.PREFIX_NETWORK));
    }
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
