package ch.dkrieger.bansystem.bukkit.event;

import java.util.UUID;

public class BukkitOnlineNetworkPlayerUpdateEvent extends BukkitNetworkPlayerEvent {

    public BukkitOnlineNetworkPlayerUpdateEvent(UUID uuid, long timeStamp, boolean onThisServer) {
        super(uuid, timeStamp,onThisServer);
    }
}
