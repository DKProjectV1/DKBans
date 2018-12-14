package ch.dkrieger.bansystem.lib.storage;

import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.filter.Filter;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.History;
import ch.dkrieger.bansystem.lib.player.history.value.Ban;
import ch.dkrieger.bansystem.lib.player.history.value.HistoryEntry;
import ch.dkrieger.bansystem.lib.report.Report;

import java.util.List;
import java.util.UUID;

public interface DKBansStorage {

    public boolean connect();

    public boolean isConnected();

    public void disconnect();

    public NetworkPlayer getPlayer(int id) throws Exception;

    public NetworkPlayer getPlayer(String name) throws Exception;

    public NetworkPlayer getPlayer(UUID uuid) throws Exception;

    public List<Ban> getBans();

    public int createPlayer(NetworkPlayer player);

    public History getHistory(UUID player);

    public void saveHistory(UUID uuid, History history);

    public void createHistoryEntry(UUID player, HistoryEntry entry);

    public void delteHistoryEntry(int id);

    public void saveReport(Report report);

    public List<Filter> loadFilters();

    public int createFilter(Filter filter);

    public void deleteFilter(int id);

    public List<Broadcast> loadBroadcasts();

    public int createBroadcast(Broadcast broadcast);

    public void updateBroadcast(Broadcast broadcast);

    public int deleteBroadcast(int id);

}
