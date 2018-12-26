package ch.dkrieger.bansystem.lib.storage.mongodb;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 16.11.18 19:49
 *
 */

import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.config.Config;
import ch.dkrieger.bansystem.lib.filter.Filter;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineSession;
import ch.dkrieger.bansystem.lib.player.chatlog.ChatLog;
import ch.dkrieger.bansystem.lib.player.chatlog.ChatLogEntry;
import ch.dkrieger.bansystem.lib.player.history.History;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.HistoryEntry;
import ch.dkrieger.bansystem.lib.reason.UnbanReason;
import ch.dkrieger.bansystem.lib.report.Report;
import ch.dkrieger.bansystem.lib.stats.NetworkStats;
import ch.dkrieger.bansystem.lib.stats.PlayerStats;
import ch.dkrieger.bansystem.lib.storage.DKBansStorage;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import ch.dkrieger.bansystem.lib.utils.RuntimeTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.mongodb.client.model.Filters.*;

public class MongoDBDKBansStorage implements DKBansStorage {

    private Config config;
    private MongoClient mongoClient;
    private MongoDatabase database;

    private MongoCollection playerCollection, chatlogCollection, historyCollection, reportCollection, filterCollection, broadcastCollection, networkStatsCollection;

    public MongoDBDKBansStorage(Config config) {
        this.config = config;
    }
    @Override
    public boolean connect() {
        String uri = "mongodb"+(config.mongoDbSrv?"+srv":"")+"://";
        if(config.mongoDbAuthentication) uri += config.storageUser+":"+config.storagePassword+"@";
        uri += config.storageHost+"/";
        if(config.mongoDbAuthentication) uri += config.mongoDbAuthDB;
        uri += "?retryWrites=true&connectTimeoutMS=500&socketTimeoutMS=500";
        if(config.storageSSL) uri+= "&ssl=true";

        this.mongoClient = new MongoClient(new MongoClientURI(uri));
        this.database = this.mongoClient.getDatabase(config.storageDatabase);
        this.playerCollection = database.getCollection("DKBans_players");
        this.chatlogCollection = database.getCollection("DKBans_chatlogs");
        this.broadcastCollection = database.getCollection("DKBans_broadcasts");
        this.historyCollection = database.getCollection("DKBans_historys");
        this.reportCollection = database.getCollection("DKBans_reports");
        this.filterCollection = database.getCollection("DKBans_filters");
        this.networkStatsCollection = database.getCollection("DKBans_networkstats");

        return true;
    }
    @Override
    public void disconnect() {

    }
    @Override
    public boolean isConnected() {
        MongoDatabase database = mongoClient.getDatabase(config.storageDatabase);
        org.bson.Document serverStatus = database.runCommand(new org.bson.Document("serverStatus", 1));
        Map connections = (Map) serverStatus.get("connections");
        Integer current = (Integer) connections.get("current");
        System.out.println(current);
        return true;
    }

    @Override
    public NetworkPlayer getPlayer(int id) throws Exception {
        return getPlayer(eq("id",id));
    }

    @Override
    public NetworkPlayer getPlayer(String name) throws Exception {
        return getPlayer(new Document().append("name",new Document("$regex",name).append("$options","i")));
    }

    @Override
    public NetworkPlayer getPlayer(UUID uuid) throws Exception {
        return getPlayer(eq("uuid",uuid.toString()));
    }
    public NetworkPlayer getPlayer(Bson bson){
        NetworkPlayer player = MongoDBUtil.findFirst(this.playerCollection,bson,NetworkPlayer.class);
        if(player != null){
            player.setHistory(getHistory(player.getUUID()));
            player.setReports(MongoDBUtil.find(this.reportCollection,eq("uuid",player.getUUID().toString()),Report.class));
        }
        return player;
    }

    @Override
    public void saveStaffSettings(UUID player, boolean report, boolean teamchat) {
        this.playerCollection.updateOne(eq("uuid",player.toString()),new Document("$set"
                ,new Document("teamChatLogin",teamchat).append("reportLogin",report)));
    }

    @Override
    public List<NetworkPlayer> getPlayersByIp(String ip) {
        return null;//"$in
    }

    @Override
    public int getRegisteredPlayerCount() {
        return Integer.valueOf(String.valueOf(playerCollection.countDocuments()));
    }

    @Override
    public int getCountryCount() {
        return 0;
    }
    @Override
    public Ban getBan(int id) {
        return null;
    }
    @Override
    public int createPlayer(NetworkPlayer player) {
        player.setID(getRegisteredPlayerCount()+1);
        MongoDBUtil.insertOne(this.playerCollection,player);
        return player.getID();
    }
    @Override
    public void createChatLogEntry(ChatLogEntry entry) {
        MongoDBUtil.insertOne(this.chatlogCollection,entry);
    }
    @Override
    public ChatLog getChatLog(UUID uuid) {
        List<ChatLogEntry> entries = MongoDBUtil.find(this.chatlogCollection,eq("uuid",uuid.toString()),ChatLogEntry.class);
        return new ChatLog(entries);
    }
    @Override
    public ChatLog getChatLog(String server) {
        List<ChatLogEntry> entries = MongoDBUtil.find(this.chatlogCollection,eq("server",server),ChatLogEntry.class);
        return new ChatLog(entries);
    }
    @Override
    public History getHistory(UUID uuid) {
        List<HistoryEntry> entries = MongoDBUtil.find(this.historyCollection,eq("uuid",uuid.toString()),HistoryEntry.class);
        Map<Integer,HistoryEntry> mapEntries = new HashMap<>();
        GeneralUtil.iterateForEach(entries, object -> mapEntries.put(object.getID(),object));
        return new History(mapEntries);
    }

    @Override
    public void clearHistory(NetworkPlayer player) {
        MongoDBUtil.deleteMany(this.historyCollection,eq("uuid",player.getUUID().toString()));
    }

    @Override
    public int createHistoryEntry(NetworkPlayer player, HistoryEntry entry) {
        entry.setID(Integer.valueOf(String.valueOf(this.historyCollection.countDocuments()+1)));
        //MongoDBUtil.insertOne(this.historyCollection,entry);
        historyCollection.insertOne(Document.parse(GeneralUtil.GSON.toJson(entry,new TypeToken<HistoryEntry>(){}.getType())));
        return entry.getID();
    }

    @Override
    public void deleteHistoryEntry(NetworkPlayer player, int id) {
        MongoDBUtil.deleteOne(this.historyCollection,and(eq("uuid",player.getUUID().toString()),eq("id",player.getID())));
    }

    @Override
    public List<Report> getReports() {
        return MongoDBUtil.findALL(this.reportCollection,Report.class);

    }

    @Override
    public void createReport(Report report) {
        MongoDBUtil.insertOne(this.reportCollection,report);
    }

    @Override
    public void processReports(NetworkPlayer player, NetworkPlayer staff) {
        MongoDBUtil.updateMany(this.reportCollection,and(eq("uuid",player.getUUID().toString()),exists("staff",false))
                ,new Document().append("$set",new Document("staff",staff.getUUID().toString())));
    }

    @Override
    public void deleteReports(NetworkPlayer player) {
        MongoDBUtil.deleteMany(this.reportCollection,eq("uuid",player.getUUID().toString()));
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
        return MongoDBUtil.findALL(this.filterCollection,Filter.class);
    }

    @Override
    public int createFilter(Filter filter) {
        filter.setID(Integer.valueOf(String.valueOf(filterCollection.countDocuments()+1)));
        MongoDBUtil.insertOne(this.filterCollection,filter);
        return filter.getID();
    }

    @Override
    public void deleteFilter(int id) {
        MongoDBUtil.deleteOne(this.filterCollection,eq("id",id));
    }

    @Override
    public List<Broadcast> loadBroadcasts() {
        return MongoDBUtil.findALL(this.filterCollection,Broadcast.class);
    }

    @Override
    public int createBroadcast(Broadcast broadcast) {
        broadcast.setID(Integer.valueOf(String.valueOf(broadcastCollection.countDocuments()+1)));
        MongoDBUtil.insertOne(this.broadcastCollection,broadcast);
        return broadcast.getID();
    }

    @Override
    public void updateBroadcast(Broadcast broadcast) {
        MongoDBUtil.replaceOne(this.broadcastCollection,eq("id",broadcast.getID()),broadcast);
    }

    @Override
    public void deleteBroadcast(int id) {
        MongoDBUtil.deleteOne(this.broadcastCollection,eq("id",id));
    }

    @Override
    public NetworkStats getNetworkStats() {
        List<NetworkStats> stats = MongoDBUtil.findALL(this.networkStatsCollection,NetworkStats.class);
        if(stats.size() > 0) return stats.get(0);
        return new NetworkStats();
    }

    @Override
    public void createOnlineSession(NetworkPlayer player, OnlineSession session) {

    }

    @Override
    public void finishStartedOnlineSession(UUID uuid, long login, long logout, String server) {

    }

    @Override
    public void updatePlayerLoginInfos(UUID player, String name, long lastLogin, String color, boolean bypass, String lastIP, String lastCountry, long logins) {

    }

    @Override
    public void updatePlayerLogoutInfos(UUID player, long lastLogin, long onlineTime, String color, boolean bypass, long messages) {

    }

    @Override
    public void updatePlayerStats(UUID uuid, PlayerStats stats) {

    }

    @Override
    public void deleteOldChatLog(long before) {

    }
}