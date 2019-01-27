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

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import de.fridious.bansystem.extension.gui.DKBansGuiExtension;
import de.fridious.bansystem.extension.gui.api.inventory.gui.AnvilInputGui;
import de.fridious.bansystem.extension.gui.api.inventory.gui.MessageAnvilInputGui;
import de.fridious.bansystem.extension.gui.api.inventory.gui.PrivateGui;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemBuilder;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemStorage;
import de.fridious.bansystem.extension.gui.guis.Guis;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class BanSelfGui extends PrivateGui {

    private BanType banType;
    private String reason;
    private long duration;
    private TimeUnit timeUnit;

    public BanSelfGui(Player owner, UUID target) {
        super(45, target, owner);
        this.banType = BanType.parse((String) getSettings().get("bantype"));
        this.timeUnit = TimeUnit.valueOf(((String) getSettings().get("timeunit")).toUpperCase());
        this.reason = "";
        this.duration = Long.valueOf((String) getSettings().get("duration"));
        updatePage(null);
        fill(ItemStorage.get("placeholder"));
    }

    public String getReason() {
        return reason;
    }

    @Override
    public void updatePage(Event event) {
        if(this.banType == BanType.NETWORK) setItem(10, ItemStorage.get("selfban_network", this::replace));
        else if(this.banType == BanType.CHAT) setItem(10, ItemStorage.get("selfban_chat", this::replace));
        setItem(12, ItemStorage.get("selfban_reason", this::replace));
        setItem(14, ItemStorage.get("selfban_editmessage", this::replace));
        setItem(16, ItemStorage.get("selfban_duration", this::replace));
        setItem(28, new ItemBuilder(ItemStorage.get("selfban_timeunit_seconds", this::replace)).setGlowing(timeUnit == TimeUnit.SECONDS));
        setItem(30, new ItemBuilder(ItemStorage.get("selfban_timeunit_minutes", this::replace)).setGlowing(timeUnit == TimeUnit.MINUTES));
        setItem(32, new ItemBuilder(ItemStorage.get("selfban_timeunit_hours", this::replace)).setGlowing(timeUnit == TimeUnit.HOURS));
        setItem(34, new ItemBuilder(ItemStorage.get("selfban_timeunit_days", this::replace)).setGlowing(timeUnit == TimeUnit.DAYS));
        setItem(44, ItemStorage.get("selfban_send", this::replace));
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {

    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        if(event.getSlot() == 10) {
            if(this.banType == BanType.NETWORK) this.banType = BanType.CHAT;
            else if(this.banType == BanType.CHAT) this.banType = BanType.NETWORK;
            updatePage(null);
        } else if(event.getSlot() == 12) {
            Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                    DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                            .create(Guis.ANVIL_INPUT, new AnvilInputGui(this, this.reason) {
                                @Override
                                public boolean setInput(String input) {
                                    setReason(input);
                                    return true;
                                }
                            }).open());
        } else if(event.getSlot() == 14) {
            Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                    DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                            .create(Guis.ANVIL_INPUT, new MessageAnvilInputGui(this)).open());
        } else if(event.getSlot() == 16) {
            Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()-> {
                DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                        .create(Guis.ANVIL_INPUT, new AnvilInputGui(this, String.valueOf(this.duration)) {
                            @Override
                            public boolean setInput(String input) {
                                try {
                                    setDuration(Long.valueOf(input));
                                    return true;
                                } catch (NumberFormatException e) {
                                    return false;
                                }
                            }
                        }).open();
            });
        } else if(event.getSlot() == 28) {
            this.timeUnit = TimeUnit.SECONDS;
            updatePage(null);
        } else if(event.getSlot() == 30) {
            this.timeUnit = TimeUnit.MINUTES;
            updatePage(null);
        } else if(event.getSlot() == 32) {
            this.timeUnit = TimeUnit.HOURS;
            updatePage(null);
        } else if(event.getSlot() == 34) {
            this.timeUnit = TimeUnit.DAYS;
            updatePage(null);
        } else if(event.getSlot() == 44) {
            if(this.banType != null) {
                NetworkPlayer targetNetworkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(getTarget());
                Ban ban  = targetNetworkPlayer.ban(this.banType, this.duration, this.timeUnit, this.reason, getMessage(), -1, player.getUniqueId());
                player.sendMessage(Messages.BAN_SUCCESS
                        .replace("[prefix]", Messages.PREFIX_BAN)
                        .replace("[player]", targetNetworkPlayer.getColoredName())
                        .replace("[type]", ban.getBanType().getDisplay())
                        .replace("[reason]", ban.getReason())
                        .replace("[points]", String.valueOf(ban.getPoints()))
                        .replace("[staff]", ban.getStaffName())
                        .replace("[reasonID]", String.valueOf(ban.getReasonID()))
                        .replace("[ip]", ban.getIp())
                        .replace("[duration]", GeneralUtil.calculateDuration(ban.getDuration()))
                        .replace("[remaining]", GeneralUtil.calculateRemaining(ban.getDuration(),false))
                        .replace("[remaining-short]", GeneralUtil.calculateRemaining(ban.getDuration(),true)));
                player.closeInventory();
            }
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis((Player) event.getPlayer()).remove(Guis.BAN_SELF);
    }

    private String replace(String replace) {
        return replace
                .replace("[reason]", getReason())
                .replace("[message]", getMessage())
                .replace("[timeunit]", this.timeUnit.toString().toLowerCase())
                .replace("[bantype]", this.banType.getDisplay())
                .replace("[duration]", String.valueOf(this.duration));
    }
}