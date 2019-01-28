package de.fridious.bansystem.extension.gui.guis.report;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 05.01.19 01:42
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
import ch.dkrieger.bansystem.lib.report.Report;
import de.fridious.bansystem.extension.gui.DKBansGuiExtension;
import de.fridious.bansystem.extension.gui.api.inventory.gui.AnvilInputGui;
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

import java.util.UUID;

public class ReportSelfGui extends PrivateGui {

    private String reason;

    public ReportSelfGui(Player owner, UUID target) {
        super(27, target, owner);
        this.reason = "";
        setItem(11, ItemStorage.get("reportself_reason", replace -> replace.replace("[reason]", reason)));
        setItem(15, ItemStorage.get("reportself_message", replace -> replace.replace("[message]", getMessage())));
        setItem(26, ItemStorage.get("reportself_send", replace ->
                replace.replace("[reason]", reason).replace("[message]", getMessage())));
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void updatePage(Event event) {
        setItem(11, ItemStorage.get("reportself_reason", replace -> replace.replace("[reason]", reason)));
        setItem(15, ItemStorage.get("reportself_message", replace -> replace.replace("[message]", getMessage())));
        setItem(26, ItemStorage.get("reportself_send", replace ->
                replace.replace("[reason]", reason).replace("[message]", getMessage())));
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {

    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        if(player.hasPermission("dkbans.report")) {
            if(event.getSlot() == 11) {
                Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                        DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                                .create(Guis.ANVIL_INPUT, new AnvilInputGui(this, this.reason) {
                                    @Override
                                    public boolean setInput(String input) {
                                        setReason(input);
                                        return true;
                                    }
                                }).open());

            } else if(event.getSlot() == 15) {
                Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                        DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                                .create(Guis.ANVIL_INPUT, new MessageAnvilInputGui(this)).open());
            } else if(event.getSlot() == 26) {
                if(reason == null) return;
                NetworkPlayer targetNetworkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(getTarget());
                OnlineNetworkPlayer targetOnlineNetworkPlayer = targetNetworkPlayer.getOnlinePlayer();
                Report report = targetNetworkPlayer.getReport(player.getUniqueId());
                if(report != null){
                    if(report.getTimeStamp()+BanSystem.getInstance().getConfig().reportDelay > System.currentTimeMillis()) {
                        player.sendMessage(Messages.PLAYER_ALREADY_REPORTED
                                .replace("[player]",targetNetworkPlayer.getColoredName())
                                .replace("[prefix]", Messages.PREFIX_REPORT));
                        player.closeInventory();
                        return;
                    } //@Todo remove report if delay
                }
                report = targetNetworkPlayer.report(player.getUniqueId(), reason, getMessage(), -1, targetOnlineNetworkPlayer.getServer());
                if(report != null){
                    player.sendMessage(Messages.REPORT_SUCCESS
                            .replace("[player]", targetNetworkPlayer.getColoredName())
                            .replace("[reason]",report.getReason())
                            .replace("[server]",report.getReportedServer())
                            .replace("[reasonID]",String.valueOf(report.getReasonID()))
                            .replace("[prefix]", Messages.PREFIX_BAN));
                }
                player.closeInventory();
            }
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis((Player) event.getPlayer()).remove(Guis.REPORT_SELF);
    }
}