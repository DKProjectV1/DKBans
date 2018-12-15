package ch.dkrieger.bansystem.lib.report;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.List;

public class ReportManager {

    private List<Report> cachedReports;

    public List<Report> getReports(){
        if(this.cachedReports == null) loadReports();
        return this.cachedReports;
    }
    public List<Report> getOpenReports(){
        return GeneralUtil.iterateAcceptedReturn(this.cachedReports, object -> object.getStaff() == null);
    }
    @SuppressWarnings("Use The methode player.report(...) to report a player (this is only to save and send the report")
    public void report(Report report){

    }
    public void processOpenReports(NetworkPlayer player,NetworkPlayer staff){
        GeneralUtil.iterateForEach(this.cachedReports, object -> object.setStaff(staff.getUUID()));
        GeneralUtil.iterateForEach(player.getReports(), object -> object.setStaff(staff.getUUID()));
        BanSystem.getInstance().getStorage().processReports(player.getUUID(),staff.getUUID());
    }
    public void deleteReports(NetworkPlayer player){
        player.getReports().clear();
        BanSystem.getInstance().getStorage().deleteReports(player.getUUID());
    }
    public void loadReports(){
        this.cachedReports = BanSystem.getInstance().getStorage().getReports();
    }
    public void clearCachedReports(){
        this.cachedReports = null;
    }
}
