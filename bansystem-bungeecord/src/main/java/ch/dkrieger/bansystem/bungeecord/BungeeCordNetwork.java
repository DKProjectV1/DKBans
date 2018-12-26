package ch.dkrieger.bansystem.bungeecord;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.DKNetwork;
import ch.dkrieger.bansystem.lib.JoinMe;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

import java.util.*;

public class BungeeCordNetwork implements DKNetwork {

    private Map<UUID,JoinMe> joinme;
    private SubServerConnection connection;

    public BungeeCordNetwork(SubServerConnection connection) {
        this.connection = connection;
        this.joinme = new LinkedHashMap<>();
    }

    @Override
    public JoinMe getJoinMe(OnlineNetworkPlayer player) {
        return getJoinMe(player.getUUID());
    }
    @Override
    public JoinMe getJoinMe(NetworkPlayer player) {
        return getJoinMe(player.getUUID());
    }
    @Override
    public JoinMe getJoinMe(UUID uuid) {
        return this.joinme.get(uuid);
    }
    @Override
    public void broadcast(String message) {
        broadcast(new TextComponent(message));
    }
    @Override
    public void broadcast(TextComponent component) {
        BungeeCord.getInstance().broadcast(component);
    }

    @Override
    public void broadcast(Broadcast broadcast) {
        broadcastLocal(broadcast);
    }

    @Override
    public void broadcastLocal(Broadcast broadcast) {
        if(broadcast == null) return;
        for(ProxiedPlayer player : BungeeCord.getInstance().getPlayers()){
            if(broadcast.getPermission() == null || broadcast.getPermission().length() == 0|| player.hasPermission(broadcast.getPermission())){
                NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
                player.sendMessage(GeneralUtil.replaceTextComponent(Messages.BROADCAST_FORMAT_SEND.replace("[prefix]",Messages.PREFIX_NETWORK)
                        ,"[message]",broadcast.build(networkPlayer)));
            }
        }
    }

    @Override
    public void sendJoinMe(JoinMe joinMe) {
        this.joinme.put(joinMe.getUUID(),joinMe);
        List<TextComponent> components = joinMe.create();
        for(ProxiedPlayer player : BungeeCord.getInstance().getPlayers()){
            for(TextComponent component : components) player.sendMessage(component);
        }
    }
    @Override
    public void sendTeamMessage(String message) {
        sendTeamMessage(new TextComponent(message));
    }
    @Override
    public void sendTeamMessage(String message, boolean onlyLogin) {
        sendTeamMessage(new TextComponent(message),onlyLogin);
    }
    @Override
    public void sendTeamMessage(TextComponent component) {
        sendTeamMessage(component,false);
    }
    @Override
    public void sendTeamMessage(TextComponent component, boolean onlyLogin) {
        for(ProxiedPlayer player : BungeeCord.getInstance().getPlayers()){
            if(player.hasPermission("dkbans.team")){//set right permission
                NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
                if(networkPlayer != null && (!onlyLogin || networkPlayer.isTeamChatLoggedIn())) player.sendMessage(component);
            }
        }
    }
    @Override
    public List<String> getPlayersOnServer(String server) {
        ServerInfo serverInfo = BungeeCord.getInstance().getServerInfo(server);
        List<String> players = new ArrayList<>();
        if(serverInfo != null) for(ProxiedPlayer player : serverInfo.getPlayers()) players.add(player.getName());
        return players;
    }
    @Override
    public void reloadFilter() {
        BanSystem.getInstance().getFilterManager().reloadLocal();
        connection.sendToAll("reloadFilter",new Document());
    }
    @Override
    public void reloadBroadcast() {
        BanSystem.getInstance().getBroadcastManager().reloadLocal();
        connection.sendToAll("reloadBroadcast",new Document());
    }
}
