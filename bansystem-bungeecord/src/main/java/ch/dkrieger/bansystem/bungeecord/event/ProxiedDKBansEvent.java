package ch.dkrieger.bansystem.bungeecord.event;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import net.md_5.bungee.api.plugin.Event;

import java.util.UUID;

public class ProxiedDKBansEvent extends Event {

    private UUID uuid;
    private long timeStamp;
    private boolean onThisServer;

    public ProxiedDKBansEvent(UUID uuid, long timeStamp,boolean onThisServer) {
        this.uuid = uuid;
        this.timeStamp = timeStamp;
        this.onThisServer = onThisServer;
    }
    public UUID getUUID() {
        return uuid;
    }
    public long getTimeStamp() {
        return timeStamp;
    }
    public NetworkPlayer getPlayer(){
        return BanSystem.getInstance().getPlayerManager().getPlayer(uuid);
    }
    public boolean isOnThisServer() {
        return onThisServer;
    }

    public OnlineNetworkPlayer getOnlinePlayer(){
        return BanSystem.getInstance().getPlayerManager().getOnlinePlayer(uuid);
    }
}
