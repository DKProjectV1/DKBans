package ch.dkrieger.bansystem.bungeecord.event;

import java.util.UUID;

public class ProxiedOnlineNetworkPlayerUpdateEvent extends ProxiedDKBansEvent{

    public ProxiedOnlineNetworkPlayerUpdateEvent(UUID uuid, long timeStamp,boolean onThisServer) {
        super(uuid, timeStamp,onThisServer);
    }
}
