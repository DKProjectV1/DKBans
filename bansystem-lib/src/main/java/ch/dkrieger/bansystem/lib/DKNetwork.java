package ch.dkrieger.bansystem.lib;

import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

public interface DKNetwork {

    public JoinMe getJoinMe(OnlineNetworkPlayer player);

    public JoinMe getJoinMe(NetworkPlayer player);

    public JoinMe getJoinMe(UUID uuid);

    public void broadcast(String message);

    public void broadcast(TextComponent component);

    public void sendJoinMe(JoinMe joinMe);

    public void sendTeamMessage(String message);

    public void sendTeamMessage(String message, boolean onlyLogin);

    public void sendTeamMessage(TextComponent component);

    public void sendTeamMessage(TextComponent component,boolean onlyLogin);

    public void reloadFilter();

    public void reloadBroadcast();

}
