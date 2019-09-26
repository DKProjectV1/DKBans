/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 26.09.19, 20:38
 * @Website https://github.com/DevKrieger/DKBans
 *
 * The DKBans Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.report.Report;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

public class ReportsCommand extends NetworkCommand {

    public ReportsCommand() {
        super("reports","","dkbans.report.receive");
        setPrefix(Messages.PREFIX_REPORT);
    }

    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        List<Report> reports = BanSystem.getInstance().getReportManager().getOpenReports();
        if(reports.size() <= 0){
            sender.sendMessage(Messages.REPORT_LIST_NO .replace("[prefix]",getPrefix()));
            return;
        }
        int page = 1;
        if(args.length > 0 && GeneralUtil.isNumber(args[0])) page = Integer.parseInt(args[0]);

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
            TextComponent component = new TextComponent(Messages.REPORT_LIST_LIST
                    .replace("[reason]",report.getReason())
                    .replace("[reasonID]",""+report.getReasonID())
                    .replace("[player]",report.getPlayer().getColoredName())
                    .replace("[reporter]",report.getReporter().getColoredName())
                    .replace("[prefix]",getPrefix()));
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(Messages.REPORT_MESSAGE_HOVER
                    .replace("[prefix]",Messages.PREFIX_REPORT)
                    .replace("[reason]",report.getReason())
                    .replace("[server]",report.getReportedServer())
                    .replace("[message]",report.getMessage())
                    .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(report.getTimeStamp()))
                    .replace("[reporter]",report.getReporter().getColoredName())
                    .replace("[player]",report.getPlayer().getColoredName())).create()));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/report take "+report.getUUID()));
            sender.sendMessage(component);
        }
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
