package ch.dkrieger.bansystem.lib.player.history;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.HistoryEntry;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HistoryManager {

    private List<Ban> activeBans;

    public NetworkPlayer getPlayerByHistoryEntry(int id){
        HistoryEntry entry = getHistoryEntry(id);
        if(entry != null) return entry.getPlayer();
        return null;
    }
    public NetworkPlayer getPlayerByBan(int id){
        HistoryEntry entry = getHistoryEntry(id);
        if(entry instanceof Ban) return entry.getPlayer();
        return null;
    }
    public HistoryEntry getHistoryEntry(int id){
        return BanSystem.getInstance().getStorage().getHistoryEntry(id);
    }

    @SuppressWarnings("This methode is dangerous, it (can) return many datas and have a long delay.")
    public List<Ban> getActiveBans(BanType type){
        if(this.activeBans == null) activeBans = BanSystem.getInstance().getStorage().getNotTimeOutedBans();
        GeneralUtil.iterateAndRemove(activeBans, object -> {
            History history = object.getPlayer().getHistory();
            return !history.getBan(type).equals(object);
        });
        return this.activeBans;
    }
    @SuppressWarnings("This methode is dangerous, it (can) return many datas and have a long delay.")
    public List<Ban> getActiveBans(BanType type, String reason){
        return GeneralUtil.iterateAcceptedReturn(getActiveBans(type), object -> object.getReason().equalsIgnoreCase(reason));
    }
    @SuppressWarnings("This methode is dangerous, it (can) return many datas and have a long delay.")
    public List<Ban> getActiveBans(BanType type,int reasonID){
        return GeneralUtil.iterateAcceptedReturn(getActiveBans(type), object -> object.getID() == reasonID);
    }
    @SuppressWarnings("This methode is dangerous, it (can) return many datas and have a long delay.")
    public List<Ban> getActiveBans(BanType type,UUID staff){
        return GeneralUtil.iterateAcceptedReturn(getActiveBans(type), object -> object.getStaff().equals(staff.toString()));
    }
    public void clearCache(){
        this.activeBans = null;
    }
}
