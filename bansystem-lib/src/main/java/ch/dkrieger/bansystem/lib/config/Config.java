package ch.dkrieger.bansystem.lib.config;

import ch.dkrieger.bansystem.lib.config.mode.BanMode;
import ch.dkrieger.bansystem.lib.config.mode.ReportMode;
import ch.dkrieger.bansystem.lib.config.mode.UnbanMode;
import ch.dkrieger.bansystem.lib.utils.TabCompleteOption;

import java.util.List;

public class Config {

    public BanMode banMode;
    public UnbanMode unbanMode;

    public boolean joinMeEnabled;
    public int joinMeCooldown;
    public int joinMeTimeOut;

    public boolean reportEnabled;
    public boolean reportControls;
    public ReportMode reportMode;
    public long reportDelay;

    public boolean onJoinChatClear;
    public boolean onJoinTeamChatInfo;
    public boolean onJoinReportInfo;
    public boolean onJoinReportSize;

    public boolean tabCompleteBlockEnabled;
    public List<TabCompleteOption> tabCompleteOptions;

    public boolean chatBlockPlugin;
    public long chatDelay;

}
