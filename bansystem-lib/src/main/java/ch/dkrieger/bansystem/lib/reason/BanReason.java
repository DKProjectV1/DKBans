package ch.dkrieger.bansystem.lib.reason;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.config.mode.BanMode;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.utils.Document;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BanReason extends KickReason {

    private double divider;
    private BanType historyType;
    private Map<Integer, BanReasonEntry> durations;

    public BanReason(int id, int points, String name, String display, String permission, boolean hidden, List<String> aliases, double divider, BanType historyType, Map<Integer, BanReasonEntry> durations) {
        super(id, points, name, display, permission, hidden, aliases);
        this.divider = divider;
        this.historyType = historyType;
        this.durations = durations;
    }
    public BanReason(int id, int points, String name, String display, String permission, boolean hidden, List<String> aliases, double divider, BanType historyType, BanReasonEntry... durations) {
        super(id, points, name, display, permission, hidden, aliases);
        this.divider = divider;
        this.historyType = historyType;
        this.durations = new LinkedHashMap<>();
        for(BanReasonEntry duration : durations) this.durations.put(this.durations.size()+1,duration);
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
    public Map<Integer, BanReasonEntry> getDurations() {
        return durations;
    }
    public BanReasonEntry getDefaultDuration(){
        return getNextDuration(0);
    }
    public BanReasonEntry getNextDuration(NetworkPlayer player){
        return getNextDuration(player.getHistory().getBanCount(getHistoryType()));
    }
    public BanReasonEntry getNextDuration(int bans){
        bans++;
        if(durations.containsKey(bans)) return durations.get(bans);
        else{
            int last = -1;
            BanReasonEntry highest = null;
            for(Map.Entry<Integer, BanReasonEntry> entry : this.durations.entrySet()){
                if(entry.getKey() == bans) return entry.getValue();
                if(entry.getKey() > last && last < bans){
                    highest = entry.getValue();
                    last = entry.getKey();
                }
            }
            return highest;
        }
    }
    public Ban toBan(NetworkPlayer player,String message, String staff){
        int points = 0;
        long timeOut = 0;
        BanType type = null;
        if(BanSystem.getInstance().getConfig().banMode == BanMode.TEMPLATE){
            BanReasonEntry value = getNextDuration(player);
            timeOut = value.getDuration().getTime() > 0?System.currentTimeMillis()+value.getDuration().getMillisTime():-1;
            type = value.getType();
        }else{
            points = getPoints();

        }
        return new Ban(player.getUUID(),player.getIP(),getDisplay(),message,System.currentTimeMillis(),-1,points,getID(),staff,new Document(),timeOut,type);
    }
}
