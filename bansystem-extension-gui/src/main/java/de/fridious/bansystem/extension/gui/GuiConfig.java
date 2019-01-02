package de.fridious.bansystem.extension.gui;

/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 30.12.18 14:55
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

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.config.SimpleConfig;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemBuilder;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemStorage;
import de.fridious.bansystem.extension.gui.guis.PlayerInfoGui;
import de.fridious.bansystem.extension.gui.guis.TemplateBanGui;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class GuiConfig extends SimpleConfig {

    public GuiConfig() {
        super(new File(BanSystem.getInstance().getPlatform().getFolder(), "gui.yml"));
    }

    @Override
    public void onLoad() {
        PlayerInfoGui.INVENTORY_TITLE = addAndGetMessageValue("playerinfo.title", "&4PlayerInfo");
        TemplateBanGui.INVENTORY_TITLE = addAndGetMessageValue("ban.title", "&4Ban");
        ItemStorage.put("playerinfo_skull", addAndGetItemStack("playerinfo.items.skull", new ItemBuilder(Material.SKULL_ITEM, 1, (short) 3).setDisplayName("[player]").setLore("test").build()));
        ItemStorage.put("playerinfo_ban", addAndGetItemStack("playerinfo.items.ban", new ItemBuilder(Material.BARRIER).setDisplayName("&4Ban").setLore("&7Ban the player").build()));
        ItemStorage.put("playerinfo_unban", addAndGetItemStack("playerinfo.items.unban", new ItemBuilder(Material.IRON_FENCE).setDisplayName("&4Unban").setLore("&7Unban the player").build()));
        ItemStorage.put("playerinfo_kick", addAndGetItemStack("playerinfo.items.kick", new ItemBuilder(Material.RABBIT_FOOT).setDisplayName("&4Kick").setLore("&7Kick the player").build()));
        ItemStorage.put("playerinfo_jumpto", addAndGetItemStack("playerinfo.items.jumpto", new ItemBuilder(Material.ENDER_PEARL).setDisplayName("&4Jump").setLore("&7Jump to the player").build()));
        ItemStorage.put("playerinfo_showhistory", addAndGetItemStack("playerinfo.items.showhistory", new ItemBuilder(Material.BOOK_AND_QUILL).setDisplayName("&bShow history").setLore("&7Show the history of the player").build()));
        ItemStorage.put("templateban_reason", addAndGetItemStack("ban.template.items.reason", new ItemBuilder(Material.PAPER).setDisplayName("[reason]").build()));
    }

    private ItemStack addAndGetItemStack(String path, ItemStack itemStack) {
        ItemBuilder itemBuilder;
        if(DKBansGuiExtension.getInstance().isConfigItemIds())
            itemBuilder = new ItemBuilder(addAndGetMessageValue(path+".item", itemStack.getData().getData()));
        else
            itemBuilder = new ItemBuilder(Material.getMaterial(addAndGetMessageValue(path+".item", itemStack.getType().toString().toLowerCase()).toUpperCase()));
        itemBuilder.setDisplayName(addAndGetMessageValue(path+".displayname", itemStack.getItemMeta().getDisplayName()))
                .setLore(addAndGetMessageListValue(path+".lore", itemStack.getItemMeta().getLore()));
        return itemBuilder.build();
    }
}