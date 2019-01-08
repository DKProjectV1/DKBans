package de.fridious.bansystem.extension.gui.guis.history;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 05.01.19 16:46
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
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.*;
import de.fridious.bansystem.extension.gui.DKBansGuiExtension;
import de.fridious.bansystem.extension.gui.api.inventory.gui.PrivateGui;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemStorage;
import de.fridious.bansystem.extension.gui.guis.Guis;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.UUID;

public class HistoryAllGui extends PrivateGui<HistoryEntry> {

    public HistoryAllGui(Player owner, UUID target) {
        super(54, target, owner);
        NetworkPlayer targetNetworkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(target);
        setPageEntries(targetNetworkPlayer.getHistory().getEntriesSorted());
        if(getOwner().hasPermission("dkbans.history.reset") && !getPageEntries().isEmpty())
            setItem(45, ItemStorage.get("history_clear", replace -> replace.replace("[player]", targetNetworkPlayer.getColoredName())));
    }

    @Override
    public void afterUpdatePage() {
        NetworkPlayer targetNetworkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(getTarget());
        if(getOwner().hasPermission("dkbans.history.reset") && !getPageEntries().isEmpty())
            setItem(45, ItemStorage.get("history_clear", replace -> replace.replace("[player]", targetNetworkPlayer.getColoredName())));
    }

    @Override
    public void beforeUpdatePage() {
        NetworkPlayer targetNetworkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(getTarget());
        setPageEntries(targetNetworkPlayer.getHistory().getEntriesSorted());
    }

    @Override
    public void setPageItem(int slot, HistoryEntry historyEntry) {
        if(historyEntry instanceof Ban) {
            if(((Ban) historyEntry).getBanType() == BanType.CHAT) setItem(slot, ItemStorage.get("history_ban_chat", (Ban) historyEntry));
            else setItem(slot, ItemStorage.get("history_ban_network", (Ban) historyEntry));
        } else if(historyEntry instanceof Warn) {
            setItem(slot, ItemStorage.get("history_warn", (Warn) historyEntry));
        } else if(historyEntry instanceof Kick) {
            setItem(slot, ItemStorage.get("history_kick", (Kick) historyEntry));
        } else if(historyEntry instanceof Unban) {
            setItem(slot, ItemStorage.get("history_unban", (Unban) historyEntry));
        }
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {
        setChildGui(false);
    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        HistoryEntry historyEntry = getEntryBySlot().get(event.getSlot());
        if(historyEntry != null && player.hasPermission("dkbans.history.reset")) {
            setChildGui(true);
            Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                    DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                            .create(Guis.HISTORY_DELETE, new HistoryEntryDeleteGui(getOwner(), getTarget(), historyEntry, this)).open());
        } else if(event.getSlot() == 45 && player.hasPermission("dkbans.history.reset")) {
            NetworkPlayer targetNetworkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(getTarget());
            targetNetworkPlayer.resetHistory();
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        if(!hasChildGui())DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis((Player) event.getPlayer()).remove(Guis.HISTORY_ALL);
    }
}