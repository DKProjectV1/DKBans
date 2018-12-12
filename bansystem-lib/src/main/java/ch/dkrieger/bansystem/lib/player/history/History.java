package ch.dkrieger.bansystem.lib.player.history;

import ch.dkrieger.bansystem.lib.player.history.value.Ban;
import ch.dkrieger.bansystem.lib.player.history.value.HistoryValue;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class History {


    public Map<Integer,HistoryValue> values;


    public int size(){
        return this.values.size();
    }

    public List<HistoryValue> getValues(){
        return new ArrayList<>(this.values.values());
    }
    public HistoryValue getValue(int id){
        return values.get(id);
    }
    public boolean isBanned(){
        return isBanned(null);
    }
    public boolean isBanned(BanType type){

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

    private class Filter {

        private Class<HistoryValue> type;
        private long from, to;
        private int reasonID;
        private String staff, reason, message;

    }
}
