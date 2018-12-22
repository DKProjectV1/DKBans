package ch.dkrieger.bansystem.bungeecord;

import ch.dkrieger.bansystem.bungeecord.event.*;
import ch.dkrieger.bansystem.bungeecord.listener.PlayerListener;
import ch.dkrieger.bansystem.bungeecord.player.BungeeCordPlayerManager;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.DKBansPlatform;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommandManager;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.NetworkPlayerUpdateCause;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.report.Report;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.regexp.internal.RE;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BungeeCordBanSystemBootstrap extends Plugin implements DKBansPlatform {

    private static BungeeCordBanSystemBootstrap instance;
    private BungeeCordCommandManager commandManager;

    @Override
    public void onLoad() {
        instance = this;
        this.commandManager = new BungeeCordCommandManager();
        new BanSystem(this,new BungeeCordNetwork(),new BungeeCordPlayerManager());
    }
    @Override
    public void onEnable() {
        BungeeCord.getInstance().getPluginManager().registerListener(this,new PlayerListener());
    }

    @Override
    public void onDisable() {
        BanSystem.getInstance().shutdown();
    }

    @Override
    public String getPlatformName() {
        return "BungeeCord";
    }

    @Override
    public String getServerVersion() {
        return BungeeCord.getInstance().getVersion()+" | "+BungeeCord.getInstance().getGameVersion();
    }

    @Override
    public File getFolder() {
        return new File("plugins/DKBans/");
    }

    @Override
    public NetworkCommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public String getColor(NetworkPlayer player) {
        return "§8";
    }

    public String getProxyName(){
        return "Proxy-1";
    }

    public void executePlayerUpdateEvents(NetworkPlayer player, NetworkPlayerUpdateCause cause, Document properties, boolean onThisServer){
        if(cause == NetworkPlayerUpdateCause.LOGIN){
            BungeeCord.getInstance().getPluginManager().callEvent(new ProxiedNetworkPlayerLoginEvent(player.getUUID(),System.currentTimeMillis(),onThisServer));
        }else if(cause == NetworkPlayerUpdateCause.LOGOUT){
            System.out.println("logout");
            List<Report> reports = properties.getObject("reports",new TypeToken<List<Report>>(){}.getType());
            List<UUID> sentStaffs = new ArrayList<>();
            GeneralUtil.iterateForEach(reports, object -> {
                ProxiedPlayer reporter = BungeeCord.getInstance().getPlayer(object.getReporteUUID());
                if(reporter != null) reporter.sendMessage(new TextComponent(Messages.REPORT_LEAVED_USER
                        .replace("[player]",object.getPlayer().getColoredName())
                        .replace("[prefix]",Messages.PREFIX_REPORT)));
                if(!sentStaffs.contains(object.getStaff())){
                    sentStaffs.add(object.getStaff());
                    ProxiedPlayer staff = BungeeCord.getInstance().getPlayer(object.getStaff());
                    if(staff != null) staff.sendMessage(new TextComponent(Messages.REPORT_LEAVED_STAFF
                            .replace("[player]",object.getPlayer().getColoredName())
                            .replace("[prefix]",Messages.PREFIX_REPORT)));
                }
            });
            BungeeCord.getInstance().getPluginManager().callEvent(new ProxiedNetworkPlayerLogoutEvent(player.getUUID(),System.currentTimeMillis(),onThisServer,reports));
        }else if(cause == NetworkPlayerUpdateCause.BAN){
            System.out.println("ban");
            List<Report> reports = properties.getObject("reports",new TypeToken<List<Report>>(){}.getType());
            BungeeCord.getInstance().getPluginManager().callEvent(new ProxiedNetworkPlayerBanEvent(player.getUUID()
                    ,System.currentTimeMillis(),onThisServer,properties.getObject("ban", Ban.class)));
            if(reports.size() > 0){
                GeneralUtil.iterateForEach(reports, object -> {
                    ProxiedPlayer reporter = BungeeCord.getInstance().getPlayer(object.getReporteUUID());
                    if(reporter != null) reporter.sendMessage(new TextComponent(Messages.REPORT_ACCEPTED
                            .replace("[player]",object.getPlayer().getColoredName())
                            .replace("[prefix]",Messages.PREFIX_REPORT)));
                });
                BungeeCord.getInstance().getPluginManager().callEvent(new ProxiedNetworkPlayerReportsAcceptEvent(player.getUUID()
                        ,System.currentTimeMillis(),onThisServer,reports));
            }
        }else if(cause == NetworkPlayerUpdateCause.KICK){
            BungeeCord.getInstance().getPluginManager().callEvent(new ProxiedNetworkPlayerKickEvent(player.getUUID(),System.currentTimeMillis(),onThisServer));
        }else if(cause == NetworkPlayerUpdateCause.UNBAN){
            BungeeCord.getInstance().getPluginManager().callEvent(new ProxiedNetworkPlayerUnbanEvent(player.getUUID(),System.currentTimeMillis(),onThisServer));
        }else if(cause == NetworkPlayerUpdateCause.REPORTSEND){
            Report report = properties.getObject("report",Report.class);
            System.out.println("send report");
            for(ProxiedPlayer players : BungeeCord.getInstance().getPlayers()){
                if(players.hasPermission("dkbans.report.receive")){
                    NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(players.getUniqueId());
                    if(networkPlayer != null && networkPlayer.isReportLoggedIn()) players.sendMessage(report.toMessage());
                }
            }
            BungeeCord.getInstance().getPluginManager().callEvent(new ProxiedNetworkPlayerReportEvent(player.getUUID()
                    ,System.currentTimeMillis(),onThisServer,report));
            BanSystem.getInstance().getReportManager().getReports().add(report);
        }else if(cause == NetworkPlayerUpdateCause.REPORTPROCESS){
            BanSystem.getInstance().getReportManager().clearCachedReports();
            BungeeCord.getInstance().getPluginManager().callEvent(new ProxiedNetworkPlayerReportsProcessEvent(player.getUUID()
                    ,System.currentTimeMillis(),onThisServer,properties.getObject("staff", UUID.class)));
        }else if(cause == NetworkPlayerUpdateCause.REPORTDENY){
            List<Report> reports = properties.getObject("reports",new TypeToken<List<Report>>(){}.getType());
            GeneralUtil.iterateForEach(reports, object -> {
                ProxiedPlayer reporter = BungeeCord.getInstance().getPlayer(object.getReporteUUID());
                if(reporter != null) reporter.sendMessage(new TextComponent(Messages.REPORT_DENIED_USER
                        .replace("[player]",object.getPlayer().getColoredName())
                        .replace("[prefix]",Messages.PREFIX_REPORT)));
            });
            BanSystem.getInstance().getReportManager().clearCachedReports();
            BungeeCord.getInstance().getPluginManager().callEvent(new ProxiedNetworkPlayerReportsDenyEvent(player.getUUID()
                    ,System.currentTimeMillis(),onThisServer,reports));
        }
        BungeeCord.getInstance().getPluginManager().callEvent(new ProxiedNetworkPlayerUpdateEvent(player.getUUID(),System.currentTimeMillis(),onThisServer,cause));
    }

    public static BungeeCordBanSystemBootstrap getInstance() {
        return instance;
    }
    /*
    NetworkPlayerLoginEvent -
    NetworkPlayerUpdateEvent -
    NetworkPlayerLogoutEvent -
    OnlineNetworkPlayerUpdateEvent -
    NetworkPlayerBanEvent -
    NetworkPlayerKickEvent -
    NetworkPlayerReportEvent -
    NetworkPlayerReportProcessEvent
    NetworkPlayerReportAcceptEvent
    NetworkPlayerReportDenyEvent

     */
}
