package ch.dkrieger.bansystem.lib.player;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.History;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.HistoryEntry;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import ch.dkrieger.bansystem.lib.player.history.entry.Unban;
import ch.dkrieger.bansystem.lib.reason.BanReason;
import ch.dkrieger.bansystem.lib.reason.KickReason;
import ch.dkrieger.bansystem.lib.reason.ReportReason;
import ch.dkrieger.bansystem.lib.reason.UnbanReason;
import ch.dkrieger.bansystem.lib.report.Report;
import ch.dkrieger.bansystem.lib.stats.PlayerStats;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class NetworkPlayer {

    private int id;
    private UUID uuid;
    private String name, color, lastIP, lastCountry;
    private long lastLogin, firstLogin, onlineTime;
    private boolean bypass, teamChatLogin, reportLogin;
    private transient History history;
    private Document properties;
    private PlayerStats stats;
    private List<OnlineSession> onlineSessions;
    private transient List<Report> reports;

    public NetworkPlayer(UUID uuid, String name, String ip, String country) {
        this.id = -1;
        this.uuid = uuid;
        this.name = name;
        this.color = BanSystem.getInstance().getConfig().playerColorDefault;
        this.lastIP = ip;
        this.lastCountry = country;
        this.lastLogin = System.currentTimeMillis();
        this.firstLogin = System.currentTimeMillis();
        this.onlineTime = 0L;
        this.bypass = false;
        this.teamChatLogin = true;
        this.reportLogin = true;
        this.history = new History();
        this.properties = new Document();
        this.stats = new PlayerStats();
        this.onlineSessions = new ArrayList<>();
        this.reports = new ArrayList<>();
    }
    public int getID() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getColor() {
        String color = BanSystem.getInstance().getPlatform().getColor(this);
        if(color == null) color = this.color;
        return color;
    }
    public String getColoredName(){
        return getColor()+this.name;
    }
    public String getCountry(){
        return this.lastCountry;
    }
    public String getIP(){
        return this.lastIP;
    }
    public UUID getUUID(){
        return this.uuid;
    }
    public long getLastLogin() {
        return lastLogin;
    }

    public boolean isTeamChatLoggedIn() {
        return teamChatLogin;
    }

    public boolean isReportLoggedIn() {
        return reportLogin;
    }

    public boolean isTeamChatLogin() {
        return teamChatLogin;
    }

    public boolean isReportLogin() {
        return reportLogin;
    }

    public PlayerStats getStats() {
        return stats;
    }

    public long getFirstLogin() {
        return firstLogin;
    }

    public long getOnlineTime() {
        return onlineTime;
    }

    public History getHistory() {
        return history;
    }

    public Document getProperties() {
        return properties;
    }

    public List<OnlineSession> getOnlineSessions() {
        return onlineSessions;
    }

    public void setTeamChatLogin(boolean teamChatLogin) {
        this.teamChatLogin = teamChatLogin;
        saveStaffSettings();
    }

    public void setReportLogin(boolean reportLogin) {
        this.reportLogin = reportLogin;
        saveStaffSettings();
    }
    public List<Report> getReports(){
        return this.reports;
    }
    public List<String> getIPs(){
        List<String> ips = new ArrayList<>();
        GeneralUtil.iterateAcceptedForEach(this.onlineSessions, object -> !ips.contains(object.getIp()), object ->ips.add(object.getIp()));
        return ips;
    }
    public List<Report> getOpenReports(){
        return GeneralUtil.iterateAcceptedReturn(this.reports, object -> object.getStaff() == null);
    }
    public List<Report> getProcessingReports(){
        return GeneralUtil.iterateAcceptedReturn(this.reports, object -> object.getStaff() != null);
    }
    public Report getProcessingReport(){
        List<Report> reports = getProcessingReports();
        System.out.println(reports.size());
        if(reports.size() >= 1) return reports.get(0);
        return null;
    }
    public Report getOpenReport(){
        List<Report> reports = getOpenReports();
        if(reports.size() >= 1) return reports.get(0);
        return null;
    }
    public UUID getReportStaff(){
        Report report = getProcessingReport();
        if(report != null) return report.getStaff();
        return null;
    }
    public Report getOpenReportWhenNoProcessing(){
        List<Report> reports = getReports();
        if(reports.size() >= 1 && reports.size() == this.reports.size()) return reports.get(0);
        return null;
    }
    public Report getReport(NetworkPlayer reporter) {
        return getReport(reporter.getUUID());
    }
    public Report getReport(UUID reporter){
        return GeneralUtil.iterateOne(this.reports, object -> object.getReporteUUID().equals(reporter));
    }
    public boolean hasReport(NetworkPlayer reporter){
        return getReport(reporter) != null;
    }
    public boolean hasReport(UUID reporter){
        return getReport(reporter) != null;
    }
    public Ban getBan(BanType type){
        return history.getBan(type);
    }

    public OnlineNetworkPlayer getOnlinePlayer(){
        return BanSystem.getInstance().getPlayerManager().getOnlinePlayer(this.uuid);
    }
    public boolean hasBypass() {
        return bypass;
    }
    public boolean isOnline(){
        return getOnlinePlayer() != null;
    }

    public boolean isBanned(){
        return history.isBanned();
    }
    public boolean isBanned(BanType type){
        return getBan(type) != null;
    }

    public Ban ban(BanType type, long duration, TimeUnit unit, String reason){
        return ban(type,duration,unit,reason,-1);
    }
    public Ban ban(BanType type, long duration, TimeUnit unit,String reason, int reasonID){
        return ban(type,duration,unit,reason,reasonID,"Console");
    }
    public Ban ban(BanType type, long duration, TimeUnit unit,String reason, int reasonID, UUID staff){
        return ban(type, duration,unit,reason,reasonID,staff==null?"Console":staff.toString());
    }

    public Ban ban(BanType type, long duration, TimeUnit unit,String reason, int reasonID, String staff){
        return ban(type, duration,unit,reason,"",reasonID,staff);
    }
    public Ban ban(BanType type, long duration, TimeUnit unit,String reason,String message, int reasonID, UUID staff){
        return ban(type, duration,unit,reason,message,0,reasonID,staff);
    }

    public Ban ban(BanType type, long duration, TimeUnit unit,String reason,String message, int reasonID, String staff){
        return ban(type, duration,unit,reason,message,0,reasonID,staff);
    }

    public Ban ban(BanType type, long duration, TimeUnit unit,String reason,String message,int points, int reasonID, UUID staff){
        return ban(type, duration,unit,reason,message,points,reasonID,staff==null?"Console":staff.toString());
    }

    public Ban ban(BanType type, long duration, TimeUnit unit,String reason,String message,int points, int reasonID, String staff){
        return ban(type, duration,unit,reason,message,points,reasonID,staff,new Document());
    }

    public Ban ban(BanType type, long duration, TimeUnit unit,String reason,String message,int points, int reasonID, UUID staff, Document properties){
        return ban(type, duration,unit,reason,message,points,reasonID,staff==null?"Console":staff.toString(),properties);
    }

    public Ban ban(BanType type, long duration, TimeUnit unit,String reason,String message,int points, int reasonID, String staff, Document properties){
        return ban(new Ban(this.uuid,this.lastIP,reason,message,System.currentTimeMillis(),-1,points,reasonID,staff,properties
                ,System.currentTimeMillis()+unit.toMillis(duration),type));
    }
    public Ban ban(BanReason reason){
        return ban(reason.toBan(this,"",null));
    }
    public Ban ban(BanReason reason,String message,UUID staff) {
        return ban(reason,message,staff==null?"Console":staff.toString());
    }
    public Ban ban(BanReason reason,String message, String staff){
        return ban(reason.toBan(this,message,staff));
    }
    public Ban ban(Ban ban){
        ban.setID(addToHistory(ban,NetworkPlayerUpdateCause.BAN,new Document().append("ban",ban).append("reports",this.reports)));
        this.reports.clear();
        OnlineNetworkPlayer player = getOnlinePlayer();
        if(player != null) player.sendBan(ban);
        return ban;
    }
    public Kick kick(String reason, String message){
        return kick(reason,message,0);
    }

    public Kick kick(String reason, String message,int points){
        return kick(reason,message,points,-1);
    }

    public Kick kick(String reason, String message,int points, int reasonID){
        return kick(reason,message,points,reasonID,"Console");
    }

    public Kick kick(String reason, String message,int points, int reasonID, UUID staff){
        return kick(reason,message,points,reasonID,staff==null?"Console":staff.toString());
    }

    public Kick kick(String reason, String message,int points, int reasonID, String staff){
        OnlineNetworkPlayer online = getOnlinePlayer();
        return kick(reason,message,points,reasonID,staff,new Document(),online!=null?online.getServer():"");
    }

    public Kick kick(String reason, String message,int points, int reasonID, UUID staff, Document properties){
        return kick(reason,message,points,reasonID,staff==null?"Console":staff.toString(),properties);
    }

    public Kick kick(String reason, String message,int points, int reasonID, String staff, Document properties){
        OnlineNetworkPlayer online = getOnlinePlayer();
        return kick(reason,message,points,reasonID,staff,properties,online!=null?online.getServer():"");
    }

    public Kick kick(String reason, String message,int points, int reasonID, UUID staff, Document properties, String server){
        return kick(new Kick(this.uuid,this.lastIP,reason,message,System.currentTimeMillis(),-1,points,reasonID,staff==null?"Console":staff.toString(),properties,server));
    }

    public Kick kick(String reason, String message,int points, int reasonID, String staff, Document properties, String server){
        return kick(new Kick(this.uuid,this.lastIP,reason,message,System.currentTimeMillis(),-1,points,reasonID,staff,properties,server));
    }

    public Kick kick(KickReason reason){
        return kick(reason.toKick(this,null));
    }
    public Kick kick(KickReason reason, UUID staff){
        return kick(reason,staff==null?"Console":staff.toString());
    }
    public Kick kick(KickReason reason, String staff){
        return kick(reason.toKick(this,staff));
    }
    public Kick kick(Kick kick){
        kick.setID(addToHistory(kick,NetworkPlayerUpdateCause.KICK));
        OnlineNetworkPlayer player = getOnlinePlayer();
        if(player != null) player.kick(kick);
        return kick;
    }
    public Unban unban(BanType type) {
        return unban(type,"");
    }
    public Unban unban(BanType type, String reason, UUID staff) {
        return unban(type,reason,"",0,-1,staff);
    }

    public Unban unban(BanType type, String reason) {
        return unban(type,reason,"");
    }

    public Unban unban(BanType type, String reason, String message) {
        return unban(type,reason,message,0,-1);
    }

    public Unban unban(BanType type, String reason, String message, int points) {
        return unban(type,reason,message,points,-1);
    }

    public Unban unban(BanType type, String reason, String message, int points, int reasonID) {
        return unban(type,reason,message,points,reasonID,"Console");
    }

    public Unban unban(BanType type, String reason, String message, int points, int reasonID, UUID staff) {
        return unban(type,reason,message,points,reasonID,staff==null?"Console":staff.toString());
    }

    public Unban unban(BanType type, String reason, String message, int points, int reasonID, String staff) {
        return unban(type,reason,message,-1,points,reasonID,staff,new Document());
    }

    public Unban unban(BanType type, String reason, String message, int points, int reasonID, UUID staff, Document properties) {
        return unban(new Unban(this.uuid,this.lastIP,reason,message,System.currentTimeMillis(),-1,points,reasonID,staff==null?"Console":staff.toString(),properties,type));
    }

    public Unban unban(BanType type, String reason, String message, int id, int points, int reasonID, String staff, Document properties) {
        return unban(new Unban(this.uuid,this.lastIP,reason,message,System.currentTimeMillis(),id,points,reasonID,staff,properties,type));
    }

    public Unban unban(BanType type, UnbanReason reason){
        return unban(type,reason,"Console");
    }

    public Unban unban(BanType type, UnbanReason reason,UUID staff){
        return unban(type,reason,staff==null?"Console":staff.toString());
    }

    public Unban unban(BanType type, UnbanReason reason, String staff){
        return unban(type,reason,"",staff);
    }

    public Unban unban(BanType type, UnbanReason reason,String message,UUID staff){
        return unban(type,reason,message,staff==null?"Console":staff.toString());
    }
    public Unban unban(BanType type, UnbanReason reason,String message,String staff){
        return unban(reason.toUnban(type,this,message,staff));
    }
    public Unban unban(Unban unban) {
        unban.setID(addToHistory(unban,NetworkPlayerUpdateCause.UNBAN));
        return unban;
    }
    public Report report(String reason){
        return report(null,reason);
    }
    public Report report(UUID reporter,String reason){
        return report(reporter,reason,"");
    }
    public Report report(UUID reporter,String reason,String message){
        return report(reporter,reason,message,-1);
    }
    public Report report(UUID reporter,String reason, String message, int reasonID){
        OnlineNetworkPlayer online = getOnlinePlayer();
        return report(reporter,reason,message,reasonID,online!=null?online.getServer():"Unknown");
    }

    public Report report(UUID reporter, String reason, String message, int reasonID, String lastServer){
        return report(new Report(this.uuid,null,reporter,reason,message,lastServer,System.currentTimeMillis(),reasonID));
    }

    public Report report(ReportReason reason){
        return report(reason,null);
    }

    public Report report(ReportReason reason,UUID reporter) {
        OnlineNetworkPlayer online = getOnlinePlayer();
        return report(reason.toReport(this,reporter,"",online!=null?online.getServer():"Unknown"));
    }

    public Report report(ReportReason reason,String message,UUID reporter) {
        OnlineNetworkPlayer online = getOnlinePlayer();
        return report(reason.toReport(this,reporter,message,online!=null?online.getServer():"Unknown"));
    }

    public Report report(ReportReason reason,String message,UUID reporter, String lastServer) {
        return report(reason.toReport(this,reporter,message,lastServer));
    }
    public Report report(Report report) {
        if(report.getUUID() != this.uuid) throw new IllegalArgumentException("This reports ist not from this player");
        this.reports.add(report);
        BanSystem.getInstance().getStorage().createReport(report);
        update(NetworkPlayerUpdateCause.REPORTSEND,new Document().append("report",report));
        return report;
    }
    public void processOpenReports(NetworkPlayer staff){
        GeneralUtil.iterateForEach(this.reports, object -> object.setStaff(staff.getUUID()));
        BanSystem.getInstance().getStorage().processReports(this,staff);
        update(NetworkPlayerUpdateCause.REPORTPROCESS,new Document().append("staff",staff.getUUID()));
    }
    public void deleteReports(){
        this.reports.clear();
        BanSystem.getInstance().getStorage().deleteReports(this);
        update(NetworkPlayerUpdateCause.REPORTDELETE);
    }
    public void denyReports(){
        BanSystem.getInstance().getStorage().deleteReports(this);
        update(NetworkPlayerUpdateCause.REPORTDENY,new Document().append("reports",this.reports));
        this.reports.clear();
    }
    public void resetHistory(){
        this.history = new History();
        BanSystem.getInstance().getStorage().clearHistory(this);
        update(NetworkPlayerUpdateCause.HISTORYUPDATE);
    }
    public void resetHistory(int id){
        this.history.getRawEntries().remove(id);
        BanSystem.getInstance().getStorage().deleteHistoryEntry(this,id);
        update(NetworkPlayerUpdateCause.HISTORYUPDATE);
    }
    public int addToHistory(HistoryEntry entry) {
        return addToHistory(entry,NetworkPlayerUpdateCause.HISTORYUPDATE);
    }
    public int addToHistory(HistoryEntry entry,NetworkPlayerUpdateCause cause){
        return addToHistory(entry, cause,new Document());
    }
    public int addToHistory(HistoryEntry entry,NetworkPlayerUpdateCause cause,Document properties) {
        this.history.getRawEntries().put(entry.getID(),entry);
        update(cause,properties);
        return BanSystem.getInstance().getStorage().createHistoryEntry(this,entry);
    }
    private void saveStaffSettings(){
        BanSystem.getInstance().getStorage().saveStaffSettings(this.uuid,reportLogin,teamChatLogin);
        update(NetworkPlayerUpdateCause.STAFFSETTINGS);
    }
    public void update(){
        update(NetworkPlayerUpdateCause.NOTSET);
    }
    public void update(NetworkPlayerUpdateCause cause){
        update(cause,new Document());
    }
    public void update(Document properties){
        update(NetworkPlayerUpdateCause.NOTSET,properties);
    }
    public void update(NetworkPlayerUpdateCause cause,Document properties){
        BanSystem.getInstance().getPlayerManager().updatePlayer(this,cause,properties);
    }
    @SuppressWarnings("Only for databse id creation")
    public void setID(int id) {
        this.id = id;
    }
    @SuppressWarnings("Only for databse id creation")
    public void setHistory(History history) {
        this.history = history;
    }
    @SuppressWarnings("Only for databse id creation")
    public void setReports(List<Report> reports) {
        this.reports = reports;
    }
    public void playerLogin(String name, String ip,int clientVersion, String clientLanguage, String proxy, String color ,boolean bypass){
        this.reports.clear();
        this.name = name;
        this.color = color;
        this.bypass = bypass;
        this.lastIP = ip;
        this.lastLogin = System.currentTimeMillis();
        this.onlineSessions.add(new OnlineSession(ip,"Unknown","Unknown",proxy,clientLanguage,clientVersion
                ,System.currentTimeMillis(),System.currentTimeMillis()));
        BanSystem.getInstance().getStorage().deleteReports(this);
        update(NetworkPlayerUpdateCause.LOGIN);
    }
    public void playerLogout(String color ,boolean bypass,String lastServer){
        this.color = color;
        this.bypass = bypass;
        this.lastLogin = System.currentTimeMillis();
        BanSystem.getInstance().getStorage().deleteReports(this);
        update(NetworkPlayerUpdateCause.LOGOUT,new Document().append("reports",reports));
    }
}
