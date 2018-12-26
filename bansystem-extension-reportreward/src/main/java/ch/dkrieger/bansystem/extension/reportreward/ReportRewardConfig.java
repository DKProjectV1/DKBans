package ch.dkrieger.bansystem.extension.reportreward;

import ch.dkrieger.bansystem.lib.BanSystem;

public class ReportRewardConfig {


    public int reportRewardCoins;
    public String reportRewardMessage;

    public ReportRewardConfig() {
        reportRewardCoins = BanSystem.getInstance().getConfig().addAndGetIntValue("extension.reportreward.coins",80);
        BanSystem.getInstance().getConfig().save();

        reportRewardMessage = BanSystem.getInstance().getMessageConfig().addAndGetMessageValue("extension.reportreward","[coin-prefix]&7+&6[coins] &7Coins");
        BanSystem.getInstance().getMessageConfig().save();
    }
}
