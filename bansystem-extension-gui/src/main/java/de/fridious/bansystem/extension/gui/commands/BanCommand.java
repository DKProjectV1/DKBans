package de.fridious.bansystem.extension.gui.commands;

/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 30.12.18 22:42
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
import ch.dkrieger.bansystem.lib.config.mode.BanMode;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import de.fridious.bansystem.extension.gui.DKBansGuiExtension;
import de.fridious.bansystem.extension.gui.guis.GuiManager;
import de.fridious.bansystem.extension.gui.guis.Guis;
import de.fridious.bansystem.extension.gui.guis.ban.BanGlobalGui;
import de.fridious.bansystem.extension.gui.guis.ban.BanSelfGui;
import de.fridious.bansystem.extension.gui.guis.ban.BanTemplateGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Messages.SYSTEM_PREFIX + "You can't execute this command from console");
            return true;
        }
        final Player player = (Player)sender;
        if(!player.hasPermission("dkbans.ban")) {
            player.sendMessage(Messages.NOPERMISSIONS.replace("[prefix]", Messages.PREFIX_BAN));
            return true;
        }
        GuiManager guiManager = DKBansGuiExtension.getInstance().getGuiManager();
        if(args.length == 0 && guiManager.isGuiEnabled(BanGlobalGui.class)) {
            GuiManager.CachedGuis cachedGuis = DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player);
            if(cachedGuis.hasCached(Guis.BAN_GLOBAL)) cachedGuis.getAsPrivateGui(Guis.BAN_GLOBAL).open();
            else cachedGuis.create(Guis.BAN_GLOBAL, new BanGlobalGui(player)).open();
        } else if(args.length == 1 && (guiManager.isGuiEnabled(BanSelfGui.class) || guiManager.isGuiEnabled(BanTemplateGui.class))) {
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
            if(target.hasBypass() && !(player.hasPermission("dkbans.bypass.ignore"))){
                player.sendMessage(Messages.BAN_BYPASS
                        .replace("[prefix]", Messages.PREFIX_BAN)
                        .replace("[player]",target.getColoredName()));
                return true;
            }
            BanMode banMode = BanSystem.getInstance().getConfig().banMode;
            if(banMode == BanMode.TEMPLATE || banMode == BanMode.POINT) {
                DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                        .create(Guis.BAN_TEMPLATE, new BanTemplateGui(player, target.getUUID())).open();
            } else if(banMode == BanMode.SELF) {
                DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                        .create(Guis.BAN_SELF, new BanSelfGui(player, target.getUUID())).open();
            }
        } else {
            //USAGE
        }
        return true;
    }
}