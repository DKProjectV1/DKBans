package ch.dkrieger.bansystem.bukkit.player.bukkit;

import ch.dkrieger.bansystem.bukkit.BukkitBanSystemBootstrap;
import ch.dkrieger.bansystem.bukkit.player.bukkit.BukkitOnlinePlayer;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.NetworkPlayerUpdateCause;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.PlayerManager;
import ch.dkrieger.bansystem.lib.utils.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class BukkitPlayerManager extends PlayerManager {

    public Map<Player,OnlineNetworkPlayer> players;

    public BukkitPlayerManager() {
        this.players = new LinkedHashMap<>();
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(int id) {
        NetworkPlayer player = getPlayer(id);
        if(player != null) return getOnlinePlayer(player.getUUID());
        return null;
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(UUID uuid) {
        return getPlayer(Bukkit.getPlayer(uuid));
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(String name) {
        return getPlayer(Bukkit.getPlayer(name));
    }

    @Override
    public List<OnlineNetworkPlayer> getOnlinePlayers() {
        List<OnlineNetworkPlayer> players = new ArrayList<>();
        for(Player player : Bukkit.getOnlinePlayers()) players.add(getPlayer(player));
        return players;
    }
    public OnlineNetworkPlayer getPlayer(Player player){
        if(player == null) return null;
        OnlineNetworkPlayer onlinePlayer = players.get(player);
        if(onlinePlayer == null){
            onlinePlayer = new BukkitOnlinePlayer(player);
            this.players.put(player,onlinePlayer);
        }
        return onlinePlayer;
    }
    @Override
    public void removeOnlinePlayerFromCache(OnlineNetworkPlayer player) {}

    @Override
    public void removeOnlinePlayerFromCache(UUID uuid) {}

    @Override
    public void updatePlayer(NetworkPlayer player, NetworkPlayerUpdateCause cause, Document properties) {
        BukkitBanSystemBootstrap.getInstance().executePlayerUpdateEvents(player.getUUID(),cause,properties,true);
    }
    @Override
    public void updateOnlinePlayer(OnlineNetworkPlayer player) {}
    @Override
    public int getOnlineCount() {
        return Bukkit.getOnlinePlayers().size();
    }
}
