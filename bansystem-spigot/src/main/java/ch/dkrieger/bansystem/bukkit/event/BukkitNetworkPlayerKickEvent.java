package ch.dkrieger.bansystem.bukkit.event;

import ch.dkrieger.bansystem.lib.player.history.entry.Kick;

import java.util.UUID;

public class BukkitNetworkPlayerKickEvent extends BukkitNetworkPlayerEvent {

    public BukkitNetworkPlayerKickEvent(UUID uuid, long timeStamp, boolean onThisServer) {
        super(uuid, timeStamp,onThisServer);
    }
    public Kick getKick() {
        return getPlayer().getHistory().getLastKick();
    }
}
