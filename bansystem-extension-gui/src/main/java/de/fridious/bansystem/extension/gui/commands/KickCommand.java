package de.fridious.bansystem.extension.gui.commands;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 05.01.19 00:36
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
import ch.dkrieger.bansystem.lib.config.mode.ReasonMode;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import de.fridious.bansystem.extension.gui.DKBansGuiExtension;
import de.fridious.bansystem.extension.gui.guis.Guis;
import de.fridious.bansystem.extension.gui.guis.GuiManager;
import de.fridious.bansystem.extension.gui.guis.kick.KickGlobalGui;
import de.fridious.bansystem.extension.gui.guis.kick.KickSelfGui;
import de.fridious.bansystem.extension.gui.guis.kick.KickTemplateGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(Messages.SYSTEM_PREFIX + "You can't execute this command from console");
            return true;
        }
        final Player player = (Player)sender;
        if(!player.hasPermission("dkbans.kick")) {
            player.sendMessage(Messages.NOPERMISSIONS.replace("[prefix]", Messages.PREFIX_BAN));
            return true;
        }
        GuiManager guiManager = DKBansGuiExtension.getInstance().getGuiManager();
        if(args.length == 0 && guiManager.isGuiEnabled(KickGlobalGui.class)) {
            GuiManager.CachedGuis cachedGuis = DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player);
            if(cachedGuis.hasCached(Guis.KICK_GLOBAL)) cachedGuis.getAsPrivateGui(Guis.KICK_GLOBAL).open();
            else cachedGuis.create(Guis.KICK_GLOBAL, new KickGlobalGui(player));
        } else if(args.length == 1 && (guiManager.isGuiEnabled(KickSelfGui.class) || guiManager.isGuiEnabled(KickTemplateGui.class))) {
            NetworkPlayer target = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
            if(target == null){
                player.sendMessage(Messages.PLAYER_NOT_FOUND
                        .replace("[prefix]", Messages.PREFIX_BAN)
                        .replace("[player]",args[0]));
                return true;
            }
            if(target.getUUID().equals(player.getUniqueId())) {
                player.sendMessage(Messages.KICK_SELF.replace("[prefix]", Messages.PREFIX_BAN));
                return true;
            }
            if(target.hasBypass() && !(player.hasPermission("dkbans.bypass.ignore"))){
                player.sendMessage(Messages.KICK_BYPASS
                        .replace("[prefix]", Messages.PREFIX_BAN)
                        .replace("[player]",target.getColoredName()));
                return true;
            }
            ReasonMode kickMode = BanSystem.getInstance().getConfig().kickMode;
            if(kickMode == ReasonMode.TEMPLATE) {
                DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                        .create(Guis.KICK_TEMPLATE, new KickTemplateGui(player, target.getUUID())).open();
            } else if(kickMode == ReasonMode.SELF) {
                DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                        .create(Guis.KICK_SELF, new KickSelfGui(player, target.getUUID())).open();

            }
        } else {

        }
        return true;
    }
}