package ch.dkrieger.bansystem.bukkit.network;

import ch.dkrieger.bansystem.bukkit.BukkitBanSystemBootstrap;
import ch.dkrieger.bansystem.bukkit.BungeeCordConnection;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.DKNetwork;
import ch.dkrieger.bansystem.lib.JoinMe;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BukkitBungeeCordNetwork implements DKNetwork {

    private BungeeCordConnection connection;

    public BukkitBungeeCordNetwork(BungeeCordConnection connection) {
        this.connection = connection;
    }

    @Override
    public JoinMe getJoinMe(OnlineNetworkPlayer player) {
        return null;
    }

    @Override
    public JoinMe getJoinMe(NetworkPlayer player) {
        return null;
    }

    @Override
    public JoinMe getJoinMe(UUID uuid) {
        return null;
    }

    @Override
    public List<String> getPlayersOnServer(String server) {
        List<String> players = new ArrayList<>();
        for(OnlineNetworkPlayer online : BanSystem.getInstance().getPlayerManager().getOnlinePlayers()){
            players.add(online.getName());
        }
        return players;
    }

    @Override
    public void broadcast(String message) {
        broadcast(new TextComponent(message));
    }

    @Override
    public void broadcast(TextComponent component) {
        connection.send("broadcast",new Document().append("message",component));
    }

    @Override
    public void broadcast(Broadcast broadcast) {
        connection.send("broadcast",new Document().append("broadcast",broadcast));
    }

    @Override
    public void broadcastLocal(Broadcast broadcast) {
        BukkitBanSystemBootstrap.getInstance().sendLocalBroadcast(broadcast);
    }

    @Override
    public void sendJoinMe(JoinMe joinMe) {
        connection.send("sendJoinMe",new Document().append("joinme",joinMe));
    }

    @Override
    public void sendTeamMessage(String message) {
        sendTeamMessage(message,false);
    }

    @Override
    public void sendTeamMessage(String message, boolean onlyLogin) {
        sendTeamMessage(new TextComponent(message));
    }

    @Override
    public void sendTeamMessage(TextComponent component) {
        sendTeamMessage(component,false);
    }

    @Override
    public void sendTeamMessage(TextComponent component, boolean onlyLogin) {
        connection.send("sendTeamMessage",new Document().append("message",component)
                .append("onlyLogin",onlyLogin));
    }

    @Override
    public void reloadFilter() {
        connection.send("reloadFilter",new Document());
        BanSystem.getInstance().getFilterManager().reloadLocal();
    }

    @Override
    public void reloadBroadcast() {
        connection.send("reloadBroadcast",new Document());
        BanSystem.getInstance().getBroadcastManager().reloadLocal();
    }
}
