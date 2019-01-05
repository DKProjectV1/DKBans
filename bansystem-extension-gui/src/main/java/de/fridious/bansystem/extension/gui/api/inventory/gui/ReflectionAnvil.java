package de.fridious.bansystem.extension.gui.api.inventory.gui;

import ch.dkrieger.bansystem.bukkit.utils.Reflection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 04.01.19 22:01
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

public class ReflectionAnvil {
    
    public int getNextContainerId(Player player) {
        try {
            Object handle = handleCraftPlayer(player);
            Class<?> handleClass = handle.getClass();
            Method nextContainerCounter = handleClass.getDeclaredMethod("nextContainerCounter");
            Object count = nextContainerCounter.invoke(handle);
            return (int) count;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 88;
    }

    public Object handleCraftPlayer(Player player) {
        try {
            Class<?> craftPlayerClass = Reflection.getCraftBukkitClass("entity.CraftPlayer");
            Method getHandle = craftPlayerClass.getDeclaredMethod("getHandle");
            return getHandle.invoke(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void handleInventoryCloseEvent(Player player) {
        try {
            Class<?> craftEventFactory = Reflection.getCraftBukkitClass("event.CraftEventFactory");
            Method handleInventoryCloseEvent = craftEventFactory.getDeclaredMethod("handleInventoryCloseEvent", Reflection.getMinecraftClass("EntityHuman"));
            handleInventoryCloseEvent.invoke(null, handleCraftPlayer(player));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void sendPacketOpenWindow(Player player, int containerId) {
        try {
            Class<?> chatMessageClass = Reflection.getMinecraftClass("ChatMessage");
            Object chatMessage = chatMessageClass.getConstructor(String.class, Object[].class).newInstance("Repairing", new Object[]{});

            Class<?> iChatBaseComponent = Reflection.getMinecraftClass("IChatBaseComponent");
            Class<?> packetClass = Reflection.getMinecraftClass("PacketPlayOutOpenWindow");
            Object packet = packetClass.getConstructor(int.class, String.class, iChatBaseComponent, int.class)
                    .newInstance(containerId, "minecraft:anvil", chatMessage, 0);
            Reflection.sendPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendPacketCloseWindow(Player player, int containerId) {
        try {
            Class<?> packetClass = Reflection.getMinecraftClass("PacketPlayOutCloseWindow");
            Object packet = packetClass.getConstructor(int.class).newInstance(containerId);
            Reflection.sendPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setActiveContainerDefault(Player player) {
        try {
            Object handle = handleCraftPlayer(player);
            Class<?> entityHumanClass = Reflection.getMinecraftClass("EntityHuman");
            Field activeContainer = entityHumanClass.getDeclaredField("activeContainer");
            activeContainer.setAccessible(true);
            Field defaultContainerField = entityHumanClass.getDeclaredField("defaultContainer");
            Object defaultContainer = defaultContainerField.get(handle);
            activeContainer.set(handle, defaultContainer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setActiveContainer(Player player, Object container) {
        try {
            Class<?> entityHumanClass = Reflection.getMinecraftClass("EntityHuman");
            Class<?> containerClass = Reflection.getMinecraftClass("Container");
            Object handle = handleCraftPlayer(player);
            Field activeContainer = entityHumanClass.getDeclaredField("activeContainer");
            activeContainer.setAccessible(true);
            activeContainer.set(handle, containerClass.cast(container));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  
    public void setActiveContainerId(Player player, int containerId) {
        try {
            Class<?> containerClass = Reflection.getMinecraftClass("Container");
            Class<?> entityHumanClass = Reflection.getMinecraftClass("EntityHuman");

            Object handle = handleCraftPlayer(player);
            Field activeContainerField = entityHumanClass.getDeclaredField("activeContainer");

            Object activeContainer = activeContainerField.get(handle);
            Field windowIdField = containerClass.getDeclaredField("windowId");
            windowIdField.set(activeContainer, containerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addActiveContainerSlotListener(Player player) {
        try {
            Class<?> entityHumanClass = Reflection.getMinecraftClass("EntityHuman");
            Class<?> containerClass = Reflection.getMinecraftClass("Container");
            Class<?> iCraftingClass = Reflection.getMinecraftClass("ICrafting");

            Object handle = handleCraftPlayer(player);
            Field activeContainerField = entityHumanClass.getDeclaredField("activeContainer");

            Object activeContainer = activeContainerField.get(handle);
            Method addSlotListener = containerClass.getDeclaredMethod("addSlotListener", iCraftingClass);
            addSlotListener.invoke(activeContainer, handle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Inventory toBukkitInventory(Object container) {
        try {
            Class<?> containerClass = container.getClass();
            Method getBukkitView = containerClass.getDeclaredMethod("getBukkitView");
            InventoryView inventoryView = (InventoryView) getBukkitView.invoke(container);
            Inventory inventory = inventoryView.getTopInventory();
            return inventory;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
   
    public Object newContainerAnvil(Player player) {
        try {
            Object handle = handleCraftPlayer(player);
            Class<?> entityHumanClass = Reflection.getMinecraftClass("EntityHuman");
            Class<?> entityClass = Reflection.getMinecraftClass("Entity");

            Class<?> inventoryClass = Reflection.getMinecraftClass("PlayerInventory");
            Field inventoryField = entityHumanClass.getDeclaredField("inventory");
            Object inventory = inventoryField.get(handle);

            Class<?> worldClass = Reflection.getMinecraftClass("World");
            Field worldField = entityClass.getDeclaredField("world");
            Object world = worldField.get(handle);

            Class<?> blockPositionClass = Reflection.getMinecraftClass("BlockPosition");
            Object blockPosition = blockPositionClass.getConstructor(int.class, int.class, int.class).newInstance(0, 0, 0);

            Class<?> containerAnvilClass = Reflection.getMinecraftClass("ContainerAnvil");
            Object containerAnvil = containerAnvilClass.getConstructor(inventoryClass, worldClass, blockPositionClass, entityHumanClass).newInstance(inventory, world, blockPosition, handle);

            Class<?> containerClass = Reflection.getMinecraftClass("Container");
            Field checkReachableField = containerClass.getDeclaredField("checkReachable");
            checkReachableField.set(containerAnvil, false);
            return containerAnvil;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}