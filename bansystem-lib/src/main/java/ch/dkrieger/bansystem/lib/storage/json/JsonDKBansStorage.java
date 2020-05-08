/*
 * (C) Copyright 2020 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 08.05.20, 19:58
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

package ch.dkrieger.bansystem.lib.storage.json;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.config.Config;
import ch.dkrieger.bansystem.lib.filter.Filter;
import ch.dkrieger.bansystem.lib.player.IPBan;
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
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class JsonDKBansStorage implements DKBansStorage {

    private List<NetworkPlayer> players;
    private List<ChatLogEntry> chatLogEntries;
    private List<IPBan> ipBans;
    private Map<String,Document> settings;
    private File storageFolder;

    private AtomicInteger nextPlayerID, nextHistoryID;

    public JsonDKBansStorage(Config config){
        this.storageFolder = config.storageFolder;
        this.storageFolder.mkdirs();
    }
    @Override
    public boolean connect() {
        Document document = Document.loadData(new File(storageFolder,"players.json"));
        if(document.contains("players")){
            players = document.getObject("players",new TypeToken<List<NetworkPlayer>>(){}.getType());
            nextPlayerID = new AtomicInteger(document.getInt("nextPlayerID"));
            nextHistoryID  = new AtomicInteger(document.getInt("nextHistoryID"));
        }else{
            this.players = new ArrayList<>();
            this.nextPlayerID = new AtomicInteger(1);
            this.nextHistoryID = new AtomicInteger(1);
        }
        Document chatLogs = Document.loadData(new File(storageFolder,"chatlogs.json"));
        if(chatLogs.contains("entries")){
            this.chatLogEntries = chatLogs.getObject("entries",new TypeToken<List<ChatLogEntry>>(){}.getType());
        }else this.chatLogEntries = new ArrayList<>();

        Document ipBans = Document.loadData(new File(storageFolder,"ipbans.json"));
        if(ipBans.contains("bans")) this.ipBans = ipBans.getObject("bans",new TypeToken<List<IPBan>>(){}.getType());
        else this.ipBans = new ArrayList<>();

        Document settings = Document.loadData(new File(storageFolder,"settings.json"));
        if(settings.contains("settings")) this.settings = settings.getObject("settings",new TypeToken<LinkedHashMap<String,Document>>(){}.getType());
        else this.settings = new LinkedHashMap<>();

        BanSystem.getInstance().getPlatform().getTaskManager().scheduleTask(this::save,5L, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public void disconnect() {
        save();
    }
    private void save(){
        new Document().append("nextPlayerID",nextPlayerID.get()).append("nextHistoryID",nextHistoryID.get()).append("players",players)
                .saveData(new File(storageFolder,"players.json"));
        new Document().append("entries",chatLogEntries).saveData(new File(storageFolder,"chatlogs.json"));

        new Document().append("bans",ipBans).saveData(new File(storageFolder,"ipbans.json"));

        new Document().append("settings",settings).saveData(new File(storageFolder,"settings.json"));
    }

    @Override
    public NetworkPlayer getPlayer(int id) throws Exception {
        return GeneralUtil.iterateOne(this.players, object -> object.getID() == id);
    }

    @Override
    public NetworkPlayer getPlayer(String name) throws Exception {
        return GeneralUtil.iterateOne(this.players, object -> object.getName().equalsIgnoreCase(name));
    }

    @Override
    public NetworkPlayer getPlayer(UUID uuid) throws Exception {
        return GeneralUtil.iterateOne(this.players, object -> object.getUUID().equals(uuid));
    }

    @Override
    public List<NetworkPlayer> getPlayersByIp(String ip) {
        return GeneralUtil.iterateAcceptedReturn(this.players, object -> object.getIP().equalsIgnoreCase(ip));
    }

    @Override
    public int getRegisteredPlayerCount() {
        return this.players.size();
    }

    @Override
    public int createPlayer(NetworkPlayer player) {
        final int id = nextPlayerID.getAndIncrement();
        player.setID(id);
        this.players.add(player);
        return id;
    }

    @Override
    public void createOnlineSession(NetworkPlayer player, OnlineSession session) {}

    @Override
    public void finishStartedOnlineSession(UUID uuid, long login, long logout, String server) {}

    @Override
    public void saveStaffSettings(UUID uuid, boolean report, boolean teamChat) {}

    @Override
    public void setColor(UUID player, String color) {}

    @Override
    public void updatePlayerLoginInfos(UUID player, String name, long lastLogin, String color, boolean bypass, String lastIP, String lastCountry, long logins) { }

    @Override
    public void updatePlayerLogoutInfos(UUID player, long lastLogin, long onlineTime, String color, boolean bypass, long messages) {}

    @Override
    public void updatePlayerStats(UUID uuid, PlayerStats stats) {}

    @Override
    public void updatePlayerProperties(UUID uuid, Document properties) {

    }

    @Override
    public ChatLog getChatLog(UUID player) {
        return new ChatLog(GeneralUtil.iterateAcceptedReturn(this.chatLogEntries, object -> object.getUUID().equals(player)));
    }

    @Override
    public ChatLog getChatLog(String server) {
        return new ChatLog(GeneralUtil.iterateAcceptedReturn(this.chatLogEntries, object -> object.getServer().equalsIgnoreCase(server)));
    }

    @Override
    public ChatLog getChatLog(UUID player, String server) {
        return new ChatLog(GeneralUtil.iterateAcceptedReturn(this.chatLogEntries, object -> object.getServer().equalsIgnoreCase(server)
                && object.getUUID().equals(player)));
    }

    @Override
    public void createChatLogEntry(ChatLogEntry entry) {
        this.chatLogEntries.add(entry);
    }

    @Override
    public void deleteOldChatLog(long before) {
        GeneralUtil.iterateAndRemove(this.chatLogEntries, object -> object.getTime() <= before);
    }

    @Override
    public History getHistory(UUID uuid) {
        try{
            NetworkPlayer player = getPlayer(uuid);
            return player.getHistory();
        }catch (Exception e){}
        return null;
    }

    @Override
    public void clearHistory(NetworkPlayer player) {}

    @Override
    public int createHistoryEntry(NetworkPlayer player, HistoryEntry entry) {
        entry.setID(nextHistoryID.getAndIncrement());
        return entry.getID();
    }

    @Override
    public void updateHistoryEntry(NetworkPlayer player, HistoryEntry entry) {}

    @Override
    public void deleteHistoryEntry(NetworkPlayer player, int id) {}

    @Override
    public List<Report> getReports() {
        List<Report> reports = new ArrayList<>();
        GeneralUtil.iterateForEach(this.players, object -> reports.addAll(object.getReports()));
        return reports;
    }

    @Override
    public void updateWatchReportPlayer(UUID uuid, UUID watchPlayer) {}

    @Override
    public void createReport(Report report) {}

    @Override
    public void processReports(NetworkPlayer player, NetworkPlayer staff) {}

    @Override
    public void deleteReports(NetworkPlayer player) {}

    @Override
    public HistoryEntry getHistoryEntry(int id) {
        final HistoryEntry[] entryReturn = new HistoryEntry[1];
        GeneralUtil.iterateOne(this.players, player -> {
            HistoryEntry entry = GeneralUtil.iterateOne(player.getHistory().getEntries(), object -> object.getID() == id);
            entryReturn[0] = entry;
            return entry != null;
        });
        return entryReturn[0];
    }

    @Override
    public List<Ban> getBans() {
        List<Ban> bans = new ArrayList<>();
        GeneralUtil.iterateForEach(this.players, player -> GeneralUtil.iterateAcceptedForEach(player.getHistory().getEntries()
                , object -> object instanceof Ban, object -> bans.add((Ban)object)));
        return bans;
    }
    @Override
    public List<Ban> getNotTimeOutedBans() {
        long yet = System.currentTimeMillis();
        List<Ban> bans = new ArrayList<>();
        GeneralUtil.iterateForEach(this.players, player -> GeneralUtil.iterateAcceptedForEach(player.getHistory().getEntries()
                , object -> object instanceof Ban && object.getTimeStamp() > yet, object -> bans.add((Ban)object)));
        return bans;
    }

    @Override
    public IPBan getIpBan(String ip) {
        return GeneralUtil.iterateOne(this.ipBans, object -> object.getIp().equals(ip));
    }

    @Override
    public void banIp(IPBan ipBan) {
        this.ipBans.add(ipBan);
    }

    @Override
    public void unbanIp(String ip) {
        IPBan ban = getIpBan(ip);
        if(ban != null) this.ipBans.remove(ban);
    }

    @Override
    public void unbanIp(UUID lastPlayer) {
        IPBan ban = GeneralUtil.iterateOne(this.ipBans, object -> object.getLastPlayer() != null && object.getLastPlayer().equals(lastPlayer));
        if(ban != null) this.ipBans.remove(ban);
    }

    @Override
    public List<Filter> loadFilters() {
        Document document = Document.loadData(new File(storageFolder,"filters.json"));
        if(document.contains("filters")) return document.getObject("filters",new TypeToken<List<Filter>>(){}.getType());
        return new ArrayList<>();
    }

    @Override
    public int createFilter(Filter filter) {
        ArrayList<Filter> filters;
        Document document = Document.loadData(new File(storageFolder,"filters.json"));
        if(document.contains("filters")) filters = document.getObject("filters",new TypeToken<List<Filter>>(){}.getType());
        else filters = new ArrayList<>();
        filter.setID(document.getInt("nextID")+1);
        filters.add(filter);
        document.append("nextID",filter.getID()).append("filters",filters).saveData(new File(storageFolder,"filters.json"));
        return filter.getID();
    }

    @Override
    public void deleteFilter(int id) {
        Document document = Document.loadData(new File(storageFolder,"filters.json"));
        if(document.contains("filters")){
            ArrayList<Filter> filters = document.getObject("filters",new TypeToken<List<Filter>>(){}.getType());
            GeneralUtil.iterateAndRemove(filters, object -> object.getID() == id);
            document.append("filters",filters).saveData(new File(storageFolder,"filters.json"));
        }
    }

    @Override
    public List<Broadcast> loadBroadcasts() {
        Document document = Document.loadData(new File(storageFolder,"broadcasts.json"));
        if(document.contains("broadcasts")) return document.getObject("broadcasts",new TypeToken<List<Broadcast>>(){}.getType());
        return new ArrayList<>();
    }

    @Override
    public int createBroadcast(Broadcast broadcast) {
        ArrayList<Broadcast> broadcasts;
        Document document = Document.loadData(new File(storageFolder,"broadcasts.json"));
        if(document.contains("broadcasts")) broadcasts = document.getObject("broadcasts",new TypeToken<List<Broadcast>>(){}.getType());
        else broadcasts = new ArrayList<>();
        broadcast.setID(document.getInt("nextID")+1);
        broadcasts.add(broadcast);
        document.append("nextID",broadcast.getID()).append("broadcasts",broadcasts).saveData(new File(storageFolder,"broadcasts.json"));
        return broadcast.getID();
    }

    @Override
    public void updateBroadcast(Broadcast broadcast) {
        Document document = Document.loadData(new File(storageFolder,"broadcasts.json"));
        if(document.contains("broadcasts")){
            ArrayList<Broadcast> broadcasts = document.getObject("broadcasts",new TypeToken<List<Broadcast>>(){}.getType());
            GeneralUtil.iterateAndRemove(broadcasts, object -> object.getID() == broadcast.getID());
            broadcasts.add(broadcast);
            document.append("broadcasts",broadcasts).saveData(new File(storageFolder,"broadcasts.json"));
        }
    }

    @Override
    public void deleteBroadcast(int id) {
        Document document = Document.loadData(new File(storageFolder,"broadcasts.json"));
        if(document.contains("filters")){
            ArrayList<Broadcast> broadcasts = document.getObject("broadcasts",new TypeToken<List<Broadcast>>(){}.getType());
            GeneralUtil.iterateAndRemove(broadcasts, object -> object.getID() == id);
            document.append("broadcasts",broadcasts).saveData(new File(storageFolder,"broadcasts.json"));
        }
    }

    @Override
    public NetworkStats getNetworkStats() {
        NetworkStats stats = Document.loadData(new File(storageFolder,"networkstats.json")).getObject("networkStats",NetworkStats.class);
        if(stats != null) return stats;
        return new NetworkStats();
    }

    @Override
    public void updateNetworkStats(long logins, long reports, long reportsAccepted, long messages, long bans, long mutes, long unbans, long kicks, long warns) {
        new Document().append("networkStats",new NetworkStats(logins,reports,reportsAccepted,messages,bans,mutes,unbans,kicks,warns))
                .saveData(new File(storageFolder,"networkstats.json"));
    }

    @Override
    public Document getSetting(String name) {
        return settings.get(name);
    }

    @Override
    public void saveSetting(String name, Document document) {
        settings.put(name,document);
    }

    @Override
    public void deleteSetting(String name) {
        settings.remove(name);
    }
}
