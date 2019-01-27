package de.fridious.bansystem.extension.gui.guis;

import de.fridious.bansystem.extension.gui.api.inventory.gui.Gui;
import de.fridious.bansystem.extension.gui.api.inventory.gui.PrivateGui;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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

    private Map<Player, CachedGuis> cachedGuis;
    private Map<Class<? extends Gui>, GuiData> guiData;

    public GuiManager() {
        this.cachedGuis = new ConcurrentHashMap<>();
        this.guiData = new ConcurrentHashMap<>();
    }

    public Map<Player, CachedGuis> getAllCachedGuis() {
        return cachedGuis;
    }

    public Map<Class<? extends Gui>, GuiData> getGuiData() {
        return guiData;
    }

    public GuiData getGuiData(Class<? extends Gui> guiClass) {
        return getGuiData().get(guiClass);
    }

    public boolean isGuiEnabled(Class<? extends Gui> guiClass) {
        return getGuiData(guiClass).isEnabled();
    }

    public CachedGuis getCachedGuis(Player player) {
        if(!cachedGuis.containsKey(player)) cachedGuis.put(player, new CachedGuis(player));
        return cachedGuis.get(player);
    }

    //@Todo better caching and synchronisation
    public void updateAllCachedGuis(Event event, UUID target) {
        for (CachedGuis openedGuis : getAllCachedGuis().values()) {
            for (Gui gui : openedGuis.getAll()) {
                gui.updatePage(event);
            }
        }
    }

    public class CachedGuis {

        private final Player player;
        private final Map<String, Gui> guis;

        public CachedGuis(Player player) {
            this.player = player;
            this.guis = new ConcurrentHashMap<>();
        }

        public boolean hasCached(String gui) {
            return guis.containsKey(gui);
        }

        public Player getPlayer() {
            return player;
        }

        public Gui getAsGui(String guiName) {
            return this.guis.get(guiName);
        }

        public PrivateGui getAsPrivateGui(String guiName) {
            return (PrivateGui) getAsGui(guiName);
        }

        public Collection<Gui> getAll() {
            return this.guis.values();
        }

        public Gui create(String guiName, Gui gui) {
            this.guis.put(guiName, gui);
            return gui;
        }

        public PrivateGui create(String guiName, PrivateGui privateGUI) {
            this.guis.put(guiName, privateGUI);
            return privateGUI;
        }

        public void remove(String gui) {
            this.guis.remove(gui);
        }
    }
}