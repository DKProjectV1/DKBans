package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.config.mode.BanMode;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.List;

public class MuteCommand extends NetworkCommand {

    public MuteCommand() {
        super("mute");
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(BanSystem.getInstance().getConfig().banMode == BanMode.SELF) sender.executeCommand("tempmute " + GeneralUtil.arrayToString(args, " "));
        else sender.executeCommand("ban " + GeneralUtil.arrayToString(args, " "));
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
