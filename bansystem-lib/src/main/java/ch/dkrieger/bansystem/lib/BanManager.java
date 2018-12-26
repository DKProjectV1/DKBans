package ch.dkrieger.bansystem.lib;

import ch.dkrieger.bansystem.lib.player.history.entry.Ban;

import java.util.List;

public class BanManager {

    public Ban getBan(int id){
        return BanSystem.getInstance().getStorage().getBan(id);
    }
    @SuppressWarnings("This methode is dangerous, it (can) return many datas and have a long delay.")
    public List<Ban> getBans(){
        return BanSystem.getInstance().getStorage().getBans();
    }
    @SuppressWarnings("This methode is dangerous, it (can) return many datas and have a long delay.")
    public List<Ban> getBans(int reasonID){
        return BanSystem.getInstance().getStorage().getBans(reasonID);
    }
    @SuppressWarnings("This methode is dangerous, it (can) return many datas and have a long delay.")
    public List<Ban> getBans(String reason){
        return BanSystem.getInstance().getStorage().getBans(reason);
    }
    @SuppressWarnings("This methode is dangerous, it (can) return many datas and have a long delay.")
    public List<Ban> getBansFromStaff(String staff) {
        return BanSystem.getInstance().getBanManager().getBansFromStaff(staff);
    }
}
