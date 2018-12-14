package ch.dkrieger.bansystem.lib.player.history.value;

import java.util.UUID;

public class Kick extends HistoryEntry {

    private String server;

    public Kick(String ip, String reason, String message, long timeStamp, int points, int reasonID, UUID staff, String server) {
        super(ip, reason, message, timeStamp, points, reasonID, staff);
        this.server = server;
    }

    public String getServer() {
        return server;
    }
}
