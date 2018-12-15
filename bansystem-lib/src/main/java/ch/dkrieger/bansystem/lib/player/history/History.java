package ch.dkrieger.bansystem.lib.player.history;

import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.HistoryEntry;
import ch.dkrieger.bansystem.lib.player.history.entry.Unban;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class History {


    public Map<Integer,HistoryEntry> entries;

    public int size(){
        return this.entries.size();
    }
    public  Map<Integer, HistoryEntry> getRawEntries(){
        return this.entries;
    }
    public List<HistoryEntry> getEntries(){
        return new ArrayList<>(this.entries.values());
    }
    public HistoryEntry getEntry(int id){
        return entries.get(id);
    }
    public int getBanCount(){
        return getBanCount(null);
    }
    public int getBanCount(BanType type){
        return getBans(type).size();
    }
    public boolean isBanned(){
        return isBanned(null);
    }
    public boolean isBanned(BanType type){
        return getBan(type) != null;
    }
    public Ban getBan(){
        return getBan(null);
    }
    public Ban getBan(BanType type){
        List<Ban> bans = new ArrayList<>();
        List<Unban> unbans = new ArrayList<>();
        GeneralUtil.iterateAcceptedForEach(this.entries.values(), object ->
                        (object instanceof Ban && (type == null || ((Ban) object).getBanType().equals(type))
                        && ((Ban) object).getTimeOut() <= System.currentTimeMillis()) || (object instanceof Unban
                        && (type == null || ((Unban) object).getBanType().equals(type)))
                , object -> {
                    if(object instanceof Ban) bans.add((Ban)object);
                    else unbans.add((Unban)object);
                });
        if(bans.size() > 0 && bans.size() < unbans.size()){
            final Ban[] ban = new Ban[1];
            final long[] timeStamp = {0};
            GeneralUtil.iterateAcceptedForEach(bans,object -> object.getTimeStamp() > timeStamp[0], object -> {
                ban[0] = object;
                timeStamp[0] = object.getTimeStamp();
            });
            return ban[0];
        }
        return null;
    }
    public List<Ban> getBans(){
        return getBans((BanType)null);
    }
    public List<Ban> getBans(BanType type){
        List<Ban> bans = new ArrayList<>();
        GeneralUtil.iterateAcceptedForEach(this.entries.values()
                ,object -> object instanceof Ban && (type == null || ((Ban) object).getBanType().equals(type))
                ,object -> bans.add((Ban)object));
        return bans;
    }
    public List<Ban> getBans(int reasonID){
        List<Ban> bans = new ArrayList<>();
        GeneralUtil.iterateAcceptedForEach(this.entries.values(),object -> object.getID() == reasonID,object -> bans.add((Ban)object));
        return bans;
    }
    public List<Ban> getBans(String reason){
        List<Ban> bans = new ArrayList<>();
        GeneralUtil.iterateAcceptedForEach(this.entries.values(),object -> object.getReason().equalsIgnoreCase(reason),object -> bans.add((Ban)object));
        return bans;
    }
    public List<HistoryEntry> getEntries(Filter filter){
        return GeneralUtil.iterateAcceptedReturn(this.entries.values(),filter::accepted);
    }
    private class Filter {

        private Class<HistoryEntry> type;
        private long from, to;
        private int reasonID;
        private String staff, reason, message, ip;

        public Filter(Class<HistoryEntry> type, long from, long to, int reasonID, String staff, String reason, String message, String ip) {
            this.type = type;
            this.from = from;
            this.to = to;
            this.reasonID = reasonID;
            this.staff = staff;
            this.reason = reason;
            this.message = message;
            this.ip = ip;
        }
        public boolean accepted(HistoryEntry entry){
            return (type == null || entry.getClass() == type)
                    && (reasonID < 1 || entry.getReasonID() == reasonID)
                    && (reason == null || entry.getReason().equalsIgnoreCase(reason))
                    && (message == null || entry.getMessage().equalsIgnoreCase(message))
                    && (ip == null || entry.getIp().equalsIgnoreCase(ip))
                    && (staff == null || entry.getStaff().equalsIgnoreCase(staff))
                    && (from <= 0 || entry.getTimeStamp() >= from)
                    && (to <= 0 || entry.getTimeStamp() <= to);
        }
    }
}
