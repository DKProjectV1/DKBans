/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 30.12.18 14:39
 * @Website https://github.com/DevKrieger/DKBans
 *
 * The DKBans Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package ch.dkrieger.bansystem.lib;

import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.broadcast.BroadcastManager;
import ch.dkrieger.bansystem.lib.command.NetworkCommandManager;
import ch.dkrieger.bansystem.lib.command.defaults.*;
import ch.dkrieger.bansystem.lib.config.Config;
import ch.dkrieger.bansystem.lib.config.MessageConfig;
import ch.dkrieger.bansystem.lib.filter.FilterManager;
import ch.dkrieger.bansystem.lib.player.PlayerManager;
import ch.dkrieger.bansystem.lib.player.history.HistoryManager;
import ch.dkrieger.bansystem.lib.player.history.entry.HistoryEntry;
import ch.dkrieger.bansystem.lib.reason.ReasonProvider;
import ch.dkrieger.bansystem.lib.report.ReportManager;
import ch.dkrieger.bansystem.lib.stats.NetworkStats;
import ch.dkrieger.bansystem.lib.storage.DKBansStorage;
import ch.dkrieger.bansystem.lib.storage.StorageType;
import ch.dkrieger.bansystem.lib.storage.json.JsonDKBansStorage;
import ch.dkrieger.bansystem.lib.storage.mongodb.MongoDBDKBansStorage;
import ch.dkrieger.bansystem.lib.storage.sql.SQLDKBansStorage;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
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
    private HistoryManager historyManager;
    private ReasonProvider reasonProvider;
    private DKBansStorage storage;
    private DKNetwork network;

    private Config config;
    private MessageConfig messageConfig;

    private NetworkStats cachedNetworkStats, tempSyncStats;

    public BanSystem(DKBansPlatform platform, DKNetwork network, PlayerManager playerManager) {
        if(instance != null) throw new IllegalArgumentException("DKBans is already initialised");
        instance = this;
        this.platform = platform;
        this.network = network;
        this.playerManager = playerManager;
        new Messages("DKBans");

        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("dkbans.properties"));
            this.version = properties.getProperty("version");
        } catch (Exception exception) {
            System.out.println(Messages.SYSTEM_PREFIX+"Could not load DKBans plugin build information.");
            this.version = "Unknown";
        }
        System.out.println(Messages.SYSTEM_PREFIX+"plugin is starting");
        System.out.println(Messages.SYSTEM_PREFIX+"BanSystem "+this.version+" by Davide Wietlisbach");

        systemBootstrap();

        System.out.println(Messages.SYSTEM_PREFIX+"plugin successfully started");
    }
    private void systemBootstrap(){
        this.config = new Config(this.platform);
        this.config.loadConfig();

        this.messageConfig = new MessageConfig(this.platform);
        this.messageConfig.loadConfig();

        this.reasonProvider = new ReasonProvider(this.platform);
        this.historyManager = new HistoryManager();

        HistoryEntry.buildAdapter();

        if(this.config.storageType == StorageType.MONGODB) this.storage = new MongoDBDKBansStorage(this.config);
        else if(this.config.storageType == StorageType.SQLITE || this.config.storageType == StorageType.MYSQL)this.storage = new SQLDKBansStorage(config);
        else if(this.config.storageType == StorageType.JSON) this.storage = new JsonDKBansStorage(this.config);

        if(storage != null && storage.connect()){
            System.out.println(Messages.SYSTEM_PREFIX + "Used Storage: " + this.config.storageType.toString());
        }else{
            this.config.storageType = StorageType.SQLITE;
            this.storage = new SQLDKBansStorage(config);
            if(!this.storage.connect()){
                this.storage = new JsonDKBansStorage(config);
                this.config.storageType = StorageType.JSON;
                if(!this.storage.connect()){
                    System.out.println(Messages.SYSTEM_PREFIX +"Could not enable DKBans, no storage is working.");
                    return;
                }
                System.out.println(Messages.SYSTEM_PREFIX + "Used Backup Storage: " + config.storageType.toString());
            }
        }

        this.broadcastManager = new BroadcastManager();
        this.filterManager = new FilterManager();
        this.reportManager = new ReportManager();

        this.tempSyncStats = new NetworkStats();
        getPlatform().getTaskManager().scheduleTask(this::saveStats,(long)GeneralUtil.getRandom(60, 240),TimeUnit.SECONDS);

        if(this.config.bungeecord && !(platform.getPlatformName().equalsIgnoreCase("bungeecord"))) return;

        if(this.config.commandBan) getCommandManager().registerCommand(new BanCommand());
        if(this.config.commandBaninfo) getCommandManager().registerCommand(new BaninfoCommand());
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
        if(this.config.commandStaffstats) getCommandManager().registerCommand(new StaffStatsCommand());
        if(this.config.commandIPban) getCommandManager().registerCommand(new IpBanCommand());
        if(this.config.commandIPUnban) getCommandManager().registerCommand(new IpUnbanCommand());
        if(this.config.commandWarn) getCommandManager().registerCommand(new WarnCommand());
        getCommandManager().registerCommand(new DKBansCommand());
        getCommandManager().registerCommand(new BroadcastJumpCommand());

        if(config.autobroadcastEnabled)
            this.platform.getTaskManager().scheduleTask(() -> {
                Broadcast broadcast = getBroadcastManager().getNext();
                getNetwork().broadcastLocal(broadcast);
            },(long)config.autobroadcastDelay,TimeUnit.MINUTES);
        if(config.chatlogAutoDeleteEnabled){
            this.platform.getTaskManager().scheduleTask(()->{
                this.storage.deleteOldChatLog(System.currentTimeMillis()-TimeUnit.DAYS.toMillis(config.chatlogAutoDeleteInDays));
            },5L,TimeUnit.MINUTES);
        }
    }
    public void shutdown(){
        saveStats();
        if(this.storage != null) this.storage.disconnect();
    }
    private void saveStats(){
        NetworkStats stats = getStorage().getNetworkStats();
        this.cachedNetworkStats = stats;
        getStorage().updateNetworkStats((stats.getLogins()+tempSyncStats.getLogins()),(stats.getReports()+tempSyncStats.getReports())
                ,(stats.getReportsAccepted()+tempSyncStats.getReportsAccepted()),(stats.getMutes()+tempSyncStats.getMessages())
                ,(stats.getBans()+tempSyncStats.getBans()),(stats.getMutes()+tempSyncStats.getMutes())
                ,(stats.getUnbans()+tempSyncStats.getUnbans()),(stats.getKicks()+tempSyncStats.getKicks()),(stats.getWarns()+tempSyncStats.getWarns()));
        this.tempSyncStats = new NetworkStats();
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

    public MessageConfig getMessageConfig() {
        return messageConfig;
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

    public HistoryManager getHistoryManager() {
        return historyManager;
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
        if(this.cachedNetworkStats == null) this.cachedNetworkStats = getStorage().getNetworkStats();
        return this.cachedNetworkStats;
    }
    public NetworkStats getTempSyncStats() {
        if(this.tempSyncStats == null) this.tempSyncStats = new NetworkStats();
        return tempSyncStats;
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
