package ch.dkrieger.bansystem.bungeecord.event;

import ch.dkrieger.bansystem.lib.player.history.entry.Kick;

import java.util.UUID;

public class ProxiedNetworkPlayerKickEvent extends ProxiedDKBansEvent{

    public ProxiedNetworkPlayerKickEvent(UUID uuid, long timeStamp,boolean onThisServer) {
        super(uuid, timeStamp,onThisServer);
    }
    public Kick getKick() {
        return getPlayer().getHistory().getLastKick();
    }
}
