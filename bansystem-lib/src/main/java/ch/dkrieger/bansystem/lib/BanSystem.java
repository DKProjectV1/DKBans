package ch.dkrieger.bansystem.lib;

import ch.dkrieger.bansystem.lib.broadcast.BroadcastManager;
import ch.dkrieger.bansystem.lib.config.Config;
import ch.dkrieger.bansystem.lib.filter.FilterManager;
import ch.dkrieger.bansystem.lib.player.PlayerManager;
import ch.dkrieger.bansystem.lib.reason.ReasonProvider;
import ch.dkrieger.bansystem.lib.report.ReportManager;
import ch.dkrieger.bansystem.lib.storage.DKBansStorage;

public class BanSystem {

    private static BanSystem instance;
    private final String version;
    private final DKBansPlatform platform;
    private PlayerManager playerManager;
    private ReportManager reportManager;
    private BroadcastManager broadcastManager;
    private FilterManager filterManager;
    private ReasonProvider reasonProvider;
    private DKBansStorage storage;
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

    public ReportManager getReportManager() {
        return reportManager;
    }

    public BroadcastManager getBroadcastManager() {
        return broadcastManager;
    }

    public FilterManager getFilterManager() {
        return filterManager;
    }

    public ReasonProvider getReasonProvider() {
        return reasonProvider;
    }

    public DKBansStorage getStorage() {
        return storage;
    }

    public DKNetwork getNetwork() {
        return network;
    }

    public static BanSystem getInstance() {
        return instance;
    }
}
