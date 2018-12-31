package de.fridious.bansystem.extension.gui.guis;

/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 30.12.18 23:16
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
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.reason.BanReason;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import de.fridious.bansystem.extension.gui.DKBansGuiExtension;
import de.fridious.bansystem.extension.gui.api.inventory.gui.GUI;
import de.fridious.bansystem.extension.gui.api.inventory.item.ItemStorage;
import de.fridious.bansystem.extension.gui.utils.GuiExtensionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import java.util.*;

public class TemplateBanGui extends GUI {

    public static String INVENTORY_TITLE;
    private String title;
    private UUID player, target;
    private Map<Integer, BanReason> banReasonBySlot;
    private List<BanReason> interactBans;
    private int currentPage;

    protected TemplateBanGui(UUID player, UUID target) {
        this.title = INVENTORY_TITLE;
        this.player = player;
        this.target = target;
        this.interactBans = getInteractBanReasons();
        this.currentPage = 1;
        this.banReasonBySlot = new LinkedHashMap<>();
        createInventory(title,
                (interactBans.size() >= 54 ? 54 : (int) Math.ceil((double) interactBans.size() / 9) * 9));
        int slot = 0;
        for (BanReason reason : interactBans) {
            banReasonBySlot.put(slot, reason);
            setItem(slot, ItemStorage.get("templateban_reason", reason));
            slot++;
        }
    }

    @Override
    protected void onOpen(InventoryOpenEvent event) {

    }

    @Override
    protected void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        BanReason banReason = banReasonBySlot.get(event.getSlot());
        if(player.hasPermission("dkbans.ban") &&
                player.hasPermission(banReason.getPermission())
                && (!BanSystem.getInstance().getPlayerManager().getPlayer(target).hasBypass() || player.hasPermission("dkbans.bypass.ignore"))) {
            NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(target);
            Ban ban = networkPlayer.ban(banReason, "", player.getUniqueId());
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
        }
    }

    @Override
    protected void onClose(InventoryCloseEvent event) {
        DKBansGuiExtension.getInstance().getGuiManager().getCachedInventories((Player) event.getPlayer()).setTemplateBanGui(null);
    }

    private List<BanReason> getInteractBanReasons() {
        Player player = Bukkit.getPlayer(this.player);
        List<BanReason> banReasons = new LinkedList<>();
        for(BanReason reason : BanSystem.getInstance().getReasonProvider().getBanReasons()) {
            if((!BanSystem.getInstance().getPlayerManager().getPlayer(target).hasBypass() || player.hasPermission("dkbans.bypass.ignore"))
                    && !reason.isHidden() && player.hasPermission("dkbans.ban")
                    && player.hasPermission(reason.getPermission())) banReasons.add(reason);
        }
        return banReasons;
    }
}