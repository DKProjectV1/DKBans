package ch.dkrieger.bansystem.lib.player.history;

import ch.dkrieger.bansystem.lib.player.history.value.Ban;
import ch.dkrieger.bansystem.lib.player.history.value.HistoryEntry;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class History {


    public Map<Integer, HistoryEntry> values;


    public int size(){
        return this.values.size();
    }

    public List<HistoryEntry> getEntries(){
        return new ArrayList<>(this.values.values());
    }
    public HistoryEntry getEntry(int id){
        return values.get(id);
    }
    public boolean isBanned(){
        return isBanned(null);
    }
    public boolean isBanned(BanType type){

    }
    public Ban getBan(){
        return getBan(null);
    }
    public Ban getBan(BanType type){

    }

    public List<Ban> getBans(){

    }
    public List<Ban> getBans(int reasonID){

    }
    public List<Ban> getBans(String reason){

    }
    public List<Ban> getBans(UUID staff){
        return getBan(staff.toString());
    }
    public List<Ban> getBans(String staff){

    }
    public List<Ban> getBans(BanType type, long from, long to){

    }
    public List<HistoryEntry> getEntries(Filter filter){
        return GeneralUtil.iterateAcceptedReturn(this.values.values(),filter::accepted);
    }

    private class Filter {

        private Class<HistoryEntry> type;
        private long from, to, minDuration, maxDuration;
        private int reasonID;
        private String staff, reason, message, ip;

        public boolean accepted(HistoryEntry entry){
        }

    }
}
