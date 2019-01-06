package de.fridious.bansystem.extension.gui.guis.ban;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 05.01.19 01:34
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

import de.fridious.bansystem.extension.gui.api.inventory.gui.PrivateGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.UUID;

public class BanSelfGui extends PrivateGUI {

    /*
    Reason - anvil
    BAN/MUTE - BARRIER/EMPTY MAP
    Duration - CLOCK -> Anvil inv
    TimeUnit - Seconds(Lime Green), Minutes(Green), Hours(Yellow), Days(Red)
     */


    public BanSelfGui(Player owner, UUID target) {
        super(owner);
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {

    }

    @Override
    protected void onClick(InventoryClickEvent event) {

    }

    @Override
    protected void onClose(InventoryCloseEvent event) {

    }
}