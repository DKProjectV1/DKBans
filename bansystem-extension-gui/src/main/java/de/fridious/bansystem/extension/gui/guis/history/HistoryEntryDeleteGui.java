package de.fridious.bansystem.extension.gui.guis.history;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 05.01.19 23:24
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
import de.fridious.bansystem.extension.gui.guis.GuiManager;
import de.fridious.bansystem.extension.gui.guis.Guis;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class HistoryEntryDeleteGui extends PrivateGui {

    private static List<Integer> ACCEPT_SLOTS = Arrays.asList(0, 1, 2, 9, 10, 11, 18, 19, 20);
    private static List<Integer> DENY_SLOTS = Arrays.asList(6, 7, 8, 15, 16, 17, 24, 25, 26);
    private HistoryEntry historyEntry;
    private HistoryAllGui historyAllGui;
    private boolean openParentGui;

    public HistoryEntryDeleteGui(Player owner, UUID target, HistoryEntry historyEntry, HistoryAllGui historyAllGui) {
        super(27, target, owner);
        this.historyEntry = historyEntry;
        this.historyAllGui = historyAllGui;
        this.openParentGui = false;
        if(historyEntry instanceof Ban) {
            if(((Ban) historyEntry).getBanType() == BanType.CHAT) setItem(13, ItemStorage.get("history_ban_chat", (Ban) historyEntry));
            else setItem(13, ItemStorage.get("history_ban_network", (Ban) historyEntry));
        } else if(historyEntry instanceof Warn) {
            setItem(13, ItemStorage.get("history_warn", (Warn) historyEntry));
        } else if(historyEntry instanceof Kick) {
            setItem(13, ItemStorage.get("history_kick", (Kick) historyEntry));
        } else if(historyEntry instanceof Unban) {
            setItem(13, ItemStorage.get("history_unban", (Unban) historyEntry));
        }
        setItem(ItemStorage.get("historydelete_accept", historyEntry), ACCEPT_SLOTS);
        setItem(ItemStorage.get("historydelete_deny", historyEntry), DENY_SLOTS);
        fill(ItemStorage.get("placeholder"));
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {

    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        if(ACCEPT_SLOTS.contains(event.getSlot())) {
            NetworkPlayer targetNetworkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(getTarget());
            targetNetworkPlayer.resetHistory(historyEntry.getID());

            if(this.historyAllGui != null) {
                this.openParentGui = true;
                Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()-> historyAllGui.open());
            }
        } else if(DENY_SLOTS.contains(event.getSlot())) {
            if(this.historyAllGui != null) {
                this.openParentGui = true;
                Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()-> historyAllGui.open());
            }
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();
        GuiManager.CachedGuis cachedGuis = DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player);
        cachedGuis.remove(Guis.HISTORY_DELETE);
        if(this.historyAllGui != null && !this.openParentGui) cachedGuis.remove(Guis.HISTORY_ALL);
    }
}