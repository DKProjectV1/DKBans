package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;

import java.util.List;

public class HelpCommand extends NetworkCommand {

    public HelpCommand() {
        super("help");
        addAlias("?");
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        sender.sendMessage(Messages.HELP);
    }

    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
