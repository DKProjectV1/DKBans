/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 01.01.19 12:35
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

package ch.dkrieger.bansystem.extension.webinterface;

import ch.dkrieger.bansystem.extension.restapi.ResponseCode;
import ch.dkrieger.bansystem.extension.restapi.handler.RestApiHandler;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

public class WebinterfaceHandler extends RestApiHandler {

    private DKBansWebinterfaceConfig config;

    public WebinterfaceHandler(DKBansWebinterfaceConfig config) {
        super("dkbanswi/");
        this.config = config;
    }
    public void onRequest(Document query, Document response){
        if(query.contains("action")){
            if(query.getString("action").equalsIgnoreCase("checkLogin") && query.contains("player") && query.contains("password")){
                NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(query.getString("player"));
                if(player == null){
                    response.append("loginSuccess",false).append("message","Player not found");
                    return;
                }
                if(!player.hasPermission("dkbans.webinterface")){
                    response.append("loginSuccess",false).append("message","No access to webinterface");
                    return;
                }
                if(player.getProperties().contains("password") && player.getProperties().getString("password").equals(GeneralUtil.encodeMD5(query.getString("password")))){
                    if(!config.canAccess(player)){
                        response.append("loginSuccess",false).append("message","Login canceled by event");
                        return;
                    }
                    response.append("loginSuccess",true).append("player",player);
                }else{
                    response.append("loginSuccess",false).append("message","Wrong password");
                }
                return;
            }
        }
        response.append("code", ResponseCode.BAD_REQUEST).append("message","Invalid request");
    }
}
