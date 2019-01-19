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
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

public class NetworkHandler extends RestApiHandler {

    public NetworkHandler() {
        super("network/");
    }

    @Override
    public void onRequest(Query query, Document response) {
        if(query.contains("action")){
            if(query.get("action").equalsIgnoreCase("info")){
                response.append("stats", BanSystem.getInstance().getNetworkStats())
                        .append("registerCount",BanSystem.getInstance().getPlayerManager().getRegisteredCount())
                        .append("onlineCount",BanSystem.getInstance().getPlayerManager().getOnlineCount());
                return;
            }else if(query.get("action").equalsIgnoreCase("sendTeamMessage") && query.contains("message")){
                BanSystem.getInstance().getNetwork().sendTeamMessage(query.get("message"),Boolean.valueOf(query.get("onlyLogin")));
                return;
            }else if(query.get("action").equalsIgnoreCase("reasons")){
                response.append("banReasons",BanSystem.getInstance().getReasonProvider().getBanReasons())
                        .append("reportReasons",BanSystem.getInstance().getReasonProvider().getReportReasons())
                        .append("unbanReasons",BanSystem.getInstance().getReasonProvider().getBanReasons());
                return;
            }else if(query.get("action").equalsIgnoreCase("config")){
                response.append("config",BanSystem.getInstance().getConfig());
                return;
            }else if(query.get("action").equalsIgnoreCase("check")){
                response.append("available",true);
                return;
            }
        }
        response.append("code", ResponseCode.BAD_REQUEST).append("message","Invalid request");
    }
}
