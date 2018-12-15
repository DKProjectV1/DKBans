package ch.dkrieger.bansystem.lib.player.history.entry;

import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.utils.Document;

import java.util.UUID;

public class Unban extends HistoryEntry {

    private BanType banType;

    public Unban(UUID uuid, String ip, String reason, String message, long timeStamp, int id, int points, int reasonID, String staff, Document properties, BanType type) {
        super(uuid, ip, reason, message, timeStamp, id, points, reasonID, staff, properties);
        this.banType = type;
    }

    public BanType getBanType() {
        return banType;
    }

    @Override
    public String getTypeName() {
        return "Unban";
    }
}
