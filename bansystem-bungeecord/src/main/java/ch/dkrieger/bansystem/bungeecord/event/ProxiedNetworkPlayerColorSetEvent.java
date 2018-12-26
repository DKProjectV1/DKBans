package ch.dkrieger.bansystem.bungeecord.event;

import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;

import java.util.UUID;

public class ProxiedNetworkPlayerColorSetEvent extends ProxiedDKBansEvent{

    private final NetworkPlayer player;
    private String color;

    public ProxiedNetworkPlayerColorSetEvent(UUID uuid, long timeStamp, boolean onThisServer, NetworkPlayer player, String color) {
        super(uuid, timeStamp, onThisServer);
        this.player = player;
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public NetworkPlayer getPlayer() {
        return player;
    }
}
