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

package ch.dkrieger.bansystem.restapi.handler.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.filter.FilterType;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.chatlog.ChatLog;
import ch.dkrieger.bansystem.lib.player.chatlog.ChatLogEntry;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.restapi.ResponseCode;
import ch.dkrieger.bansystem.restapi.handler.RestApiHandler;

import java.util.List;

public class ChatLogHandler extends RestApiHandler {

    public ChatLogHandler() {
        super("chatlog/");
    }
    @Override
    public void onRequest(Query query, Document response) {
        ChatLog chatLog = null;
        if(query.contains("player") && query.contains("server")){
            NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(query.get("player"));
            if(player == null){
                response.append("code", ResponseCode.NO_CONTENT);
                response.append("message","ChatLog not found");
                return;
            }
            chatLog = BanSystem.getInstance().getPlayerManager().getChatLog(player.getUUID(),query.get("server"));
        }else if(query.contains("player")){
            NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(query.get("player"));
            if(player == null){
                response.append("code", ResponseCode.NO_CONTENT);
                response.append("message","ChatLog not found");
                return;
            }
            chatLog = BanSystem.getInstance().getPlayerManager().getChatLog(player);
        }else if(query.contains("server")){
            chatLog = BanSystem.getInstance().getPlayerManager().getChatLog(query.get("server"));
        }
        if(chatLog == null){
            response.append("code", ResponseCode.NO_CONTENT);
            response.append("message","ChatLog not found");
        }else{
            long from = 0;
            long to = 0;
            try{to = Long.valueOf(query.get("to"));}catch (Exception exception){}
            try{from = Long.valueOf(query.get("from"));}catch (Exception exception){}
            List<ChatLogEntry> entries = chatLog.getEntries(new ChatLog.Filter(from,to,FilterType.ParseNull(query.get("filter"))));
            if(entries.size() <= 0) {
                response.append("code", ResponseCode.NO_CONTENT);
                response.append("message", "ChatLog not found");
            }else response.append("chatLogs",entries);
        }
    }
}
