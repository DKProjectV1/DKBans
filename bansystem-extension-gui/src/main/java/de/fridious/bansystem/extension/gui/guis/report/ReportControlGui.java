package de.fridious.bansystem.extension.gui.guis.report;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.config.mode.BanMode;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.reason.ReportReason;
import ch.dkrieger.bansystem.lib.report.Report;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import de.fridious.bansystem.extension.gui.DKBansGuiExtension;
import de.fridious.bansystem.extension.gui.api.inventory.gui.MessageAnvilInputGui;
import de.fridious.bansystem.extension.gui.api.inventory.gui.PrivateGui;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemStorage;
import de.fridious.bansystem.extension.gui.guis.Guis;
import de.fridious.bansystem.extension.gui.guis.ban.BanSelfGui;
import de.fridious.bansystem.extension.gui.guis.ban.BanTemplateGui;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.UUID;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 06.01.19 00:29
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

public class ReportControlGui extends PrivateGui {

    private String message;

    public ReportControlGui(Player owner, UUID target) {
        super(36, target, owner);
        this.message = " ";
        setItem(10, ItemStorage.get("reportcontrol_accept"));
        setItem(13, ItemStorage.get("reportcontrol_custom"));
        setItem(16, ItemStorage.get("reportcontrol_deny"));
        setItem(22, ItemStorage.get("reportcontrol_editmessage", replace -> replace.replace("[message]", message)));
        fill(ItemStorage.get("placeholder"));
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void updatePage(Event event) {
        if(event == null || getUpdateEvents().contains(event.getClass())) {
            setItem(22, ItemStorage.get("reportcontrol_editmessage", replace -> replace.replace("[message]", message)));
        }
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {

    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        if(player.hasPermission("dkbans.report")) {
            NetworkPlayer targetNetworkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(getTarget());
            if(event.getSlot() == 10) {
                Report report = targetNetworkPlayer.getProcessingReport();
                ReportReason reason = BanSystem.getInstance().getReasonProvider().getReportReason(report.getReasonID());
                Ban ban = targetNetworkPlayer.ban(BanSystem.getInstance().getReasonProvider().getBanReason(reason.getForBan()), getMessage(), player.getUniqueId());
                player.sendMessage(Messages.IPBAN_SUCCESS
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
            } else if(event.getSlot() == 13) {
                BanMode banMode = BanSystem.getInstance().getConfig().banMode;
                if(banMode == BanMode.TEMPLATE || banMode == BanMode.POINT) {
                    Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                            DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                                    .create(Guis.BAN_TEMPLATE, new BanTemplateGui(player, getTarget())).open());
                } else if(banMode == BanMode.SELF) {
                    Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                            DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                                    .create(Guis.BAN_SELF, new BanSelfGui(player, getTarget())));
                }
            } else if(event.getSlot() == 16) {
                player.sendMessage(Messages.REPORT_DENIED_STAFF
                        .replace("[player]", targetNetworkPlayer.getColoredName())
                        .replace("[prefix]", Messages.PREFIX_BAN));
                targetNetworkPlayer.denyReports();
                player.closeInventory();
            } else if(event.getSlot() == 22) {
                Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                        DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                                .create(Guis.ANVIL_INPUT, new MessageAnvilInputGui(this)).open());
            }
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis((Player) event.getPlayer()).remove(Guis.REPORT_CONTROL);
    }
}