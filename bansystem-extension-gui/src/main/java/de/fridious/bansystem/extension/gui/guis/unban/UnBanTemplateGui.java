package de.fridious.bansystem.extension.gui.guis.unban;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 05.01.19 14:54
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
import ch.dkrieger.bansystem.lib.player.history.entry.Unban;
import ch.dkrieger.bansystem.lib.reason.UnbanReason;
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

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class UnBanTemplateGui extends PrivateGui<UnbanReason> {

    private String message;

    public UnBanTemplateGui(Player owner, UUID target) {
        super(54, target, owner);
        this.message = " ";
        setPageEntries(getInteractUnBanReasons());
        setItem(45, ItemStorage.get("templateunban_editmessage", replace -> replace.replace("[message]", message)));
    }

    public String getMessage() {
        return message;
    }

    private List<UnbanReason> getInteractUnBanReasons() {
        NetworkPlayer targetNetworkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(getTarget());
        List<UnbanReason> reasons = new LinkedList<>();
        for(UnbanReason reason : BanSystem.getInstance().getReasonProvider().getUnbanReasons()) {
            if((!BanSystem.getInstance().getPlayerManager().getPlayer(getTarget()).hasBypass() || getOwner().hasPermission("dkbans.bypass.ignore"))
                    && !reason.isHidden() && getOwner().hasPermission("dkbans.unban")
                    && getOwner().hasPermission(reason.getPermission())
                    && (reason.getBanType() == null || targetNetworkPlayer.isBanned(reason.getBanType()))) reasons.add(reason);
        }
        return reasons;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void updatePage(Event event) {
        if(event == null || getUpdateEvents().contains(event.getClass())) {
            setItem(45, ItemStorage.get("templateunban_editmessage", replace -> replace.replace("[message]", message)));
        }
    }

    @Override
    public void setPageItem(int slot, UnbanReason reason) {
        setItem(slot, ItemStorage.get("templateunban_reason", reason));
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {

    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        UnbanReason reason = getEntryBySlot().get(event.getSlot());
        if(reason != null) {
            if(player.hasPermission("dkbans.unban") &&
                    player.hasPermission(reason.getPermission())
                    && (!BanSystem.getInstance().getPlayerManager().getPlayer(getTarget()).hasBypass() || player.hasPermission("dkbans.bypass.ignore"))) {
                NetworkPlayer targetNetworkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(getTarget());
                BanType banType = reason.getBanType();
                if(banType == null) {
                    if(targetNetworkPlayer.isBanned(BanType.NETWORK) && targetNetworkPlayer.isBanned(BanType.CHAT)) {
                        DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player).create(Guis.UNBAN_CHOOSE_BANTYPE, new UnBanChooseBanTypeGui(player, getTarget(), reason));
                        return;
                    } else if(targetNetworkPlayer.isBanned(BanType.CHAT)) {
                        banType = BanType.CHAT;
                    } else if(targetNetworkPlayer.isBanned(BanType.NETWORK)) {
                        banType = BanType.NETWORK;
                    }
                }
                Unban unban = targetNetworkPlayer.unban(banType, reason, getMessage(), player.getUniqueId());
                if(unban.getBanType() == BanType.NETWORK) {
                    player.sendMessage(Messages.PLAYER_UNBANNED
                            .replace("[prefix]", Messages.PREFIX_BAN)
                            .replace("[reason]",unban.getReason())
                            .replace("[message]",unban.getMessage())
                            .replace("[staff]",unban.getStaffName())
                            .replace("[id]",""+unban.getID())
                            .replace("[points]",""+unban.getPoints())
                            .replace("[player]", targetNetworkPlayer.getColoredName()));
                } else if(unban.getBanType() == BanType.CHAT) {
                    player.sendMessage(Messages.PLAYER_UNMUTED
                            .replace("[prefix]", Messages.PREFIX_BAN)
                            .replace("[reason]",unban.getReason())
                            .replace("[message]",unban.getMessage())
                            .replace("[staff]",unban.getStaffName())
                            .replace("[id]",""+unban.getID())
                            .replace("[points]",""+unban.getPoints())
                            .replace("[player]", targetNetworkPlayer.getColoredName()));
                }
                player.closeInventory();
            }
        } else if(event.getSlot() == 45) {
            Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                    DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                            .create(Guis.ANVIL_INPUT, new MessageAnvilInputGui(this)).open());
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis((Player) event.getPlayer()).remove(Guis.UNBAN_TEMPLATE);
    }
}