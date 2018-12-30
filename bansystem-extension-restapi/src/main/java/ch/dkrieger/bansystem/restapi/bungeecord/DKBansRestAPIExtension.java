package ch.dkrieger.bansystem.restapi.bungeecord;

import ch.dkrieger.bansystem.restapi.DKBansRestAPIServer;
import ch.dkrieger.bansystem.restapi.DKBansRestApiConfig;
import net.md_5.bungee.api.plugin.Plugin;

public class DKBansRestAPIExtension extends Plugin {

    private DKBansRestAPIServer server;

    @Override
    public void onEnable() {
        this.server = new DKBansRestAPIServer(new DKBansRestApiConfig());

        this.server.startAsync();
    }
    @Override
    public void onDisable() {
        this.server.shutdown();
    }
}
