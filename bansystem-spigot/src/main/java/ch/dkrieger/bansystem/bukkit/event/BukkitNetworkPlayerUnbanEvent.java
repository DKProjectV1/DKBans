package ch.dkrieger.bansystem.bukkit.event;

import ch.dkrieger.bansystem.lib.player.history.entry.Unban;

import java.util.UUID;

public class BukkitNetworkPlayerUnbanEvent extends BukkitNetworkPlayerEvent {

    public BukkitNetworkPlayerUnbanEvent(UUID uuid, long timeStamp, boolean onThisServer) {
        super(uuid, timeStamp,onThisServer);
    }
    public Unban getUnban() {
        return getPlayer().getHistory().getLastUnban();
    }
}
