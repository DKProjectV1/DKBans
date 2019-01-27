package de.fridious.bansystem.extension.gui.guis.warn;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 04.01.19 20:32
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
import ch.dkrieger.bansystem.lib.player.history.entry.Warn;
import ch.dkrieger.bansystem.lib.reason.WarnReason;
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

public class WarnTemplateGui extends PrivateGui<WarnReason> {

    private String message;

    public WarnTemplateGui(Player owner, UUID target) {
        super(54, target, owner);
        this.message = " ";
        setPageEntries(getInteractWarnReasons());
        setItem(45, ItemStorage.get("templatewarn_editmessage", replace -> replace.replace("[message]", message)));
    }

    public String getMessage() {
        return message;
    }

    private List<WarnReason> getInteractWarnReasons() {
        List<WarnReason> warnReasons = new LinkedList<>();
        for(WarnReason reason : BanSystem.getInstance().getReasonProvider().getWarnReasons()) {
            if((!BanSystem.getInstance().getPlayerManager().getPlayer(getTarget()).hasBypass() || getOwner().hasPermission("dkbans.bypass.ignore"))
                    && !reason.isHidden() && getOwner().hasPermission("dkbans.warn")
                    && getOwner().hasPermission(reason.getPermission())) warnReasons.add(reason);
        }
        return warnReasons;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void updatePage(Event event) {
        if(event == null || getUpdateEvents().contains(event.getClass())) {
            setItem(45, ItemStorage.get("templatewarn_editmessage", replace -> replace.replace("[message]", message)));
        }
    }

    @Override
    public void setPageItem(int slot, WarnReason reason) {
        setItem(slot, ItemStorage.get("templatewarn_reason", reason));
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {

    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        WarnReason warnReason = getEntryBySlot().get(event.getSlot());
        if(warnReason != null) {
            if(player.hasPermission("dkbans.warn") &&
                    player.hasPermission(warnReason.getPermission())
                    && (!BanSystem.getInstance().getPlayerManager().getPlayer(getTarget()).hasBypass() || player.hasPermission("dkbans.bypass.ignore"))) {
                NetworkPlayer targetNetworkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(getTarget());
                Warn warn = targetNetworkPlayer.warn(warnReason, getMessage(), player.getUniqueId());
                player.sendMessage(Messages.WARN_SUCCESS
                        .replace("[prefix]", Messages.PREFIX_BAN)
                        .replace("[reason]",warn.getReason())
                        .replace("[ip]",warn.getIp())
                        .replace("[staff]",warn.getStaffName())
                        .replace("[message]",warn.getMessage())
                        .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(warn.getTimeStamp()))
                        .replace("[player]", targetNetworkPlayer.getColoredName()));
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
        DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis((Player) event.getPlayer()).remove(Guis.WARN_TEMPLATE);
    }
}