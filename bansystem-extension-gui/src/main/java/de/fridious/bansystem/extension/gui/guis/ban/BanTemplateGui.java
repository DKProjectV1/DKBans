package de.fridious.bansystem.extension.gui.guis.ban;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.reason.BanReason;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import de.fridious.bansystem.extension.gui.DKBansGuiExtension;
import de.fridious.bansystem.extension.gui.api.inventory.gui.MessageAnvilInputGui;
import de.fridious.bansystem.extension.gui.api.inventory.gui.PrivateGUI;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemStorage;
import de.fridious.bansystem.extension.gui.guis.GUIS;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.*;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 04.01.19 00:52
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

public class BanTemplateGui extends PrivateGUI<BanReason> {

    public static String INVENTORY_TITLE;
    public static List<Class<? extends Event>> UPDATE_EVENTS = Arrays.asList();
    private UUID target;

    public BanTemplateGui(Player player, UUID target) {
        super(player);
        String title = INVENTORY_TITLE;
        this.target = target;
        createInventory(title, 54);
        getUpdateEvents().addAll(UPDATE_EVENTS);
        setPageEntries(getInteractBanReasons());
        setItem(45, ItemStorage.get("templateban_editmessage", replace -> replace.replace("[message]", getMessage())));
    }

    private List<BanReason> getInteractBanReasons() {
        List<BanReason> banReasons = new LinkedList<>();
        for(BanReason reason : BanSystem.getInstance().getReasonProvider().getBanReasons()) {
            if((!BanSystem.getInstance().getPlayerManager().getPlayer(target).hasBypass() || getOwner().hasPermission("dkbans.bypass.ignore"))
                    && !reason.isHidden() && getOwner().hasPermission("dkbans.ban")
                    && getOwner().hasPermission(reason.getPermission())) banReasons.add(reason);
        }
        return banReasons;
    }

    @Override
    public void updatePage(Event event) {
        if(event == null || getUpdateEvents().contains(event.getClass())) {
            setItem(45, ItemStorage.get("templateban_editmessage", replace -> replace.replace("[message]", getMessage())));
        }
    }

    @Override
    public void setPageItem(int slot, BanReason reason) {
        setItem(slot, ItemStorage.get("templateban_reason", reason));
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {

    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        BanReason banReason = getEntryBySlot().get(event.getSlot());
        if(banReason != null) {
            if(player.hasPermission("dkbans.ban") &&
                    player.hasPermission(banReason.getPermission())
                    && (!BanSystem.getInstance().getPlayerManager().getPlayer(target).hasBypass() || player.hasPermission("dkbans.bypass.ignore"))) {
                NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(target);
                Ban ban = networkPlayer.ban(banReason, getMessage(), player.getUniqueId());
                player.sendMessage(Messages.BAN_SUCCESS
                        .replace("[prefix]", Messages.PREFIX_BAN)
                        .replace("[player]", networkPlayer.getColoredName())
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
            }
        } else if(event.getSlot() == 45) {
            Bukkit.getScheduler().runTask(DKBansGuiExtension.getInstance(), ()->
                    DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories(player)
                            .create(GUIS.ANVIL_INPUT, new MessageAnvilInputGui(this)).open());
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories((Player) event.getPlayer()).remove(GUIS.BAN_TEMPLATE);
    }
}