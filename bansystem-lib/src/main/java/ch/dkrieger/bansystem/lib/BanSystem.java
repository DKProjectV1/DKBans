package ch.dkrieger.bansystem.lib;

import ch.dkrieger.bansystem.lib.config.Config;
import ch.dkrieger.bansystem.lib.player.PlayerManager;
import ch.dkrieger.bansystem.lib.reason.ReasonProvider;

public class BanSystem {

    private static BanSystem instance;
    private final String version;
    private final DKBansPlatform platform;
    private PlayerManager playerManager;
    private ReasonProvider reasonProvider;
    private DKNetwork network;

    private Config config;

    public BanSystem(DKBansPlatform platform) {
        instance = this;
        this.version = getClass().getPackage().getImplementationVersion();
        this.platform = platform;
    }
    public void shutdown(){

    }

    public DKBansPlatform getPlatform() {
        return platform;
    }

    public String getVersion() {
        return version;
    }

    public Config getConfig() {
        return config;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public ReasonProvider getReasonProvider() {
        return reasonProvider;
    }

    public DKNetwork getNetwork() {
        return network;
    }

    public static BanSystem getInstance() {
        return instance;
    }
}
