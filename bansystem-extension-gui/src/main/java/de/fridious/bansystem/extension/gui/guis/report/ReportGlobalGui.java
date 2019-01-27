package de.fridious.bansystem.extension.gui.guis.report;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 04.01.19 14:46
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
import ch.dkrieger.bansystem.lib.config.mode.ReasonMode;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import de.fridious.bansystem.extension.gui.DKBansGuiExtension;
import de.fridious.bansystem.extension.gui.api.inventory.gui.PrivateGui;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemBuilder;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemStorage;
import de.fridious.bansystem.extension.gui.guis.GuiManager;
import de.fridious.bansystem.extension.gui.guis.Guis;
import de.fridious.bansystem.extension.gui.utils.GuiExtensionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.concurrent.TimeUnit;

public class ReportGlobalGui extends PrivateGui<Player> {

    public ReportGlobalGui(Player owner) {
        super(54, owner);
        setPageEntries(GuiExtensionUtils.getInteractOnlinePlayers(getOwner()));
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
    public void setPageItem(int slot, Player player) {
        NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
        setItem(slot, new ItemBuilder(ItemStorage.get("globalreport_skull", networkPlayer)).setGameProfile(networkPlayer.getName()));
    }

    @Override
    public void beforeUpdatePage() {
        setPageEntries(GuiExtensionUtils.getInteractOnlinePlayers(getOwner()));
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {

    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        Player target = getEntryBySlot().get(event.getSlot());
        if(target != null) {
            NetworkPlayer targetNetworkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(target.getUniqueId());
            if(player.hasPermission("dkbans.report") && (!targetNetworkPlayer.hasBypass()
                    || player.hasPermission("dkbans.bypass.ignore"))) {
                ReasonMode reportMode = BanSystem.getInstance().getConfig().reportMode;
                GuiManager guiManager = DKBansGuiExtension.getInstance().getGuiManager();
                if(reportMode == ReasonMode.TEMPLATE && guiManager.isGuiEnabled(ReportTemplateGui.class)) {
                    Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                            DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                                    .create(Guis.REPORT_TEMPLATE, new ReportTemplateGui(player, target.getUniqueId())).open());

                } else if(reportMode == ReasonMode.SELF && guiManager.isGuiEnabled(ReportSelfGui.class)) {
                    Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                            DKBansGuiExtension.getInstance().getGuiManager().getCachedGuis(player)
                                    .create(Guis.REPORT_SELF, new ReportSelfGui(player, target.getUniqueId())).open());
                }
                OnlineNetworkPlayer onlineNetworkPlayer = BanSystem.getInstance().getPlayerManager().getOnlinePlayer(player.getUniqueId());
                if(BanSystem.getInstance().getConfig().reportAutoCommandExecuteOnProxy){
                    for(String command :  BanSystem.getInstance().getConfig().reportAutoCommandEnter)
                        onlineNetworkPlayer.executeCommand(command.replace("[player]",player.getName()));
                }else{
                    BanSystem.getInstance().getPlatform().getTaskManager().runTaskLater(()->{
                        for(String command :  BanSystem.getInstance().getConfig().reportAutoCommandEnter)
                            Bukkit.dispatchCommand(player, command.replace("[player]",player.getName()));
                    },1L, TimeUnit.SECONDS);
                }
            }
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