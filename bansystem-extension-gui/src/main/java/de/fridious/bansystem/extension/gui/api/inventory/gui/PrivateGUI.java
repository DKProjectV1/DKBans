package de.fridious.bansystem.extension.gui.api.inventory.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.LinkedList;
import java.util.List;

/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 30.12.18 20:25
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

public abstract class PrivateGUI<T> extends GUI<T> {

    public static final List<PrivateGUI> ANVIL_GUIS;

    static {
        ANVIL_GUIS = new LinkedList<>();
    }

    private static final ReflectionAnvil REFLECTION_ANVIL;

    static {
        REFLECTION_ANVIL = new ReflectionAnvil();
    }

    private final Player owner;
    //Only for anvil gui
    private Object container;
    private int containerId;

    public PrivateGUI(Player owner) {
        this.owner = owner;
    }

    public PrivateGUI(Inventory inventory, Player owner) {
        super(inventory);
        this.owner = owner;
    }

    public PrivateGUI(String name, int size, Player owner) {
        super(name, size);
        this.owner = owner;
    }

    public PrivateGUI(InventoryType inventoryType, Player owner) {
        this.owner = owner;
        if(inventoryType == InventoryType.ANVIL) {
            ANVIL_GUIS.add(this);
            this.container = REFLECTION_ANVIL.newContainerAnvil(owner);
            this.inventory = REFLECTION_ANVIL.toBukkitInventory(container);
            this.containerId = REFLECTION_ANVIL.getNextContainerId(owner);
        }
    }

    public Player getOwner() {
        return owner;
    }

    public void open(){
        if(this.inventory.getType() == InventoryType.ANVIL && !owner.getOpenInventory().getTopInventory().equals(inventory)) {
            REFLECTION_ANVIL.sendPacketOpenWindow(owner, containerId);
            REFLECTION_ANVIL.setActiveContainer(owner, container);
            REFLECTION_ANVIL.setActiveContainerId(owner, containerId);
            REFLECTION_ANVIL.addActiveContainerSlotListener(owner);
        } else open(this.owner);

    }
}