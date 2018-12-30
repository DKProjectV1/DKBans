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
