package de.fridious.bansystem.extension.gui.guis.unban;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 07.01.19 20:53
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
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Unban;
import ch.dkrieger.bansystem.lib.reason.UnbanReason;
import de.fridious.bansystem.extension.gui.DKBansGuiExtension;
import de.fridious.bansystem.extension.gui.api.inventory.gui.PrivateGui;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemStorage;
import de.fridious.bansystem.extension.gui.guis.Guis;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.UUID;

public class UnBanChooseBanTypeGui extends PrivateGui {

    private UnbanReason reason;

    public UnBanChooseBanTypeGui(Player owner, UUID target, UnbanReason reason) {
        super(27, target, owner);
        this.reason = reason;
        setItem(11, ItemStorage.get("chooseunban_network"));
        setItem(14, ItemStorage.get("chooseunban_chat"));
        fill(ItemStorage.get("placeholder"));
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {

    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        if(!player.hasPermission("dkbans.unban") &&
                player.hasPermission(reason.getPermission())
                && (!BanSystem.getInstance().getPlayerManager().getPlayer(getTarget()).hasBypass() || player.hasPermission("dkbans.bypass.ignore")))return;
        NetworkPlayer targetNetworkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(getTarget());
        int slot = event.getSlot();
        if(slot == 11 || slot == 14) {
            Unban unban = targetNetworkPlayer.unban(slot == 11 ? BanType.NETWORK : BanType.CHAT, reason, getMessage(), player.getUniqueId());
            if(unban.getBanType() == BanType.NETWORK) {
                player.sendMessage(Messages.PLAYER_UNBANNED
                        .replace("[prefix]", Messages.PREFIX_BAN)
                        .replace("[reason]",unban.getReason())
                        .replace("[message]",unban.getMessage())
                        .replace("[staff]",unban.getStaffName())
                        .replace("[id]",""+unban.getID())
                        .replace("[points]",""+unban.getPoints())
                        .replace("[player]", targetNetworkPlayer.getColoredName()));
            } else if(unban.getBanType() == BanType.CHAT) {
                player.sendMessage(Messages.PLAYER_UNMUTED
                        .replace("[prefix]", Messages.PREFIX_BAN)
                        .replace("[reason]",unban.getReason())
                        .replace("[message]",unban.getMessage())
                        .replace("[staff]",unban.getStaffName())
                        .replace("[id]",""+unban.getID())
                        .replace("[points]",""+unban.getPoints())
                        .replace("[player]", targetNetworkPlayer.getColoredName()));
            }
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis((Player) event.getPlayer()).remove(Guis.UNBAN_CHOOSE_BANTYPE);
    }
}