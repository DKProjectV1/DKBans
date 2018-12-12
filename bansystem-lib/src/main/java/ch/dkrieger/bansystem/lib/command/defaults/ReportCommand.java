package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.config.mode.ReportMode;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.reason.BanReason;
import ch.dkrieger.bansystem.lib.reason.ReportReason;
import ch.dkrieger.bansystem.lib.report.Report;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

public class ReportCommand extends NetworkCommand {

    public ReportCommand() {
        super("report","","dkbans.report");
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 1){//perms user dkrieger
            sendHelp(sender);
            return;
        }
        NetworkPlayer networkSender = sender.getAsNetworkPlayer();
        if(sender.hasPermission("dkbans.report.receive")){
            if(args[0].equalsIgnoreCase("logout")){
                changeLogin(sender,networkSender,false);
                return;
            }
            else if(args[0].equalsIgnoreCase("login")){
                changeLogin(sender,networkSender,true);
                return;
            }
            else if(args[0].equalsIgnoreCase("toggle")){
                changeLogin(sender,networkSender,!networkSender.isReportLogin());
                return;
            }else if(args[0].equalsIgnoreCase("list")){
                sender.executeCommand("/reports");
                return;
            }else if(args[0].equalsIgnoreCase("jump") || args[0].equalsIgnoreCase("goto")) {
                NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
                if(player == null){
                    sender.sendMessage(Messages.PLAYER_NOT_FOUND.replace("[prefix]",getPrefix()));
                    return;
                }
                OnlineNetworkPlayer online = player.getOnlinePlayer();
                if(online == null){
                    sender.sendMessage(Messages.REPORT_NOTFOUND
                            .replace("[player]",player.getColoredName())
                            .replace("[prefix]",getPrefix()));
                    return;
                }
                Report report = player.getOpenReport();
                if(report == null){
                    sender.sendMessage(Messages.REPORT_NOTFOUND
                            .replace("[player]",player.getColoredName())
                            .replace("[prefix]",getPrefix()));
                    return;
                }

                OnlineNetworkPlayer staff = sender.getAsOnlineNetworkPlayer();
                sender.sendMessage(Messages.REPORT_PROCESS
                        .replace("[server]",staff.getServer())
                        .replace("[prefix]",getPrefix()));
                player.processOpenReports(sender.getUUID());
                if(staff.getServer().equalsIgnoreCase(online.getServer())){
                    sender.sendMessage(Messages.SERVER_ALREADY
                            .replace("[server]",staff.getServer())
                            .replace("[prefix]",getPrefix()));
                    return;
                }else staff.connect(online.getServer());
                if(BanSystem.getInstance().getConfig().reportControlls){
                    TextComponent deny = new TextComponent(Messages.REPORT_PROCESS_CONTROL_DENY);
                    deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/report deny "+player.getUUID()));

                    TextComponent accept = new TextComponent(Messages.REPORT_PROCESS_CONTROL_FORREASON);
                    accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/report accept "+player.getUUID()));

                    TextComponent other = new TextComponent(Messages.REPORT_PROCESS_CONTROL_OTHERREASON);
                    other.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/report other "+player.getUUID()));

                    TextComponent message = GeneralUtil.replaceTextComponent(
                            Messages.REPORT_PROCESS_CONTROL_MESSAGE.replace("[prefix]",getPrefix()),"[deny]",deny);
                    message = GeneralUtil.replaceTextComponent(message,"[forReason]",accept);
                    message = GeneralUtil.replaceTextComponent(message,"[otherReason]",other);
                    sender.sendMessage(message);
                }
                return;
            }
        }
        if(args.length < 2){
            sendHelp(sender);
            return;
        }
        NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
        if(player == null){
            sender.sendMessage(Messages.PLAYER_NOT_FOUND.replace("[prefix]",getPrefix()));
            return;
        }
        if(player.hasBypass() && !(sender.hasPermission("dkbans.bypass.ignore"))){
            sender.sendMessage(Messages.BAN_BYPASS
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",player.getColoredName()));
            return;
        }
        OnlineNetworkPlayer online = player.getOnlinePlayer();
        if(online == null){
            sender.sendMessage(Messages.PLAYER_NOT_ONLINE.replace("[prefix]",getPrefix()));
            return;
        }
        Report report = player.getReport(sender.getUUID());
        if(report != null){
            if(report.getTimeStamp()+BanSystem.getInstance().getConfig().reportDelay > System.currentTimeMillis()) {
                sender.sendMessage(Messages.PLAYER_ALREADY_REPORTED
                        .replace("[player]",player.getColoredName())
                        .replace("[prefix]",getPrefix()));
                return;
            }else; //delete old report
        }
        if(BanSystem.getInstance().getConfig().reportMode == ReportMode.SELF){
            player.report(sender.getUUID(),online.getServer(),args[1]);
        }else{
            ReportReason reason = BanSystem.getInstance().getReasonProvider().searchReportReason(args[0]);
            if(reason == null){
                sendHelp(sender);
                return;
            }
            report = player.report(reason,online.getServer(),sender.getUUID());
        }
        if(report == null) return;
        sender.sendMessage(Messages.REPORT_SUCCESS
                .replace("[player]",player.getColoredName())
                .replace("[reason]",report.getReason())
                .replace("[server]",report.getReportedServer())
                .replace("[reasonID]",String.valueOf(report.getReasonID()))
                .replace("[prefix]",getPrefix()));
    }
    private void sendHelp(NetworkCommandSender sender){

    }
    private void changeLogin(NetworkCommandSender sender, NetworkPlayer player, boolean login){
        if(player.isReportLogin() == login){
            sender.sendMessage(Messages.STAFF_STATUS_ALREADY
                    .replace("[status]",(login?Messages.STAFF_STATUS_LOGIN:Messages.STAFF_STATUS_LOGOUT))
                    .replace("[prefix]",getPrefix()));
        }else{
            sender.sendMessage(Messages.STAFF_STATUS_CHANGE
                    .replace("[status]",(login?Messages.STAFF_STATUS_LOGIN:Messages.STAFF_STATUS_LOGOUT))
                    .replace("[prefix]",getPrefix()));
        }
        player.setReportLogin(login);
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
