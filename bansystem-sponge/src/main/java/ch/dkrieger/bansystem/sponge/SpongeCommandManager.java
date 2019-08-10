/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 10.08.19, 21:12
 * @Website https://github.com/DevKrieger/DKBans
 *
 * The DKBans Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package ch.dkrieger.bansystem.sponge;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandManager;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.*;

public class SpongeCommandManager implements NetworkCommandManager {

    private final Collection<NetworkCommand> commands;

    public SpongeCommandManager() {
        commands = new HashSet<>();
    }

    @Override
    public Collection<NetworkCommand> getCommands() {
        return commands;
    }

    @Override
    public NetworkCommand getCommand(String name) {
        return GeneralUtil.iterateOne(commands, command -> command.hasAliase(name));
    }

    @Override
    public void registerCommand(NetworkCommand command) {
        Sponge.getCommandManager().register(SpongeBanSystemBootstrap.getInstance(),new SpongeNetworkCommand(command),command.getAliases());
    }

    private class SpongeNetworkCommand implements CommandCallable {

        private final NetworkCommand command;

        public SpongeNetworkCommand(NetworkCommand command) {
            this.command = command;
        }

        @Override
        public CommandResult process(CommandSource sender, String args) throws CommandException {
            if(command.getPermission() == null || command.getPermission().equalsIgnoreCase("none")
                    || sender.hasPermission("dkbans.*")||sender.hasPermission(command.getPermission())){
                Task.builder().async().execute(()->{
                    command.onExecute(new SpongeNetworkCommandSender(sender),args.trim().split(" "));
                });
                return CommandResult.success();
            }
            sender.sendMessage(Text.of(Messages.NOPERMISSIONS
                    .replace("[prefix]",(command.getPrefix() != null?command.getPrefix():Messages.PREFIX_NETWORK))));
            return CommandResult.success();
        }

        @Override
        public List<String> getSuggestions(CommandSource sender, String args, @Nullable Location<World> targetPosition) {
            if(command.getPermission() == null || command.getPermission().equalsIgnoreCase("none")
                    ||sender.hasPermission(command.getPermission())){
                List<String> tab = this.command.onTabComplete(new SpongeNetworkCommandSender(sender),args.trim().split(" "));
                if(tab != null) return tab;
            }
            return new ArrayList<>();
        }

        @Override
        public boolean testPermission(CommandSource sender) {
            return true;
        }

        @Override
        public Optional<Text> getShortDescription(CommandSource sender) {
            return Optional.of(Text.of(command.getDescription()));
        }

        @Override
        public Optional<Text> getHelp(CommandSource source) {
            return Optional.of(Text.of(command.getUsage()));
        }

        @Override
        public Text getUsage(CommandSource source) {
            return Text.of(command.getUsage());
        }
    }

    private class SpongeNetworkCommandSender implements NetworkCommandSender {

        private final CommandSource sender;

        public SpongeNetworkCommandSender(CommandSource sender) {
            this.sender = sender;
        }

        @Override
        public String getName() {
            return sender.getName();
        }

        @Override
        public UUID getUUID() {
            return sender instanceof Player?((Player) sender).getUniqueId():null;
        }

        @Override
        public NetworkPlayer getAsNetworkPlayer() {
            return sender instanceof Player? BanSystem.getInstance().getPlayerManager().getPlayer(getUUID()):null;
        }

        @Override
        public OnlineNetworkPlayer getAsOnlineNetworkPlayer() {
            return sender instanceof Player? BanSystem.getInstance().getPlayerManager().getOnlinePlayer(getUUID()):null;
        }

        @Override
        public String getServer() {
            return sender instanceof Player? ((Player) sender).getWorld().getName():"Unknown";
        }

        @Override
        public boolean hasPermission(String permission) {
            return sender.hasPermission(permission);
        }

        @Override
        public void sendMessage(String message) {
            sender.sendMessage(Text.of(message));
        }

        @Override
        public void sendMessage(TextComponent component) {
        }

        @Override
        public void executeCommand(String command) {
            Sponge.getCommandManager().process(sender,command);
        }

        @Override
        public void executeCommandOnServer(String command) {
            Sponge.getCommandManager().process(sender,command);
        }
    }
}
