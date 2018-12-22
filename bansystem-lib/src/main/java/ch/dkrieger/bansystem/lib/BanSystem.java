package ch.dkrieger.bansystem.lib;

import ch.dkrieger.bansystem.lib.broadcast.BroadcastManager;
import ch.dkrieger.bansystem.lib.command.NetworkCommandManager;
import ch.dkrieger.bansystem.lib.command.defaults.*;
import ch.dkrieger.bansystem.lib.config.Config;
import ch.dkrieger.bansystem.lib.config.MessageConfig;
import ch.dkrieger.bansystem.lib.filter.FilterManager;
import ch.dkrieger.bansystem.lib.player.PlayerManager;
import ch.dkrieger.bansystem.lib.player.history.entry.HistoryEntry;
import ch.dkrieger.bansystem.lib.reason.ReasonProvider;
import ch.dkrieger.bansystem.lib.report.ReportManager;
import ch.dkrieger.bansystem.lib.stats.NetworkStats;
import ch.dkrieger.bansystem.lib.storage.DKBansStorage;
import ch.dkrieger.bansystem.lib.storage.mongodb.MongoDBDKBansStorage;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class BanSystem {

    private static BanSystem instance;
    private String version;
    private final DKBansPlatform platform;
    private PlayerManager playerManager;
    private ReportManager reportManager;
    private BroadcastManager broadcastManager;
    private FilterManager filterManager;
    private ReasonProvider reasonProvider;
    private DKBansStorage storage;
    private DKNetwork network;

    private Config config;
    private MessageConfig messageConfig;

    private NetworkStats cachedNetworkStats;

    public BanSystem(DKBansPlatform platform, DKNetwork network, PlayerManager playerManager) {
        if(instance != null) throw new IllegalArgumentException("DKBans is already initialised");
        instance = this;
        this.platform = platform;
        this.network = network;
        this.playerManager = playerManager;
        new Messages("DKBans");

        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("dkperms.properties"));
            this.version = properties.getProperty("version");
        } catch (Exception exception) {
            System.out.println(Messages.SYSTEM_PREFIX+"Could not load DKBans plugin build information");
            this.version = "Unknown";
        }
        System.out.println(Messages.SYSTEM_PREFIX+"plugin is starting");
        System.out.println(Messages.SYSTEM_PREFIX+"BanSystem "+this.version+" by Davide Wietlisbach");
        new HistoryEntry(UUID.randomUUID(), "", "", "", 0L, 0, 0, 1, "", null) {
            @Override
            public String getTypeName() {
                return null;
            }

            @Override
            public TextComponent getListMessage() {
                return null;
            }

            @Override
            public TextComponent getInfoMessage() {
                return null;
            }
        };

        systemBootstrap();

        System.out.println(Messages.SYSTEM_PREFIX+"plugin successfully started");

    }
    private void systemBootstrap(){


        this.config = new Config(this.platform);
        this.config.loadConfig();

        this.messageConfig = new MessageConfig(this.platform);
        this.messageConfig.loadConfig();

        this.reasonProvider = new ReasonProvider(this.platform);

        this.storage = new MongoDBDKBansStorage(this.config);

        if(!storage.connect()){

        }

        this.broadcastManager = new BroadcastManager();
        this.filterManager = new FilterManager();
        this.reportManager = new ReportManager();

        if(this.config.commandBan) getCommandManager().registerCommand(new BanCommand());
        //if(this.config.commandBaninfo) getCommandManager().registerCommand(new BanCommand());
        if(this.config.commandBroadcast) getCommandManager().registerCommand(new BroadcastCommand());
        if(this.config.commandChatlog) getCommandManager().registerCommand(new ChatLogCommand());
        if(this.config.commandFilter) getCommandManager().registerCommand(new FilterCommand());
        if(this.config.commandHelp) getCommandManager().registerCommand(new HelpCommand());
        if(this.config.commandHistory) getCommandManager().registerCommand(new HistoryCommand());
        if(this.config.commandIpinfo) getCommandManager().registerCommand(new IpInfoCommand());
        if(this.config.commandJoinme) getCommandManager().registerCommand(new JoinMeCommand());
        if(this.config.commandJoinme) getCommandManager().registerCommand(new JumptoCommand());
        if(this.config.commandKick) getCommandManager().registerCommand(new KickCommand());
        if(this.config.commandBan) getCommandManager().registerCommand(new MuteCommand());
        if(this.config.commandNetworkstats) getCommandManager().registerCommand(new NetworkStatsCommand());
        if(this.config.commandOnlinetime) getCommandManager().registerCommand(new OnlinetimeCommand());
        if(this.config.commandPing) getCommandManager().registerCommand(new PingCommand());
        if(this.config.commandPlayerinfo) getCommandManager().registerCommand(new PlayerInfoCommand());
        if(this.config.commandReport) getCommandManager().registerCommand(new ReportCommand());
        if(this.config.commandReports) getCommandManager().registerCommand(new ReportsCommand());
        if(this.config.commandResethistory) getCommandManager().registerCommand(new ResetHistoryCommand());
        if(this.config.commandTeamchat) getCommandManager().registerCommand(new TeamChatCommand());
        if(this.config.commandTempban) getCommandManager().registerCommand(new TempbanCommand());
        if(this.config.commandTempmute) getCommandManager().registerCommand(new TempmuteCommand());
        if(this.config.commandUnban) getCommandManager().registerCommand(new UnbanCommand());
        getCommandManager().registerCommand(new DKBansCommand());
    }
    public void shutdown(){
        if(this.storage != null) this.storage.disconnect();
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

    public NetworkCommandManager getCommandManager(){
        return this.platform.getCommandManager();
    }

    public NetworkStats getNetworkStats(){
        if(cachedNetworkStats == null || cachedNetworkStats.getLoadTime()+TimeUnit.MINUTES.toMillis(2) < System.currentTimeMillis())
            this.cachedNetworkStats = getStorage().getNetworkStats();
        return this.cachedNetworkStats;
    }

    public void setPlayerManager(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public void setStorage(DKBansStorage storage) {
        this.storage = storage;
    }

    public void setNetwork(DKNetwork network) {
        this.network = network;
    }

    public static BanSystem getInstance() {
        return instance;
    }
}
