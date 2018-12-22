package ch.dkrieger.bansystem.bungeecord.event;

import java.util.UUID;

public class ProxiedNetworkPlayerLoginEvent extends ProxiedDKBansEvent{

    public ProxiedNetworkPlayerLoginEvent(UUID uuid, long timeStamp,boolean onThisServer) {
        super(uuid, timeStamp,onThisServer);
    }
}
