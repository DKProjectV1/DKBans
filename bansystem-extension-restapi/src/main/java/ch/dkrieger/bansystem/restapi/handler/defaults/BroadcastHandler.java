package ch.dkrieger.bansystem.restapi.handler.defaults;

import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.restapi.handler.RestApiHandler;

public class BroadcastHandler extends RestApiHandler {

    public BroadcastHandler() {
        super("broadcast/");
    }

    @Override
    public void onRequest(Query query, Document response) {

    }

}
