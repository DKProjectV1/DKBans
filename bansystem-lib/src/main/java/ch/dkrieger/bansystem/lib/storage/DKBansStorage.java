package ch.dkrieger.bansystem.lib.storage;

import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.filter.Filter;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.History;

import java.util.List;
import java.util.UUID;

public interface DKBansStorage {

    public NetworkPlayer getPlayer(int id);

    public NetworkPlayer getPlayer(String name);

    public NetworkPlayer getPlayer(UUID uuid);

    public NetworkPlayer createPlayer(NetworkPlayer player);

    public void saveHistory(UUID uuid, History history);

    public List<Filter> loadFilters();

    public int createFilter(Filter filter);

    public void deleteFilter(int id);

    public List<Broadcast> loadBroadcasts();

    public int createBroadcast(Broadcast broadcast);

    public int updateBroadcast(Broadcast broadcast);

    public int deleteBroadcast(int id);

}
