package de.fridious.bansystem.extension.gui.guis.report;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 04.01.19 14:38
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
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.reason.ReportReason;
import ch.dkrieger.bansystem.lib.report.Report;
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
import java.util.List;
import java.util.UUID;

public class ReportTemplateGui extends PrivateGui<ReportReason> {

    private UUID target;

    public ReportTemplateGui(Player owner, UUID target) {
        super(54, owner);
        this.target = target;
        setPageEntries(BanSystem.getInstance().getReasonProvider().getReportReasons());
        updatePage(null);
    }

    @Override
    public void setPageItem(int slot, ReportReason reportReason) {
        setItem(slot, ItemStorage.get("report_reason", replace -> replace.replace("[reason]", reportReason.getDisplay())));
    }

    @Override
    public void updatePage(Event event) {
        if(event == null || getUpdateEvents().contains(event.getClass())) {
            setItem(45, ItemStorage.get("report_editmessage", replace -> replace.replace("[message]", getMessage())));
        }
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {

    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        ReportReason reportReason = getEntryBySlot().get(event.getSlot());
        if(reportReason != null) {
            if(player.hasPermission("dkbans.report") && player.hasPermission(reportReason.getPermission())) {
                NetworkPlayer targetNetworkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(target);
                OnlineNetworkPlayer targetOnlineNetworkPlayer = targetNetworkPlayer.getOnlinePlayer();
                Report report = targetNetworkPlayer.getReport(player.getUniqueId());
                if(report != null){
                    if(report.getTimeStamp()+BanSystem.getInstance().getConfig().reportDelay > System.currentTimeMillis()) {
                        player.sendMessage(Messages.PLAYER_ALREADY_REPORTED
                                .replace("[player]",targetNetworkPlayer.getColoredName())
                                .replace("[prefix]", Messages.PREFIX_REPORT));
                        player.closeInventory();
                        return;
                    }else; //@Todo remove report if delay
                }
                report = targetNetworkPlayer.report(reportReason, getMessage(), player.getUniqueId(), targetOnlineNetworkPlayer.getServer());
                if(report != null){
                    player.sendMessage(Messages.REPORT_SUCCESS
                            .replace("[player]", targetNetworkPlayer.getColoredName())
                            .replace("[reason]",report.getReason())
                            .replace("[server]",report.getReportedServer())
                            .replace("[reasonID]",String.valueOf(report.getReasonID()))
                            .replace("[prefix]", Messages.PREFIX_BAN));
                }
                player.closeInventory();
            } else {
                player.sendMessage(Messages.REASON_NO_PERMISSION
                        .replace("[prefix]", Messages.PREFIX_BAN)
                        .replace("[reason]", reportReason.getDisplay()));
            }
        } else if(event.getSlot() == 45) {
            Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                    DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories(player)
                            .create(Guis.ANVIL_INPUT, new MessageAnvilInputGui(this)).open());
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories((Player) event.getPlayer()).remove(Guis.REPORT_TEMPLATE);
    }
}