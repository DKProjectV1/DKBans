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

package ch.dkrieger.bansystem.lib.storage.sql;

import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.config.Config;
import ch.dkrieger.bansystem.lib.filter.Filter;
import ch.dkrieger.bansystem.lib.filter.FilterOperation;
import ch.dkrieger.bansystem.lib.filter.FilterType;
import ch.dkrieger.bansystem.lib.player.IPBan;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineSession;
import ch.dkrieger.bansystem.lib.player.chatlog.ChatLog;
import ch.dkrieger.bansystem.lib.player.chatlog.ChatLogEntry;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.History;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.HistoryEntry;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import ch.dkrieger.bansystem.lib.player.history.entry.Unban;
import ch.dkrieger.bansystem.lib.report.Report;
import ch.dkrieger.bansystem.lib.stats.NetworkStats;
import ch.dkrieger.bansystem.lib.stats.PlayerStats;
import ch.dkrieger.bansystem.lib.storage.DKBansStorage;
import ch.dkrieger.bansystem.lib.storage.StorageType;
import ch.dkrieger.bansystem.lib.storage.sql.query.ColumnType;
import ch.dkrieger.bansystem.lib.storage.sql.query.QueryOption;
import ch.dkrieger.bansystem.lib.storage.sql.query.SelectQuery;
import ch.dkrieger.bansystem.lib.storage.sql.table.Table;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.regexp.internal.RE;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SQLDKBansStorage implements DKBansStorage {

    private Config config;
    private SQL sql;
    private Table players, chatlogs, histories, reports, filters, broadcasts,onlineSessions, networkstats, ipbans;
    private boolean v1Detected;

    public SQLDKBansStorage(Config config) {
        this.config = config;
    }
    @Override
    public boolean connect() {
        if(config.storageType==StorageType.MYSQL) sql = new MySQL(config.storageHost,config.storagePort,config.storageDatabase,config.storageUser,config.storagePassword);
        else sql = new SQLite(config.storageFolder,"dkabns.db");
        boolean connect = sql.connect();

        if(connect){

            players = new Table(sql,"DKBans_players");
            chatlogs = new Table(sql,"DKBans_chatlogs");
            histories = new Table(sql,"DKBans_histories");
            reports = new Table(sql,"DKBans_reports");
            filters = new Table(sql,"DKBans_filters");
            broadcasts = new Table(sql,"DKBans_broadcasts");
            networkstats = new Table(sql,"DKBans_networkstats");
            onlineSessions = new Table(sql,"DKBans_onlinesessions");
            ipbans = new Table(sql,"DKBans_ipbans");

            if(sql instanceof MySQL) tryTranslateFromV1ToV2();

            try{
                players.create().create("id",ColumnType.INT,QueryOption.NOT_NULL,QueryOption.PRIMARY_KEY,QueryOption.AUTO_INCREMENT)
                        .create("uuid",ColumnType.VARCHAR,80,QueryOption.NOT_NULL)
                        .create("name",ColumnType.VARCHAR,20,QueryOption.NOT_NULL)
                        .create("color",ColumnType.VARCHAR,10,QueryOption.NOT_NULL)
                        .create("lastIp",ColumnType.VARCHAR,20,QueryOption.NOT_NULL)
                        .create("lastCountry",ColumnType.VARCHAR,50,QueryOption.NOT_NULL)
                        .create("lastLogin",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("firstLogin",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("onlineTime",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("bypass",ColumnType.VARCHAR,10,QueryOption.NOT_NULL)
                        .create("teamChatLogin",ColumnType.VARCHAR,10,QueryOption.NOT_NULL)
                        .create("reportLogin",ColumnType.VARCHAR,10,QueryOption.NOT_NULL)
                        .create("statsLogins",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("statsMessages",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("statsReports",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("statsReportsAccepted",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("statsReportsReceived",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("statsStaffBans",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("statsStaffMutes",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("statsStaffKicks",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("statsStaffWarns",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("statsStaffUnbans",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("properties",ColumnType.MEDIUMTEXT,QueryOption.NOT_NULL).execute();
                this.chatlogs.create().create("uuid",ColumnType.VARCHAR,80,QueryOption.NOT_NULL)
                        .create("message",ColumnType.VARCHAR,500,QueryOption.NOT_NULL)
                        .create("server",ColumnType.VARCHAR,50,QueryOption.NOT_NULL)
                        .create("time",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("filter",ColumnType.VARCHAR,20,QueryOption.NOT_NULL).execute();
                this.filters.create().create("id",ColumnType.INT,QueryOption.NOT_NULL,QueryOption.PRIMARY_KEY,QueryOption.AUTO_INCREMENT)
                        .create("message",ColumnType.VARCHAR,100,QueryOption.NOT_NULL)
                        .create("operation",ColumnType.VARCHAR,50,QueryOption.NOT_NULL)
                        .create("type",ColumnType.VARCHAR,50,QueryOption.NOT_NULL).execute();
                this.broadcasts.create().create("id",ColumnType.INT,QueryOption.NOT_NULL,QueryOption.PRIMARY_KEY,QueryOption.AUTO_INCREMENT)
                        .create("message",ColumnType.VARCHAR,1000,QueryOption.NOT_NULL)
                        .create("hover",ColumnType.VARCHAR,500,QueryOption.NOT_NULL)
                        .create("clickType",ColumnType.VARCHAR,30,QueryOption.NOT_NULL)
                        .create("clickMessage",ColumnType.VARCHAR,500,QueryOption.NOT_NULL)
                        .create("permission",ColumnType.VARCHAR,50,QueryOption.NOT_NULL)
                        .create("auto",ColumnType.VARCHAR,10,QueryOption.NOT_NULL)
                        .create("created",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("lastChange",ColumnType.BIG_INT,QueryOption.NOT_NULL).execute();
                this.reports.create() .create("uuid",ColumnType.VARCHAR,80,QueryOption.NOT_NULL)
                        .create("reporter",ColumnType.VARCHAR,80,QueryOption.NOT_NULL)
                        .create("staff",ColumnType.VARCHAR,80,QueryOption.NOT_NULL)
                        .create("reason",ColumnType.VARCHAR,200,QueryOption.NOT_NULL)
                        .create("message",ColumnType.VARCHAR,200,QueryOption.NOT_NULL)
                        .create("reportedServer",ColumnType.VARCHAR,50,QueryOption.NOT_NULL)
                        .create("reasonID",ColumnType.VARCHAR,10,QueryOption.NOT_NULL)
                        .create("time",ColumnType.BIG_INT,QueryOption.NOT_NULL).execute();
                this.histories.create().create("id",ColumnType.INT,QueryOption.NOT_NULL,QueryOption.PRIMARY_KEY,QueryOption.AUTO_INCREMENT)
                        .create("uuid",ColumnType.VARCHAR,80,QueryOption.NOT_NULL)
                        .create("ip",ColumnType.VARCHAR,80,QueryOption.NOT_NULL)
                        .create("reason",ColumnType.VARCHAR,200,QueryOption.NOT_NULL)
                        .create("message",ColumnType.VARCHAR,200,QueryOption.NOT_NULL)
                        .create("time",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("points",ColumnType.INT,QueryOption.NOT_NULL)
                        .create("reasonID",ColumnType.INT,QueryOption.NOT_NULL)
                        .create("staff",ColumnType.VARCHAR,80,QueryOption.NOT_NULL)
                        .create("type",ColumnType.VARCHAR,15,QueryOption.NOT_NULL)
                        .create("jsonEntryObject",ColumnType.TEXT,QueryOption.NOT_NULL).execute();
                this.onlineSessions.create()
                        .create("uuid",ColumnType.VARCHAR,80,QueryOption.NOT_NULL)
                        .create("ip",ColumnType.VARCHAR,80,QueryOption.NOT_NULL)
                        .create("country",ColumnType.VARCHAR,50,QueryOption.NOT_NULL)
                        .create("lastServer",ColumnType.VARCHAR,50,QueryOption.NOT_NULL)
                        .create("proxy",ColumnType.VARCHAR,50,QueryOption.NOT_NULL)
                        .create("clientLanguage",ColumnType.VARCHAR,50,QueryOption.NOT_NULL)
                        .create("clientVersion",ColumnType.VARCHAR,50,QueryOption.NOT_NULL)
                        .create("connected",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("disconnected",ColumnType.BIG_INT,QueryOption.NOT_NULL).execute();
                this.networkstats.create().create("logins",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("reports",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("reportsAccepted",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("messages",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("bans",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("mutes",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("unbans",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("kicks",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("warns",ColumnType.BIG_INT,QueryOption.NOT_NULL).execute();

                this.ipbans.create().create("ip",ColumnType.VARCHAR,50,QueryOption.NOT_NULL)
                        .create("lastPlayer",ColumnType.VARCHAR,80,QueryOption.NOT_NULL)
                        .create("timeStamp",ColumnType.BIG_INT,QueryOption.NOT_NULL)
                        .create("timeOut",ColumnType.BIG_INT,QueryOption.NOT_NULL).execute();

                if(networkstats.select().execute("SELECT COUNT(*) FROM "+networkstats.getName(), result -> {
                    try{
                        if(result.next())return result.getInt(1);
                    }catch (Exception exception){}
                    return 0;
                }) == 0){
                    this.networkstats.insert().insert("logins").insert("reports").insert("reportsAccepted").insert("messages")
                            .insert("bans").insert("mutes").insert("unbans").insert("kicks").insert("warns").value(0).value(0).value(0)
                            .value(0).value(0).value(0).value(0).value(0).value(0).execute();
                }else{
                    try{
                        this.players.execute("ALTER TABLE "+players.getName()+" ADD COLUMN statsStaffWarns bigint AFTER statsStaffKicks");
                        this.networkstats.execute("ALTER TABLE "+networkstats.getName()+" ADD COLUMN warns bigint");
                    }catch (Exception exception){}
                }
                if(v1Detected) translateFromV1ToV2();
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
        NetworkPlayer player = players.select().where(key,value).execute(result -> {
            try{
               if(result.next()){
                   UUID uuid = UUID.fromString(result.getString("uuid"));
                   return new NetworkPlayer(result.getInt("id"),uuid
                           ,result.getString("name"),result.getString("color"),result.getString("lastIp")
                           ,result.getString("lastCountry"),result.getLong("lastLogin"),result.getLong("firstLogin")
                           ,result.getLong("onlineTime"),result.getBoolean("bypass"),result.getBoolean("teamChatLogin")
                           ,result.getBoolean("reportLogin"),null,Document.loadData(result.getString("properties"))
                           ,new PlayerStats(result.getLong("statsLogins")
                           ,result.getLong("statsReports"),result.getLong("statsReportsAccepted")
                           ,result.getLong("statsMessages"),result.getLong("statsReportsReceived")
                           ,result.getLong("statsStaffBans"),result.getLong("statsStaffMutes")
                           ,result.getLong("statsStaffUnbans"),result.getLong("statsStaffKicks"),result.getLong("statsStaffWarns")),new ArrayList<>(),null);
               }
            }catch (Exception exception){
                exception.printStackTrace();
            }
            return null;
        });
        if(player != null){
            player.setHistory(getHistory(player.getUUID()));
            player.setReports(getReports(player.getUUID()));
            player.setOnlineSessions(getSessions(player.getUUID()));
        }
        return player;
    }
    public List<OnlineSession> getSessions(UUID uuid){
        return onlineSessions.select().where("uuid",uuid.toString()).execute(result -> {
            List<OnlineSession> sessions = new ArrayList<>();
            try{
                while(result.next()){
                    sessions.add(new OnlineSession(result.getString("ip"),result.getString("country")
                            ,result.getString("lastServer"),result.getString("proxy"),result.getString("clientLanguage")
                            ,result.getInt("clientVersion"),result.getLong("connected"),result.getLong("disconnected")));
                }
            }catch (Exception e){}
            return sessions;
        });
    }

    @Override
    public List<NetworkPlayer> getPlayersByIp(String ip) {
        List<NetworkPlayer> player = players.select().where("lastIp",ip).execute(result -> {
            List<NetworkPlayer> players = new ArrayList<>();
            try{
                while(result.next()){
                    UUID uuid = UUID.fromString(result.getString("uuid"));
                    players.add(new NetworkPlayer(result.getInt("id"),uuid
                            ,result.getString("name"),result.getString("color"),result.getString("lastIp")
                            ,result.getString("lastCountry"),result.getLong("lastLogin"),result.getLong("firstLogin")
                            ,result.getLong("onlineTime"),result.getBoolean("bypass"),result.getBoolean("teamChatLogin")
                            ,result.getBoolean("reportLogin"),null,Document.loadData(result.getString("properties"))
                            ,new PlayerStats(result.getLong("statsLogins")
                            ,result.getLong("statsReports"),result.getLong("statsReportsAccepted")
                            ,result.getLong("statsMessages")
                            ,result.getLong("statsStaffBans"),result.getLong("statsStaffMutes")
                            ,result.getLong("statsStaffUnbans"),result.getLong("statsStaffKicks")
                            ,result.getLong("statsStaffWarns"),result.getLong("statsReportsReceived")),new ArrayList<>(),null));
                }
            }catch (Exception exception){}
            return players;
        });
        GeneralUtil.iterateForEach(player, object -> {
            object.setHistory(getHistory(object.getUUID()));
            object.setReports(getReports(object.getUUID()));
            object.setOnlineSessions(getSessions(object.getUUID()));
        });
        return player;
    }
    public List<Report> getReports(UUID player){
        return reports.select().where("uuid",player.toString()).execute(result -> {
            List<Report> reports = new ArrayList<>();
            try{
                while(result.next()) reports.add(new Report(UUID.fromString(result.getString("uuid")),
                        UUID.fromString(result.getString("staff")),UUID.fromString(result.getString("reporter"))
                        ,result.getString("reason"),result.getString("message"),result.getString("reportedServer")
                        ,result.getLong("time"),result.getInt("reasonID")));
            }catch (Exception e){}
            return reports;
        });
    }
    @Override
    public int getRegisteredPlayerCount() {
        return players.select().execute("SELECT COUNT(*) FROM "+players.getName(), result -> {
            try{
                if(result.next()){
                    return result.getInt(1);
                }
            }catch (Exception exception){}
            return 0;
        });
    }
    @Override
    public int createPlayer(NetworkPlayer player) {
        return players.insert().insert("uuid").insert("name").insert("color").insert("lastIp").insert("lastCountry")
                .insert("lastLogin").insert("firstLogin").insert("onlineTime").insert("bypass").insert("teamChatLogin")
                .insert("reportLogin").insert("statsLogins").insert("statsMessages").insert("statsReports")
                .insert("statsReportsAccepted").insert("statsReportsReceived").insert("statsStaffBans")
                .insert("statsStaffMutes").insert("statsStaffUnbans").insert("statsStaffKicks").insert("statsStaffWarns").insert("properties")
                .value(player.getUUID()).value(player.getName()).value(player.getColor()).value(player.getIP())
                .value(player.getCountry()).value(player.getLastLogin()).value(player.getFirstLogin())
                .value(player.getOnlineTime()).value(player.hasBypass()).value(player.isTeamChatLoggedIn())
                .value(player.isReportLoggedIn()).value(1).value(0).value(0).value(0).value(0).value(0).value(0)
                .value(0).value(0).value(0).value(player.getProperties().toJson()).executeAndGetKeyAsInt();
    }
    @Override
    public void createOnlineSession(NetworkPlayer player, OnlineSession session) {
        this.onlineSessions.insert().insert("uuid").insert("ip").insert("country").insert("lastServer")
                .insert("proxy").insert("clientLanguage").insert("clientVersion").insert("connected").insert("disconnected")
                .value(player.getUUID().toString()).value(session.getIp()).value(session.getCountry()).value(session.getLastServer())
                .value(session.getProxy()).value(session.getClientLanguage()).value(session.getClientVersion()).value(session.getConnected())
                .value(session.getDisconnected()).execute();
    }

    @Override
    public void finishStartedOnlineSession(UUID uuid, long login, long logout, String server) {
        this.onlineSessions.update().set("uuid",uuid.toString()).set("disconnected",logout).set("lastServer",server)
                .where("connected",login).where("uuid",uuid.toString()).execute();
    }

    @Override
    public void updatePlayerLoginInfos(UUID player, String name, long lastLogin, String color, boolean bypass, String lastIP, String lastCountry, long logins) {
        this.players.update().set("name",name).set("lastLogin",lastLogin).set("color",color).set("bypass",bypass).set("lastIp",lastIP)
                .set("lastCountry",lastCountry).set("statsLogins",logins).where("uuid",player.toString()).execute();
    }

    @Override
    public void updatePlayerLogoutInfos(UUID player, long lastLogin, long onlineTime, String color, boolean bypass, long messages) {
        this.players.update().set("lastLogin",lastLogin).set("color",color).set("bypass",bypass).set("onlineTime",onlineTime)
                .set("statsMessages",messages).where("uuid",player.toString()).execute();
    }

    @Override
    public void saveStaffSettings(UUID player, boolean report, boolean teamchat) {
        this.players.update().set("reportLogin",report).set("teamChatLogin",teamchat).where("uuid",player).execute();
    }

    @Override
    public void setColor(UUID player, String color) {
        this.players.update().set("color",color).where("uuid",player);
    }

    @Override
    public ChatLog getChatLog(UUID player) {
        return getChatLog(chatlogs.select().where("player",player.toString()));
    }

    @Override
    public ChatLog getChatLog(String server) {
        return getChatLog(chatlogs.select().where("server",server));
    }
    @Override
    public ChatLog getChatLog(UUID player, String server) {
        return getChatLog(chatlogs.select().where("server",server).where("player",player.toString()));
    }
    public ChatLog getChatLog(SelectQuery query){
        return query.execute(result -> {
            List<ChatLogEntry> entries = new LinkedList<>();
            try{
                while(result.next()){
                    entries.add(new ChatLogEntry(UUID.fromString(result.getString("uuid")),result.getString("message")
                            ,result.getString("server"),result.getLong("time"), FilterType.ParseNull(result.getString("filter"))));
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            return new ChatLog(entries);
        });
    }

    @Override
    public HistoryEntry getHistoryEntry(int id) {
        return this.histories.select().where("id",id).execute(result -> {
            try{
                if(result.next()){
                    HistoryEntry entry = GeneralUtil.GSON.fromJson(result.getString("jsonEntryObject"),HistoryEntry.class);
                    entry.setID(result.getInt("id"));
                    return entry;
                }
            }catch (Exception e){}
            return null;
        });
    }

    @Override
    public void createChatLogEntry(ChatLogEntry entry) {
        chatlogs.insert().insert("uuid").insert("message").insert("server").insert("time").insert("filter")
                .value(entry.getUUID().toString()).value(entry.getMessage()).value(entry.getServer())
                .value(entry.getTime()).value(entry.getFilter()==null?"NULL":entry.getFilter().toString()).execute();
    }

    @Override
    public History getHistory(UUID player) {
        return histories.select().where("uuid",player.toString()).execute(result -> {
            Map<Integer,HistoryEntry> entries = new HashMap<>();
            try{
                while(result.next()){
                    HistoryEntry entry = GeneralUtil.GSON.fromJson(result.getString("jsonEntryObject"),HistoryEntry.class);
                    entry.setID(result.getInt("id"));
                    entries.put(result.getInt("id"),entry);
                }
                return new History(entries);
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public void clearHistory(NetworkPlayer player) {
        histories.delete().where("uuid",player.getUUID().toString()).execute();
    }

    @Override
    public int createHistoryEntry(NetworkPlayer player, HistoryEntry entry) {
        return this.histories.insert().insert("uuid").insert("ip").insert("reason").insert("message")
                .insert("time").insert("points").insert("reasonID").insert("staff").insert("type").insert("jsonEntryObject")
                .value(player.getUUID().toString()).value(entry.getIp()).value(entry.getReason())
                .value(entry.getMessage()).value(entry.getTimeStamp()).value(entry.getPoints()).value(entry.getReasonID())
                .value(entry.getStaff()).value(entry.getTypeName())
                .value(GeneralUtil.GSON_NOT_PRETTY.toJson(entry,new TypeToken<HistoryEntry>(){}.getType())).executeAndGetKeyAsInt();
    }

    @Override
    public void deleteHistoryEntry(NetworkPlayer player, int id) {
        histories.delete().where("uuid",player.getUUID().toString()).where("id",id).execute();
    }

    @Override
    public List<Report> getReports() {
        return reports.select().execute(result -> {
            List<Report> reports = new ArrayList<>();
            try{
                while(result.next()) reports.add(new Report(UUID.fromString(result.getString("uuid")),
                        UUID.fromString(result.getString("staff")),UUID.fromString(result.getString("reporter"))
                        ,result.getString("reason"),result.getString("message"),result.getString("reportedServer")
                        ,result.getLong("time"),result.getInt("reasonID")));
            }catch (Exception e){}
            return reports;
        });
    }

    @Override
    public void createReport(Report report) {
        reports.insert().insert("uuid").insert("reporter").insert("staff").insert("reason").insert("message")
                .insert("reportedServer").insert("reasonID").insert("time")
                .value(report.getUUID().toString()).value(report.getReporterUUID().toString())
                .value(report.getStaff()==null?"":report.getStaff().toString()).value(report.getReason()).value(report.getMessage())
                .value(report.getReportedServer()).value(report.getReasonID()).value(report.getTimeStamp()).execute();
    }

    @Override
    public void processReports(NetworkPlayer player, NetworkPlayer staff) {
        reports.update().set("staff",staff.getUUID().toString()).where("uuid",player.getUUID().toString()).execute();
    }

    @Override
    public void deleteReports(NetworkPlayer player) {
        reports.delete().where("uuid",player.getUUID().toString()).execute();
    }

    @Override
    public List<Ban> getBans() {
        return this.histories.select().where("type","ban").execute(result -> {
            List<Ban> bans = new ArrayList<>();
            try {
                while(result.next()){
                    bans.add((Ban) GeneralUtil.GSON.fromJson(result.getString("jsonEntryObject"),HistoryEntry.class));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return bans;
        });
    }

    @Override
    public void updatePlayerStats(UUID uuid, PlayerStats stats) {
        this.players.update().set("statsLogins",stats.getLogins()).set("statsMessages",stats.getMessages())
                .set("statsReports",stats.getReports()).set("statsReportsAccepted",stats.getReportsAccepted())
                .set("statsStaffMutes",stats.getMutes()).set("statsStaffKicks",stats.getKicks())
                .set("statsStaffWarns",stats.getWarns()).set("statsStaffBans",stats.getBans())
                .set("statsStaffUnbans",stats.getUnbans()).where("uuid",uuid.toString()).execute();
    }
    @Override
    public void deleteOldChatLog(long before) {
        this.chatlogs.delete().whereLower("time",before).execute();
    }

    @Override
    public void updatePlayerProperties(UUID uuid, Document properties) {
        this.players.update().set("properties",properties.toJson()).where("uuid",uuid.toString()).execute();
    }

    @Override
    public List<Ban> getNotTimeOutedBans() {
        return getBans();
    }

    @Override
    public List<Filter> loadFilters() {
        return this.filters.select().execute(result -> {
            List<Filter> filters = new ArrayList<>();
            try{
                while(result.next()){
                    filters.add(new Filter(result.getInt("id"),result.getString("message")
                            ,FilterOperation.valueOf(result.getString("operation"))
                            ,FilterType.valueOf(result.getString("type"))));
                }
            }catch (Exception exception){}
            return filters;
        });
    }
    @Override
    public int createFilter(Filter filter) {
        return  filters.insert().insert("message").insert("operation").insert("type")
                .value(filter.getMessage()).value(filter.getOperation().toString())
                .value(filter.getType().toString()).executeAndGetKeyAsInt();
    }

    @Override
    public void deleteFilter(int id) {
        this.filters.delete().where("id",id).execute();
    }

    @Override
    public List<Broadcast> loadBroadcasts() {
        return broadcasts.select().execute(result -> {
            List<Broadcast> broadcasts = new ArrayList<>();
            try{
                while(result.next()){
                    broadcasts.add(new Broadcast(result.getInt("id"),result.getString("message")
                            ,result.getString("permission")
                            ,result.getString("hover"),result.getLong("created"),result.getLong("lastChange")
                            ,result.getBoolean("auto"),new Broadcast.Click(result.getString("clickMessage")
                            ,Broadcast.ClickType.valueOf(result.getString("clickType")))));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return broadcasts;
        });
    }
    @Override
    public int createBroadcast(Broadcast broadcast) {
        return broadcasts.insert().insert("message").insert("permission").insert("hover").insert("clickType").insert("clickMessage")
                .insert("auto").insert("created").insert("lastChange").value(broadcast.getMessage()).value(broadcast.getPermission()).value(broadcast.getHover())
                .value(broadcast.getClick().getType().toString()).value(broadcast.getClick().getMessage()).value(broadcast.isAuto())
                .value(broadcast.getCreated()).value(broadcast.getLastChange()).executeAndGetKeyAsInt();
    }

    @Override
    public void updateBroadcast(Broadcast broadcast) {
        broadcasts.update().set("permission",broadcast.getPermission()).set("message",broadcast.getMessage()).set("hover",broadcast.getHover())
                .set("clickType",broadcast.getClick().getType().toString()).set("clickMessage",broadcast.getClick().getMessage())
                .set("auto",broadcast.isAuto()).set("lastChange",broadcast.getLastChange()).where("id",broadcast.getID()).execute();
    }

    @Override
    public void deleteBroadcast(int id) {
        this.broadcasts.delete().where("id",id);
    }

    @Override
    public NetworkStats getNetworkStats() {
        return networkstats.select().execute(result -> {
            try{
                if(result.next()){
                    return new NetworkStats(result.getLong("logins"),result.getLong("reports")
                            ,result.getLong("reportsAccepted")
                            ,result.getLong("messages"),result.getLong("bans"),result.getLong("mutes")
                            ,result.getLong("unbans"),result.getLong("kicks"),result.getLong("warns"));
                }
            }catch (Exception exception){}
            return new NetworkStats();
        });
    }

    @Override
    public void updateNetworkStats(long logins, long reports, long reportsAccepted, long messages, long bans, long mutes, long unbans, long kicks, long warns) {
        networkstats.update().set("logins",logins).set("reports",reports).set("reportsAccepted",reportsAccepted).set("messages",messages)
                .set("bans",bans).set("mutes",mutes).set("unbans",unbans).set("kicks",kicks).set("warns",warns).execute();
    }

    @Override
    public IPBan getIpBan(String ip) {
        return ipbans.select().where("ip",ip).execute(result -> {
            try{
                while(result.next()){
                    return new IPBan(result.getString("lastPlayer").equalsIgnoreCase("NULL")?null
                            :UUID.fromString(result.getString("lastPlayer"))
                            ,result.getString("ip"),result.getLong("timeStamp")
                            ,result.getLong("timeOut"));
                }
            }catch (Exception exception){}
            return null;
        });
    }

    @Override
    public void banIp(IPBan ipBan) {
        ipbans.insert().insert("ip").insert("timeStamp").insert("timeOut").insert("lastPlayer")
                .value(ipBan.getIp()).value(ipBan.getTimeStamp()).value(ipBan.getTimeOut())
                .value((ipBan.getLastPlayer()==null?"NULL":ipBan.getLastPlayer())).execute();
    }

    @Override
    public void unbanIp(String ip) {
        ipbans.delete().where("ip",ip).execute();
    }

    @Override
    public void unbanIp(UUID lastPlayer) {
        ipbans.delete().where("lastPlayer",lastPlayer.toString()).execute();
    }

    private void tryTranslateFromV1ToV2(){
        try{
            this.players.select("SELECT 1 FROM DKBans_autobroadcast LIMIT 1;");
            System.out.println(Messages.SYSTEM_PREFIX+"Translating DKbansV1 mysql tables to DKBansV2, please wait.");
            this.players.execute("RENAME TABLE `DKBans_players` TO `DKBans_playersOld`");

            this.players.execute("DROP TABLE IF EXISTS `DKBans_reports`");
            this.players.execute("DROP TABLE IF EXISTS `DKBans_chatlog`");
            v1Detected = true;
            return;
        }catch (Exception ignored){}
        v1Detected = false;
    }
    private void translateFromV1ToV2(){
        if(!v1Detected) return;
        Table playersOld = new Table(sql,"DKBans_playersOld");
        Table historyOld = new Table(sql,"DKBans_history");
        Table autobroadcastOld = new Table(sql,"DKBans_autobroadcast");
        Table filterOld = new Table(sql,"DKBans_filter");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy kk:mm");
        Map<UUID,NetworkPlayer> players = new HashMap<>();
        try{
            playersOld.select().execute(result -> {
                try{
                    while(result.next()){
                        NetworkPlayer player = new NetworkPlayer(-1,UUID.fromString(result.getString("uuid")),result.getString("name")
                                ,result.getString("color"),result.getString("ip"),result.getString("country")
                                ,dateFormat.parse(result.getString("lastlogin")).getTime()
                                ,dateFormat.parse(result.getString("firstlogin")).getTime(),result.getLong("onlinetime")
                                ,result.getBoolean("bypass"),result.getBoolean("teamchatlogin")
                                ,result.getBoolean("reportlogin"),null,new Document(),null,null,null);
                        players.put(player.getUUID(),player);
                        createPlayer(player);
                    }
                }catch (Exception exception){
                    System.out.println(Messages.SYSTEM_PREFIX + "Could not translate players");
                    exception.printStackTrace();
                }
                return null;
            });
            List<UUID> banned = new ArrayList<>();
            List<UUID> muted = new ArrayList<>();
            long yet = System.currentTimeMillis();
            historyOld.select().execute(result -> {
                try{
                    while(result.next()){
                        long timeStamp = dateFormat.parse(result.getString("date")).getTime();
                        NetworkPlayer player = players.get(UUID.fromString(result.getString("playeruuid")));
                        if(player == null) continue;
                        switch (result.getString("historytype")){
                            case "KICK":
                                createHistoryEntry(player,new Kick(player.getUUID(),player.getIP(),result.getString("reason")
                                        ,"",timeStamp,-1,0,-1
                                        ,result.getString("staffuuid"),new Document(),"Unknown"));
                                break;
                            case "NETWORKBAN":
                                long timeOut = timeStamp+result.getLong("duration");
                                if(timeOut > yet) banned.add(player.getUUID());
                                createHistoryEntry(player,new Ban(player.getUUID(),player.getIP(),result.getString("reason")
                                        ,"",timeStamp,-1,0,-1,result.getString("staffuuid")
                                        ,new Document(),timeOut,BanType.NETWORK));
                                break;
                            case "CHATBAN":
                                long timeOut2 = timeStamp+result.getLong("duration");
                                if(timeOut2 > yet) muted.add(player.getUUID());
                                createHistoryEntry(player,new Ban(player.getUUID(),player.getIP(),result.getString("reason")
                                        ,"",timeStamp,-1,0,-1,result.getString("staffuuid")
                                        ,new Document(),timeOut2,BanType.CHAT));
                                break;
                            case "UNBAN":
                                if(result.getString("staffuuid").equalsIgnoreCase("AutoUnban")) continue;
                                if(banned.contains(player.getUUID())){
                                    createHistoryEntry(player,new Unban(player.getUUID(),player.getIP(),result.getString("reason")
                                            ,"",timeStamp,-1,0,-1,result.getString("staffuuid")
                                            ,new Document(),BanType.NETWORK));
                                }
                                if(muted.contains(player.getUUID())){
                                    createHistoryEntry(player,new Unban(player.getUUID(),player.getIP(),result.getString("reason")
                                            ,"",timeStamp,-1,0,-1,result.getString("staffuuid")
                                            ,new Document(),BanType.CHAT));
                                }
                                break;
                        }
                    }
                }catch (Exception exception){
                    System.out.println(Messages.SYSTEM_PREFIX + "Could not translate history");
                    exception.printStackTrace();
                }
                return null;
            });
            autobroadcastOld.select().execute(result -> {
                try{
                    while(result.next()){
                        createBroadcast(new Broadcast(-1,result.getString("message"),null,null,System.currentTimeMillis()
                                ,System.currentTimeMillis(),true,new Broadcast.Click("", Broadcast.ClickType.URL)));
                    }
                }catch (Exception exception){
                    System.out.println(Messages.SYSTEM_PREFIX + "Could not translate autobroadcasts.");
                    exception.printStackTrace();
                }
                return null;
            });
            filterOld.select().execute(result -> {
                try{
                    while(result.next()){
                        createFilter(new Filter(-1,result.getString("message"),FilterOperation.CONTAINS
                                ,FilterType.valueOf(result.getString("filtertype"))));
                    }
                }catch (Exception exception){
                    System.out.println(Messages.SYSTEM_PREFIX + "Could not translate filters.");
                    exception.printStackTrace();
                }
                return null;
            });

        }catch (Exception exception){
            exception.printStackTrace();
        }
        this.players.execute("RENAME TABLE `DKBans_history` TO `DKBans_historyOld`");
        this.players.execute("RENAME TABLE `DKBans_autobroadcast` TO `DKBans_autobroadcastOld`");
        this.players.execute("RENAME TABLE `DKBans_filter` TO `DKBans_filterOld`");
        this.players.execute("RENAME TABLE `DKBans_bans` TO `DKBans_bansOld`");

        System.out.println(Messages.SYSTEM_PREFIX+"Translating finished.");
    }
}
