package ch.dkrieger.bansystem.restapi.handler.defaults;

import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.restapi.handler.RestApiHandler;

public class PlayerHandler extends RestApiHandler {

    public PlayerHandler() {
        super("player/");
    }

    @Override
    public void onRequest(Query query, Document response) {
        String action = query.get("action");
        if(action.equalsIgnoreCase("onlineList")){

        }else if(action.equalsIgnoreCase("onlineCount")){

        }else if(query.contains("player")){
            if(action.equalsIgnoreCase("info")){

            }else if(action.equalsIgnoreCase("setTeamChatLogin")){

            }else if(action.equalsIgnoreCase("setReportLogin")){

            }else if(action.equalsIgnoreCase("updateProperties")){

            }else if(action.equalsIgnoreCase("isOnline")){

            }else if(action.equalsIgnoreCase("isBanned")){

            }else if(action.equalsIgnoreCase("isMuted")){

            }else if(action.equalsIgnoreCase("ban")){

            }else if(action.equalsIgnoreCase("kick")){

            }else if(action.equalsIgnoreCase("unban")){

            }
        }
    }
}
