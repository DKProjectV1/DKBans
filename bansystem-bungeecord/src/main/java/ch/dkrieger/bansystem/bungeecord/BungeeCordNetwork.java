package ch.dkrieger.bansystem.bungeecord;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.DKNetwork;
import ch.dkrieger.bansystem.lib.JoinMe;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class BungeeCordNetwork implements DKNetwork {

    private Map<UUID,JoinMe> joinme;

    public BungeeCordNetwork() {
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
    public JoinMe getJoinMe(UUID player) {
        return this.joinme.get(player);
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
    public void sendJoinMe(JoinMe joinMe) {
        this.joinme.put(joinMe.getPlayerUUID(),joinMe);
        //send joinme
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
    public void reloadFilter() {
        BanSystem.getInstance().getFilterManager().reloadLocal();
    }
    @Override
    public void reloadBroadcast() {
        BanSystem.getInstance().getBroadcastManager().reloadLocal();
    }
}
