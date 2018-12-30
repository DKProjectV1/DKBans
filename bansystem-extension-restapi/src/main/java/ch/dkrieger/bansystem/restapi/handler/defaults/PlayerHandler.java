package ch.dkrieger.bansystem.restapi.handler.defaults;

import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.restapi.handler.RestApiHandler;

public class PlayerHandler extends RestApiHandler {

    public PlayerHandler() {
        super("player/");
    }

    @Override
    public void onRequest(Query query, Document response) {
        if(query.contains("")){

        }
    }
}
