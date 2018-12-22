package ch.dkrieger.bansystem.bungeecord.player;

import ch.dkrieger.bansystem.bungeecord.BungeeCordBanSystemBootstrap;
import ch.dkrieger.bansystem.bungeecord.event.ProxiedNetworkPlayerLoginEvent;
import ch.dkrieger.bansystem.bungeecord.event.ProxiedNetworkPlayerLogoutEvent;
import ch.dkrieger.bansystem.bungeecord.event.ProxiedNetworkPlayerUpdateEvent;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.NetworkPlayerUpdateCause;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.PlayerManager;
import ch.dkrieger.bansystem.lib.utils.Document;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.*;

public class BungeeCordPlayerManager extends PlayerManager {

    private Map<ProxiedPlayer,LocalBungeeCordOnlinePlayer> onlinePlayers;

    public BungeeCordPlayerManager() {
        this.onlinePlayers = new HashMap<>();
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(int id) {
        NetworkPlayer player = getPlayer(id);
        if(player != null) return getOnlinePlayer(player.getUUID());
        return null;
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(UUID uuid) {
        return getOnlinePlayer(BungeeCord.getInstance().getPlayer(uuid));
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(String name) {
        return getOnlinePlayer(BungeeCord.getInstance().getPlayer(name));
    }
    public OnlineNetworkPlayer getOnlinePlayer(ProxiedPlayer player){
        if(player != null){
            LocalBungeeCordOnlinePlayer online = this.onlinePlayers.get(player);
            if(online == null){
                online = new LocalBungeeCordOnlinePlayer(player);
                this.onlinePlayers.put(player,online);
            }
            return online;
        }
        return null;
    }

    @Override
    public List<OnlineNetworkPlayer> getOnlinePlayers() {
        List<OnlineNetworkPlayer> players = new ArrayList<>();
        for(ProxiedPlayer player : BungeeCord.getInstance().getPlayers()) players.add(getOnlinePlayer(player));
        return players;
    }

    @Override
    public void updatePlayer(NetworkPlayer player, NetworkPlayerUpdateCause cause, Document properties) {
        BungeeCordBanSystemBootstrap.getInstance().executePlayerUpdateEvents(player,cause,properties,true);
        //sync spigot11,
    }

    @Override
    public void updateOnlinePlayer(OnlineNetworkPlayer player) {

    }

    @Override
    public void removeOnlinePlayerFromCache(OnlineNetworkPlayer player) {
        removeOnlinePlayerFromCache(player.getUUID());
    }

    @Override
    public void removeOnlinePlayerFromCache(UUID uuid) {
        this.onlinePlayers.remove(BungeeCord.getInstance().getPlayer(uuid));
    }

    @Override
    public int getOnlineCount() {
        return BungeeCord.getInstance().getOnlineCount();
    }
}
