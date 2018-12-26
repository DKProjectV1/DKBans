package ch.dkrieger.bansystem.bukkit.event;

import java.util.UUID;

public class BukkitNetworkPlayerLoginEvent extends BukkitNetworkPlayerEvent {

    public BukkitNetworkPlayerLoginEvent(UUID uuid, long timeStamp, boolean onThisServer) {
        super(uuid, timeStamp,onThisServer);
    }
}
