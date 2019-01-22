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

import ch.dkrieger.bansystem.extension.restapi.ResponseCode;
import ch.dkrieger.bansystem.extension.restapi.handler.RestApiHandler;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

public class BroadcastHandler extends RestApiHandler {

    public BroadcastHandler() {
        super("broadcast/");
    }

    @Override
    public void onRequest(Document query, Document response) {
        String action = query.getString("action");
        if(action != null){
            if(action.equalsIgnoreCase("list")){
                response.append("broadcasts", BanSystem.getInstance().getBroadcastManager().getBroadcasts());
                return;
            }else if(action.equalsIgnoreCase("create") && query.contains("message")){
                Broadcast.ClickType type = null;
                if(query.contains("clickType")){
                    type = Broadcast.ClickType.parseNull(query.getString("clickType"));
                    if(type == null){
                        response.append("code", ResponseCode.NO_CONTENT).append("message","Click type was not found");
                        return;
                    }
                }else type = Broadcast.ClickType.URL;
                Broadcast broadcast = new Broadcast(-1,query.getString("message"),query.getString("permission"),query.getString("hover")
                        ,System.currentTimeMillis(),System.currentTimeMillis(),Boolean.valueOf(query.getString("auto"))
                        ,new Broadcast.Click(query.getString("clickMessage"),type));
                broadcast = BanSystem.getInstance().getBroadcastManager().createBroadcast(broadcast);
                response.append("broadcast",broadcast).append("message","Broadcast created");
                return;
            }else if(action.equalsIgnoreCase("update") && query.contains("id") && GeneralUtil.isNumber(query.getString("id"))){
                Broadcast broadcast = BanSystem.getInstance().getBroadcastManager().getBroadcast(Integer.valueOf(query.getString("id")));
                if(broadcast == null){
                    response.append("code",ResponseCode.NO_CONTENT).append("message","Broadcast not found");
                    return;
                }
                if(query.contains("message")) broadcast.setMessage(query.getString("message"));
                if(query.contains("hover")) broadcast.setHover(query.getString("hover"));
                if(query.contains("permission")) broadcast.setPermission(query.getString("permission"));
                if(query.contains("auto")) broadcast.setAuto(Boolean.valueOf(query.getString("auto")));
                if(query.contains("clickMessage")) broadcast.getClick().setMessage(query.getString("clickMessage"));
                if(query.contains("clickType")){
                    Broadcast.ClickType type = Broadcast.ClickType.parseNull(query.getString("clickType"));
                    if(type == null){
                        response.append("code",ResponseCode.NO_CONTENT).append("message","Click type was not found");
                        return;
                    }
                    broadcast.getClick().setType(type);
                }
                response.append("broadcast",broadcast).append("message","Broadcast updated");
                return;
            }else if(action.equalsIgnoreCase("delete") && query.contains("id") && GeneralUtil.isNumber(query.getString("id"))){
                BanSystem.getInstance().getBroadcastManager().deleteBroadcast(Integer.valueOf(query.getString("id")));
                response.append("message","Broadcast deleted");
                return;
            }else if(action.equalsIgnoreCase("direct") && query.contains("message")){
                BanSystem.getInstance().getNetwork().broadcast(Messages.BROADCAST_FORMAT_DIRECT
                        .replace("[message]",query.getString("message")).replace("[prefix]",Messages.PREFIX_NETWORK));
                response.append("message","Message sent");
                return;
            }else if(action.equalsIgnoreCase("send") && query.contains("id") && GeneralUtil.isNumber(query.getString("id"))){
                Broadcast broadcast = BanSystem.getInstance().getBroadcastManager().getBroadcast(Integer.valueOf(query.getString("id")));
                BanSystem.getInstance().getNetwork().broadcast(broadcast);
                response.append("message","Broadcast sent");
                return;
            }
        }
        response.append("code", ResponseCode.BAD_REQUEST).append("message","Invalid request");
    }

}
