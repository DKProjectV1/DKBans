package de.fridious.bansystem.extension.gui.guis.playerinfo;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import de.fridious.bansystem.extension.gui.DKBansGuiExtension;
import de.fridious.bansystem.extension.gui.api.inventory.gui.PrivateGUI;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemBuilder;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemStorage;
import de.fridious.bansystem.extension.gui.guis.GUIS;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 04.01.19 12:56
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

public class PlayerInfoGlobalGui extends PrivateGUI<Player> {

    public static String INVENTORY_TITLE;
    public static List<Class<? extends Event>> UPDATE_EVENTS = Arrays.asList(PlayerJoinEvent.class, PlayerQuitEvent.class);
    private String title;

    public PlayerInfoGlobalGui(Player owner) {
        super(owner);
        this.title = INVENTORY_TITLE;
        createInventory(this.title, 54);
        setPageEntries(new LinkedList<>(Bukkit.getOnlinePlayers()));
        getUpdateEvents().addAll(UPDATE_EVENTS);
    }

    @Override
    public void beforeUpdatePage() {
        setPageEntries(new LinkedList<>(Bukkit.getOnlinePlayers()));
    }

    @Override
    public void setPageItem(int slot, Player player) {
        NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
        setItem(slot, new ItemBuilder(ItemStorage.get("globalplayerinfo_skull", networkPlayer)).setGameProfile(networkPlayer.getName()));
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {

    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        Player target = getEntryBySlot().get(event.getSlot());
        if(target != null) {
            if(player.hasPermission("dkbans.playerinfo")) {
                Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                        DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories(player).create(GUIS.PLAYERINFO_PLAYER, new PlayerInfoGui(player, target.getUniqueId())).open());
            }
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {

    }
}