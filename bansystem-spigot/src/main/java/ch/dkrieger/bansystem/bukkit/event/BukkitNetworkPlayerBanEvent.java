package ch.dkrieger.bansystem.bukkit.event;

import ch.dkrieger.bansystem.lib.player.history.entry.Ban;

import java.util.UUID;

public class BukkitNetworkPlayerBanEvent extends BukkitNetworkPlayerEvent {

    private final Ban ban;

    public BukkitNetworkPlayerBanEvent(UUID uuid, long timeStamp, boolean onThisServer, Ban ban) {
        super(uuid, timeStamp,onThisServer);
        this.ban = ban;
    }

    public Ban getBan() {
        return ban;
    }
}
