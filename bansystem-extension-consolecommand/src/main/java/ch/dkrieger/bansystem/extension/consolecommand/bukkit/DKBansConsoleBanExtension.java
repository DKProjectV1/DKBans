package ch.dkrieger.bansystem.extension.consolecommand.bukkit;

import ch.dkrieger.bansystem.extension.consolecommand.commands.ConsoleBanCommand;
import ch.dkrieger.bansystem.extension.consolecommand.commands.ConsoleKickCommand;
import ch.dkrieger.bansystem.extension.consolecommand.commands.ConsoleUnbanCommand;
import ch.dkrieger.bansystem.lib.BanSystem;
import org.bukkit.plugin.java.JavaPlugin;

public class DKBansConsoleBanExtension extends JavaPlugin {

    @Override
    public void onEnable() {
        BanSystem.getInstance().getCommandManager().registerCommand(new ConsoleBanCommand());
        BanSystem.getInstance().getCommandManager().registerCommand(new ConsoleUnbanCommand());
        BanSystem.getInstance().getCommandManager().registerCommand(new ConsoleKickCommand());
        System.out.println("[DKBansConsoleBanExtension] successfully started");
    }
}
