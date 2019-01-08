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

import ch.dkrieger.bansystem.bukkit.event.*;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.config.SimpleConfig;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import de.fridious.bansystem.extension.gui.api.inventory.gui.Gui;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemBuilder;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemStorage;
import de.fridious.bansystem.extension.gui.guis.GuiData;
import de.fridious.bansystem.extension.gui.guis.ban.BanGlobalGui;
import de.fridious.bansystem.extension.gui.guis.ban.BanSelfGui;
import de.fridious.bansystem.extension.gui.guis.history.HistoryAllGui;
import de.fridious.bansystem.extension.gui.guis.history.HistoryEntryDeleteGui;
import de.fridious.bansystem.extension.gui.guis.history.HistoryGlobalGui;
import de.fridious.bansystem.extension.gui.guis.kick.KickGlobalGui;
import de.fridious.bansystem.extension.gui.guis.kick.KickSelfGui;
import de.fridious.bansystem.extension.gui.guis.kick.KickTemplateGui;
import de.fridious.bansystem.extension.gui.guis.playerinfo.PlayerInfoGlobalGui;
import de.fridious.bansystem.extension.gui.guis.playerinfo.PlayerInfoGui;
import de.fridious.bansystem.extension.gui.guis.ban.BanTemplateGui;
import de.fridious.bansystem.extension.gui.guis.report.*;
import de.fridious.bansystem.extension.gui.guis.unban.UnBanChooseBanTypeGui;
import de.fridious.bansystem.extension.gui.guis.unban.UnBanSelfGui;
import de.fridious.bansystem.extension.gui.guis.unban.UnBanTemplateGui;
import de.fridious.bansystem.extension.gui.guis.warn.WarnGlobalGui;
import de.fridious.bansystem.extension.gui.guis.warn.WarnSelfGui;
import de.fridious.bansystem.extension.gui.guis.warn.WarnTemplateGui;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class GuiConfig extends SimpleConfig {

    public GuiConfig() {
        super(new File(BanSystem.getInstance().getPlatform().getFolder(), "gui.yml"));
    }

    @Override
    public void onLoad() {
        Map<Class<? extends Gui>, GuiData> guiData = DKBansGuiExtension.getInstance().getGuiManager().getGuiData();
        guiData.put(BanGlobalGui.class, addAndGetGuiData("ban.global", new GuiData(true, "&4Global ban",PlayerJoinEvent.class, PlayerQuitEvent.class)));
        guiData.put(BanSelfGui.class, addAndGetGuiData("ban.self", new GuiData(true, "&4Ban", new ConcurrentHashMap<String, Object>(){{
            put("bantype", BanType.NETWORK.toString().toLowerCase());
            put("timeunit", TimeUnit.HOURS.toString().toLowerCase());
            put("duration", -1);
        }})));
        guiData.put(BanTemplateGui.class, addAndGetGuiData("ban.template", new GuiData(true, "&4Ban")));

        guiData.put(HistoryAllGui.class, addAndGetGuiData("history.all", new GuiData(true, "&bHistory", BukkitNetworkPlayerHistoryUpdateEvent.class)));
        guiData.put(HistoryEntryDeleteGui.class, addAndGetGuiData("history.delete", new GuiData(true, "&8Are you sure to delete?")));
        guiData.put(HistoryGlobalGui.class, addAndGetGuiData("history.global", new GuiData(true, "&bGlobal history", PlayerJoinEvent.class, PlayerQuitEvent.class)));

        guiData.put(KickGlobalGui.class, addAndGetGuiData("kick.global", new GuiData(true, "&4Global Kick", PlayerJoinEvent.class, PlayerQuitEvent.class)));
        guiData.put(KickSelfGui.class, addAndGetGuiData("kick.self", new GuiData(true, "&4Kick")));
        guiData.put(KickTemplateGui.class, addAndGetGuiData("kick.template", new GuiData(true, "&4Kick")));

        guiData.put(PlayerInfoGlobalGui.class, addAndGetGuiData("playerinfo.global", new GuiData(true, "&4Global player info", PlayerJoinEvent.class, PlayerQuitEvent.class)));
        guiData.put(PlayerInfoGui.class, addAndGetGuiData("playerinfo.player", new GuiData(true, "&4Player info", PlayerJoinEvent.class, PlayerQuitEvent.class, BukkitNetworkPlayerBanEvent.class, BukkitNetworkPlayerUnbanEvent.class)));

        guiData.put(ReportControlGui.class, addAndGetGuiData("report.control", new GuiData(true, "&6Report control")));
        guiData.put(ReportGlobalGui.class, addAndGetGuiData("report.global", new GuiData(true, "&6Global report", PlayerJoinEvent.class, PlayerQuitEvent.class)));
        guiData.put(ReportListGui.class, addAndGetGuiData("report.list", new GuiData(true, "&6Reports", BukkitNetworkPlayerReportEvent.class, BukkitNetworkPlayerReportsProcessEvent.class, PlayerQuitEvent.class)));
        guiData.put(ReportSelfGui.class, addAndGetGuiData("report.self", new GuiData(true, "&6Report")));
        guiData.put(ReportTemplateGui.class, addAndGetGuiData("report.template", new GuiData(true, "&6Report")));

        //guiData.put(UnBanGlobalGui.class, addAndGetGuiData("unban.global", new GuiData(true, )))
        guiData.put(UnBanSelfGui.class, addAndGetGuiData("unban.self", new GuiData(true, "&4Unban")));
        guiData.put(UnBanTemplateGui.class, addAndGetGuiData("unban.template", new GuiData(true, "&4Unban")));
        guiData.put(UnBanChooseBanTypeGui.class, addAndGetGuiData("unban.choosetype", new GuiData(true, "&4Choose unban type")));

        guiData.put(WarnGlobalGui.class, addAndGetGuiData("warn.global", new GuiData(true, "&6Global warn", PlayerJoinEvent.class, PlayerQuitEvent.class)));
        guiData.put(WarnSelfGui.class, addAndGetGuiData("warn.self", new GuiData(true, "&6Warn")));
        guiData.put(WarnTemplateGui.class, addAndGetGuiData("warn.template", new GuiData(true, "&6Warn")));

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
        ItemStorage.put("selfban_network", addAndGetItemStack("ban.self.items.network", new ItemBuilder(Material.BARRIER).setDisplayName("&4Ban").setLore("&7Click to change to mute").build()));
        ItemStorage.put("selfban_chat", addAndGetItemStack("ban.self.items.chat", new ItemBuilder(Material.EMPTY_MAP).setDisplayName("&4Mute").setLore("&7Click to change to ban").build()));
        ItemStorage.put("selfban_editmessage", addAndGetItemStack("ban.self.items.editmessage", new ItemBuilder(Material.PAPER).setDisplayName("&cSet a message of the ban").setLore("&7Current message&8: &c[message]").build()));
        ItemStorage.put("selfban_reason", addAndGetItemStack("ban.self.items.reason", new ItemBuilder(Material.ANVIL).setDisplayName("&cSet the reason").setLore("&7Current reason&8: &c[reason]").build()));
        ItemStorage.put("selfban_duration", addAndGetItemStack("ban.self.items.duration", new ItemBuilder(Material.WATCH).setDisplayName("&cDuration").setLore("&7Current duration&8: &c[duration]").build()));
        ItemStorage.put("selfban_timeunit_seconds", addAndGetItemStack("ban.self.items.timeunit.seconds", new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 5).setDisplayName("&aSeconds").build()));
        ItemStorage.put("selfban_timeunit_minutes", addAndGetItemStack("ban.self.items.timeunit.minutes", new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 13).setDisplayName("&2Minutes").build()));
        ItemStorage.put("selfban_timeunit_hours", addAndGetItemStack("ban.self.items.timeunit.hours", new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 4).setDisplayName("&eHours").build()));
        ItemStorage.put("selfban_timeunit_days", addAndGetItemStack("ban.self.items.timeunit.days", new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (short) 14).setDisplayName("&4Days").build()));
        ItemStorage.put("selfban_send", addAndGetItemStack("ban.self.items.send", new ItemBuilder(Material.INK_SACK, 1, (short) 10).setDisplayName("&aSend").build()));

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
        ItemStorage.put("reportcontrol_editmessage", addAndGetItemStack("report.control.items.editmessage", new ItemBuilder(Material.ANVIL).setDisplayName("&cSet a message of the report").setLore("&7Current message&8: &7[message]").build()));
        ItemStorage.put("reportself_reason", addAndGetItemStack("report.self.items.reason", new ItemBuilder(Material.ANVIL).setDisplayName("&cSet the reason").setLore("&7Current reason&8: [reason]").build()));
        ItemStorage.put("reportself_message", addAndGetItemStack("report.self.items.message", new ItemBuilder(Material.PAPER).setDisplayName("&cSet the message").setLore("&7Current message&8: [message]").build()));
        ItemStorage.put("reportself_send", addAndGetItemStack("report.self.items.send", new ItemBuilder(Material.INK_SACK, 1, (short) 10).setDisplayName("&aSend").build()));
        //Warn
        ItemStorage.put("globalwarn_skull", addAndGetItemStack("warn.global.items.skull", new ItemBuilder(Material.SKULL_ITEM).setDisplayName("[player]").setLore("&7Click to warn").build()));
        ItemStorage.put("templatewarn_editmessage", addAndGetItemStack("warn.template.items.editmessage", new ItemBuilder(Material.ANVIL).setDisplayName("&cSet a message of a warn").setLore("&7Current message&8: &7[message]").build()));
        ItemStorage.put("templatewarn_reason", addAndGetItemStack("warn.template.items.reason", new ItemBuilder(Material.PAPER).setDisplayName("[reason]").setLore("&7Id&8: &c[id]").build()));
        ItemStorage.put("warnself_reason", addAndGetItemStack("warn.self.items.reason", new ItemBuilder(Material.ANVIL).setDisplayName("&cSet the reason").setLore("&7Current reason&8: [reason]").build()));
        ItemStorage.put("warnself_message", addAndGetItemStack("warn.self.items.message", new ItemBuilder(Material.PAPER).setDisplayName("&cSet the message").setLore("&7Current message&8: [message]").build()));
        ItemStorage.put("warnself_send", addAndGetItemStack("warn.self.items.send", new ItemBuilder(Material.INK_SACK, 1, (short) 10).setDisplayName("&aSend").build()));
        //Kick
        ItemStorage.put("globalkick_skull", addAndGetItemStack("kick.global.items.skull", new ItemBuilder(Material.SKULL_ITEM).setDisplayName("[player]").setLore("&7Click to kick").build()));
        ItemStorage.put("templatekick_editmessage", addAndGetItemStack("kick.template.items.editmessage", new ItemBuilder(Material.ANVIL).setDisplayName("&cSet a message of a kick").setLore("&7Current message&8: &7[message]").build()));
        ItemStorage.put("templatekick_reason", addAndGetItemStack("kick.template.items.reason", new ItemBuilder(Material.PAPER).setDisplayName("[reason]").setLore("&7Id&8: &c[id]").build()));
        ItemStorage.put("kickself_reason", addAndGetItemStack("kick.self.items.reason", new ItemBuilder(Material.ANVIL).setDisplayName("&cSet the reason").setLore("&7Current reason&8: [reason]").build()));
        ItemStorage.put("kickself_message", addAndGetItemStack("kick.self.items.message", new ItemBuilder(Material.PAPER).setDisplayName("&cSet the message").setLore("&7Current message&8: [message]").build()));
        ItemStorage.put("kickself_send", addAndGetItemStack("kick.self.items.send", new ItemBuilder(Material.INK_SACK, 1, (short) 10).setDisplayName("&aSend").build()));
        //UnBan
        ItemStorage.put("templateunban_reason", addAndGetItemStack("unban.template.items.reason", new ItemBuilder(Material.PAPER).setDisplayName("[reason]").setLore("&7Id&8: &c[id]", "&7BanType&8: [banType]").build()));
        ItemStorage.put("templateunban_editmessage", addAndGetItemStack("unban.template.items.editmessage", new ItemBuilder(Material.ANVIL).setDisplayName("&cSet message of unban").setLore("&7Current message&8: &7[message]").build()));
        ItemStorage.put("unbanself_reason", addAndGetItemStack("unban.self.items.reason", new ItemBuilder(Material.ANVIL).setDisplayName("&cSet the reason").setLore("&7Current reason&8: [reason]").build()));
        ItemStorage.put("unbanself_message", addAndGetItemStack("unban.self.items.message", new ItemBuilder(Material.PAPER).setDisplayName("&cSet the message").setLore("&7Current message&8: [message]").build()));
        ItemStorage.put("unbanself_send", addAndGetItemStack("unban.self.items.send", new ItemBuilder(Material.INK_SACK, 1, (short) 10).setDisplayName("&aSend").build()));
        ItemStorage.put("chooseunban_network", addAndGetItemStack("unban.choosetype.items.network", new ItemBuilder(Material.BARRIER).setDisplayName("&4Network").build()));
        ItemStorage.put("chooseunban_chat", addAndGetItemStack("unban.choosetype.items.chat", new ItemBuilder(Material.EMPTY_MAP).setDisplayName("&4Chat").build()));
        //History
        ItemStorage.put("history_ban_network", addAndGetItemStack("history.items.ban.network", new ItemBuilder(Material.BARRIER).setDisplayName("&4Ban").setLore("&7Id&8: &c[id]", "&7Reason&8: &c[reason]", "&7Time&8: &c[time]", "&7Timeout&8: &c[timeout]", "&7Message&8: &c[message]", "&7Type&8: &c[type]", "&7Staff&8: &c[staff]", "&7Ip&8: &c[ip]").build()));
        ItemStorage.put("history_ban_chat", addAndGetItemStack("history.items.ban.chat", new ItemBuilder(Material.EMPTY_MAP).setDisplayName("&4Mute").setLore("&7Id&8: &c[id]", "&7Reason&8: &c[reason]", "&7Time&8: &c[time]", "&7Timeout&8: &c[timeout]", "&7Message&8: &c[message]", "&7Type&8: &c[type]", "&7Staff&8: &c[staff]", "&7Ip&8: &c[ip]").build()));
        ItemStorage.put("history_unban", addAndGetItemStack("history.items.unban", new ItemBuilder(Material.IRON_FENCE).setDisplayName("&4Unban").setLore("&7Id&8: &c[id]", "&7Reason&8: &c[reason]", "&7Message&8: &c[message]", "&7Type&8: &c[type]", "&7Time&8: &c[time]", "&7Staff&8: &c[staff]", "&7Ip&8: &c[ip]").build()));
        ItemStorage.put("history_warn", addAndGetItemStack("history.items.warn", new ItemBuilder(Material.TNT).setDisplayName("&6Warn").setLore("&7Id&8: &c[id]", "&7Reason&8: &c[reason]", "&7Message&8: &c[message]", "&7Type&8: &c[type]", "&7Time&8: &c[time]", "&7Staff&8: &c[staff]", "&7Ip&8: &c[ip]").build()));
        ItemStorage.put("history_kick", addAndGetItemStack("history.items.kick", new ItemBuilder(Material.RABBIT_FOOT).setDisplayName("&4Kick").setLore("&7Id&8: &c[id]", "&7Reason&8: &c[reason]", "&7Message&8: &c[message]", "&7Type&8: &c[type]", "&7Server&8: &c[server]", "&7Time&8: &c[time]", "&7Staff&8: &c[staff]", "&7Ip&8: &c[ip]").build()));
        ItemStorage.put("history_clear", addAndGetItemStack("history.all.items.clear", new ItemBuilder(Material.HOPPER).setDisplayName("&cClear history").setDisplayName("&7Clear the whole history of [player]").build()));
        ItemStorage.put("historydelete_accept", addAndGetItemStack("history.delete.items.accept", new ItemBuilder(Material.STAINED_CLAY, 1, (short) 13).setDisplayName("&aYes").build()));
        ItemStorage.put("historydelete_deny", addAndGetItemStack("history.delete.items.deny", new ItemBuilder(Material.STAINED_CLAY, 1, (short) 14).setDisplayName("&cNo").build()));
        ItemStorage.put("globalhistory_skull", addAndGetItemStack("history.global.items.skull", new ItemBuilder(Material.SKULL_ITEM).setDisplayName("[player]").setLore("&7Click to open history").build()));

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

    private GuiData addAndGetGuiData(String path, GuiData guiData) {
        boolean enabled = addAndGetBooleanValue(path+".enabled", guiData.isEnabled());
        String title = addAndGetMessageValue(path+".title", guiData.getTitle());
        Map<String, Object> settings = new ConcurrentHashMap<>();
        for (Map.Entry<String, Object> entry : guiData.getSettings().entrySet())
            settings.put(entry.getKey(), addAndGetValue(path + ".settings." + entry.getKey(), entry.getValue()));
        return new GuiData(enabled, title, settings, guiData.getUpdateEvents());
    }
}