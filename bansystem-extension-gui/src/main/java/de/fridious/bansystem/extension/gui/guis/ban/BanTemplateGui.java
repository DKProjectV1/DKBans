/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 06.09.19, 22:57
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

package de.fridious.bansystem.extension.gui.guis.ban;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.reason.BanReason;
import de.fridious.bansystem.extension.gui.DKBansGuiExtension;
import de.fridious.bansystem.extension.gui.api.inventory.gui.MessageAnvilInputGui;
import de.fridious.bansystem.extension.gui.api.inventory.gui.PrivateGui;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemStorage;
import de.fridious.bansystem.extension.gui.guis.Guis;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class BanTemplateGui extends PrivateGui<BanReason> {

    public BanTemplateGui(Player player, UUID target) {
        super(54, target, player);
        setPageEntries(getInteractBanReasons());
        setItem(45, ItemStorage.get("templateban_editmessage", replace -> replace.replace("[message]", getMessage())));
    }

    private List<BanReason> getInteractBanReasons() {
        List<BanReason> banReasons = new LinkedList<>();
        for(BanReason reason : BanSystem.getInstance().getReasonProvider().getBanReasons()) {
            if((!BanSystem.getInstance().getPlayerManager().getPlayer(getTarget()).hasBypass() || getOwner().hasPermission("dkbans.bypass.ignore"))
                    && !reason.isHidden() && getOwner().hasPermission("dkbans.ban")
                    && getOwner().hasPermission(reason.getPermission())) banReasons.add(reason);
        }
        return banReasons;
    }

    @Override
    public void updatePage(Event event) {
        if(event == null || getUpdateEvents().contains(event.getClass())) {
            setItem(45, ItemStorage.get("templateban_editmessage", replace -> replace.replace("[message]", getMessage())));
        }
    }

    @Override
    public void setPageItem(int slot, BanReason reason) {
        setItem(slot, ItemStorage.get("templateban_reason", reason));
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {

    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        BanReason banReason = getEntryBySlot().get(event.getSlot());
        if(banReason != null) {
            if(player.hasPermission("dkbans.ban") &&
                    player.hasPermission(banReason.getPermission())
                    && (!BanSystem.getInstance().getPlayerManager().getPlayer(getTarget()).hasBypass() || player.hasPermission("dkbans.bypass.ignore"))) {
                NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(getTarget());
                Ban ban = networkPlayer.ban(banReason, getMessage(), player.getUniqueId());
                if(ban.getBanType() == BanType.NETWORK) player.sendMessage(ban.replace(Messages.BAN_NETWORK_SUCCESS,false));
                else player.sendMessage(ban.replace(Messages.BAN_CHAT_SUCCESS,false));
                player.closeInventory();
            }
        } else if(event.getSlot() == 45) {
            Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                    DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                            .create(Guis.ANVIL_INPUT, new MessageAnvilInputGui(this)).open());
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis((Player) event.getPlayer()).remove(Guis.BAN_TEMPLATE);
    }
}