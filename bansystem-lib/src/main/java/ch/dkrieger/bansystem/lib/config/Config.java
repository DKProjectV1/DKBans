package ch.dkrieger.bansystem.lib.config;

import ch.dkrieger.bansystem.lib.DKBansPlatform;
import ch.dkrieger.bansystem.lib.config.mode.BanMode;
import ch.dkrieger.bansystem.lib.config.mode.KickMode;
import ch.dkrieger.bansystem.lib.config.mode.ReportMode;
import ch.dkrieger.bansystem.lib.config.mode.UnbanMode;
import ch.dkrieger.bansystem.lib.player.PlayerColor;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.report.Report;
import ch.dkrieger.bansystem.lib.storage.StorageType;
import ch.dkrieger.bansystem.lib.utils.TabCompleteOption;
import net.md_5.bungee.api.ChatColor;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config extends SimpleConfig{

    public StorageType storageType;
    public File storageFile;
    public String storageHost, storagePort, storageUser, storagePassword, storageDatabase, mongoDbAuthDB;
    public boolean storageSSL, mongoDbSrv, mongoDbAuthentication;

    public BanMode banMode;
    public UnbanMode unbanMode;
    public KickMode kickMode;

    public DateFormat dateFormat;

    public boolean joinMeEnabled;
    public long joinMeCooldown;
    public long joinMeTimeOut;
    public boolean joinMeDisabledServerEquals;
    public List<String> joinMeDisabledServerList;

    public boolean reportControls;
    public ReportMode reportMode;
    public long reportDelay;
    public boolean reportAutoCommandExecuteOnProxy;
    public List<String> reportAutoCommandEnter;
    public List<String> reportAutoCommandExit;

    public boolean onJoinChatClear;
    public boolean onJoinTeamChatInfo;
    public boolean onJoinReportInfo;
    public boolean onJoinReportSize;

    public boolean tabCompleteBlockEnabled;
    public List<TabCompleteOption> tabCompleteOptions;

    public boolean chatBlockPlugin;
    public long chatDelay;
    public boolean chatFilterEnabled;
    public int chatFilterAutobanCount;
    public boolean chatFilterAutobanEnabled;
    public boolean chatFilterAutobanMessageBanID;
    public boolean chatFilterAutobanPromotionBanID;

    public int banPointsTime;
    public int banPointsMaxHistory;

    public boolean playerSaveIP;
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

    public Config(DKBansPlatform platform) {
        super(new File(platform.getFolder(),"config.yml"));
    }
    @Override
    public void onLoad() {
        this.storageType = StorageType.parse(addAndGetStringValue("storage.type",StorageType.SQLITE.toString()));
        this.storageFile = new File(addAndGetStringValue("storage.file","/data/"));
        this.storageHost = addAndGetStringValue("storage.host","localhost");
        this.storagePort = addAndGetStringValue("storage.port","3306");
        this.storageUser = addAndGetStringValue("storage.user","root");
        this.storageSSL = addAndGetBooleanValue("storage.ssl",false);
        this.storagePassword = addAndGetStringValue("storage.password","password");
        this.storageDatabase = addAndGetStringValue("storage.database","DKBans");
        this.mongoDbAuthentication = addAndGetBooleanValue("storage.mongodb.authentication",false);
        this.mongoDbAuthDB = addAndGetStringValue("storage.mongodb.authdb","admin");
        this.mongoDbSrv = addAndGetBooleanValue("storage.mongodb.srv",false);

        this.banMode = BanMode.parse(addAndGetStringValue("ban.mode", BanMode.TEMPLATE.toString()));
        this.unbanMode = UnbanMode.parse(addAndGetStringValue("unban.mode", BanMode.SELF.toString()));
        this.kickMode = KickMode.parse(addAndGetStringValue("kick.mode", BanMode.SELF.toString()));

        this.reportMode = ReportMode.parse(addAndGetStringValue("report.mode", ReportMode.TEMPLATE.toString()));
        this.reportControls = addAndGetBooleanValue("report.controls",true);
        this.reportDelay = addAndGetLongValue("report.delay",900000);
        this.reportAutoCommandExecuteOnProxy = addAndGetBooleanValue("report.autocommand.onproxy",false);
        this.reportAutoCommandEnter = addAndGetStringListValue("report.autocommand.enter", Arrays.asList("v","tp [player]"));
        this.reportAutoCommandExit = addAndGetStringListValue("report.autocommand.exit", Arrays.asList("spawn"));

        this.dateFormat = new SimpleDateFormat(addAndGetStringValue("date.format","dd.MM.yyyy HH:mm"));

        this.joinMeEnabled = addAndGetBooleanValue("joinme.enabled",false);
        this.joinMeCooldown = addAndGetLongValue("joinme.cooldown",120000L);
        this.joinMeTimeOut = addAndGetLongValue("joinme.timeout",300000);
        this.joinMeDisabledServerEquals = addAndGetBooleanValue("joinme.disabledservers.equals",false);
        this.joinMeDisabledServerList = addAndGetStringListValue("joinme.disabledservers.list", Arrays.asList("lobby"));

        this.onJoinChatClear = addAndGetBooleanValue("onjoin.chatclear",true);
        this.onJoinTeamChatInfo = addAndGetBooleanValue("onjoin.teamchatinfo",true);
        this.onJoinReportInfo = addAndGetBooleanValue("onjoin.reportinfo",true);
        this.onJoinReportSize = addAndGetBooleanValue("onjoin.reportsize",true);

        this.tabCompleteBlockEnabled = addAndGetBooleanValue("tabcomplet.block.enabled",true);
        List<String> tabOptions = addAndGetStringListValue("tabcomplet.options",Arrays.asList("dkbans.ban:/ban","dkbans.kick:/kick"));
        this.tabCompleteOptions = new ArrayList<>();
        for(String tabOption : tabOptions){
            try{
                String[] split = tabOption.split(":");
                this.tabCompleteOptions.add(new TabCompleteOption(split[0],split[1]));
            }catch (Exception exception){}
        }

        this.chatBlockPlugin = addAndGetBooleanValue("chat.block.plugin",true);
        this.chatDelay = addAndGetLongValue("chat.delay",500);
        this.chatFilterEnabled = addAndGetBooleanValue("chat.filter.enabled",true);
        this.chatFilterAutobanEnabled = addAndGetBooleanValue("chat.autoban.enabled",true);
        this.chatFilterAutobanCount = addAndGetIntValue("chat.filter.autoban.count",8);
        this.chatFilterAutobanMessageBanID = addAndGetBooleanValue("chat.autoban.banid.message",3);
        this.chatFilterAutobanPromotionBanID = addAndGetBooleanValue("chat.autoban.banid.promotion",4);

        this.playerSaveIP = addAndGetBooleanValue("player.saveip",true);
        this.playerIPDeletionInDays = addAndGetIntValue("player.ipautodeleteindays",-1);
        this.playerColorLiveUpdate = addAndGetBooleanValue("player.color.liveupdate",true);
        this.playerColorDefault = addAndGetMessageValue("player.color.default","&8");
        this.playerColorConsole = addAndGetMessageValue("player.color.console","&4");
        this.playerColorColors = new ArrayList<>();
        List<String> colors = addAndGetStringListValue("player.color.colors",Arrays.asList("dkbans.color.admin:&4"
                ,"dkbans.color.developer:&b","dkbans.color.mod:&c","dkbans.color.supporter:&9","dkbans.color.builder:&3"
                ,"dkbans.color.youtuber:&5","dkbans.color.premium:&6"));
        for(String tabOption : tabOptions){
            try{
                String[] split = tabOption.split(":");
                this.playerColorColors.add(new PlayerColor(split[0], ChatColor.translateAlternateColorCodes('&',split[1])));
            }catch (Exception exception){}
        }

        this.chatlogEnabled = addAndGetBooleanValue("chat.log.enabled",true);
        this.chatlogAutoDeleteEnabled = addAndGetBooleanValue("chat.log.autodelete.enabled",true);
        this.chatlogAutoDeleteInDays = addAndGetIntValue("chat.log.autodelete.indays",30);

        this.autobroadcastEnabled = addAndGetBooleanValue("autobroadcast.enabled",true);
        this.autobroadcastSorted = addAndGetBooleanValue("autobroadcast.sorted",true);
        this.autobroadcastDelay = addAndGetIntValue("autobroadcast.delay",10);

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
    }
}
