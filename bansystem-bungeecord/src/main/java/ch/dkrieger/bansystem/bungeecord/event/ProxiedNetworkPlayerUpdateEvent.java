package ch.dkrieger.bansystem.bungeecord.event;

import ch.dkrieger.bansystem.lib.player.NetworkPlayerUpdateCause;

import java.util.UUID;

public class ProxiedNetworkPlayerUpdateEvent extends ProxiedDKBansEvent{

    private NetworkPlayerUpdateCause cause;

    public ProxiedNetworkPlayerUpdateEvent(UUID uuid, long timeStamp,boolean onThisServer, NetworkPlayerUpdateCause cause) {
        super(uuid, timeStamp,onThisServer);
        this.cause = cause;
    }
    public NetworkPlayerUpdateCause getCause() {
        return cause;
    }
}
