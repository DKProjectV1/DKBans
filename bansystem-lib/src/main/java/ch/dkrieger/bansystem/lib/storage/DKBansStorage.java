package ch.dkrieger.bansystem.lib.storage;

import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.filter.Filter;
import ch.dkrieger.bansystem.lib.filter.FilterType;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.chatlog.ChatLog;
import ch.dkrieger.bansystem.lib.player.history.History;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.HistoryEntry;
import ch.dkrieger.bansystem.lib.report.Report;
import ch.dkrieger.bansystem.lib.stats.NetworkStats;

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

    public ChatLog getChatLog(UUID player);

    public ChatLog getChatLog(String server);

    public History getHistory(UUID player);

    public void resetHistory(UUID uuid);

    public void resetHistory(UUID uuid, int id);

    public int addHistoryEntry(UUID uuid, HistoryEntry entry);

    public void createChatLogEntry(UUID uuid, String message, String server, FilterType filter);

    public void saveHistory(UUID uuid, History history);

    public void createHistoryEntry(UUID player, HistoryEntry entry);

    public void delteHistoryEntry(int id);

    public List<Report> getReports();

    public void createReport(Report report);

    public void processReports(UUID player, UUID staff);

    public void deleteReports(UUID uuid);

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

    public int deleteBroadcast(int id);

    public NetworkStats getNetworkStats();

}
