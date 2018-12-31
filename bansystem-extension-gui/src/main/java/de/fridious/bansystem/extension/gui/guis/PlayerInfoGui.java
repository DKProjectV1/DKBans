package de.fridious.bansystem.extension.gui.guis;

/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 30.12.18 20:27
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
import ch.dkrieger.bansystem.lib.config.mode.BanMode;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import de.fridious.bansystem.extension.gui.DKBansGuiExtension;
import de.fridious.bansystem.extension.gui.api.inventory.gui.GUI;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemStorage;
import de.fridious.bansystem.extension.gui.utils.GuiExtensionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.UUID;

public class PlayerInfoGui extends GUI {

    public static String INVENTORY_TITLE;
    private String title;
    private UUID target;

    protected PlayerInfoGui(UUID target) {
        this.title = INVENTORY_TITLE;
        this.target = target;
        NetworkPlayer networkPlayer = getNetworkPlayer();
        createInventory(GuiExtensionUtils.replaceNetworkPlayer(title, networkPlayer), 27);
        if(networkPlayer.isBanned()) setItem(10, ItemStorage.get("playerinfo_unban", networkPlayer));
        else setItem(10, ItemStorage.get("playerinfo_ban", networkPlayer));
        if(networkPlayer.isOnline()) {
            setItem(11, ItemStorage.get("playerinfo_kick", networkPlayer));
            setItem(15, ItemStorage.get("playerinfo_jumpto", networkPlayer));
        }
        setItem(16, ItemStorage.get("playerinfo_showhistory", networkPlayer));
    }

    public NetworkPlayer getNetworkPlayer() {
        return BanSystem.getInstance().getPlayerManager().getPlayer(this.target);
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {

    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
        final NetworkPlayer targetNetworkPlayer = getNetworkPlayer();
        if(event.getSlot() == 10) {
            //Unban / ban
            if(networkPlayer.isBanned() & player.hasPermission("dkbans.unban")) {

            }else if(player.hasPermission("dkbans.ban")) {
                if(BanSystem.getInstance().getConfig().banMode == BanMode.TEMPLATE) {
                    Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                            DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories(player)
                                    .getTemplateBanGui(targetNetworkPlayer.getUUID()).open(player)
                    );
                } else if(BanSystem.getInstance().getConfig().banMode == BanMode.SELF) {

                }
            }
        } else if(event.getSlot() == 11 && player.hasPermission("dkbans.kick")) {
            //Kick
            if(networkPlayer.isOnline()) {

            }
        } else if(event.getSlot() == 15 && player.hasPermission("dkbans.jumpto")) {
            if(targetNetworkPlayer == null) {
                player.sendMessage(Messages.PLAYER_NOT_FOUND
                        .replace("[prefix]", Messages.PREFIX_NETWORK)
                        .replace("[player]", networkPlayer.getName()));
                return;
            }
            if(!targetNetworkPlayer.isOnline()) {
                player.sendMessage(Messages.PLAYER_NOT_ONLINE
                        .replace("[prefix]", Messages.PREFIX_NETWORK)
                        .replace("[player]", networkPlayer.getColoredName()));
                return;
            }
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
        } else if(event.getSlot() == 16 && player.hasPermission("dkbans.history")) {

        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories((Player) event.getPlayer()).setPlayerInfoGui(null);
    }
}