package ch.dkrieger.bansystem.bungeecord;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandManager;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.*;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 16.11.18 17:47
 *
 */

public class BungeeCordCommandManager implements NetworkCommandManager {

    private Collection<NetworkCommand> commands;

    public BungeeCordCommandManager() {
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
        BungeeCord.getInstance().getPluginManager().registerCommand(BungeeCordBanSystemBootstrap.getInstance()
                ,new BungeeCordNetworkCommand(command));
    }
    private class BungeeCordNetworkCommand extends Command implements TabExecutor {

        private NetworkCommand command;

        public BungeeCordNetworkCommand(NetworkCommand command) {
            super(command.getName(),null,command.getAliases().toArray(new String[command.getAliases().size()]));
            this.command = command;
        }
        @Override
        public void execute(CommandSender sender, String[] args) {
            if(command.getPermission() == null || command.getPermission().equalsIgnoreCase("none")
                    ||sender.hasPermission(command.getPermission())){
                BungeeCord.getInstance().getScheduler().runAsync(BungeeCordBanSystemBootstrap.getInstance(),()->{
                    command.onExecute(new BungeeCordNetworkCommandSender(sender),args);
                });
                return;
            }
            sender.sendMessage(new TextComponent(Messages.NOPERMISSIONS
                    .replace("[prefix]",(command.getPrefix() != null?command.getPrefix():Messages.PREFIX_NETWORK))));
        }
        @Override
        public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
            if(command.getPermission() == null || command.getPermission().equalsIgnoreCase("none")
                    ||sender.hasPermission(command.getPermission())){
                List<String> tab = this.command.onTabComplete(new BungeeCordNetworkCommandSender(sender),args);
                if(tab != null) return tab;
            }
            return new LinkedHashSet<>();
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
           if(sender instanceof ProxiedPlayer) return ((ProxiedPlayer) sender).getServer().getInfo().getName();
           return "";
        }

        @Override
        public UUID getUUID() {
            if(sender instanceof ProxiedPlayer) return ((ProxiedPlayer) sender).getUniqueId();
            else return null;
        }
        @Override
        public NetworkPlayer getAsNetworkPlayer() {
            if(this.sender instanceof  ProxiedPlayer) return BanSystem.getInstance().getPlayerManager().getPlayer(getUUID());
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
            sender.sendMessage(component);
        }
        @Override
        public void executeCommand(String command) {
            BungeeCord.getInstance().getPluginManager().dispatchCommand(sender,command);
        }
        @Override
        public OnlineNetworkPlayer getAsOnlineNetworkPlayer() {
            if(this.sender instanceof  ProxiedPlayer) return BanSystem.getInstance().getPlayerManager().getOnlinePlayer(getUUID());
            return null;
        }

        @Override
        public void executeCommandOnServer(String message) {
            if(!message.startsWith("/")) message = "/"+message;
            if(this.sender instanceof  ProxiedPlayer) ((ProxiedPlayer) sender).chat(message);
            else executeCommand(message);
        }
    }
}