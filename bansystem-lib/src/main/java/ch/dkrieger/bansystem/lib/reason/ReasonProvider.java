package ch.dkrieger.bansystem.lib.reason;

import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import java.util.List;

public class ReasonProvider {

    private List<KickReason> kickReasons;
    private List<BanReason> banReasons;
    private List<ReportReason> reportReasons;
    private List<UnbanReason> unbanReasons;

    public ReasonProvider(List<KickReason> kickReasons, List<BanReason> banReasons, List<ReportReason> reportReasons, List<UnbanReason> unbanReasons) {
        this.kickReasons = kickReasons;
        this.banReasons = banReasons;
        this.reportReasons = reportReasons;
        this.unbanReasons = unbanReasons;
    }
    public List<KickReason> getKickReasons() {
        return kickReasons;
    }
    public List<BanReason> getBanReasons() {
        return banReasons;
    }
    public List<ReportReason> getReportReasons() {
        return reportReasons;
    }
    public List<UnbanReason> getUnbanReasons() {
        return unbanReasons;
    }


    public KickReason searchKickReason(String search){
        if(GeneralUtil.isNumber(search)) return getKickReason(Integer.valueOf(search));
        return getKickReason(search);
    }
    public KickReason getKickReason(int id){
      return GeneralUtil.iterateOne(this.kickReasons,reason -> reason.getID() == id);
    }
    public KickReason getKickReason(String name){
        return GeneralUtil.iterateOne(this.kickReasons,reason -> reason.hasAlias(name));
    }

    public BanReason searchBanReason(String search){
        if(GeneralUtil.isNumber(search)) return getBanReason(Integer.valueOf(search));
        return getBanReason(search);
    }

    public BanReason getBanReason(int id){
        return GeneralUtil.iterateOne(this.banReasons,reason -> reason.getID() == id);
    }


    public BanReason getBanReason(String name){
        return GeneralUtil.iterateOne(this.banReasons,reason -> reason.hasAlias(name));
    }

    public ReportReason searchReportReason(String search){
        if(GeneralUtil.isNumber(search)) return getReportReason(Integer.valueOf(search));
        return getReportReason(search);
    }

    public ReportReason getReportReason(int id){
        return GeneralUtil.iterateOne(this.reportReasons,reason -> reason.getID() == id);
    }
    public ReportReason getReportReason(String name){
        return GeneralUtil.iterateOne(this.reportReasons,reason -> reason.hasAlias(name));
    }
}
