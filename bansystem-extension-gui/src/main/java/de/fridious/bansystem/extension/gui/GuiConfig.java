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
import de.fridious.bansystem.extension.gui.guis.ban.BanGlobalGui;
import de.fridious.bansystem.extension.gui.guis.kick.KickGlobalGui;
import de.fridious.bansystem.extension.gui.guis.playerinfo.PlayerInfoGlobalGui;
import de.fridious.bansystem.extension.gui.guis.playerinfo.PlayerInfoGui;
import de.fridious.bansystem.extension.gui.guis.ban.BanTemplateGui;
import de.fridious.bansystem.extension.gui.guis.report.ReportGlobalGui;
import de.fridious.bansystem.extension.gui.guis.report.ReportControlGui;
import de.fridious.bansystem.extension.gui.guis.report.ReportListGui;
import de.fridious.bansystem.extension.gui.guis.report.ReportTemplateGui;
import de.fridious.bansystem.extension.gui.guis.warn.WarnGlobalGui;
import de.fridious.bansystem.extension.gui.guis.warn.WarnTemplateGui;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import java.io.File;

public class GuiConfig extends SimpleConfig {

    public GuiConfig() {
        super(new File(BanSystem.getInstance().getPlatform().getFolder(), "gui.yml"));
    }

    @Override
    public void onLoad() {
        PlayerInfoGui.INVENTORY_TITLE = addAndGetMessageValue("playerinfo.player.title", "&4Player info");
        PlayerInfoGlobalGui.INVENTORY_TITLE = addAndGetMessageValue("playerinfo.global.title", "&4Global player info");
        BanTemplateGui.INVENTORY_TITLE = addAndGetMessageValue("ban.template.title", "&4Ban");
        BanGlobalGui.INVENTORY_TITLE = addAndGetMessageValue("ban.global.title", "&4Global ban");
        ReportGlobalGui.INVENTORY_TITLE = addAndGetMessageValue("report.global.title", "&6Global report");
        ReportListGui.INVENTORY_TITLE = addAndGetMessageValue("report.list.title", "&6Reports");
        ReportControlGui.INVENTORY_TITLE = addAndGetMessageValue("report.control.title", "&6Report control");
        ReportTemplateGui.INVENTORY_TITLE = addAndGetMessageValue("report.template.title", "&6Report");
        WarnGlobalGui.INVENTORY_TITLE = addAndGetMessageValue("warn.global.title", "&6Global Warns");
        WarnTemplateGui.INVENTORY_TITLE = addAndGetMessageValue("warn.template.title", "&6Warn");
        KickGlobalGui.INVENTORY_TITLE = addAndGetMessageValue("kick.template.title", "&4Kick");
        //General
        ItemStorage.put("placeholder", addAndGetItemStack("items.placeholder", new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short)15).setDisplayName(" ").build()));
        ItemStorage.put("nextpage", addAndGetItemStack("items.nextpage", new ItemBuilder(Material.ARROW).setDisplayName("&aNext page").setLore("Go to page [next_page]").build()));
        ItemStorage.put("currentpage", addAndGetItemStack("items.currentpage", new ItemBuilder(Material.WATCH).setDisplayName("&6Current page:").setLore("&7[current_page]&8/&7[max_pages]").build()));
        ItemStorage.put("previouspage", addAndGetItemStack("items.previouspage", new ItemBuilder(Material.ARROW).setDisplayName("&cPrevious page").setLore("Go to page [previous_page]").build()));
        ItemStorage.put("custom_message", addAndGetItemStack("items.custommessage", new ItemBuilder(Material.PAPER).setDisplayName(" ").build()));
        //Player info
        ItemStorage.put("playerinfo_skull_online", addAndGetItemStack("playerinfo.player.items.skull.online", new ItemBuilder(Material.SKULL_ITEM).setDisplayName("[player]").setLore("&7Id&8: &c[id]", "&7UUID&8: &c[uuid]", "&7Current server&8: &c[server]", "&7Current proxy&8: &c[proxy]", "&7Country&8: &c[country]", "&7Ip&8: &c[ip]", "&7First login&8: &c[firstLogin]", "&7Last login&8: &c[lastLogin]", "&7Online time&8: &c[onlineTime]", "&7Messages&8: &c[messages]", "&7Bans&8: &c[bans]", "&7Mutes&8: &c[mutes]", "&7Logins&8: &c[logins]").build()));
        ItemStorage.put("playerinfo_skull_offline", addAndGetItemStack("playerinfo.player.items.skull.offline", new ItemBuilder(Material.SKULL_ITEM).setDisplayName("[player]").setLore("&7Id&8: &c[id]", "UUID&8: &c[uuid]", "&7Country&8: &c[country]", "&7Ip&8: &c[ip]", "&7First login&8: &c[firstLogin]", "&7Last login&8: &c[lastLogin]", "&7Online time&8: &c[onlineTime]", "&7Messages&8: &c[messages]", "&7Bans&8: &c[bans]", "&7Mutes&8: &c[mutes]", "&7Logins&8: &c[logins]").build()));
        ItemStorage.put("playerinfo_ban", addAndGetItemStack("playerinfo.player.items.ban", new ItemBuilder(Material.BARRIER).setDisplayName("&4Ban").setLore("&7Ban the player").build()));
        ItemStorage.put("playerinfo_unban", addAndGetItemStack("playerinfo.player.items.unban", new ItemBuilder(Material.IRON_FENCE).setDisplayName("&4Unban").setLore("&7Unban the player").build()));
        ItemStorage.put("playerinfo_kick", addAndGetItemStack("playerinfo.player.items.kick", new ItemBuilder(Material.RABBIT_FOOT).setDisplayName("&4Kick").setLore("&7Kick the player").build()));
        ItemStorage.put("playerinfo_jumpto", addAndGetItemStack("playerinfo.player.items.jumpto", new ItemBuilder(Material.ENDER_PEARL).setDisplayName("&4Jump").setLore("&7Jump to the player").build()));
        ItemStorage.put("playerinfo_showhistory", addAndGetItemStack("playerinfo.player.items.showhistory", new ItemBuilder(Material.BOOK_AND_QUILL).setDisplayName("&bShow history").setLore("&7Show the history of the player").build()));
        ItemStorage.put("playerinfo_warn", addAndGetItemStack("playerinfo.player.items.warn", new ItemBuilder(Material.TNT).setDisplayName("&6Warn").setLore("&7Warn the player").build()));
        ItemStorage.put("globalplayerinfo_skull", addAndGetItemStack("playerinfo.global.items.skull", new ItemBuilder(Material.SKULL_ITEM).setDisplayName("[player]").build()));
        //Ban
        ItemStorage.put("globalban_skull", addAndGetItemStack("ban.global.items.skull", new ItemBuilder(Material.SKULL_ITEM).setDisplayName("[player]").setLore("&7Click to ban").build()));
        ItemStorage.put("templateban_reason", addAndGetItemStack("ban.template.items.reason", new ItemBuilder(Material.PAPER).setDisplayName("[reason]").setLore("&7Id&8: &c[id]", "&7BanType&8: &4[banType]").build()));
        ItemStorage.put("templateban_editmessage", addAndGetItemStack("ban.template.items.editmessage", new ItemBuilder(Material.ANVIL).setDisplayName("&cSet message of ban").setLore("&7Current message&8: &7[message]").build()));
        //Report
        ItemStorage.put("report_editmessage", addAndGetItemStack("report.template.items.editmessage", new ItemBuilder(Material.ANVIL).setDisplayName("&cSet a message of a report").setLore("&7Current message&8: &7[message]").build()));
        ItemStorage.put("report_reason", addAndGetItemStack("report.template.items.skull", new ItemBuilder(Material.PAPER).setDisplayName("[reason]").build()));
        ItemStorage.put("globalreport_skull", addAndGetItemStack("report.global.items.skull", new ItemBuilder(Material.SKULL_ITEM).setDisplayName("[player]").setLore("&aClick to report").build()));
        ItemStorage.put("globalreport_login", addAndGetItemStack("report.global.items.login", new ItemBuilder(Material.STAINED_CLAY, 1, (short) 13).setDisplayName("&aLogin").build()));
        ItemStorage.put("globalreport_logout", addAndGetItemStack("report.global.items.logout", new ItemBuilder(Material.STAINED_CLAY, 1, (short) 14).setDisplayName("&cLogout").build()));
        ItemStorage.put("reportlist_skull", addAndGetItemStack("report.list.items.skull", new ItemBuilder(Material.SKULL_ITEM).setDisplayName("[player]").setLore("&7Reason&8: [reason]", "&7Time&8: &c[time]").build()));
        ItemStorage.put("reportcontrol_accept", addAndGetItemStack("report.control.accept", new ItemBuilder(Material.EMERALD).setDisplayName("&aAccept").build()));
        ItemStorage.put("reportcontrol_deny", addAndGetItemStack("report.control.deny", new ItemBuilder(Material.REDSTONE).setDisplayName("&cDeny").build()));
        ItemStorage.put("reportcontrol_custom", addAndGetItemStack("report.control.custom", new ItemBuilder(Material.BARRIER).setDisplayName("&7Change ban reason").setLore("&7Ban the player with a custom ban reason").build()));
        ItemStorage.put("reportcontrol_editmessage", addAndGetItemStack("report.control.items.editmessage", new ItemBuilder(Material.ANVIL).setDisplayName("&cSet a message of the ban").setLore("&7Current message&8: &7[message]").build()));
        //Warn
        ItemStorage.put("globalwarn_skull", addAndGetItemStack("warn.global.items.skull", new ItemBuilder(Material.SKULL_ITEM).setDisplayName("[player]").setLore("&7Click to warn").build()));
        ItemStorage.put("templatewarn_editmessage", addAndGetItemStack("warn.template.items.editmessage", new ItemBuilder(Material.ANVIL).setDisplayName("&cSet a message of a warn").setLore("&7Current message&8: &7[message]").build()));
        ItemStorage.put("templatewarn_reason", addAndGetItemStack("ban.template.items.reason", new ItemBuilder(Material.PAPER).setDisplayName("[reason]").setLore("&7Id&8: &c[id]").build()));
        //Kick
        ItemStorage.put("globalkick_skull", addAndGetItemStack("kick.global.items.skull", new ItemBuilder(Material.SKULL_ITEM).setDisplayName("[player]").setLore("&7Click to kick").build()));
        ItemStorage.put("templatekick_editmessage", addAndGetItemStack("kick.template.items.editmessage", new ItemBuilder(Material.ANVIL).setDisplayName("&cSet a message of a kick").setLore("&7Current message&8: &7[message]").build()));
        ItemStorage.put("templatekick_reason", addAndGetItemStack("kick.template.items.reason", new ItemBuilder(Material.PAPER).setDisplayName("[reason]").setLore("&7Id&8: &c[id]").build()));

    }

    private ItemStack addAndGetItemStack(String path, ItemStack itemStack) {
        ItemBuilder itemBuilder;
        if(DKBansGuiExtension.getInstance().isConfigItemIds()) {
            if(itemStack.getType() == Material.SKULL_ITEM) itemBuilder = new ItemBuilder(Material.SKULL_ITEM);
            else itemBuilder = new ItemBuilder(addAndGetMessageValue(path+".item", itemStack.getTypeId() + ":" + itemStack.getData().getData()));
        }
        else {
            if(itemStack.getType() == Material.SKULL_ITEM) itemBuilder = new ItemBuilder(Material.SKULL_ITEM);
            else itemBuilder = new ItemBuilder(Material.getMaterial(addAndGetMessageValue(path+".item", itemStack.getType().toString().toLowerCase()).toUpperCase()));
        }
        if(itemStack.getItemMeta().hasDisplayName())
            itemBuilder.setDisplayName(addAndGetMessageValue(path+".displayname", itemStack.getItemMeta().getDisplayName()));
        if(itemStack.getItemMeta().hasLore())
            itemBuilder.setLore(addAndGetMessageListValue(path+".lore", itemStack.getItemMeta().getLore()));
        return itemBuilder.build();
    }
}