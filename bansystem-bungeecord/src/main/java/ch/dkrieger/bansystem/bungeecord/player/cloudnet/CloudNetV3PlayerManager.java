package ch.dkrieger.bansystem.bungeecord.player.cloudnet;

import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.NetworkPlayerUpdateCause;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.PlayerManager;
import ch.dkrieger.bansystem.lib.utils.Document;

import java.util.List;
import java.util.UUID;

public class CloudNetV3PlayerManager extends PlayerManager {

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(int id) {
        return null;
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(UUID uuid) {
        return null;
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(String name) {
        return null;
    }

    @Override
    public List<OnlineNetworkPlayer> getOnlinePlayers() {
        return null;
    }

    @Override
    public void removeOnlinePlayerFromCache(OnlineNetworkPlayer player) {

    }

    @Override
    public void removeOnlinePlayerFromCache(UUID uuid) {

    }

    @Override
    public void updatePlayer(NetworkPlayer player, NetworkPlayerUpdateCause cause, Document properties) {

    }

    @Override
    public void updateOnlinePlayer(OnlineNetworkPlayer player) {

    }

    @Override
    public int getOnlineCount() {
       // return;
        return 0;
    }
}
