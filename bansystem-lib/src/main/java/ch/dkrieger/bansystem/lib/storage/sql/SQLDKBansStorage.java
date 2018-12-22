package ch.dkrieger.bansystem.lib.storage.sql;

import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.config.Config;
import ch.dkrieger.bansystem.lib.filter.Filter;
import ch.dkrieger.bansystem.lib.filter.FilterType;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.chatlog.ChatLog;
import ch.dkrieger.bansystem.lib.player.chatlog.ChatLogEntry;
import ch.dkrieger.bansystem.lib.player.history.History;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.HistoryEntry;
import ch.dkrieger.bansystem.lib.report.Report;
import ch.dkrieger.bansystem.lib.stats.NetworkStats;
import ch.dkrieger.bansystem.lib.stats.PlayerStats;
import ch.dkrieger.bansystem.lib.storage.DKBansStorage;
import ch.dkrieger.bansystem.lib.storage.StorageType;
import ch.dkrieger.bansystem.lib.storage.sql.query.ColumnType;
import ch.dkrieger.bansystem.lib.storage.sql.query.QueryOption;
import ch.dkrieger.bansystem.lib.storage.sql.table.Table;
import ch.dkrieger.bansystem.lib.utils.Document;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class SQLDKBansStorage implements DKBansStorage {

    private Config config;
    private SQL sql;
    private Table players, chatlogs, histories, reports, filters, broadcasts, networkstats;

    public SQLDKBansStorage(Config config) {
        this.config = config;
    }
    @Override
    public boolean connect() {
        if(config.storageType==StorageType.MYSQL) sql = new MySQL(config.storageHost,config.storagePort,config.storageDatabase,config.storageUser,config.storagePassword);
        boolean connect = sql.connect();

        if(connect){
            players = new Table(sql,"DKBans_players");
            chatlogs = new Table(sql,"DKBans_chatlogs");
            histories = new Table(sql,"DKBans_histories");
            reports = new Table(sql,"DKBans_reports");
            filters = new Table(sql,"DKBans_filters");
            broadcasts = new Table(sql,"DKBans_broadcasts");
            networkstats = new Table(sql,"DKBans_networkstats");
            try{
                players.create().create("id",ColumnType.INT,QueryOption.NOT_NULL,QueryOption.PRIMARY_KEY,QueryOption.AUTO_INCREMENT)
                        .create("uuid",ColumnType.VARCHAR,80,QueryOption.NOT_NULL)
                        .create("name",ColumnType.VARCHAR,20,QueryOption.NOT_NULL)
                        .create("color",ColumnType.VARCHAR,10,QueryOption.NOT_NULL)
                        .create("lastIp",ColumnType.VARCHAR,20,QueryOption.NOT_NULL)
                        .create("lastCountry",ColumnType.VARCHAR,50,QueryOption.NOT_NULL)
                        .create("lastLogin",ColumnType.INT,30,QueryOption.NOT_NULL)
                        .create("firstLogin",ColumnType.INT,30,QueryOption.NOT_NULL)
                        .create("onlineTime",ColumnType.INT,QueryOption.NOT_NULL)
                        .create("bypass",ColumnType.VARCHAR,10,QueryOption.NOT_NULL)
                        .create("teamChatLogin",ColumnType.VARCHAR,10,QueryOption.NOT_NULL)
                        .create("reportLogin",ColumnType.VARCHAR,10,QueryOption.NOT_NULL)
                        .create("statsLogins",ColumnType.INT,30,QueryOption.NOT_NULL)
                        .create("statsMessages",ColumnType.INT,30,QueryOption.NOT_NULL)
                        .create("statsReports",ColumnType.INT,30,QueryOption.NOT_NULL)
                        .create("statsReportsAccepted",ColumnType.INT,30,QueryOption.NOT_NULL)
                        .create("statsReportsReceived",ColumnType.INT,30,QueryOption.NOT_NULL)
                        .create("properties",ColumnType.TEXT,QueryOption.NOT_NULL).execute();
                this.chatlogs.create().create("uuid",ColumnType.VARCHAR,80,QueryOption.NOT_NULL)
                        .create("message",ColumnType.VARCHAR,500,QueryOption.NOT_NULL)
                        .create("server",ColumnType.VARCHAR,50,QueryOption.NOT_NULL)
                        .create("time",ColumnType.INT,30,QueryOption.NOT_NULL)
                        .create("filter",ColumnType.VARCHAR,20,QueryOption.NOT_NULL).execute();
                this.filters.create().create("id",ColumnType.INT,QueryOption.NOT_NULL,QueryOption.PRIMARY_KEY)
                        .create("message",ColumnType.VARCHAR,100,QueryOption.NOT_NULL)
                        .create("operation",ColumnType.VARCHAR,50,QueryOption.NOT_NULL)
                        .create("type",ColumnType.VARCHAR,50,QueryOption.NOT_NULL).execute();
                this.broadcasts.create().create("id",ColumnType.INT,QueryOption.NOT_NULL,QueryOption.PRIMARY_KEY)
                        .create("message",ColumnType.VARCHAR,1000,QueryOption.NOT_NULL)
                        .create("hover",ColumnType.VARCHAR,500,QueryOption.NOT_NULL)
                        .create("clickType",ColumnType.VARCHAR,30,QueryOption.NOT_NULL)
                        .create("clickMessage",ColumnType.VARCHAR,500,QueryOption.NOT_NULL)
                        .create("auto",ColumnType.VARCHAR,10,QueryOption.NOT_NULL)
                        .create("created",ColumnType.INT,QueryOption.NOT_NULL)
                        .create("lastChange",ColumnType.INT,QueryOption.NOT_NULL).execute();
                this.reports.create() .create("uuid",ColumnType.VARCHAR,80,QueryOption.NOT_NULL)
                        .create("reporter",ColumnType.VARCHAR,80,QueryOption.NOT_NULL)
                        .create("staff",ColumnType.VARCHAR,80,QueryOption.NOT_NULL)
                        .create("reason",ColumnType.VARCHAR,200,QueryOption.NOT_NULL)
                        .create("message",ColumnType.VARCHAR,200,QueryOption.NOT_NULL)
                        .create("reportedServer",ColumnType.VARCHAR,50,QueryOption.NOT_NULL)
                        .create("reasonID",ColumnType.VARCHAR,10,QueryOption.NOT_NULL)
                        .create("time",ColumnType.INT,30,QueryOption.NOT_NULL).execute();
                this.networkstats.create().create("logins",ColumnType.INT,30,QueryOption.NOT_NULL)
                        .create("reports",ColumnType.INT,30,QueryOption.NOT_NULL)
                        .create("reportsAccepted",ColumnType.INT,30,QueryOption.NOT_NULL)
                        .create("messages",ColumnType.INT,30,QueryOption.NOT_NULL)
                        .create("bans",ColumnType.INT,30,QueryOption.NOT_NULL)
                        .create("mutes",ColumnType.INT,30,QueryOption.NOT_NULL)
                        .create("unbans",ColumnType.INT,30,QueryOption.NOT_NULL)
                        .create("kicks",ColumnType.INT,30,QueryOption.NOT_NULL)
                        .create("uptime",ColumnType.INT,30,QueryOption.NOT_NULL).execute();
                this.histories.create().create("id",ColumnType.INT,QueryOption.NOT_NULL,QueryOption.PRIMARY_KEY)
                        .create("uuid",ColumnType.VARCHAR,80,QueryOption.NOT_NULL)
                        .create("ip",ColumnType.VARCHAR,80,QueryOption.NOT_NULL)
                        .create("reason",ColumnType.VARCHAR,200,QueryOption.NOT_NULL)
                        .create("message",ColumnType.VARCHAR,200,QueryOption.NOT_NULL)
                        .create("time",ColumnType.INT,QueryOption.NOT_NULL)
                        .create("points",ColumnType.INT,QueryOption.NOT_NULL)
                        .create("reasonID",ColumnType.INT,QueryOption.NOT_NULL)
                        .create("staff",ColumnType.VARCHAR,80,QueryOption.NOT_NULL)
                        .create("jsonEntryObject",ColumnType.TEXT,QueryOption.NOT_NULL).execute();
            }catch (Exception exception){
                exception.printStackTrace();
                connect = false;
            }
        }
        return connect;
    }

    @Override
    public boolean isConnected() {
        return sql.isConnected();
    }

    @Override
    public void disconnect() {
        sql.disconnect();
    }

    @Override
    public NetworkPlayer getPlayer(int id) throws Exception {
        return getPlayer("id",id);
    }

    @Override
    public NetworkPlayer getPlayer(String name) throws Exception {
        return getPlayer("name",name);
    }

    @Override
    public NetworkPlayer getPlayer(UUID uuid) throws Exception {
        return getPlayer("uuid",uuid.toString());
    }
    public NetworkPlayer getPlayer(String key, Object value){
        ResultSet result = players.select().where(key,value).execute();
        //public NetworkPlayer(int id, UUID uuid, String name, String color, String lastIP, String lastCountry, long lastLogin, long firstLogin, long onlineTime, boolean bypass
        // , boolean teamChatLogin, boolean reportLogin, History history, Document properties, PlayerStats stats, List<OnlineSession> onlineSessions, List<Report> reports) {
        try {
            if(result.next()){
                UUID uuid = UUID.fromString(result.getString("uuid"));
                return new NetworkPlayer(result.getInt("id"),uuid
                        ,result.getString("name"),result.getString("color"),result.getString("lastIp")
                        ,result.getString("lastCountry"),result.getLong("lastLogin"),result.getLong("firstLogin")
                        ,result.getLong("onlineTime"),result.getBoolean("bypass"),result.getBoolean("teamChatLogin")
                        ,result.getBoolean("reportLogin"),getHistory(uuid),Document.loadData(result.getString("properties"))
                        ,new PlayerStats(result.getLong("statsLogins")
                        ,result.getLong("statsMessages") ,result.getLong("statsReports"),result.getLong("statsReportsAccepted")
                        ,result.getLong("statsReportsReceived")),new ArrayList<>(),null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<NetworkPlayer> getPlayersByIp(String ip) {
        return null;
    }

    @Override
    public int getRegisteredPlayerCount() {
        return 0;
    }

    @Override
    public int getCountryCount() {
        return 0;
    }
    @Override
    public int createPlayer(NetworkPlayer player) {
        System.out.println("create player");
        return players.insert().insert("uuid").insert("name").insert("color").insert("lastIp").insert("lastCountry")
                .insert("lastLogin").insert("firstLogin").insert("onlineTime").insert("bypass").insert("teamChatLogin")
                .insert("reportLogin").insert("statsLogins").insert("statsMessages").insert("statsReports")
                .insert("statsReportsAccepted").insert("statsReportsReceived").insert("properties")
                .value(player.getUUID()).value(player.getName()).value(player.getColor()).value(player.getIP())
                .value(player.getCountry()).value(player.getLastLogin()).value(player.getFirstLogin())
                .value(player.getOnlineTime()).value(player.hasBypass()).value(player.isTeamChatLoggedIn())
                .value(player.isReportLoggedIn()).value(1).value(0).value(0).value(0).value(0).value(player.getProperties().toJson()).executeAndGetKeyAsInt();
                /*

        players.create().create("id",ColumnType.INT,QueryOption.NOT_NULL,QueryOption.PRIMARY_KEY).
                        .create("uuid",ColumnType.VARCHAR,80,QueryOption.NOT_NULL)
                        .create("name",ColumnType.VARCHAR,20,QueryOption.NOT_NULL)
                        .create("color",ColumnType.VARCHAR,10,QueryOption.NOT_NULL)
                        .create("lastIp",ColumnType.VARCHAR,20,QueryOption.NOT_NULL)
                        .create("lastCountry",ColumnType.VARCHAR,50,QueryOption.NOT_NULL)
                        .create("lastLogin",ColumnType.VARCHAR,QueryOption.NOT_NULL)
                        .create("firstLogin",ColumnType.VARCHAR,QueryOption.NOT_NULL)
                        .create("onlineTime",ColumnType.VARCHAR,QueryOption.NOT_NULL)
                        .create("bypass",ColumnType.VARCHAR,10,QueryOption.NOT_NULL)
                        .create("teamChatLogin",ColumnType.VARCHAR,10,QueryOption.NOT_NULL)
                        .create("reportLogin",ColumnType.VARCHAR,10,QueryOption.NOT_NULL)
                        .create("statsLogins",ColumnType.INT,QueryOption.NOT_NULL)
                        .create("statsMessages",ColumnType.INT,QueryOption.NOT_NULL)
                        .create("statsReports",ColumnType.INT,QueryOption.NOT_NULL)
                        .create("statsReportsAccepted",ColumnType.INT,QueryOption.NOT_NULL)
                        .create("statsReportsReceived",ColumnType.INT,QueryOption.NOT_NULL)
                        .create("properties",ColumnType.TEXT,QueryOption.NOT_NULL).execute();
         */
    }

    @Override
    public void saveStaffSettings(UUID player, boolean report, boolean teamchat) {
        this.players.update().set("reportLogin",report).set("teamChatLogin",teamchat).where("player",player);
    }

    @Override
    public ChatLog getChatLog(UUID player) {
        List<ChatLogEntry> entries = new LinkedList<>();
        try{
            ResultSet result = chatlogs.select().where("uuid",player).execute();
            while(result.next()){
                entries.add(new ChatLogEntry(UUID.fromString(result.getString("uuid")),result.getString("message")
                ,result.getString("server"),result.getLong("time"), FilterType.valueOf(result.getString("filter"))));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return new ChatLog(entries);
    }

    @Override
    public ChatLog getChatLog(String server) {
        return null;
    }

    @Override
    public void createChatLogEntry(ChatLogEntry entry) {

    }

    @Override
    public History getHistory(UUID player) {
        return new History();
    }

    @Override
    public void clearHistory(NetworkPlayer player) {

    }

    @Override
    public int createHistoryEntry(NetworkPlayer player, HistoryEntry entry) {
        return 0;
    }

    @Override
    public void deleteHistoryEntry(NetworkPlayer player, int id) {

    }

    @Override
    public List<Report> getReports() {
        return null;
    }

    @Override
    public void createReport(Report report) {

    }

    @Override
    public void processReports(NetworkPlayer player, NetworkPlayer staff) {

    }

    @Override
    public void deleteReports(NetworkPlayer player) {

    }

    @Override
    public List<Ban> getBans() {
        return null;
    }

    @Override
    public List<Ban> getBans(int reasonID) {
        return null;
    }

    @Override
    public List<Ban> getBans(String reason) {
        return null;
    }

    @Override
    public List<Ban> getBansFromStaff(String staff) {
        return null;
    }

    @Override
    public List<Filter> loadFilters() {
        return new ArrayList<>();
    }

    @Override
    public int createFilter(Filter filter) {
        return 0;
    }

    @Override
    public void deleteFilter(int id) {

    }

    @Override
    public List<Broadcast> loadBroadcasts() {
        return new ArrayList<>();
    }

    @Override
    public int createBroadcast(Broadcast broadcast) {
        return 0;
    }

    @Override
    public void updateBroadcast(Broadcast broadcast) {

    }

    @Override
    public void deleteBroadcast(int id) {

    }

    @Override
    public NetworkStats getNetworkStats() {
        return null;
    }
}
