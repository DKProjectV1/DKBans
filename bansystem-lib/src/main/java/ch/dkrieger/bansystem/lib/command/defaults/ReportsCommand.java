package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.report.Report;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.List;

public class ReportsCommand extends NetworkCommand {

    public ReportsCommand() {
        super("reports","","dkbans.report.receive");
    }

    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        List<Report> reports = BanSystem.getInstance().getReportManager().getReports();
        if(reports.size() <= 0){
            sender.sendMessage(Messages.REPORT_LIST_NO);
            return;
        }
        int page = 0;
        if(args.length > 0 && GeneralUtil.isNumber(args[0])) page = Integer.valueOf(args[0]);

        int from = 1;
        if(page > 1) from = 8 * (page - 1) + 1;
        int to = 8 * page;
        int max = GeneralUtil.getMaxPages(8,reports);

        sender.sendMessage(Messages.REPORT_LIST_HEADER
                .replace("[page]",""+page)
                .replace("[maxPage]",""+max)
                .replace("[nextPage]",(max >= page?"1":""+(page+1)))
                .replace("[prefix]",getPrefix()));
        for(int h = from; h <= to; h++) {
            if (h > reports.size()) break;
            Report report = reports.get(h - 1);
            sender.sendMessage(Messages.REPORT_LIST_LIST
                    .replace("[reason]",report.getReason())
                    .replace("[reasonID]",""+report.getReasonID())
                    .replace("[player]",report.getPlayer().getColoredName())
                    .replace("[reporter]",report.getReporter().getColoredName())
                    .replace("[prefix]",getPrefix()));
        }
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
