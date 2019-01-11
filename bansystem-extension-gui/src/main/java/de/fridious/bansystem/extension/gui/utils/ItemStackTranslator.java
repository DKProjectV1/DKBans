package de.fridious.bansystem.extension.gui.utils;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 10.01.19 21:36
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
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ItemStackTranslator {

    //Glass panes
    WHITE_STAINED_GLASS_PANE("STAINED_GLASS_PANE", 0, "WHITE_STAINED_GLASS_PANE"),
    ORANGE_STAINED_GLASS_PANE("STAINED_GLASS_PANE", 1, "ORANGE_STAINED_GLASS_PANE"),
    MAGENTA_STAINED_GLASS_PANE("STAINED_GLASS_PANE", 2, "MAGENTA_STAINED_GLASS_PANE"),
    LIGHT_BLUE_STAINED_GLASS_PANE("STAINED_GLASS_PANE", 3, "LIGHT_BLUE_STAINED_GLASS_PANE"),
    YELLOW_STAINED_GLASS_PANE("STAINED_GLASS_PANE", 4, "YELLOW_STAINED_GLASS_PANE"),
    LIME_STAINED_GLASS_PANE("STAINED_GLASS_PANE", 5, "LIME_STAINED_GLASS_PANE"),
    PINK_STAINED_GLASS_PANE("STAINED_GLASS_PANE", 6, "PINK_STAINED_GLASS_PANE"),
    GRAY_STAINED_GLASS_PANE("STAINED_GLASS_PANE", 7, "GRAY_STAINED_GLASS_PANE"),
    LIGHT_GRAY_STAINED_GLASS_PANE("STAINED_GLASS_PANE", 8, "LIGHT_GRAY_STAINED_GLASS_PANE"),
    CYAN_STAINED_GLASS_PANE("STAINED_GLASS_PANE", 9, "CYAN_STAINED_GLASS_PANE"),
    PURPLE_STAINED_GLASS_PANE("STAINED_GLASS_PANE", 10, "PURPLE_STAINED_GLASS_PANE"),
    BLUE_STAINED_GLASS_PANE("STAINED_GLASS_PANE", 11, "BLUE_STAINED_GLASS_PANE"),
    BROWN_STAINED_GLASS_PANE("STAINED_GLASS_PANE", 12, "BROWN_STAINED_GLASS_PANE"),
    GREEN_STAINED_GLASS_PANE("STAINED_GLASS_PANE", 13, "GREEN_STAINED_GLASS_PANE"),
    RED_STAINED_GLASS_PANE("STAINED_GLASS_PANE", 14, "RED_STAINED_GLASS_PANE"),
    BLACK_STAINED_GLASS_PANE("STAINED_GLASS_PANE", 15, "BLACK_STAINED_GLASS_PANE"),
    //Clay
    CLAY("CLAY", "CLAY_BALL"),
    WHITE_STAINED_CLAY("STAINED_CLAY", 0, "WHITE_TERRACOTTA"),
    ORANGE_STAINED_CLAY("STAINED_CLAY", 1, "ORANGE_TERRACOTTA"),
    MAGENTA_STAINED_CLAY("STAINED_CLAY", 2, "MAGENTA_TERRACOTTA"),
    LIGHT_BLUE_STAINED_CLAY("STAINED_CLAY", 3, "LIGHT_BLUE_WHITE_TERRACOTTA"),
    YELLOW_STAINED_CLAY("STAINED_CLAY", 4, "YELLOW_TERRACOTTA"),
    LIME_STAINED_CLAY("STAINED_CLAY", 5, "LIME_TERRACOTTA"),
    PINK_STAINED_CLAY("STAINED_CLAY", 6, "PINK_TERRACOTTA"),
    GRAY_STAINED_CLAY("STAINED_CLAY", 7, "GRAY_TERRACOTTA"),
    LIGHT_GRAY_STAINED_CLAY("STAINED_CLAY", 8, "LIGHT_GRAY_TERRACOTTA"),
    CYAN_STAINED_CLAY("STAINED_CLAY", 9, "CYAN_TERRACOTTA"),
    PURPLE_STAINED_CLAY("STAINED_CLAY", 10, "PURPLE_TERRACOTTA"),
    BLUE_STAINED_CLAY("STAINED_CLAY", 11, "BLUE_TERRACOTTA"),
    BROWN_STAINED_CLAY("STAINED_CLAY", 12, "BROWN_TERRACOTTA"),
    GREEN_STAINED_CLAY("STAINED_CLAY", 13, "GREEN_TERRACOTTA"),
    RED_STAINED_CLAY("STAINED_CLAY", 14, "RED_TERRACOTTA"),
    BLACK_STAINED_CLAY("STAINED_CLAY", 15, "BLACK_TERRACOTTA"),
    //DYE
    BLACK_DYE("INK_SACK", 0, "INK_SAC"),
    RED_DYE("INK_SACK", 1, "ROSE_RED"),
    GREEN_DYE("INK_SACK", 2, "CACTUS_GREEN"),
    BROWN_DYE("INK_SACK", 3, "COCOA_BEANS"),
    BLUE_DYE("INK_SACK", 4, "LAPIS_LAZULI"),
    PURPLE_DYE("INK_SACK", 5, "PURPLE_DYE"),
    CYAN_DYE("INK_SACK", 6, "CYAN_DYE"),
    LIGHT_GRAY_DYE("INK_SACK", 7, "LIGHT_GRAY_DYE"),
    GRAY_DYE("INK_SACK", 8, "GRAY_DYE"),
    PINK_DYE("INK_SACK", 9, "PINK_DYE"),
    LIME_DYE("INK_SACK", 10, "LIME_DYE"),
    YELLOW_DYE("INK_SACK", 11, "DANDELION_YELLOW"),
    LIGHT_BLUE_DYE("INK_SACK", 12, "LIGHT_BLUE_DYE"),
    MAGENTA_DYE("INK_SACK", 13, "MAGENTA_DYE"),
    ORANGE_DYE("INK_SACK", 10, "ORANGE_DYE"),
    WHITE_DYE("INK_SACK", 10, "BONE_MEAL"),

    IRON_FENCE("IRON_FENCE", "IRON_BARS"),
    WATCH("WATCH", "CLOCK"),
    EMPTY_MAP("EMPTY_MAP", "FILLED_MAP"),
    BOOK_AND_QUILL("BOOK_AND_QUILL", "WRITABLE_BOOK"),
    SKULL_ITEM("SKULL_ITEM", 3, "PLAYER_HEAD");

    private String oldVersion, newVersion;
    private int subId;

    ItemStackTranslator(String oldVersion, String newVersion) {
        this.oldVersion = oldVersion;
        this.newVersion = newVersion;
    }

    ItemStackTranslator(String oldVersion, int subId, String newVersion) {
        this.oldVersion = oldVersion;
        this.subId = subId;
        this.newVersion = newVersion;
    }

    public ItemStack getItemStack() {
        if(DKBansGuiExtension.getInstance().isConfigItemIds()) return new ItemStack(Material.valueOf(this.oldVersion), 1, (short) this.subId);
        else return new ItemStack(Material.valueOf(this.newVersion));
    }

    public String getOldVersionMaterial() {
        return oldVersion;
    }

    public String getNewVersionMaterial() {
        return newVersion;
    }

    public String getMaterial() {
        if(DKBansGuiExtension.getInstance().isConfigItemIds()) return this.oldVersion;
        return this.newVersion;
    }

    public int getSubId() {
        return subId;
    }
}