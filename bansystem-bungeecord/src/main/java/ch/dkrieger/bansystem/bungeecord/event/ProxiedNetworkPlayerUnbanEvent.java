package ch.dkrieger.bansystem.bungeecord.event;

import ch.dkrieger.bansystem.lib.player.history.entry.Unban;

import java.util.UUID;

public class ProxiedNetworkPlayerUnbanEvent extends ProxiedDKBansEvent{

    public ProxiedNetworkPlayerUnbanEvent(UUID uuid, long timeStamp, boolean onThisServer) {
        super(uuid, timeStamp,onThisServer);
    }
    public Unban getUnban() {
        return getPlayer().getHistory().getLastUnban();
    }
}
