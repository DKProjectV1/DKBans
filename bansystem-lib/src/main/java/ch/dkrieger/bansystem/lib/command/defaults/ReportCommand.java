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
import java.util.concurrent.TimeUnit;

public class ReportCommand extends NetworkCommand {

    public ReportCommand() {
        super("report","","dkbans.report");
        setPrefix(Messages.PREFIX_REPORT);
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
            }else if(args[0].equalsIgnoreCase("deny")){
                NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[1]);
                if(player == null || !(sender.getUUID().equals(player.getReportStaff()))){
                    sender.sendMessage(Messages.REPORT_NOTFOUND
                            .replace("[player]",player==null?args[1]:player.getColoredName())
                            .replace("[prefix]",getPrefix()));
                    return;
                }
                sender.sendMessage(Messages.REPORT_DENIED_STAFF
                        .replace("[player]",player.getColoredName())
                        .replace("[prefix]",getPrefix()));
                player.denyReports();
                return;
            }else if(args[0].equalsIgnoreCase("accept")){
                NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[1]);
                if(player == null || !(sender.getUUID().equals(player.getReportStaff()))){
                    sender.sendMessage(Messages.REPORT_NOTFOUND
                            .replace("[player]",player==null?args[1]:player.getColoredName())
                            .replace("[prefix]",getPrefix()));
                    return;
                }
                Report report = player.getProcessingReport();
                ReportReason reason = BanSystem.getInstance().getReasonProvider().getReportReason(report.getReasonID());
                sender.executeCommand("ban "+report.getUUID()+" "+reason.getForBan());
                return;
            }else if(args[0].equalsIgnoreCase("other")){
                NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[1]);
                if(player == null || !(sender.getUUID().equals(player.getReportStaff()))){
                    sender.sendMessage(Messages.REPORT_NOTFOUND
                            .replace("[player]",player==null?args[1]:player.getColoredName())
                            .replace("[prefix]",getPrefix()));
                    return;
                }
                sender.sendMessage(Messages.REPORT_OTHERREASON
                        .replace("[player]",player.getColoredName())
                        .replace("[prefix]",getPrefix()));
                for(BanReason reason : BanSystem.getInstance().getReasonProvider().getBanReasons()){
                    if(!sender.hasPermission(reason.getPermission())) continue;
                    TextComponent component = new TextComponent(Messages.BAN_HELP_REASON
                            .replace("[prefix]",getPrefix())
                            .replace("[id]",""+reason.getID())
                            .replace("[name]",reason.getDisplay())
                            .replace("[historyType]",reason.getHistoryType().getDisplay())
                            .replace("[banType]",reason.getBanType().getDisplay())
                            .replace("[reason]",reason.getDisplay())
                            .replace("[points]",""+reason.getPoints()));
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/ban "+player.getUUID()+" "+reason.getID()));
                    sender.sendMessage(component);
                }
                return;
            }else if(args[0].equalsIgnoreCase("jump") || args[0].equalsIgnoreCase("goto") || args[0].equalsIgnoreCase("take")) {
                NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[1]);
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
                Report report = player.getOpenReportWhenNoProcessing();
                if(report == null){
                    sender.sendMessage(Messages.REPORT_NOTFOUND
                            .replace("[player]",player.getColoredName())
                            .replace("[prefix]",getPrefix()));
                    return;
                }
                OnlineNetworkPlayer staff = sender.getAsOnlineNetworkPlayer();
                player.processOpenReports(sender.getAsNetworkPlayer());
                if(staff.getServer().equalsIgnoreCase(online.getServer())){
                    sender.sendMessage(Messages.SERVER_ALREADY
                            .replace("[server]",staff.getServer())
                            .replace("[prefix]",getPrefix()));
                }else staff.connect(online.getServer());
                sender.sendMessage(Messages.REPORT_PROCESS
                        .replace("[player]",player.getColoredName())
                        .replace("[message]",report.getMessage())
                        .replace("[reason]",report.getReason())
                        .replace("[reporter]",report.getReporter().getColoredName())
                        .replace("[server]",staff.getServer())
                        .replace("[prefix]",getPrefix()));
                if(BanSystem.getInstance().getConfig().reportControls){
                    TextComponent deny = new TextComponent(Messages.REPORT_PROCESS_CONTROL_DENY);
                    deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/report deny "+player.getUUID()));

                    ReportReason reportReason = BanSystem.getInstance().getReasonProvider().getReportReason(report.getReasonID());
                    BanReason reason = null;
                    if(reportReason != null) reason =  BanSystem.getInstance().getReasonProvider().getBanReason(reportReason.getForBan());

                    TextComponent accept = new TextComponent(Messages.REPORT_PROCESS_CONTROL_FORREASON
                            .replace("[reason]",reason==null?"Unknown":reason.getDisplay()));
                    accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/report accept "+player.getUUID()));

                    TextComponent other = new TextComponent(Messages.REPORT_PROCESS_CONTROL_OTHERREASON);
                    other.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/report other "+player.getUUID()));

                    TextComponent message = GeneralUtil.replaceTextComponent(
                            Messages.REPORT_PROCESS_CONTROL_MESSAGE.replace("[prefix]",getPrefix()),"[deny]",deny);
                    message = GeneralUtil.replaceTextComponent(message,"[forReason]",accept);
                    message = GeneralUtil.replaceTextComponent(message,"[otherReason]",other);
                    sender.sendMessage(message);
                }
                if(BanSystem.getInstance().getConfig().reportAutoCommandExecuteOnProxy){
                    for(String command :  BanSystem.getInstance().getConfig().reportAutoCommandEnter) sender.executeCommand(command.replace("[player]",player.getName()));
                }else{
                    BanSystem.getInstance().getPlatform().getTaskManager().runTaskLater(()->{
                        for(String command :  BanSystem.getInstance().getConfig().reportAutoCommandEnter) sender.executeCommandOnServer(command.replace("[player]",player.getName()));
                    },1L, TimeUnit.SECONDS);
                }
                return;
            }
        }
        if(args.length < 2){
            sendHelp(sender);
            return;
        }
        if(sender.getName().equalsIgnoreCase(args[0])){
            sender.sendMessage(Messages.REPORT_SELF.replace("[prefix]",getPrefix()));
            return;
        }
        NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
        if(player == null){
            sender.sendMessage(Messages.PLAYER_NOT_FOUND.replace("[prefix]",getPrefix()));
            return;
        }
        if(player.hasBypass() && !(sender.hasPermission("dkbans.bypass.ignore"))){
            sender.sendMessage(Messages.REPORT_BYPASS
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
        String message = "";
        for(int i = 2;i < args.length;i++) message += args[i]+" ";

        if(BanSystem.getInstance().getConfig().reportMode == ReportMode.SELF){
            report = player.report(sender.getUUID(),message,args[1],-1,online.getServer());
        }else{
            ReportReason reason = BanSystem.getInstance().getReasonProvider().searchReportReason(args[1]);
            if(reason == null){
                sendHelp(sender);
                return;
            }
            report = player.report(reason,message,sender.getUUID(),online.getServer());
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
        sender.sendMessage(Messages.REPORT_HELP_HEADER.replace("[prefix]",getPrefix()));
        for(ReportReason reason : BanSystem.getInstance().getReasonProvider().getReportReasons()){
            if(!sender.hasPermission(reason.getPermission())) continue;
            sender.sendMessage(Messages.REPORT_HELP_REASON
                    .replace("[prefix]",getPrefix())
                    .replace("[id]",""+reason.getID())
                    .replace("[name]",reason.getDisplay())
                    .replace("[reason]",reason.getDisplay())
                    .replace("[points]",""+reason.getPoints()));
        }
        sender.sendMessage(Messages.REPORT_HELP_HELP.replace("[prefix]",getPrefix()));
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
        if(args.length == 1) return GeneralUtil.calculateTabComplete(args[0],sender.getName(),BanSystem.getInstance().getNetwork().getPlayersOnServer(sender.getServer()));
        return null;
    }
}
