package ch.dkrieger.bansystem.restapi.handler.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.restapi.handler.RestApiHandler;

public class NetworkInfoHandler extends RestApiHandler {

    public NetworkInfoHandler() {
        super("networkinfo/");
    }

    @Override
    public void onRequest(Query query, Document response) {
        response.append("stats", BanSystem.getInstance().getNetworkStats())
                .append("onlineCount",BanSystem.getInstance().getPlayerManager().getOnlineCount());
    }
}
