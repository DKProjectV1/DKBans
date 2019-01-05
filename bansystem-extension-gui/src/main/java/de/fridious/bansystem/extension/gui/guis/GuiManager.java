package de.fridious.bansystem.extension.gui.guis;

import de.fridious.bansystem.extension.gui.DKBansGuiExtension;
import de.fridious.bansystem.extension.gui.api.inventory.gui.GUI;
import de.fridious.bansystem.extension.gui.api.inventory.gui.PrivateGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 31.12.18 17:34
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

public class GuiManager {

    private Map<Player, CachedInventories> cachedInventories;

    public GuiManager() {
        this.cachedInventories = new LinkedHashMap<>();
    }

    public Map<Player, CachedInventories> getAllCachedInventories() {
        return cachedInventories;
    }

    public CachedInventories getCachedInventories(Player player) {
        if(!cachedInventories.containsKey(player)) cachedInventories.put(player, new CachedInventories(player));
        return cachedInventories.get(player);
    }

    public void updateAllCachedInventories(Event event) {
        for (CachedInventories openedInventories : getAllCachedInventories().values()) {
            openedInventories.getAll().forEach(gui -> gui.updatePage(event));
        }

    }

    public class CachedInventories {

        private final Player player;
        private final Map<String, GUI> inventories;

        public CachedInventories(Player player) {
            this.player = player;
            this.inventories = new LinkedHashMap<>();
        }

        public boolean hasCached(String inventory) {
            return inventories.containsKey(inventory);
        }

        public Player getPlayer() {
            return player;
        }

        public GUI getAsGui(String inventory) {
            return this.inventories.get(inventory);
        }

        public PrivateGUI getAsPrivateGui(String inventory) {
            return (PrivateGUI) getAsGui(inventory);
        }

        public Collection<GUI> getAll() {
            return this.inventories.values();
        }

        public GUI create(String inventory, GUI gui) {
            this.inventories.put(inventory, gui);
            return gui;
        }

        public PrivateGUI create(String inventory, PrivateGUI privateGUI) {
            this.inventories.put(inventory, privateGUI);
            return privateGUI;
        }

        public void remove(String inventory) {
            this.inventories.remove(inventory);
        }
    }
}