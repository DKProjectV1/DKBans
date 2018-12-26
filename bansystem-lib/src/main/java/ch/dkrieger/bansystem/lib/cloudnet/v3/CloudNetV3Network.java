package ch.dkrieger.bansystem.lib.cloudnet.v3;

import ch.dkrieger.bansystem.lib.DKNetwork;
import ch.dkrieger.bansystem.lib.JoinMe;
import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import de.dytanic.cloudnet.common.document.Document;
import de.dytanic.cloudnet.wrapper.Wrapper;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class CloudNetV3Network implements DKNetwork {

    private Map<UUID,JoinMe> joinmes;

    public CloudNetV3Network() {
        this.joinmes = new HashMap<>();
    }
    public void inserJoinMe(JoinMe joinme){
        this.joinmes.put(joinme.getUUID(),joinme);
    }

    @Override
    public JoinMe getJoinMe(OnlineNetworkPlayer player) {
        return this.joinmes.get(player.getUUID());
    }

    @Override
    public JoinMe getJoinMe(NetworkPlayer player) {
        return this.joinmes.get(player.getUUID());
    }

    @Override
    public JoinMe getJoinMe(UUID uuid) {
        return this.joinmes.get(uuid);
    }

    @Override
    public List<String> getPlayersOnServer(String server) {
        //Wrapper.getInstance().get
        return null;
    }

    @Override
    public void broadcast(String message) {
        broadcast(new TextComponent(message));
    }

    @Override
    public void broadcast(TextComponent component) {
        Wrapper.getInstance().sendChannelMessage("DKBans","broadcast",new Document().append("message",component));
    }

    @Override
    public void broadcast(Broadcast broadcast) {
        Wrapper.getInstance().sendChannelMessage("DKBans","broadcast",new Document().append("message",broadcast));
    }

    @Override
    public void sendJoinMe(JoinMe joinMe) {
        Wrapper.getInstance().sendChannelMessage("DKBans","sendJoinMe",new Document().append("joinme",joinMe));
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
        Wrapper.getInstance().sendChannelMessage("DKBans","sendTeamMessage",new Document().append("message",component)
                .append("onlyLogin",onlyLogin));
    }

    @Override
    public void reloadFilter() {
        Wrapper.getInstance().sendChannelMessage("DKBans","reloadFilter",new Document());
    }

    @Override
    public void reloadBroadcast() {
        Wrapper.getInstance().sendChannelMessage("DKBans","reloadBroadcast",new Document());
    }
}
