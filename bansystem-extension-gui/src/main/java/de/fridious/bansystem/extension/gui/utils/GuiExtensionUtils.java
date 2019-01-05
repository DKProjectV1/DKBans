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

import ch.dkrieger.bansystem.bukkit.utils.Reflection;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.reason.BanReason;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import com.mojang.authlib.GameProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class GuiExtensionUtils {

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

    public static <T> List<T> getListRange(List<? extends T> list, int from, int to) {
        List<T> rangeList = new LinkedList<>();
        for(int i = from; i <= to; i++) {
            if(i < list.size()) rangeList.add(list.get(i));
        }
        return rangeList;
    }

    public static GameProfile getGameProfile(Player player) {
        try{
            Class<?> craftPlayerClass = Reflection.getCraftBukkitClass("entity.CraftPlayer");

            Method getHandle = craftPlayerClass.getMethod("getProfile");
            return (GameProfile)getHandle.invoke(player);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    public static List<Player> getInteractOnlinePlayers(Player player) {
        List<Player> interactOnlinePlayers = new LinkedList<>();
        for (Player targetPlayer : Bukkit.getOnlinePlayers()) {
            if(player.getUniqueId().equals(targetPlayer.getUniqueId()))continue;
            NetworkPlayer target = BanSystem.getInstance().getPlayerManager().getPlayer(targetPlayer.getUniqueId());
            if(target.hasBypass() && !(player.hasPermission("dkbans.bypass.ignore"))) continue;
            interactOnlinePlayers.add(targetPlayer);
        }
        return interactOnlinePlayers;
    }
}