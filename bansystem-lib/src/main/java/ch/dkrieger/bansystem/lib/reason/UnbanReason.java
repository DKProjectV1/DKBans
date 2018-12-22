package ch.dkrieger.bansystem.lib.reason;

import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Unban;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.Duration;

import java.util.List;

public class UnbanReason extends KickReason{

    private Duration maxDuration;
    private int maxPoints;
    private List<Integer> notForBanID;
    private Duration removeDuration;
    private double durationDivider;

    public UnbanReason(int id, int points, String name, String display, String permission, boolean hidden, List<String> aliases, Duration maxDuration, int maxPoints, List<Integer> notForBanID, Duration removeDuration, double durationDivider) {
        super(id, points, name, display, permission, hidden, aliases);
        this.maxDuration = maxDuration;
        this.maxPoints = maxPoints;
        this.notForBanID = notForBanID;
        this.removeDuration = removeDuration;
        this.durationDivider = durationDivider;
    }


    public Duration getMaxDuration() {
        return maxDuration;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public List<Integer> getNotForBanID() {
        return notForBanID;
    }
    public Unban toUnban(BanType type, NetworkPlayer player,String message, String staff){
        return new Unban(player.getUUID(),player.getIP(),getDisplay(),message,System.currentTimeMillis(),-1,getPoints(),getID(),staff,new Document(),type);
    }
}
