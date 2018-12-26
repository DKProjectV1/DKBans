package ch.dkrieger.bansystem.extension.consolecommand;

import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;

import java.util.List;

public class ConsoleBanCommand extends NetworkCommand {

    public ConsoleBanCommand() {
        super("cban");
    }

    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(sender.getUUID() == null){

        }
    }

    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
