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

package ch.dkrieger.bansystem.lib.storage.mongodb;

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
import ch.dkrieger.bansystem.lib.report.Report;
import ch.dkrieger.bansystem.lib.stats.NetworkStats;
import ch.dkrieger.bansystem.lib.stats.PlayerStats;
import ch.dkrieger.bansystem.lib.storage.DKBansStorage;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import com.google.gson.reflect.TypeToken;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
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

    private MongoCollection<Document> playerCollection, chatlogCollection, historyCollection, reportCollection, filterCollection, broadcastCollection, networkStatsCollection;

    public MongoDBDKBansStorage(Config config) {
        this.config = config;
    }
    @Override
    public boolean connect() {//Indexes.descending("name")
        String uri = "mongodb"+(config.mongoDbSrv?"+srv":"")+"://";
        if(config.mongoDbAuthentication) uri += config.storageUser+":"+config.storagePassword+"@";
        uri += config.storageHost+"/";
        if(config.mongoDbAuthentication) uri += config.mongoDbAuthDB;
        uri += "?retryWrites=true&connectTimeoutMS=500&socketTimeoutMS=500";
        if(config.storageSSL) uri+= "&ssl=true";

        this.mongoClient = new MongoClient(new MongoClientURI(uri));
        this.database = this.mongoClient.getDatabase(config.storageDatabase);
        this.playerCollection = database.getCollection("DKBans_players",Document.class);
        this.chatlogCollection = database.getCollection("DKBans_chatlogs",Document.class);
        this.broadcastCollection = database.getCollection("DKBans_broadcasts",Document.class);
        this.historyCollection = database.getCollection("DKBans_historys",Document.class);
        this.reportCollection = database.getCollection("DKBans_reports",Document.class);
        this.filterCollection = database.getCollection("DKBans_filters",Document.class);
        this.networkStatsCollection = database.getCollection("DKBans_networkstats",Document.class);

        try{this.playerCollection.createIndex(Indexes.descending("id"),new IndexOptions().unique(true)); }catch (Exception exception){}
        try{this.historyCollection.createIndex(Indexes.descending("id"),new IndexOptions().unique(true)); }catch (Exception exception){}
        try{this.filterCollection.createIndex(Indexes.descending("id"),new IndexOptions().unique(true)); }catch (Exception exception){}
        try{this.broadcastCollection.createIndex(Indexes.descending("id"),new IndexOptions().unique(true)); }catch (Exception exception){}

        if(this.playerCollection.countDocuments() == 0)
            this.playerCollection.insertOne(new Document("name","DKBansUniquePlayerIDCounter").append("counterNext",1));

        if(this.historyCollection.countDocuments() == 0)
            this.historyCollection.insertOne(new Document("name","DKBansUniqueHistoryIDCounter").append("counterNext",1));

        if(this.filterCollection.countDocuments() == 0)
            this.filterCollection.insertOne(new Document("name","DKBansUniqueFilterIDCounter").append("counterNext",1));

        if(this.broadcastCollection.countDocuments() == 0)
            this.broadcastCollection.insertOne(new Document("name","DKBansUniqueBroadcastIDCounter").append("counterNext",1));

        if(this.networkStatsCollection.countDocuments() == 0){
            MongoDBUtil.insertOne(networkStatsCollection,new NetworkStats());
        }
        return true;
    }
    @Override
    public void disconnect() {

    }
    @Override
    public boolean isConnected() {
        try{
            playerCollection.countDocuments();
            return true;
        }catch (Exception exception){}
        return false;
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
    public void updatePlayerProperties(UUID uuid, ch.dkrieger.bansystem.lib.utils.Document properties) {
        this.playerCollection.updateOne(eq("uuid",uuid.toString()),new Document("$set"
                ,new Document("properties",properties)));
    }

    @Override
    public List<NetworkPlayer> getPlayersByIp(String ip) {
        List<NetworkPlayer> players = MongoDBUtil.find(this.playerCollection,eq("lastIP",ip),NetworkPlayer.class);
        GeneralUtil.iterateForEach(players, player -> {
            player.setHistory(getHistory(player.getUUID()));
            player.setReports(MongoDBUtil.find(reportCollection,eq("uuid",player.getUUID().toString()),Report.class));
        });
        return players;
    }

    @Override
    public int getRegisteredPlayerCount() {
        return Integer.valueOf(String.valueOf(playerCollection.countDocuments()-1));
    }

    @Override
    public HistoryEntry getHistoryEntry(int id) {
        return MongoDBUtil.findFirst(this.historyCollection,eq("id",id),HistoryEntry.class);
    }

    @Override
    public int createPlayer(NetworkPlayer player) {
        Document result = playerCollection.findOneAndUpdate(eq("name","DKBansUniquePlayerIDCounter")
                ,new Document("$inc",new Document("counterNext",1)));
        int id = result.getInteger("counterNext");
        player.setID(id);
        Document document = MongoDBUtil.toDocument(player);
        document.remove("history");
        document.remove("reports");
        playerCollection.insertOne(document);
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
    public ChatLog getChatLog(UUID player, String server) {
        List<ChatLogEntry> entries = MongoDBUtil.find(this.chatlogCollection,and(eq("server",server),eq("uuid",player.toString())),ChatLogEntry.class);
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
    public void setColor(UUID player, String color) {
        this.playerCollection.updateOne(eq("uuid",player.toString()),new Document("$set",new Document("color",color)));
    }

    @Override
    public void clearHistory(NetworkPlayer player) {
        MongoDBUtil.deleteMany(this.historyCollection,eq("uuid",player.getUUID().toString()));
    }

    @Override
    public int createHistoryEntry(NetworkPlayer player, HistoryEntry entry) {
        Document result = historyCollection.findOneAndUpdate(eq("name","DKBansUniqueHistoryIDCounter")
                ,new Document("$inc",new Document("counterNext",1)));
        entry.setID(result.getInteger("counterNext"));
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
        return MongoDBUtil.find(this.historyCollection,eq("historyAdapterType","BAN"),Ban.class);
    }

    @Override
    public List<Ban> getNotTimeOutedBans() {
        return MongoDBUtil.find(this.historyCollection,and(eq("historyAdapterType","BAN")
                ,gt("timeOut",System.currentTimeMillis())),Ban.class);
    }

    @Override
    public List<Filter> loadFilters() {
        return MongoDBUtil.find(this.filterCollection,not(eq("name","DKBansUniqueFilterIDCounter")),Filter.class);
    }

    @Override
    public int createFilter(Filter filter) {
        Document result = filterCollection.findOneAndUpdate(eq("name","DKBansUniqueFilterIDCounter")
                ,new Document("$inc",new Document("counterNext",1)));
        filter.setID(result.getInteger("counterNext"));
        MongoDBUtil.insertOne(this.filterCollection,filter);
        return filter.getID();
    }

    @Override
    public void deleteFilter(int id) {
        MongoDBUtil.deleteOne(this.filterCollection,eq("id",id));
    }

    @Override
    public List<Broadcast> loadBroadcasts() {
        return MongoDBUtil.find(this.broadcastCollection,not(eq("name","DKBansUniqueBroadcastIDCounter")),Broadcast.class);
    }

    @Override
    public int createBroadcast(Broadcast broadcast) {
        Document result = broadcastCollection.findOneAndUpdate(eq("name","DKBansUniqueBroadcastIDCounter")
                ,new Document("$inc",new Document("counterNext",1)));
        broadcast.setID(result.getInteger("counterNext"));
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
        this.playerCollection.updateOne(eq("uuid",player.getUUID().toString()),new Document("$push"
                ,new Document("onlineSessions",MongoDBUtil.toDocument(session))));
    }

    @Override
    public void finishStartedOnlineSession(UUID uuid, long login, long logout, String server) {
        this.playerCollection.updateOne(and(eq("uuid",uuid.toString()),eq("onlineSessions.connected",login))
                ,new Document("$set",new Document("onlineSessions.$.disconnected",logout).append("onlineSessions.$.lastServer",server)));
    }

    @Override
    public void updatePlayerLoginInfos(UUID player, String name, long lastLogin, String color, boolean bypass, String lastIP, String lastCountry, long logins) {
        this.playerCollection.updateOne(eq("uuid",player.toString()),new Document("$set"
                ,new Document("lastLogin",lastLogin).append("lastIP",lastIP).append("lastCountry",lastCountry).append("color",color)
                .append("bypass",bypass).append("lastCountry",lastCountry).append("stats.logins",logins)));
    }

    @Override
    public void updatePlayerLogoutInfos(UUID player, long lastLogin, long onlineTime, String color, boolean bypass, long messages) {
        this.playerCollection.updateOne(eq("uuid",player.toString()),new Document("$set"
                ,new Document("lastLogin",lastLogin).append("onlineTime",onlineTime).append("color",color)
                .append("bypass",bypass).append("stats.messages",messages)));
    }

    @Override
    public void updatePlayerStats(UUID uuid, PlayerStats stats) {
       playerCollection.updateOne(eq("uuid",uuid.toString()),new Document("$set",new Document("stats",MongoDBUtil.toDocument(stats))));
    }

    @Override
    public void deleteOldChatLog(long before) {
        chatlogCollection.deleteMany(lt("time",before));
    }

    @Override
    public void updateNetworkStats(long logins, long reports, long reportsAccepted, long messages, long bans, long mutes, long unbans, long kicks) {
        MongoDBUtil.replaceOne(networkStatsCollection,new Document(),new NetworkStats(logins, reports, reportsAccepted
                , messages, bans, mutes, bans, kicks));
    }
}