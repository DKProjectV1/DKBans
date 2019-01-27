package de.fridious.bansystem.extension.gui.guis.playerinfo;

import ch.dkrieger.bansystem.bukkit.event.BukkitNetworkPlayerBanEvent;
import ch.dkrieger.bansystem.bukkit.event.BukkitNetworkPlayerUnbanEvent;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.config.mode.BanMode;
import ch.dkrieger.bansystem.lib.config.mode.ReasonMode;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import de.fridious.bansystem.extension.gui.DKBansGuiExtension;
import de.fridious.bansystem.extension.gui.api.inventory.gui.PrivateGui;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemBuilder;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemStorage;
import de.fridious.bansystem.extension.gui.guis.Guis;
import de.fridious.bansystem.extension.gui.guis.ban.BanSelfGui;
import de.fridious.bansystem.extension.gui.guis.ban.BanTemplateGui;
import de.fridious.bansystem.extension.gui.guis.history.HistoryAllGui;
import de.fridious.bansystem.extension.gui.guis.kick.KickSelfGui;
import de.fridious.bansystem.extension.gui.guis.kick.KickTemplateGui;
import de.fridious.bansystem.extension.gui.guis.unban.UnBanSelfGui;
import de.fridious.bansystem.extension.gui.guis.unban.UnBanTemplateGui;
import de.fridious.bansystem.extension.gui.guis.warn.WarnSelfGui;
import de.fridious.bansystem.extension.gui.guis.warn.WarnTemplateGui;
import de.fridious.bansystem.extension.gui.utils.GuiExtensionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 04.01.19 12:54
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

public class PlayerInfoGui extends PrivateGui {

    public PlayerInfoGui(Player player, UUID target) {
        super(45, replace -> GuiExtensionUtils.replaceNetworkPlayer(replace, BanSystem.getInstance().getPlayerManager().getPlayer(target)), target, player);
        updatePage(null);
        fill(ItemStorage.get("placeholder"));
    }

    @Override
    public void updatePage(Event event) {
        if(event == null || getUpdateEvents().contains(event.getClass())) {
            NetworkPlayer targetNetworkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(getTarget());
            if(targetNetworkPlayer.isBanned()) setItem(29, ItemStorage.get("playerinfo_unban", targetNetworkPlayer));
            else setItem(29, ItemStorage.get("playerinfo_ban", targetNetworkPlayer));
            if(targetNetworkPlayer.isOnline()) {
                setItem(28, ItemStorage.get("playerinfo_kick", targetNetworkPlayer));
                setItem(13, new ItemBuilder(ItemStorage.get("playerinfo_skull_online", targetNetworkPlayer)).setGameProfile(targetNetworkPlayer.getName()));
                setItem(34, ItemStorage.get("playerinfo_jumpto", targetNetworkPlayer));
            } else {
                setItem(28, ItemStorage.get("placeholder"));
                setItem(13, new ItemBuilder(ItemStorage.get("playerinfo_skull_offline", targetNetworkPlayer)).setGameProfile(targetNetworkPlayer.getName()));
                setItem(34, ItemStorage.get("placeholder"));
            }
            setItem(31, ItemStorage.get("playerinfo_showhistory", targetNetworkPlayer));
            setItem(33, ItemStorage.get("playerinfo_warn", targetNetworkPlayer));
        }
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {

    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
        final NetworkPlayer targetNetworkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(getTarget());
        if(targetNetworkPlayer == null) {
            player.sendMessage(Messages.PLAYER_NOT_FOUND
                    .replace("[prefix]", Messages.PREFIX_NETWORK)
                    .replace("[player]", networkPlayer.getName()));
            return;
        }
        if(event.getSlot() == 29) {
            if(targetNetworkPlayer.isBanned() & player.hasPermission("dkbans.unban")) {
                if(targetNetworkPlayer.getUUID().equals(networkPlayer.getUUID())) {
                    player.sendMessage(Messages.BAN_SELF.replace("[prefix]", Messages.PREFIX_BAN));
                    return;
                }
                ReasonMode unbanMode = BanSystem.getInstance().getConfig().unbanMode;
                if(unbanMode == ReasonMode.TEMPLATE) {
                    Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                            DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                                    .create(Guis.UNBAN_TEMPLATE, new UnBanTemplateGui(player, getTarget())).open());
                } else if(unbanMode == ReasonMode.SELF) {
                    Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                            DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                                    .create(Guis.UNBAN_SELF, new UnBanSelfGui(player, getTarget())).open());
                }
            }else if(player.hasPermission("dkbans.ban")) {
                if(targetNetworkPlayer.getUUID().equals(networkPlayer.getUUID())) {
                    player.sendMessage(Messages.BAN_SELF.replace("[prefix]", Messages.PREFIX_BAN));
                    return;
                }
                if(targetNetworkPlayer.hasBypass() && !(player.hasPermission("dkbans.bypass.ignore"))){
                    player.sendMessage(Messages.BAN_BYPASS
                            .replace("[prefix]", Messages.PREFIX_BAN)
                            .replace("[player]",targetNetworkPlayer.getColoredName()));
                    return;
                }
                if(BanSystem.getInstance().getConfig().banMode == BanMode.TEMPLATE) {
                    Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                            DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                                    .create(Guis.BAN_TEMPLATE, new BanTemplateGui(player, getTarget())).open());
                } else if(BanSystem.getInstance().getConfig().banMode == BanMode.SELF) {
                    Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                            DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                                    .create(Guis.BAN_SELF, new BanSelfGui(player, getTarget())).open());
                }
            }
        } else if(event.getSlot() == 28 && player.hasPermission("dkbans.kick")) {
            //Kick
            if(!targetNetworkPlayer.isOnline()) return;
            if(networkPlayer.getUUID().equals(targetNetworkPlayer.getUUID())){
                player.sendMessage(Messages.KICK_SELF.replace("[prefix]", Messages.PREFIX_BAN));
                return;
            }
            if(targetNetworkPlayer.hasBypass() && !(player.hasPermission("dkbans.bypass.ignore"))) {
                player.sendMessage(Messages.KICK_BYPASS
                        .replace("[prefix]", Messages.PREFIX_BAN)
                        .replace("[player]", targetNetworkPlayer.getColoredName()));
                return;
            }
            if(BanSystem.getInstance().getConfig().kickMode == ReasonMode.TEMPLATE) {
                Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), () ->
                        DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                                .create(Guis.KICK_TEMPLATE, new KickTemplateGui(player, getTarget())).open());
            } else if(BanSystem.getInstance().getConfig().kickMode == ReasonMode.SELF) {
                Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), () ->
                        DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                                .create(Guis.KICK_SELF, new KickSelfGui(player, getTarget())).open());
            }

        } else if(event.getSlot() == 34 && player.hasPermission("dkbans.jumpto")) {
            if(!targetNetworkPlayer.isOnline()) return;
            final OnlineNetworkPlayer targetOnlineNetworkPlayer = targetNetworkPlayer.getOnlinePlayer();
            if(targetOnlineNetworkPlayer.getServer() == null) {
                player.sendMessage(Messages.SERVER_NOT_FOUND
                        .replace("[prefix]", Messages.PREFIX_NETWORK));
                return;
            }
            if(networkPlayer.getOnlinePlayer().getServer().equalsIgnoreCase(targetOnlineNetworkPlayer.getServer())){
                player.sendMessage(Messages.SERVER_ALREADY
                        .replace("[prefix]", Messages.PREFIX_NETWORK)
                        .replace("[player]", targetNetworkPlayer.getName())
                        .replace("[server]", targetOnlineNetworkPlayer.getServer()));
                return;
            }
            player.sendMessage(Messages.SERVER_CONNECTING.replace("[prefix]", Messages.PREFIX_NETWORK));
            networkPlayer.getOnlinePlayer().connect(targetOnlineNetworkPlayer.getServer());
        } else if(event.getSlot() == 31 && player.hasPermission("dkbans.history")) {
            Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                    DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                            .create(Guis.HISTORY_ALL, new HistoryAllGui(player, getTarget())).open());
        } else if(event.getSlot() == 33 && player.hasPermission("dkbans.warn")) {
            if(networkPlayer.getUUID().equals(targetNetworkPlayer.getUUID())){
                player.sendMessage(Messages.KICK_SELF.replace("[prefix]", Messages.PREFIX_BAN));
                return;
            }
            if(targetNetworkPlayer.hasBypass() && !(player.hasPermission("dkbans.bypass.ignore"))) {
                player.sendMessage(Messages.WARN_BYPASS
                        .replace("[prefix]", Messages.PREFIX_BAN)
                        .replace("[player]", targetNetworkPlayer.getColoredName()));
                return;
            }
            if(BanSystem.getInstance().getConfig().warnMode == ReasonMode.TEMPLATE) {
                Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), () ->
                        DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                                .create(Guis.WARN_TEMPLATE, new WarnTemplateGui(player, getTarget())).open());
            } else if(BanSystem.getInstance().getConfig().warnMode == ReasonMode.SELF) {
                Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), () ->
                        DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                                .create(Guis.WARN_SELF, new WarnSelfGui(player, getTarget())).open());
            }
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis((Player) event.getPlayer()).remove(Guis.PLAYERINFO_PLAYER);
    }
}