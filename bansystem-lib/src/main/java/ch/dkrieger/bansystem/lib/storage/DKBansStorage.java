package ch.dkrieger.bansystem.lib.storage;

import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.filter.Filter;
import ch.dkrieger.bansystem.lib.filter.FilterType;
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

import java.util.List;
import java.util.UUID;

public interface DKBansStorage {

    public boolean connect();

    public boolean isConnected();

    public void disconnect();

    public NetworkPlayer getPlayer(int id) throws Exception;

    public NetworkPlayer getPlayer(String name) throws Exception;

    public NetworkPlayer getPlayer(UUID uuid) throws Exception;

    public List<NetworkPlayer> getPlayersByIp(String ip);

    public int getRegisteredPlayerCount();

    public int getCountryCount();

    public int createPlayer(NetworkPlayer player);

    public void createOnlineSession(NetworkPlayer player, OnlineSession session);

    public void finishStartedOnlineSession(UUID uuid, long login, long logout, String server);

    public void saveStaffSettings(UUID player, boolean report,boolean teamChat);

    public void updatePlayerLoginInfos(UUID player,String name, long lastLogin, String color, boolean bypass, String lastIP,String lastCountry, long logins);

    public void updatePlayerLogoutInfos(UUID player, long lastLogin, long onlineTime, String color, boolean bypass, long messages);

    public void updatePlayerStats(UUID uuid, PlayerStats stats);

    public ChatLog getChatLog(UUID player);

    public ChatLog getChatLog(String server);

    public void createChatLogEntry(ChatLogEntry entry);

    public void deleteOldChatLog(long before);

    public History getHistory(UUID player);

    public void clearHistory(NetworkPlayer player);

    public int createHistoryEntry(NetworkPlayer player, HistoryEntry entry);

    public void deleteHistoryEntry(NetworkPlayer player,int id);

    public List<Report> getReports();

    public void createReport(Report report);

    public void processReports(NetworkPlayer player, NetworkPlayer staff);

    public void deleteReports(NetworkPlayer player);

    public Ban getBan(int id);

    @SuppressWarnings("This methode is dangerous, it (can) return many datas and have a long delay.")
    public List<Ban> getBans();
    @SuppressWarnings("This methode is dangerous, it (can) return many datas and have a long delay.")
    public List<Ban> getBans(int reasonID);
    @SuppressWarnings("This methode is dangerous, it (can) return many datas and have a long delay.")
    public List<Ban> getBans(String reason);


    @SuppressWarnings("This methode is dangerous, it (can) return many datas and have a long delay.")
    public List<Ban> getBansFromStaff(String staff);

    public List<Filter> loadFilters();

    public int createFilter(Filter filter);

    public void deleteFilter(int id);

    public List<Broadcast> loadBroadcasts();

    public int createBroadcast(Broadcast broadcast);

    public void updateBroadcast(Broadcast broadcast);

    public void deleteBroadcast(int id);

    public NetworkStats getNetworkStats();

}
