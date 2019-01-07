package de.fridious.bansystem.extension.gui.listeners;

/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 30.12.18 21:50
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
import de.fridious.bansystem.extension.gui.api.inventory.gui.Gui;
import de.fridious.bansystem.extension.gui.api.inventory.gui.PrivateGui;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if(!(event.getWhoClicked() instanceof Player) || event.getInventory() == null ||
                event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR ||
                event.getCurrentItem().getItemMeta() == null) return;
        if(event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof Gui) {
            event.setCancelled(true);
            Bukkit.getScheduler().runTaskAsynchronously(DKBansGuiExtension.getInstance(), ()-> ((Gui)event.getInventory().getHolder()).handleClick(event));
        } else {
            for (PrivateGui privateGUI : PrivateGui.ANVIL_GUIS) {
                if (privateGUI.getInventory().equals(event.getInventory())) {
                    event.setCancelled(true);
                    Bukkit.getScheduler().runTaskAsynchronously(DKBansGuiExtension.getInstance(), () -> privateGUI.handleClick(event));
                }
            }
        }
    }
}