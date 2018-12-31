package de.fridious.bansystem.extension.gui.api.inventory.item;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.reason.BanReason;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import de.fridious.bansystem.extension.gui.utils.GuiExtensionUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 30.12.18 20:24
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

public class ItemStorage {

    private static Map<String, ItemStack> itemStacks;

    static {
        itemStacks = new LinkedHashMap<>();
    }

    public static ItemStack get(String key) {
        return itemStacks.get(key).clone();
    }

    public static ItemStack get(String key, NetworkPlayer player) {
        return get(key, replace -> GuiExtensionUtils.replaceNetworkPlayer(replace, player));
    }

    public static ItemStack get(String key, BanReason reason) {
        return get(key, replace -> GuiExtensionUtils.replaceBanReason(replace, reason));
    }

    public static ItemStack get(String key, StringReplacer replacer) {
        ItemStack itemStack = get(key);
        if(itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta().clone();
            if(itemStack.getItemMeta().hasLore()) {
                List<String> replacedLore = new LinkedList<>();
                for (String lore : itemStack.getItemMeta().getLore()) {
                    replacedLore.add(replacer.replace(lore));
                }
                itemMeta.setLore(replacedLore);
            }
            if(itemStack.getItemMeta().hasDisplayName()) {
                String displayName = replacer.replace(itemStack.getItemMeta().getDisplayName());
                itemMeta.setDisplayName(displayName);
            }
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    public static boolean contains(String key) {
        return itemStacks.containsKey(key);
    }

    public static ItemStack put(String key, ItemStack itemStack) {
        itemStacks.put(key, itemStack);
        return itemStack;
    }

    public interface StringReplacer {
        String replace(String replace);
    }
}