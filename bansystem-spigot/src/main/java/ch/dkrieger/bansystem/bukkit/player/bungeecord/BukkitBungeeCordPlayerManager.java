package ch.dkrieger.bansystem.bukkit.player.bungeecord;

import ch.dkrieger.bansystem.bukkit.BukkitBanSystemBootstrap;
import ch.dkrieger.bansystem.bukkit.BungeeCordConnection;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.NetworkPlayerUpdateCause;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.PlayerManager;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.*;

public class BukkitBungeeCordPlayerManager extends PlayerManager {

    private Map<UUID,OnlineNetworkPlayer> onlinePlayers;
    private BungeeCordConnection connection;

    public BukkitBungeeCordPlayerManager(BungeeCordConnection connection) {
        this.onlinePlayers = new HashMap<>();
        this.connection = connection;
    }
    public void insertOnlinePlayer(OnlineNetworkPlayer online){
        this.onlinePlayers.put(online.getUUID(),online);
    }
    @Override
    public OnlineNetworkPlayer getOnlinePlayer(int id) {
        NetworkPlayer player = getPlayer(id);
        if(player != null) return getOnlinePlayer(player.getUUID());
        return null;
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(UUID uuid) {
        return onlinePlayers.get(uuid);
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(String name) {
        return GeneralUtil.iterateOne(this.onlinePlayers.values(), object -> object.getName().equalsIgnoreCase(name));
    }

    @Override
    public List<OnlineNetworkPlayer> getOnlinePlayers() {
        return new ArrayList<>(this.onlinePlayers.values());
    }

    @Override
    public void removeOnlinePlayerFromCache(OnlineNetworkPlayer player) {
        this.onlinePlayers.remove(player.getUUID());
    }

    @Override
    public void removeOnlinePlayerFromCache(UUID uuid) {
        this.onlinePlayers.remove(uuid);
    }

    @Override
    public void updatePlayer(NetworkPlayer player, NetworkPlayerUpdateCause cause, Document properties) {
        System.out.println("update "+player.getColoredName());
        BukkitBanSystemBootstrap.getInstance().executePlayerUpdateEvents(player.getUUID(),cause,properties,true);
        connection.send("updatePlayer",new Document().append("uuid",player.getUUID()).append("cause",cause)
                .append("properties",properties));
    }
    public void updateAll(List<OnlineNetworkPlayer> players){
        this.onlinePlayers.clear();
        GeneralUtil.iterateForEach(players, object -> onlinePlayers.put(object.getUUID(),object));
    }
    @Override
    public void updateOnlinePlayer(OnlineNetworkPlayer player) {}

    @Override
    public int getOnlineCount() {
        return this.onlinePlayers.size();
    }
}
