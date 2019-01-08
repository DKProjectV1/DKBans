package de.fridious.bansystem.extension.gui.api.inventory.gui;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 06.01.19 00:42
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

import de.fridious.bansystem.extension.gui.DKBansGuiExtension;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemBuilder;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemStorage;
import de.fridious.bansystem.extension.gui.guis.Guis;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

public abstract class AnvilInputGui extends PrivateGui {

    private PrivateGui gui;

    public AnvilInputGui(PrivateGui gui, String input) {
        super(InventoryType.ANVIL, gui.getOwner());
        setItem(AnvilSlot.INPUT_LEFT, new ItemBuilder(ItemStorage.get("custom_message"))
                .setDisplayName((input == null || input.equalsIgnoreCase("")) ? " " : input));
        this.gui = gui;
    }

    public PrivateGui getGui() {
        return gui;
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {

    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        if(event.getRawSlot() == AnvilSlot.OUTPUT) {
            String input = getInput();
            if(input != null) {
                boolean successful = setInput(input.trim());
                if(successful) {
                    inventory.clear();
                    if(gui != null) {
                        gui.updatePage(null);
                        Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()-> gui.open());
                    }
                }
            }
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis((Player) event.getPlayer()).remove(Guis.ANVIL_INPUT);
        event.getInventory().clear();
    }

    public abstract boolean setInput(String input);
}