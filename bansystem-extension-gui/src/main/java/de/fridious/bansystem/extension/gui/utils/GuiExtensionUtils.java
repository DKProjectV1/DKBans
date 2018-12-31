package de.fridious.bansystem.extension.gui.utils;

/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 31.12.18 20:50
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
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.reason.BanReason;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

public class GuiExtensionUtils {

    public static String replaceBanReason(String replace, BanReason reason) {
        return replace
                .replace("[id]",""+reason.getID())
                .replace("[name]",reason.getDisplay())
                .replace("[historyType]",reason.getHistoryType().getDisplay())
                .replace("[banType]",reason.getBanType().getDisplay())
                .replace("[reason]",reason.getDisplay())
                .replace("[points]",""+reason.getPoints());
    }

    public static String replaceNetworkPlayer(String replace, NetworkPlayer player) {
        String replaced = replace
                .replace("[player]",player.getColoredName())
                .replace("[color]",player.getColor())
                .replace("[country]",player.getCountry())
                .replace("[ip]",player.getIP())
                .replace("[firstLogin]", BanSystem.getInstance().getConfig().dateFormat.format(player.getFirstLogin()))
                .replace("[lastLogin]",BanSystem.getInstance().getConfig().dateFormat.format(player.getLastLogin()))
                .replace("[id]",String.valueOf(player.getID()))
                .replace("[onlineTime-short]",""+ GeneralUtil.calculateRemaining(player.getOnlineTime(true),true))
                .replace("[onlineTime]",""+GeneralUtil.calculateRemaining(player.getOnlineTime(true),false))
                .replace("[messages]",""+player.getStats().getMessages())
                .replace("[reportsSent]",""+player.getStats().getReports())
                .replace("[reportsAccepted]",""+player.getStats().getReportsAccepted())
                .replace("[reportsDenies]",""+player.getStats().getReportsDenied())
                .replace("[reportsReceived]",""+player.getStats().getReportsReceived())
                .replace("[isBanned]",""+player.isBanned(BanType.NETWORK))
                .replace("[isMuted]",""+player.isBanned(BanType.CHAT))
                .replace("[bans]",""+player.getHistory().getBanCount(BanType.NETWORK))
                .replace("[mutes]",""+player.getHistory().getBanCount(BanType.CHAT))
                .replace("[logins]",""+player.getStats().getLogins())
                .replace("[uuid]", String.valueOf(player.getUUID()));
        if(player.isOnline()) {
            OnlineNetworkPlayer onlineNetworkPlayer = player.getOnlinePlayer();
            replaced = replaced
                    .replace("[server]",onlineNetworkPlayer.getServer())
                    .replace("[proxy]",onlineNetworkPlayer.getProxy());
        }
        return replaced;
    }

}