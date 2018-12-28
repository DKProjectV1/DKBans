package ch.dkrieger.bansystem.lib.report;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.ArrayList;
import java.util.List;

public class ReportManager {

    private List<Report> cachedReports;

    public ReportManager() {
        this.cachedReports = new ArrayList<>();
    }
    public List<Report> getReports(){
        if(this.cachedReports == null) loadReports();
        return this.cachedReports;
    }
    public List<Report> getOpenReports(){
        if(this.cachedReports == null) loadReports();
        return GeneralUtil.iterateAcceptedReturn(this.cachedReports, object -> object.getStaff() == null);
    }
    public void loadReports(){
        this.cachedReports = BanSystem.getInstance().getStorage().getReports();
    }
    public void clearCachedReports(){
        this.cachedReports = null;
    }
}
