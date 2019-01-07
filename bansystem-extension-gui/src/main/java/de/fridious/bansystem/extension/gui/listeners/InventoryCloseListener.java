package de.fridious.bansystem.extension.gui.listeners;

/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 30.12.18 21:56
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

import de.fridious.bansystem.extension.gui.api.inventory.gui.Gui;
import de.fridious.bansystem.extension.gui.api.inventory.gui.PrivateGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(!(event.getPlayer() instanceof Player) || (event.getInventory() == null)) return;
        if(event.getInventory().getHolder() != null && event.getInventory().getHolder() instanceof Gui)
            ((Gui)event.getInventory().getHolder()).handleClose(event);
        else {
            PrivateGui.ANVIL_GUIS.forEach(privateGUI -> {
                if(privateGUI.getInventory().equals(event.getInventory()))
                    privateGUI.handleClose(event);
            });
        }
    }
}