package de.fridious.bansystem.extension.gui.commands;

/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 30.12.18 22:49
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

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import de.fridious.bansystem.extension.gui.DKBansGuiExtension;
import de.fridious.bansystem.extension.gui.guis.Guis;
import de.fridious.bansystem.extension.gui.guis.GuiManager;
import de.fridious.bansystem.extension.gui.guis.playerinfo.PlayerInfoGlobalGui;
import de.fridious.bansystem.extension.gui.guis.playerinfo.PlayerInfoGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerInfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Messages.SYSTEM_PREFIX + "You can't execute this command from the console.");
            return true;
        }
        final Player player = (Player) sender;
        if(!player.hasPermission("dkbans.playerinfo")) {
            player.sendMessage(Messages.NOPERMISSIONS.replace("[prefix]", Messages.PREFIX_BAN));
            return true;
        }
        if(args.length == 0) {
            //All players
            GuiManager.CachedInventories cachedInventories = DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories(player);
            if(cachedInventories.hasCached(Guis.PLAYERINFO_GLOBAL)) cachedInventories.getAsPrivateGui(Guis.PLAYERINFO_GLOBAL).open();
            else cachedInventories.create(Guis.PLAYERINFO_GLOBAL, new PlayerInfoGlobalGui(player)).open();
        } else if(args.length == 1) {
            NetworkPlayer targetPlayer = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
            if(targetPlayer == null){
                sender.sendMessage(Messages.PLAYER_NOT_FOUND
                        .replace("[prefix]", Messages.PREFIX_BAN)
                        .replace("[player]",args[0]));
                return true;
            }
            DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories(player).create(Guis.PLAYERINFO_PLAYER, new PlayerInfoGui(player, targetPlayer.getUUID())).open();
        } else {
            player.sendMessage(Messages.PLAYER_INFO_HELP.replace("[prefix]", Messages.PREFIX_BAN));
        }
        return true;
    }
}