package ch.dkrieger.bansystem.lib.storage;

import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.History;

import java.util.UUID;

public interface BanPlayerStorage {

    public NetworkPlayer getPlayer(int id);

    public NetworkPlayer getPlayer(String name);

    public NetworkPlayer getPlayer(UUID uuid);

    public NetworkPlayer createPlayer(NetworkPlayer player);

    public void saveHistory(UUID uuid, History history);

}
