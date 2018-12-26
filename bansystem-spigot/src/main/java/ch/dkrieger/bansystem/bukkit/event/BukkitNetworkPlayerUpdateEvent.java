package ch.dkrieger.bansystem.bukkit.event;

import ch.dkrieger.bansystem.lib.player.NetworkPlayerUpdateCause;

import java.util.UUID;

public class BukkitNetworkPlayerUpdateEvent extends BukkitNetworkPlayerEvent {

    private NetworkPlayerUpdateCause cause;

    public BukkitNetworkPlayerUpdateEvent(UUID uuid, long timeStamp, boolean onThisServer, NetworkPlayerUpdateCause cause) {
        super(uuid, timeStamp,onThisServer);
        this.cause = cause;
    }
    public NetworkPlayerUpdateCause getCause() {
        return cause;
    }
}
