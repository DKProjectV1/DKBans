package ch.dkrieger.bansystem.lib.player.chatlog;

import ch.dkrieger.bansystem.lib.filter.FilterType;

import java.util.UUID;

public class ChatLogEntry {

    private UUID uuid;
    private String message, server;
    private long time;
    private FilterType filtered;

    public ChatLogEntry(UUID uuid, String message, String server, long time, FilterType filtered) {
        this.uuid = uuid;
        this.message = message;
        this.server = server;
        this.time = time;
        this.filtered = filtered;
    }

    public UUID getUUID() {
        return uuid;
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

    public FilterType getFiltered() {
        return filtered;
    }
}
