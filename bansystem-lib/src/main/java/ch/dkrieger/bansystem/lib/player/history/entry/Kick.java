package ch.dkrieger.bansystem.lib.player.history.entry;

import ch.dkrieger.bansystem.lib.utils.Document;

import java.util.UUID;

public class Kick extends HistoryEntry {

    private String server;

    public Kick(UUID uuid, String ip, String reason, String message, long timeStamp, int id, int points, int reasonID, String staff, Document properties, String server) {
        super(uuid, ip, reason, message, timeStamp, id, points, reasonID, staff, properties);
        this.server = server;
    }

    public String getServer() {
        return server;
    }
    @Override
    public String getTypeName() {
        return "Kick";
    }
}
