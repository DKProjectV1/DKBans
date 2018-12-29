package ch.dkrieger.bansystem.extension.consolecommand.bungeecord;

import ch.dkrieger.bansystem.extension.consolecommand.commands.ConsoleBanCommand;
import ch.dkrieger.bansystem.extension.consolecommand.commands.ConsoleKickCommand;
import ch.dkrieger.bansystem.extension.consolecommand.commands.ConsoleUnbanCommand;
import ch.dkrieger.bansystem.lib.BanSystem;
import net.md_5.bungee.api.plugin.Plugin;

public class DKBansConsoleBanExtension extends Plugin {

    @Override
    public void onEnable() {
        BanSystem.getInstance().getCommandManager().registerCommand(new ConsoleBanCommand());
        BanSystem.getInstance().getCommandManager().registerCommand(new ConsoleUnbanCommand());
        BanSystem.getInstance().getCommandManager().registerCommand(new ConsoleKickCommand());
        System.out.println("[DKBansConsoleBanExtension] successfully started");
    }
}
