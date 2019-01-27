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

package ch.dkrieger.bansystem.lib.config;

import ch.dkrieger.bansystem.lib.DKBansPlatform;
import ch.dkrieger.bansystem.lib.config.mode.BanMode;
import ch.dkrieger.bansystem.lib.config.mode.ReasonMode;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.PlayerColor;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.HistoryPoints;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.storage.StorageType;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import ch.dkrieger.bansystem.lib.utils.TabCompleteOption;
import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Config extends SimpleConfig{

    public boolean bungeecord;

    public StorageType storageType;
    public transient File storageFolder;
    public transient String storageHost, storagePort, storageUser, storagePassword, storageDatabase, mongoDbAuthDB;
    public boolean storageSSL, mongoDbSrv, mongoDbAuthentication;

    public BanMode banMode;
    public ReasonMode unbanMode;
    public ReasonMode kickMode;

    public transient DateFormat dateFormat;
    public char serverGroupSplit;

    public boolean joinMeEnabled;
    public long joinMeCooldown;
    public long joinMeTimeOut;
    public boolean joinMeDisabledServerEquals;
    public List<String> joinMeDisabledServerList;

    public boolean reportControls;
    public ReasonMode reportMode;
    public long reportDelay;
    public boolean reportAutoCommandExecuteOnProxy;
    public List<String> reportAutoCommandEnter;
    public List<String> reportAutoCommandExit;

    public ReasonMode warnMode;
    public String  warnStaffName;
    public int warnAutoBanCount;
    public boolean warnAutoBanBanForLastReason;
    public long warnAutoBanCustomDuration;
    public String warnAutoBanCustomReason;
    public int warnAutoBanCustomPoints;
    public BanType warnAutoBanBanType;

    public boolean onJoinChatClear;
    public boolean onJoinTeamChatInfo;
    public boolean onJoinReportInfo;
    public boolean onJoinReportSize;

    public boolean tabCompleteBlockEnabled;
    public List<TabCompleteOption> tabCompleteOptions;

    public boolean chatBlockPlugin;
    public long chatDelay;
    public boolean chatFilterEnabled;
    public int chatFilterRepeatMatchesPercent;
    public int chatFilterAutobanCount;
    public boolean chatFilterAutobanEnabled;
    public int chatFilterAutobanMessageBanID;
    public int chatFilterAutobanPromotionBanID;

    public boolean banPointsSeparateChatAndNetwork;
    public long banPointsChatTime;
    public int banPointsChatPermanently;
    public long banPointsNetworkTime;
    public int banPointsNetworkPermanently;

    public boolean playerSaveIP;
    public boolean playerOnlineSessionSaving;
    public int playerIPDeletionInDays;
    public boolean playerColorLiveUpdate;
    public String playerColorDefault;
    public String playerColorConsole;
    public List<PlayerColor> playerColorColors;

    public boolean chatlogEnabled;
    public boolean chatlogAutoDeleteEnabled;
    public int chatlogAutoDeleteInDays;

    public boolean autobroadcastEnabled;
    public boolean autobroadcastSorted;
    public int autobroadcastDelay;

    public boolean ipBanBanOnlyNewPlayers;
    public long ipBanBanDuration;
    public String ipBanBanReason;
    public String ipBanBanSraff;
    public int ipBanBanPoints;
    public boolean ipBanOnBanEnabled;
    public long ipBanOnBanDuration;



    public boolean commandBan;
    public boolean commandUnban;
    public boolean commandBaninfo;
    public boolean commandBroadcast;
    public boolean commandChatlog;
    public boolean commandFilter;
    public boolean commandHelp;
    public boolean commandHistory;
    public boolean commandIpinfo;
    public boolean commandJoinme;
    public boolean commandJumpto;
    public boolean commandKick;
    public boolean commandNetworkstats;
    public boolean commandOnlinetime;
    public boolean commandPing;
    public boolean commandPlayerinfo;
    public boolean commandReport;
    public boolean commandReports;
    public boolean commandResethistory;
    public boolean commandTeamchat;
    public boolean commandTempban;
    public boolean commandTempmute;
    public boolean commandStaffstats;
    public boolean commandIPban;
    public boolean commandIPUnban;
    public boolean commandWarn;
    public boolean commandEdit;
    public boolean commandMyHistoryPoints;

    private transient DKBansPlatform platform;

    public Config(DKBansPlatform platform) {
        super(new File(platform.getFolder(),"config.yml"));
        this.platform = platform;
    }
    @Override
    public void onLoad() {
        this.bungeecord = addAndGetBooleanValue("bungeecord",false);
        this.storageType = StorageType.parse(addAndGetStringValue("storage.type",StorageType.SQLITE.toString()));
        this.storageFolder = new File(addAndGetStringValue("storage.folder",platform.getFolder().toString()+"/data/"));
        this.storageHost = addAndGetStringValue("storage.host","localhost");
        this.storagePort = addAndGetStringValue("storage.port","3306");
        this.storageUser = addAndGetStringValue("storage.user","root");
        this.storageSSL = addAndGetBooleanValue("storage.ssl",false);
        this.storagePassword = addAndGetStringValue("storage.password","password");
        this.storageDatabase = addAndGetStringValue("storage.database","DKBans");
        this.mongoDbAuthentication = addAndGetBooleanValue("storage.mongodb.authentication",false);
        this.mongoDbAuthDB = addAndGetStringValue("storage.mongodb.authdb","admin");
        this.mongoDbSrv = addAndGetBooleanValue("storage.mongodb.srv",false);

        this.banMode = BanMode.parse(addAndGetStringValue("ban.mode",BanMode.TEMPLATE.toString()));
        this.banPointsSeparateChatAndNetwork = addAndGetBooleanValue("ban.points.separate.chatandnetwork",true);
        this.banPointsChatTime =  GeneralUtil.convertToMillis(addAndGetLongValue("ban.points.chat.duration.time",3)
                ,addAndGetStringValue("ban.points.chat.duration.unit",TimeUnit.HOURS.toString()));
        this.banPointsChatPermanently = addAndGetIntValue("ban.points.chat.permanently",200);
        this.banPointsNetworkTime =  GeneralUtil.convertToMillis(addAndGetLongValue("ban.points.network.duration.time",1)
                ,addAndGetStringValue("ban.points.network.duration.unit",TimeUnit.DAYS.toString()));
        this.banPointsNetworkPermanently = addAndGetIntValue("ban.points.network.permanently",80);

        this.unbanMode = ReasonMode.parse(addAndGetStringValue("unban.mode",BanMode.SELF.toString()));
        this.kickMode = ReasonMode.parse(addAndGetStringValue("kick.mode",BanMode.SELF.toString()));

        this.reportMode = ReasonMode.parse(addAndGetStringValue("report.mode",ReasonMode.TEMPLATE.toString()));
        this.reportControls = addAndGetBooleanValue("report.controls",true);
        this.reportDelay = addAndGetLongValue("report.delay",900000);
        this.reportAutoCommandExecuteOnProxy = addAndGetBooleanValue("report.autocommand.onproxy",false);
        this.reportAutoCommandEnter = addAndGetStringListValue("report.autocommand.enter", Arrays.asList());//"tp [player]"
        this.reportAutoCommandExit = addAndGetStringListValue("report.autocommand.exit", Arrays.asList());

        this.warnMode = ReasonMode.parse(addAndGetStringValue("warn.mode",ReasonMode.TEMPLATE.toString()));
        this.warnStaffName = addAndGetStringValue("warn.staffname","WarnManager");
        this.warnAutoBanCount = addAndGetIntValue("warn.autoban.count",5);
        this.warnAutoBanBanForLastReason = addAndGetBooleanValue("warn.autoban.banforlastreason",true);
        this.warnAutoBanCustomReason = addAndGetStringValue("warn.autoban.reason","Warns");
        this.warnAutoBanCustomDuration = GeneralUtil.convertToMillis(addAndGetLongValue("warn.autoban.duration.time",30)
                ,addAndGetStringValue("warn.autoban.duration.unit",TimeUnit.DAYS.toString()));
        this.warnAutoBanCustomPoints = addAndGetIntValue("warn.autoban.points",20);
        this.warnAutoBanBanType  = BanType.parse(addAndGetStringValue("warn.autoban.type",BanType.NETWORK.toString()));

        this.dateFormat = new SimpleDateFormat(addAndGetStringValue("date.format","dd.MM.yyyy HH:mm"));
        this.serverGroupSplit = addAndGetCharValue("servergroup.split",'-');

        this.joinMeEnabled = addAndGetBooleanValue("joinme.enabled",false);
        this.joinMeCooldown = addAndGetLongValue("joinme.cooldown",120000L);
        this.joinMeTimeOut = addAndGetLongValue("joinme.timeout",300000);
        this.joinMeDisabledServerEquals = addAndGetBooleanValue("joinme.disabledservers.equals",false);
        this.joinMeDisabledServerList = addAndGetStringListValue("joinme.disabledservers.list", Arrays.asList("lobby"));

        this.onJoinChatClear = addAndGetBooleanValue("player.onjoin.chatclear",true);
        this.onJoinTeamChatInfo = addAndGetBooleanValue("player.onjoin.teamchatinfo",true);
        this.onJoinReportInfo = addAndGetBooleanValue("player.onjoin.reportinfo",true);
        this.onJoinReportSize = addAndGetBooleanValue("player.onjoin.reportsize",true);

        this.tabCompleteBlockEnabled = addAndGetBooleanValue("tabcomplete.block.enabled",true);
        List<String> tabOptions = addAndGetStringListValue("tabcomplete.options",Arrays.asList("dkbans.ban:/ban","dkbans.kick:/kick","report"));
        this.tabCompleteOptions = new ArrayList<>();
        for(String tabOption : tabOptions){
            try{
                String[] split = tabOption.split(":");
                this.tabCompleteOptions.add(new TabCompleteOption(split[0],split[1]));
            }catch (Exception exception){
                this.tabCompleteOptions.add(new TabCompleteOption(null,tabOption));
            }
        }

        this.chatBlockPlugin = addAndGetBooleanValue("chat.block.plugin",true);
        this.chatDelay = addAndGetLongValue("chat.delay",500);
        this.chatFilterEnabled = addAndGetBooleanValue("chat.filter.enabled",true);
        this.chatFilterRepeatMatchesPercent =  (100-addAndGetIntValue("chat.filter.repeat.matchespercent",40));
        if(this.chatFilterRepeatMatchesPercent < 0) this.chatFilterRepeatMatchesPercent = 0;
        this.chatFilterAutobanEnabled = addAndGetBooleanValue("chat.autoban.enabled",true);
        this.chatFilterAutobanCount = addAndGetIntValue("chat.filter.autoban.count",8);
        this.chatFilterAutobanMessageBanID = addAndGetIntValue("chat.filter.autoban.banid.message",3);
        this.chatFilterAutobanPromotionBanID = addAndGetIntValue("chat.filter.autoban.banid.promotion",4);

        this.playerSaveIP = addAndGetBooleanValue("player.saveip",true);
        //this.playerIPDeletionInDays = addAndGetIntValue("player.ipautodeleteindays",-1);
        this.playerOnlineSessionSaving = addAndGetBooleanValue("player.save.onlinesession",true);
        this.playerColorLiveUpdate = addAndGetBooleanValue("player.color.liveupdate",true);
        this.playerColorDefault = addAndGetMessageValue("player.color.default","&8");
        this.playerColorConsole = addAndGetMessageValue("player.color.console","&4");
        this.playerColorColors = new ArrayList<>();
        List<String> colors = addAndGetStringListValue("player.color.colors",Arrays.asList("dkbans.color.admin:&4"
                ,"dkbans.color.developer:&b","dkbans.color.mod:&c","dkbans.color.supporter:&9","dkbans.color.builder:&3"
                ,"dkbans.color.youtuber:&5","dkbans.color.premium:&6"));
        for(String color : colors){
            try{
                String[] split = color.split(":");
                this.playerColorColors.add(new PlayerColor(split[0], ChatColor.translateAlternateColorCodes('&',split[1])));
            }catch (Exception exception){}
        }

        this.chatlogEnabled = addAndGetBooleanValue("chat.log.enabled",true);
        this.chatlogAutoDeleteEnabled = addAndGetBooleanValue("chat.log.autodelete.enabled",true);
        this.chatlogAutoDeleteInDays = addAndGetIntValue("chat.log.autodelete.indays",7);

        this.autobroadcastEnabled = addAndGetBooleanValue("autobroadcast.enabled",true);
        this.autobroadcastSorted = addAndGetBooleanValue("autobroadcast.sorted",true);
        this.autobroadcastDelay = addAndGetIntValue("autobroadcast.delay",8);

        this.ipBanBanOnlyNewPlayers = addAndGetBooleanValue("ipban.onlynewplayers",true);
        this.ipBanBanDuration = GeneralUtil.convertToMillis(addAndGetLongValue("ipban.reason.duration.time",365)
                , addAndGetStringValue("ipban.reason.duration.unit",TimeUnit.DAYS.toString()));
        this.ipBanBanReason = addAndGetStringValue("ipban.reason.reason","AltAccount");
        this.ipBanBanSraff = addAndGetStringValue("ipban.reason.staff","AltManager");
        this.ipBanBanPoints = addAndGetIntValue("ipban.reason.points",40);
        this.ipBanOnBanEnabled = addAndGetBooleanValue("ipban.onban.enabled",true);
        this.ipBanOnBanDuration = GeneralUtil.convertToMillis(addAndGetLongValue("ipban.onban.duration.time",24)
                ,addAndGetStringValue("ipban.onban.duration.unit",TimeUnit.HOURS.toString()));

        this.commandBan = addAndGetBooleanValue("command.ban.enabled",true);
        this.commandUnban = addAndGetBooleanValue("command.unban.enabled",true);
        this.commandBaninfo = addAndGetBooleanValue("command.baninfo.enabled",true);
        this.commandBroadcast = addAndGetBooleanValue("command.broadcast.enabled",true);
        this.commandChatlog = addAndGetBooleanValue("command.chatlog.enabled",true);
        this.commandFilter = addAndGetBooleanValue("command.filter.enabled",true);
        this.commandHelp = addAndGetBooleanValue("command.help.enabled",true);
        this.commandHistory = addAndGetBooleanValue("command.history.enabled",true);
        this.commandIpinfo = addAndGetBooleanValue("command.ipinfo.enabled",true);
        this.commandJoinme = addAndGetBooleanValue("command.joinme.enabled",true);
        this.commandJumpto = addAndGetBooleanValue("command.Jumpto.enabled",true);
        this.commandKick = addAndGetBooleanValue("command.kick.enabled",true);
        this.commandNetworkstats = addAndGetBooleanValue("command.networkstats.enabled",true);
        this.commandOnlinetime = addAndGetBooleanValue("command.onlinetime.enabled",true);
        this.commandPing = addAndGetBooleanValue("command.ping.enabled",true);
        this.commandPlayerinfo = addAndGetBooleanValue("command.playerinfo.enabled",true);
        this.commandReport = addAndGetBooleanValue("command.report.enabled",true);
        this.commandReports = addAndGetBooleanValue("command.reports.enabled",true);
        this.commandResethistory = addAndGetBooleanValue("command.resethistory.enabled",true);
        this.commandTeamchat = addAndGetBooleanValue("command.teamchat.enabled",true);
        this.commandTempban = addAndGetBooleanValue("command.tempban.enabled",true);
        this.commandTempmute = addAndGetBooleanValue("command.tempmute.enabled",true);
        this.commandStaffstats = addAndGetBooleanValue("command.staffstats.enabled",true);
        this.commandIPban = addAndGetBooleanValue("command.ipban.enabled",true);
        this.commandIPUnban = addAndGetBooleanValue("command.ipunban.enabled",true);
        this.commandWarn = addAndGetBooleanValue("command.warn.enabled",true);
        this.commandEdit = addAndGetBooleanValue("command.edit.enabled",true);
        this.commandMyHistoryPoints = addAndGetBooleanValue("command.myhistorypoints.enabled",true);
    }
    public Ban createAltAccountBan(NetworkPlayer player, String ip){
        return new Ban(player.getUUID(),ip,ipBanBanReason,"",System.currentTimeMillis(),-1,new HistoryPoints(ipBanBanPoints,BanType.NETWORK),666
                ,ipBanBanSraff,new Document(),System.currentTimeMillis()+ipBanBanDuration,BanType.NETWORK);
    }
}
