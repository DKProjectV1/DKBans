/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 30.12.18 14:39
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

package ch.dkrieger.bansystem.bukkit.hook;

import ch.dkrieger.bansystem.bukkit.BukkitBanSystemBootstrap;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;

public class PlaceHolderApiHook extends EZPlaceholderHook {

    public PlaceHolderApiHook() {
        super(BukkitBanSystemBootstrap.getInstance(),"dkbans");
    }

    @Override
    public String onPlaceholderRequest(Player requestPlayer, String identifier) {
        if(identifier.startsWith("player_")){
            NetworkPlayer player = BanSystem.getInstance().getPlayerManager().getPlayer(requestPlayer.getUniqueId());
            if(player == null) return "PlayerNotFound";
            if(identifier.endsWith("_name")){
                return player.getColoredName();
            }else if(identifier.endsWith("_color")){
                return player.getColor();
            }else if(identifier.endsWith("_ip")){
                return player.getIP();
            }else if(identifier.endsWith("_country")){
                return player.getCountry();
            }else if(identifier.endsWith("_id")){
                return String.valueOf(player.getID());
            }else if(identifier.endsWith("_uuid")){
                return player.getUUID().toString();
            }else if(identifier.endsWith("_report")){
                return (player.isReportLoggedIn()? Messages.STAFF_STATUS_LOGIN:Messages.STAFF_STATUS_LOGOUT);
            }else if(identifier.endsWith("_teamchat")){
                return (player.isTeamChatLoggedIn()? Messages.STAFF_STATUS_LOGIN:Messages.STAFF_STATUS_LOGOUT);
            }else if(identifier.endsWith("_lastlogin")){
                return BanSystem.getInstance().getConfig().dateFormat.format(player.getLastLogin());
            }else if(identifier.endsWith("_firstlogin")){
                return BanSystem.getInstance().getConfig().dateFormat.format(player.getFirstLogin());
            }else if(identifier.endsWith("_onlinetime")){
                return GeneralUtil.calculateRemaining(player.getOnlineTime(),false);
            }else if(identifier.endsWith("_onlinetime-short")){
                return GeneralUtil.calculateRemaining(player.getOnlineTime(),false);
            }else if(identifier.endsWith("_onlinetime-hour")){
                return String.valueOf(Math.round(((player.getOnlineTime()/1000D)/60D)/60D));
            }else if(identifier.endsWith("_onlinetime-days")){
                return String.valueOf(Math.round((((player.getOnlineTime()/1000D)/60D)/60D)/24D));
            }else if(identifier.endsWith("_sendedmessages")){
                return String.valueOf(player.getStats().getMessages());
            }else if(identifier.endsWith("_reportsAccepted")){
                return String.valueOf(player.getStats().getReportsAccepted());
            }else if(identifier.endsWith("_reportsSent")){
                return String.valueOf(player.getStats().getReports());
            }else if(identifier.endsWith("_reportsDenied")){
                return String.valueOf(player.getStats().getReportsDenied());
            }else if(identifier.endsWith("_bans")){
                return String.valueOf(player.getHistory().getBan(BanType.NETWORK));
            }else if(identifier.endsWith("_mutes")){
                return String.valueOf(player.getHistory().getBan(BanType.CHAT));
            }else if(identifier.endsWith("_server")){
                OnlineNetworkPlayer online = player.getOnlinePlayer();
                if(online != null) return online.getServer();
                else return Messages.UNKNOWN;
            }
        }
        return "&cPlaceHolderNotFound";
    }
}
