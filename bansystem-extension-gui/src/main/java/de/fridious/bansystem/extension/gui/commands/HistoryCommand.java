package de.fridious.bansystem.extension.gui.commands;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 06.01.19 20:46
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
import de.fridious.bansystem.extension.gui.guis.history.HistoryAllGui;
import de.fridious.bansystem.extension.gui.guis.history.HistoryGlobalGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HistoryCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Messages.SYSTEM_PREFIX + "You can't execute this command from console");
            return true;
        }
        final Player player = (Player)sender;
        if(!player.hasPermission("dkbans.history")) {
            player.sendMessage(Messages.NOPERMISSIONS.replace("[prefix]", Messages.PREFIX_NETWORK));
            return true;
        }
        GuiManager guiManager = DKBansGuiExtension.getInstance().getGuiManager();
        if(args.length == 0 && guiManager.isGuiEnabled(HistoryGlobalGui.class)) {
            GuiManager.CachedGuis cachedGuis = DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player);
            if(cachedGuis.hasCached(Guis.HISTORY_GLOBAL)) cachedGuis.getAsPrivateGui(Guis.HISTORY_GLOBAL).open();
            else cachedGuis.create(Guis.HISTORY_GLOBAL, new HistoryGlobalGui(player)).open();
        } else if(args.length == 1 && guiManager.isGuiEnabled(HistoryAllGui.class)) {
            NetworkPlayer target = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
            if(target == null){
                player.sendMessage(Messages.PLAYER_NOT_FOUND
                        .replace("[prefix]", Messages.PREFIX_BAN)
                        .replace("[player]",args[0]));
                return true;
            }
            if(target.getUUID().equals(player.getUniqueId())) {
                player.sendMessage(Messages.BAN_SELF.replace("[prefix]", Messages.PREFIX_BAN));
                return true;
            }
            DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                    .create(Guis.HISTORY_ALL, new HistoryAllGui(player, target.getUUID())).open();
        }
        return true;
    }
}