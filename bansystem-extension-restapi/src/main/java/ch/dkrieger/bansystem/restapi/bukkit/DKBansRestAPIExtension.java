package ch.dkrieger.bansystem.restapi.bukkit;

import ch.dkrieger.bansystem.restapi.DKBansRestAPIServer;
import ch.dkrieger.bansystem.restapi.DKBansRestApiConfig;
import org.bukkit.plugin.java.JavaPlugin;

public class DKBansRestAPIExtension extends JavaPlugin {

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
