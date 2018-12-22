package ch.dkrieger.bansystem.bungeecord.event;

import ch.dkrieger.bansystem.lib.player.history.entry.Ban;

import java.util.UUID;

public class ProxiedNetworkPlayerBanEvent extends ProxiedDKBansEvent{

    private final Ban ban;

    public ProxiedNetworkPlayerBanEvent(UUID uuid, long timeStamp, boolean onThisServer, Ban ban) {
        super(uuid, timeStamp,onThisServer);
        this.ban = ban;
    }

    public Ban getBan() {
        return ban;
    }
}
