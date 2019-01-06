package de.fridious.bansystem.extension.gui.api.inventory.item;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.*;
import ch.dkrieger.bansystem.lib.reason.BanReason;
import ch.dkrieger.bansystem.lib.reason.KickReason;
import ch.dkrieger.bansystem.lib.reason.UnbanReason;
import ch.dkrieger.bansystem.lib.reason.WarnReason;
import ch.dkrieger.bansystem.lib.report.Report;
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
        return get(key, replace -> replace
                .replace("[id]",""+reason.getID())
                .replace("[name]",reason.getDisplay())
                .replace("[historyType]",reason.getHistoryType().getDisplay())
                .replace("[banType]",reason.getBanType().getDisplay())
                .replace("[reason]",reason.getDisplay())
                .replace("[points]",""+reason.getPoints()));
    }

    public static ItemStack get(String key, WarnReason reason) {
        return get(key, replace -> replace
                .replace("[id]", String.valueOf(reason.getID()))
                .replace("[name]",reason.getDisplay())
                .replace("[reason]",reason.getDisplay())
                .replace("[points]",""+reason.getPoints()));
    }

    public static ItemStack get(String key, KickReason reason) {
        return get(key, replace -> replace
                .replace("[id]", "" + reason.getID())
                .replace("[reason]", reason.getDisplay())
                .replace("[name]", reason.getDisplay())
                .replace("[points]", "" + reason.getPoints()));
    }

    public static ItemStack get(String key, UnbanReason reason) {
        return get(key, replace -> replace
                .replace("[reason]",reason.getDisplay())
                .replace("[id]",""+reason.getID())
                .replace("[name]",reason.getDisplay())
                .replace("[maxPoints]",""+reason.getMaxPoints())
                .replace("[maxDuration]",reason.getMaxDuration().getFormattedTime(true))
                .replace("[points]",""+reason.getPoints())
                .replace("[banType]", reason.getBanType() == null ? "§4" + BanType.NETWORK.getDisplay() + " §8| §4" + BanType.CHAT.getDisplay() : "§4" + reason.getBanType().getDisplay()));
    }

    public static ItemStack get(String key, int page, int maxPages) {
        return get(key, replace -> replace
                .replace("[current_page]", String.valueOf(page))
                .replace("[next_page]", String.valueOf(page+1))
                .replace("[previous_page]", String.valueOf(page-1))
                .replace("[max_pages]", String.valueOf(maxPages)));
    }

    public static ItemStack get(String key, Report report) {
        return get(key, replace -> replace.replace("[reason]",report.getReason())
                .replace("[reasonID]",""+report.getReasonID())
                .replace("[player]",report.getPlayer().getColoredName())
                .replace("[reporter]",report.getReporter().getColoredName())
                .replace("[time]", BanSystem.getInstance().getConfig().dateFormat.format(report.getTimeStamp())));
    }

    public static ItemStack get(String key, HistoryEntry entry) {
        if(entry instanceof Ban) return get(key, (Ban) entry);
        else if(entry instanceof Unban) return get(key, (Unban) entry);
        else if(entry instanceof Kick) return get(key, (Kick) entry);
        else if(entry instanceof Warn) return get(key, (Warn) entry);
        return null;
    }

    public static ItemStack get(String key, Ban ban) {
        return get(key, replace -> replace
                .replace("[player]",ban.getPlayer().getColoredName())
                .replace("[id]",""+ban.getID())
                .replace("[banId]",""+ban.getID())
                .replace("[banid]",""+ban.getID())
                .replace("[reason]",ban.getReason())
                .replace("[reasonID]",""+ban.getReasonID())
                .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(ban.getTimeStamp()))
                .replace("[timeout]",BanSystem.getInstance().getConfig().dateFormat.format(ban.getTimeOut()))
                .replace("[message]",ban.getMessage())
                .replace("[type]",ban.getBanType().getDisplay())
                .replace("[staff]",ban.getStaffName())
                .replace("[ip]",ban.getIp())
                .replace("[points]",""+ban.getPoints().getPoints())
                .replace("[pointsType]",ban.getPoints().getHistoryType().getDisplay())
                .replace("[duration]",GeneralUtil.calculateDuration(ban.getDuration()))
                .replace("[remaining]",GeneralUtil.calculateRemaining(ban.getRemaining(),false))
                .replace("[remaining-short]",GeneralUtil.calculateRemaining(ban.getRemaining(),true))
                .replace("[prefix]",Messages.PREFIX_BAN));
    }

    public static ItemStack get(String key, Kick kick) {
        return get(key, replace -> replace
                .replace("[prefix]",Messages.PREFIX_BAN)
                .replace("[id]",""+kick.getID())
                .replace("[reason]",kick.getReason())
                .replace("[type]",kick.getTypeName())
                .replace("[points]",""+kick.getPoints().getPoints())
                .replace("[pointsType]",kick.getPoints().getHistoryType().getDisplay())
                .replace("[reasonID]",""+kick.getReasonID())
                .replace("[message]",kick.getMessage())
                .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(kick.getTimeStamp()))
                .replace("[ip]",kick.getIp())
                .replace("[server]",kick.getServer())
                .replace("[staff]",kick.getStaffName())
                .replace("[player]",kick.getPlayer().getColoredName()));
    }

    public static ItemStack get(String key, Unban unban) {
        return get(key, replace -> replace
                .replace("[prefix]",Messages.PREFIX_BAN)
                .replace("[id]",""+unban.getID())
                .replace("[reason]",unban.getReason())
                .replace("[type]",unban.getTypeName())
                .replace("[points]",""+unban.getPoints().getPoints())
                .replace("[pointsType]",unban.getPoints().getHistoryType().getDisplay())
                .replace("[reasonID]",""+unban.getReasonID())
                .replace("[message]",unban.getMessage())
                .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(unban.getTimeStamp()))
                .replace("[ip]",unban.getIp())
                .replace("[banType]",unban.getBanType().getDisplay())
                .replace("[staff]",unban.getStaffName())
                .replace("[player]",unban.getPlayer().getColoredName()));
    }

    public static ItemStack get(String key, Warn warn) {
        return get(key, replace -> replace
                .replace("[prefix]",Messages.PREFIX_BAN)
                .replace("[id]",""+warn.getID())
                .replace("[reason]",warn.getReason())
                .replace("[type]",warn.getTypeName())
                .replace("[points]",""+warn.getPoints().getPoints())
                .replace("[pointsType]",warn.getPoints().getHistoryType().getDisplay())
                .replace("[reasonID]",""+warn.getReasonID())
                .replace("[message]",warn.getMessage())
                .replace("[time]", BanSystem.getInstance().getConfig().dateFormat.format(warn.getTimeStamp()))
                .replace("[ip]",warn.getIp())
                .replace("[staff]",warn.getStaffName())
                .replace("[player]",warn.getPlayer().getColoredName()));
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