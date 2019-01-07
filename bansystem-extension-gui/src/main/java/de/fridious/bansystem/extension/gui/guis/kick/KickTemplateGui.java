package de.fridious.bansystem.extension.gui.guis.kick;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 05.01.19 00:22
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
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import ch.dkrieger.bansystem.lib.reason.KickReason;
import de.fridious.bansystem.extension.gui.DKBansGuiExtension;
import de.fridious.bansystem.extension.gui.api.inventory.gui.MessageAnvilInputGui;
import de.fridious.bansystem.extension.gui.api.inventory.gui.PrivateGui;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemStorage;
import de.fridious.bansystem.extension.gui.guis.Guis;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class KickTemplateGui extends PrivateGui<KickReason> {

    private UUID target;
    private String message;

    public KickTemplateGui(Player owner, UUID target) {
        super(54, owner);
        this.target = target;
        this.message = " ";
        setPageEntries(getInteractKickReasons());
        setItem(45, ItemStorage.get("templatekick_editmessage", replace -> replace.replace("[message]", message)));
    }

    public String getMessage() {
        return message;
    }

    private List<KickReason> getInteractKickReasons() {
        List<KickReason> kickReasons = new LinkedList<>();
        for(KickReason reason : BanSystem.getInstance().getReasonProvider().getKickReasons()) {
            if((!BanSystem.getInstance().getPlayerManager().getPlayer(target).hasBypass() || getOwner().hasPermission("dkbans.bypass.ignore"))
                    && !reason.isHidden() && getOwner().hasPermission("dkbans.kick")
                    && getOwner().hasPermission(reason.getPermission())) kickReasons.add(reason);
        }
        return kickReasons;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void updatePage(Event event) {
        if(event == null || getUpdateEvents().contains(event.getClass())) {
            setItem(45, ItemStorage.get("templatekick_editmessage", replace -> replace.replace("[message]", message)));
        }
    }

    @Override
    public void setPageItem(int slot, KickReason reason) {
        setItem(slot, ItemStorage.get("templatekick_reason", reason));
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {

    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        KickReason kickReason = getEntryBySlot().get(event.getSlot());
        if(kickReason != null) {
            if(player.hasPermission("dkbans.ban") &&
                    player.hasPermission(kickReason.getPermission())
                    && (!BanSystem.getInstance().getPlayerManager().getPlayer(target).hasBypass() || player.hasPermission("dkbans.bypass.ignore"))) {
                NetworkPlayer targetNetworkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(target);
                Kick kick = targetNetworkPlayer.kick(kickReason, getMessage(), player.getUniqueId());
                player.sendMessage(Messages.KICK_SUCCESS
                        .replace("[prefix]", Messages.PREFIX_BAN)
                        .replace("[server]", kick.getServer())
                        .replace("[reason]", kick.getReason())
                        .replace("[reasonID]",""+kick.getReasonID())
                        .replace("[player]", targetNetworkPlayer.getColoredName()));
                player.closeInventory();
            }
        } else if(event.getSlot() == 45) {
            Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                    DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories(player)
                            .create(Guis.ANVIL_INPUT, new MessageAnvilInputGui(this)).open());
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories((Player) event.getPlayer()).remove(Guis.KICK_TEMPLATE);
    }
}