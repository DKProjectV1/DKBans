package ch.dkrieger.bansystem.bukkit.event;

import ch.dkrieger.bansystem.lib.player.NetworkPlayer;

import java.util.UUID;

public class BukkitNetworkPlayerColorSetEvent extends BukkitNetworkPlayerEvent {

    private final NetworkPlayer player;
    private String color;

    public BukkitNetworkPlayerColorSetEvent(UUID uuid, long timeStamp, boolean onThisServer, NetworkPlayer player, String color) {
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
