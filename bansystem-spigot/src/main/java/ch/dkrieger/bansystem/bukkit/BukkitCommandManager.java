package ch.dkrieger.bansystem.bukkit;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandManager;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.*;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 16.11.18 17:47
 *
 */

public class BukkitCommandManager implements NetworkCommandManager {

    private Collection<NetworkCommand> commands;

    public BukkitCommandManager() {
        this.commands = new LinkedHashSet<>();
    }
    public Collection<NetworkCommand> getCommands() {
        return this.commands;
    }
    public NetworkCommand getCommand(String name) {
        for(NetworkCommand command : this.commands) if(command.getName().equalsIgnoreCase(name)) return command;
        return null;
    }
    public void registerCommand(final NetworkCommand command) {
        CommandMap cmap = null;
        try{
            final Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            cmap = (CommandMap)field.get(Bukkit.getServer());
            cmap.register("",new SpigotNetworkCommand(command));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private class SpigotNetworkCommand extends Command implements TabCompleter {

        private NetworkCommand command;

        public SpigotNetworkCommand(NetworkCommand command) {
            super(command.getName(),"","",command.getAliases());
            this.command = command;
        }

        @Override
        public boolean execute(CommandSender sender, String s, String[] args) {
            if(command.getPermission() == null || command.getPermission().equalsIgnoreCase("none")
                    ||sender.hasPermission(command.getPermission())){
                Bukkit.getScheduler().runTaskAsynchronously(BukkitBanSystemBootstrap.getInstance(),()->{
                    command.onExecute(new BungeeCordNetworkCommandSender(sender),args);
                });
                return false;
            }
            sender.sendMessage(Messages.NOPERMISSIONS
                    .replace("[prefix]",(command.getPrefix() != null?command.getPrefix():Messages.PREFIX_NETWORK)));
            return false;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
            if(command.getPermission() == null || command.getPermission().equalsIgnoreCase("none")
                    ||sender.hasPermission(command.getPermission())){
                List<String> tab = this.command.onTabComplete(new BungeeCordNetworkCommandSender(sender),args);
                if(tab != null) return tab;
            }
            return new ArrayList<>();
        }
    }
    private class BungeeCordNetworkCommandSender implements NetworkCommandSender {

        private CommandSender sender;

        public BungeeCordNetworkCommandSender(CommandSender sender) {
            this.sender = sender;
        }
        @Override
        public String getName() {
            return sender.getName();
        }
        @Override
        public String getServer() {
           if(sender instanceof Player) return ((Player) sender).getWorld().getName();
           return "";
        }

        @Override
        public UUID getUUID() {
            if(sender instanceof Player) return ((Player) sender).getUniqueId();
            else return null;
        }
        @Override
        public NetworkPlayer getAsNetworkPlayer() {
            if(this.sender instanceof Player) return BanSystem.getInstance().getPlayerManager().getPlayer(getUUID());
            return null;
        }
        @Override
        public boolean hasPermission(String permission) {
            return sender.hasPermission(permission);
        }
        @Override
        public void sendMessage(String message) {
            sender.sendMessage(message);
        }
        @Override
        public void sendMessage(TextComponent component) {
            if(sender instanceof Player) BukkitBanSystemBootstrap.getInstance().sendTextComponent((Player)sender,component);
            else sender.sendMessage(component.toLegacyText());
        }
        @Override
        public void executeCommand(String command) {
            Bukkit.dispatchCommand(sender,command);
        }
        @Override
        public OnlineNetworkPlayer getAsOnlineNetworkPlayer() {
            if(this.sender instanceof Player) return BanSystem.getInstance().getPlayerManager().getOnlinePlayer(getUUID());
            return null;
        }
        @Override
        public void executeCommandOnServer(String message) {
            executeCommand(message);
        }
    }
}