package ch.dkrieger.bansystem.restapi.handler.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.chatlog.ChatLog;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.restapi.ResponseCode;
import ch.dkrieger.bansystem.restapi.handler.RestApiHandler;

public class ChatLogHandler extends RestApiHandler {

    public ChatLogHandler() {
        super("chatlog/");
    }
    @Override
    public void onRequest(Query query, Document response) {
        if(query.contains("player")){
            NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(query.get("player"));
            if(player == null){
                response.append("code", ResponseCode.NO_CONTENT);
                response.append("message","ChatLog not found");
            }
            ChatLog chatLog = BanSystem.getInstance().getPlayerManager().getChatLog(player);
            if(chatLog == null){
                response.append("code", ResponseCode.NO_CONTENT);
                response.append("message","ChatLog not found");
            }else response.append("entries",chatLog.getEntries());
            return;
        }else if(query.contains("server")){
            ChatLog chatLog = BanSystem.getInstance().getPlayerManager().getChatLog(query.get("server"));
            if(chatLog == null){
                response.append("code", ResponseCode.NO_CONTENT);
                response.append("message","ChatLog not found");
            }else response.append("entries",chatLog.getEntries());
            return;
        }
        response.append("code", ResponseCode.BAD_REQUEST).append("message","Invalid request");
    }
}
