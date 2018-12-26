package ch.dkrieger.bansystem.bukkit.event;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class BukkitNetworkPlayerEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private UUID uuid;
    private long timeStamp;
    private boolean onThisServer;

    public BukkitNetworkPlayerEvent(UUID uuid, long timeStamp, boolean onThisServer) {
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
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
