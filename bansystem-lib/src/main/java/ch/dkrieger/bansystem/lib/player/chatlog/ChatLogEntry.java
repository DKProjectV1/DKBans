package ch.dkrieger.bansystem.lib.player.chatlog;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.filter.FilterType;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;

import java.util.UUID;

public class ChatLogEntry {

    private UUID uuid;
    private String message, server;
    private long time;
    private FilterType filter;

    public ChatLogEntry(UUID uuid, String message, String server, long time, FilterType filter) {
        this.uuid = uuid;
        this.message = message;
        this.server = server;
        this.time = time;
        this.filter = filter;
    }

    public UUID getUUID() {
        return uuid;
    }
    public NetworkPlayer getPlayer(){
        return BanSystem.getInstance().getPlayerManager().getPlayer(this.uuid);
    }

    public String getMessage() {
        return message;
    }

    public String getServer() {
        return server;
    }

    public long getTime() {
        return time;
    }

    public FilterType getFilter() {
        return filter;
    }

    public boolean isBlocked(){
        return this.filter != null;
    }
}
