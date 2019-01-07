package de.fridious.bansystem.extension.gui.guis.report;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 04.01.19 15:42
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

import ch.dkrieger.bansystem.bukkit.event.BukkitNetworkPlayerReportEvent;
import ch.dkrieger.bansystem.bukkit.event.BukkitNetworkPlayerReportsProcessEvent;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.report.Report;
import de.fridious.bansystem.extension.gui.api.inventory.gui.PrivateGui;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemBuilder;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemStorage;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Arrays;
import java.util.List;

public class ReportListGui extends PrivateGui<Report> {

    public static String INVENTORY_TITLE;
    public static List<Class<? extends Event>> UPDATE_EVENTS = Arrays.asList(BukkitNetworkPlayerReportEvent.class, BukkitNetworkPlayerReportsProcessEvent.class, PlayerQuitEvent.class);

    public ReportListGui(Player owner) {
        super(owner);
        String title = INVENTORY_TITLE;
        getUpdateEvents().addAll(UPDATE_EVENTS);
        createInventory(title, 54);
        setPageEntries(BanSystem.getInstance().getReportManager().getOpenReports());
        setReportStatusItems();
    }

    public void setReportStatusItems() {
        if(getOwner().hasPermission("dkbans.report") && getOwner().hasPermission("dkbans.report.receive")) {
            NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(getOwner().getUniqueId());
            if(networkPlayer.isReportLoggedIn()) {
                setItem(45, ItemStorage.get("globalreport_logout", networkPlayer));
            } else {
                setItem(45, ItemStorage.get("globalreport_login", networkPlayer));
            }
        }
    }

    @Override
    public void updatePage(Event event) {
        super.updatePage(event);
        setReportStatusItems();
    }

    @Override
    public void setPageItem(int slot, Report report) {
        NetworkPlayer networkPlayer = report.getPlayer();
        setItem(slot, new ItemBuilder(ItemStorage.get("reportlist_skull", report)).setGameProfile(networkPlayer.getName()));
    }

    @Override
    public void beforeUpdatePage() {
        setPageEntries(BanSystem.getInstance().getReportManager().getOpenReports());
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {

    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        Report report = getEntryBySlot().get(event.getSlot());
        if(report != null) {
            NetworkPlayer target = BanSystem.getInstance().getPlayerManager().searchPlayer(report.getPlayer().getUUID().toString());
            if(target == null){
                player.sendMessage(Messages.PLAYER_NOT_FOUND.replace("[prefix]", Messages.PREFIX_BAN));
                return;
            }
            OnlineNetworkPlayer onlineTarget = target.getOnlinePlayer();
            if(onlineTarget == null){
                player.sendMessage(Messages.REPORT_NOTFOUND
                        .replace("[player]", target.getColoredName())
                        .replace("[prefix]", Messages.PREFIX_BAN));
                return;
            }
            if(target.getOpenReportWhenNoProcessing() == null){
                player.sendMessage(Messages.REPORT_NOTFOUND
                        .replace("[player]",target.getColoredName())
                        .replace("[prefix]", Messages.PREFIX_BAN));
                return;
            }
            NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
            OnlineNetworkPlayer onlineNetworkPlayer = networkPlayer.getOnlinePlayer();
            target.processOpenReports(networkPlayer);
            if(onlineNetworkPlayer.getServer().equalsIgnoreCase(onlineTarget.getServer())){
                player.sendMessage(Messages.SERVER_ALREADY
                        .replace("[server]", onlineNetworkPlayer.getServer())
                        .replace("[prefix]", Messages.PREFIX_BAN));
            }else onlineNetworkPlayer.connect(onlineTarget.getServer());
            player.sendMessage(Messages.REPORT_PROCESS
                    .replace("[player]", target.getColoredName())
                    .replace("[message]",report.getMessage())
                    .replace("[reason]",report.getReason())
                    .replace("[reporter]",report.getReporter().getColoredName())
                    .replace("[server]", onlineNetworkPlayer.getServer())
                    .replace("[prefix]", Messages.PREFIX_BAN));
            player.closeInventory();
        } else if(event.getSlot() == 45) {
            NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
            networkPlayer.setReportLogin(!networkPlayer.isReportLoggedIn());
            setReportStatusItems();
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {

    }
}