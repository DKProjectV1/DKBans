package de.fridious.bansystem.extension.gui.guis.playerinfo;

import ch.dkrieger.bansystem.bukkit.event.BukkitNetworkPlayerBanEvent;
import ch.dkrieger.bansystem.bukkit.event.BukkitNetworkPlayerUnbanEvent;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.config.mode.BanMode;
import ch.dkrieger.bansystem.lib.config.mode.ReasonMode;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import de.fridious.bansystem.extension.gui.DKBansGuiExtension;
import de.fridious.bansystem.extension.gui.api.inventory.gui.GUI;
import de.fridious.bansystem.extension.gui.api.inventory.gui.PrivateGUI;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemBuilder;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemStorage;
import de.fridious.bansystem.extension.gui.guis.GUIS;
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

public class PlayerInfoGui extends PrivateGUI {

    public static String INVENTORY_TITLE;
    public static List<Class<? extends Event>> UPDATE_EVENTS = Arrays.asList(PlayerJoinEvent.class, PlayerQuitEvent.class, BukkitNetworkPlayerBanEvent.class, BukkitNetworkPlayerUnbanEvent.class);
    private String title;
    private UUID target;

    public PlayerInfoGui(Player player, UUID target) {
        super(player);
        this.title = INVENTORY_TITLE;
        this.target = target;
        getUpdateEvents().addAll(UPDATE_EVENTS);
        NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(this.target);
        createInventory(GuiExtensionUtils.replaceNetworkPlayer(title, networkPlayer), 36);
        updatePage(null);
        fill(ItemStorage.get("placeholder"));
    }

    public UUID getTarget() {
        return target;
    }

    @Override
    public void updatePage(Event event) {
        if(event == null || getUpdateEvents().contains(event.getClass())) {
            NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(this.target);
            System.out.println("Update PlayerInfo: " + networkPlayer.isOnline() + ":" + networkPlayer.isBanned());
            if(networkPlayer.isBanned()) setItem(20, ItemStorage.get("playerinfo_unban", networkPlayer));
            else setItem(20, ItemStorage.get("playerinfo_ban", networkPlayer));
            if(networkPlayer.isOnline()) {
                setItem(19, ItemStorage.get("playerinfo_kick", networkPlayer));
                setItem(13, new ItemBuilder(ItemStorage.get("playerinfo_skull_online", networkPlayer)).setGameProfile(networkPlayer.getName()));
                setItem(25, ItemStorage.get("playerinfo_jumpto", networkPlayer));
            } else {
                setItem(19, ItemStorage.get("placeholder"));
                setItem(13, new ItemBuilder(ItemStorage.get("playerinfo_skull_offline", networkPlayer)).setGameProfile(networkPlayer.getName()));
                setItem(25, ItemStorage.get("placeholder"));
            }
            setItem(22, ItemStorage.get("playerinfo_showhistory", networkPlayer));
            setItem(24, ItemStorage.get("playerinfo_warn", networkPlayer));
        }
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {

    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
        final NetworkPlayer targetNetworkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(this.target);
        if(targetNetworkPlayer == null) {
            player.sendMessage(Messages.PLAYER_NOT_FOUND
                    .replace("[prefix]", Messages.PREFIX_NETWORK)
                    .replace("[player]", networkPlayer.getName()));
            return;
        }
        if(event.getSlot() == 20) {
            if(targetNetworkPlayer.isBanned() & player.hasPermission("dkbans.unban")) {
                if(targetNetworkPlayer.getUUID().equals(networkPlayer.getUUID())) {
                    player.sendMessage(Messages.BAN_SELF.replace("[prefix]", Messages.PREFIX_BAN));
                    return;
                }
                ReasonMode unbanMode = BanSystem.getInstance().getConfig().unbanMode;
                if(unbanMode == ReasonMode.TEMPLATE) {
                    Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                            DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories(player)
                                    .create(GUIS.UNBAN_TEMPLATE, new UnBanTemplateGui(player, target)).open());
                } else if(unbanMode == ReasonMode.SELF) {
                    Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                            DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories(player)
                                    .create(GUIS.UNBAN_SELF, new UnBanSelfGui(player, target)).open());
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
                            DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories(player)
                                    .create(GUIS.BAN_TEMPLATE, new BanTemplateGui(player, target)).open());
                } else if(BanSystem.getInstance().getConfig().banMode == BanMode.SELF) {
                    Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                            DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories(player)
                                    .create(GUIS.BAN_SELF, new BanSelfGui(player, target)).open());
                }
            }
        } else if(event.getSlot() == 19 && player.hasPermission("dkbans.kick")) {
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
                        DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories(player)
                                .create(GUIS.KICK_TEMPLATE, new KickTemplateGui(player, target)).open());
            } else if(BanSystem.getInstance().getConfig().kickMode == ReasonMode.SELF) {
                Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), () ->
                        DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories(player)
                                .create(GUIS.KICK_SELF, new KickSelfGui(player, target)).open());
            }

        } else if(event.getSlot() == 25 && player.hasPermission("dkbans.jumpto")) {
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
        } else if(event.getSlot() == 22 && player.hasPermission("dkbans.history")) {
            Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                    DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories(player)
                            .create(GUIS.HISTORY_ALL, new HistoryAllGui(player, target)).open());
        } else if(event.getSlot() == 24 && player.hasPermission("dkbans.warn")) {
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
                        DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories(player)
                                .create(GUIS.WARN_TEMPLATE, new WarnTemplateGui(player, target)).open());
            } else if(BanSystem.getInstance().getConfig().warnMode == ReasonMode.SELF) {
                Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), () ->
                        DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories(player)
                                .create(GUIS.WARN_SELF, new WarnSelfGui(player, target)).open());
            }
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories((Player) event.getPlayer()).remove(GUIS.PLAYERINFO_PLAYER);
    }
}