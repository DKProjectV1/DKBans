/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 01.01.19 13:25
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

package ch.dkrieger.bansystem.extension.restapi.handler.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.HistoryPoints;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import ch.dkrieger.bansystem.lib.player.history.entry.Unban;
import ch.dkrieger.bansystem.lib.reason.BanReason;
import ch.dkrieger.bansystem.lib.reason.KickReason;
import ch.dkrieger.bansystem.lib.reason.WarnReason;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import ch.dkrieger.bansystem.extension.restapi.ResponseCode;
import ch.dkrieger.bansystem.extension.restapi.handler.RestApiHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PlayerHandler extends RestApiHandler {

    public PlayerHandler() {
        super("player/");
    }

    @Override
    public void onRequest(Query query, Document response) {
        String action = query.get("action");
        if(action != null){
            if(action.equalsIgnoreCase("onlineList")){
                List<UUID> players = new ArrayList<>();
                for(OnlineNetworkPlayer player : BanSystem.getInstance().getPlayerManager().getOnlinePlayers()) players.add(player.getUUID());
                response.append("onlinePlayers",players);
                return;
            }else if(action.equalsIgnoreCase("onlineCount")){
                response.append("count", BanSystem.getInstance().getPlayerManager().getOnlineCount());
                return;
            }else if(action.equalsIgnoreCase("registeredCount")){
                response.append("count", BanSystem.getInstance().getPlayerManager().getRegisteredCount());
                return;
            }else if(query.contains("player")){
                NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(query.get("player"));
                if(player == null){
                    response.append("code", ResponseCode.NO_CONTENT).append("message","Player not found");
                    return;
                }
                if(action.equalsIgnoreCase("info")){
                    response.append("player",player);
                    try{
                        response.getJsonObject("player").get("properties").getAsJsonObject().remove("password");
                    }catch (Exception exception){}
                    return;
                }else if(action.equalsIgnoreCase("onlineInfo")){
                    OnlineNetworkPlayer online = player.getOnlinePlayer();
                    if(online == null){
                        response.append("code", ResponseCode.NO_CONTENT).append("message","Player not online");
                        return;
                    }
                    response.append("name",online.getName()).append("uuid",online.getUUID())
                            .append("server",online.getServer()).append("proxy",online.getProxy());
                    return;
                }else if(action.equalsIgnoreCase("history")){
                    if(Boolean.valueOf(query.get("sorted"))) response.append("entries",player.getHistory().getEntriesSorted());
                    else response.append("entries",player.getHistory().getEntries());
                }else if(action.equalsIgnoreCase("update")){
                    if(query.contains("reportLogin")) player.setReportLogin(Boolean.valueOf(query.get("reportLogin")));
                    if(query.contains("teamChatLogin")) player.setTeamChatLogin(Boolean.valueOf(query.get("teamChatLogin")));
                    if(query.contains("color")) player.setColor(query.get("color"));
                    if(query.contains("appendProperties-key") && query.contains("appendProperties-value")){
                        player.getProperties().append(query.get("appendProperties-key"),query.get("appendProperties-value"));
                        player.saveProperties();
                    }
                    if(query.contains("removeProperties")){
                        player.getProperties().remove(query.get("removeProperties"));
                        player.saveProperties();
                    }
                    return;
                }else if(action.equalsIgnoreCase("isOnline")){
                    response.append("online",player.isOnline());
                    return;
                }else if(action.equalsIgnoreCase("isWarned")){
                    response.append("warned",player.getHistory().getWarnCountSinceLastBan()> 0);
                    return;
                }else if(action.equalsIgnoreCase("isBanned")){
                    if(query.contains("type")){
                        BanType type = BanType.parse(query.get("type"));
                        if(type == null)response.append("code", ResponseCode.BAD_REQUEST).append("message","Invalid ban type");
                        else response.append("banned",player.isBanned(BanType.NETWORK));
                    }else response.append("banned",player.isBanned(BanType.NETWORK));
                    return;
                }else if(action.equalsIgnoreCase("ban") && (query.contains("reason") || query.contains("reasonid"))){
                    if(query.contains("reasonid")){
                        BanReason reason = BanSystem.getInstance().getReasonProvider().searchBanReason(query.get("reasonid"));
                        if(reason == null){
                            response.append("code", ResponseCode.BAD_REQUEST).append("message","Ban reason not found");
                            return;
                        }
                        Ban ban = player.ban(reason,(query.contains("message")?query.get("message"):""),query.get("staff"));
                        response.append("ban",ban).append("message","Player was banned");
                    }else if(query.contains("reason") && query.contains("type") && query.contains("duration") && GeneralUtil.isNumber("duration")){
                        BanType type = BanType.parse(query.get("type"));
                        if(type == null){
                            response.append("code", ResponseCode.BAD_REQUEST).append("message","Invalid ban type");
                            return;
                        }
                        /*
                        Ban ban = player.ban(type,GeneralUtil.convertToMillis(Long.valueOf(query.get("duration")),query.get("unit")),TimeUnit.MILLISECONDS
                                ,query.get("reason"),(query.contains("message")?query.get("message"):"")
                                ,GeneralUtil.isNumber(query.get("points"))?Integer.valueOf(query.get("points")):0,-1,query.get("staff"));
                         */
                        //response.append("ban",ban).append("message","Player was banned");
                        return;
                    }
                }else if(action.equalsIgnoreCase("kick") && (query.contains("reason") || query.contains("reasonid"))){
                    if(query.contains("reasonid")){
                        KickReason reason = BanSystem.getInstance().getReasonProvider().searchKickReason(query.get("reasonid"));
                        if(reason == null){
                            response.append("code", ResponseCode.BAD_REQUEST).append("message","Kick reason not found");
                            return;
                        }
                        Kick kick = player.kick(reason,(query.contains("message")?query.get("message"):""),query.get("staff"));
                        response.append("kick",kick).append("message","Player was kicked");
                    }else{
                        /*
                        Kick kick = player.kick(query.get("reason"),(query.contains("message")?query.get("message"):"")
                                ,GeneralUtil.isNumber(query.get("points"))?Integer.valueOf(query.get("points")):0,-1
                                ,query.get("staff"));
                         */
                       // response.append("kick",kick).append("message","Player was kicked");
                        return;
                    }
                }else if(action.equalsIgnoreCase("unban") && query.contains("type")){
                    BanType type = BanType.parse(query.get("type"));
                    if(type == null){
                        response.append("code", ResponseCode.BAD_REQUEST).append("message","Invalid ban type");
                        return;
                    }
                    if(!player.isBanned(type)){
                        response.append("code", ResponseCode.NO_CONTENT).append("message","Player is not banned");
                        return;
                    }
                    Unban unban = player.unban(type,query.contains("reason")?query.get("reason"):""
                            ,(query.contains("message")?query.get("message"):"")
                            ,GeneralUtil.isNumber(query.get("points"))?Integer.valueOf(query.get("points")):0,-1
                            ,query.get("staff"));
                    response.append("unban",unban).append("message","Player unbanned");
                    return;
                }else if(action.equalsIgnoreCase("warn") && (query.contains("reason") || query.contains("reasonid"))){
                    if(query.contains("reasonID")){
                        WarnReason reason = BanSystem.getInstance().getReasonProvider().getWarnReason(query.get("reasonid"));
                        if(reason == null){
                            response.append("code", ResponseCode.NO_CONTENT).append("message","Reason not found");
                            return;
                        }
                        player.warn(reason,query.get("message"),query.get("staff"));
                        response.append("message","player warned");
                    }else{
                        /*
                        player.warn(query.get("reason"),query.get("message")
                                ,new HistoryPoints(GeneralUtil.isNumber(query.get("points"))?Integer.valueOf(query.get("points")):0,null),query.get("staff"));
                         */
                        response.append("message","player warned");
                    }
                    return;
                }else if(action.equalsIgnoreCase("denyReports")){
                    player.denyReports();
                    response.append("message","Reports denied");
                    return;
                }else if(action.equalsIgnoreCase("processOpenReports") && query.contains("staff")){
                    NetworkPlayer staff = BanSystem.getInstance().getPlayerManager().searchPlayer(query.get("staff"));
                    if(staff == null){
                        response.append("code", ResponseCode.NO_CONTENT).append("message","Staff not found");
                        return;
                    }
                    player.processOpenReports(staff);
                    response.append("message","Processing open reports");
                    return;
                }else if(action.equalsIgnoreCase("sendMessage") && query.contains("message")){
                    OnlineNetworkPlayer online = player.getOnlinePlayer();
                    if(online == null){
                        response.append("code", ResponseCode.NO_CONTENT).append("message","Player not online");
                        return;
                    }
                    online.sendMessage(query.get("message"));
                    return;
                }else if(action.equalsIgnoreCase("connect") && query.contains("server")){
                    OnlineNetworkPlayer online = player.getOnlinePlayer();
                    if(online == null){
                        response.append("code", ResponseCode.NO_CONTENT).append("message","Player not online");
                        return;
                    }
                    online.connect(query.get("server"));
                    return;
                }else if(action.equalsIgnoreCase("executeCommand") && query.contains("command")){
                    OnlineNetworkPlayer online = player.getOnlinePlayer();
                    if(online == null){
                        response.append("code", ResponseCode.NO_CONTENT).append("message","Player not online");
                        return;
                    }
                    online.executeCommand(query.get("command"));
                    return;
                }else if(action.equalsIgnoreCase("hasPermission") && query.contains("permission")){
                    response.append("hasPermission",player.hasPermission(query.get("permission")));
                    return;
                }
            }
        }
        response.append("code", ResponseCode.BAD_REQUEST).append("message","Invalid request");
    }
}
