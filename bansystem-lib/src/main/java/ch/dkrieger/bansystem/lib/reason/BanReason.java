package ch.dkrieger.bansystem.lib.reason;

import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BanReason extends KickReason {

    private double divider;
    private BanType historyType;
    private Map<Integer,BanReasonValue> durations;

    public BanReason(int id, int points, String name, String display, String permission, boolean hidden, List<String> aliases, double divider, BanType historyType, Map<Integer,BanReasonValue> durations) {
        super(id, points, name, display, permission, hidden, aliases);
        this.divider = divider;
        this.historyType = historyType;
        this.durations = durations;
    }
    public BanReason(int id, int points, String name, String display, String permission, boolean hidden, List<String> aliases,  double divider, BanType historyType,BanReasonValue... durations) {
        super(id, points, name, display, permission, hidden, aliases);
        this.divider = divider;
        this.historyType = historyType;
        this.durations = new LinkedHashMap<>();
        for(BanReasonValue duration : durations) this.durations.put(this.durations.size()+1,duration);
    }
    public double getDivider() {
        return divider;
    }
    public BanType getHistoryType() {
        return historyType;
    }
    public BanType getBanType(){
        return getDefaultDuration().getType();
    }
    public Map<Integer, BanReasonValue> getDurations() {
        return durations;
    }
    public BanReasonValue getDefaultDuration(){

    }
    public BanReasonValue getNextDuration(NetworkPlayer player){

    }
}
