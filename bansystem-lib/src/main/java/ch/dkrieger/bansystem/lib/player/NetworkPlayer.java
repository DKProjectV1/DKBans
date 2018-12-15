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
import com.sun.org.apache.regexp.internal.RE;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class NetworkPlayer {

    private int id;
    private String name, color, lastIP, lastCountry;
    private UUID uuid;
    private long lastLogin, firstLogin, onlineTime;
    private boolean bypass, teamChatLogin, reportLogin;
    private History history;
    private Document properties;
    private PlayerStats stats;
    private List<OnlineSession> onlineSessions;
    private List<Report> reports;

    public int getID() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getColor() {
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
    public UUID getUUID() {
        return uuid;
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
    }

    public void setReportLogin(boolean reportLogin) {
        this.reportLogin = reportLogin;
    }
    public List<Report> getReports(){
        return this.reports;
    }
    public List<String> getIPs(){
        List<String> ips = new ArrayList<>();
        GeneralUtil.iterateAcceptedForEach(this.onlineSessions, object -> !ips.contains(object.getIp()), object ->ips.add(object.getIp()));
        return ips;
    }
    public List<Report> getOpenReport(){
        return GeneralUtil.iterateAcceptedReturn(this.reports, object -> object.getStaff() == null);
    }
    public Report getOneOpenReport(){
        List<Report> reports = getReports();
        if(reports.size() >= 1) return reports.get(0);
        return null;
    }
    public Report getReport(NetworkPlayer reporter) {
        return getReport(reporter.getUUID());
    }
    public Report getReport(UUID reporter){
        return GeneralUtil.iterateOne(this.reports, object -> object.getReporterUUID().equals(reporter));
    }
    public boolean hasReport(NetworkPlayer reporter){
        return getReport(reporter) != null;
    }
    public boolean hasReport(UUID reporter){
        return getReport(reporter) != null;
    }
    public Ban getBan(){
        return history.getBan();
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
        return getBan() != null;
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
        return ban(type, duration,unit,reason,reasonID,staff.toString());
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
        return ban(type, duration,unit,reason,message,points,reasonID,staff.toString());
    }

    public Ban ban(BanType type, long duration, TimeUnit unit,String reason,String message,int points, int reasonID, String staff){
        return ban(type, duration,unit,reason,message,points,reasonID,staff,new Document());
    }

    public Ban ban(BanType type, long duration, TimeUnit unit,String reason,String message,int points, int reasonID, UUID staff, Document properties){
        return ban(type, duration,unit,reason,message,points,reasonID,staff.toString(),properties);
    }

    public Ban ban(BanType type, long duration, TimeUnit unit,String reason,String message,int points, int reasonID, String staff, Document properties){
        return ban(new Ban(this.uuid,this.lastIP,reason,message,System.currentTimeMillis(),-1,points,reasonID,staff,properties
                ,System.currentTimeMillis()+unit.toMillis(duration),type));
    }
    public Ban ban(BanReason reason){
        return ban(reason.toBan(this,null));
    }
    public Ban ban(BanReason reason, UUID staff) {
        return ban(reason,staff.toString());
    }
    public Ban ban(BanReason reason, String staff){
        return ban(reason.toBan(this,staff));
    }
    public Ban ban(Ban ban){
        ban.setID(addToHistory(ban));
        OnlineNetworkPlayer player = getOnlinePlayer();
        if(player != null) player.kickForBan(ban);
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
        return kick(reason,message,points,reasonID,staff.toString());
    }

    public Kick kick(String reason, String message,int points, int reasonID, String staff){
        OnlineNetworkPlayer online = getOnlinePlayer();
        return kick(reason,message,points,reasonID,staff,new Document(),online!=null?online.getServer():"");
    }

    public Kick kick(String reason, String message,int points, int reasonID, UUID staff, Document properties){
        return kick(reason,message,points,reasonID,staff.toString(),properties);
    }

    public Kick kick(String reason, String message,int points, int reasonID, String staff, Document properties){
        OnlineNetworkPlayer online = getOnlinePlayer();
        return kick(reason,message,points,reasonID,staff,properties,online!=null?online.getServer():"");
    }

    public Kick kick(String reason, String message,int points, int reasonID, UUID staff, Document properties, String server){
        return kick(new Kick(this.uuid,this.lastIP,reason,message,System.currentTimeMillis(),-1,points,reasonID,staff.toString(),properties,server));
    }

    public Kick kick(String reason, String message,int points, int reasonID, String staff, Document properties, String server){
        return kick(new Kick(this.uuid,this.lastIP,reason,message,System.currentTimeMillis(),-1,points,reasonID,staff,properties,server));
    }

    public Kick kick(KickReason reason){
        return kick(reason.toKick(this,null));
    }
    public Kick kick(KickReason reason, UUID staff){
        return kick(reason,staff.toString());
    }
    public Kick kick(KickReason reason, String staff){
        return kick(reason.toKick(this,staff));
    }
    public Kick kick(Kick kick){
        kick.setID(addToHistory(kick));
        OnlineNetworkPlayer player = getOnlinePlayer();
        if(player != null) player.kick(kick);
        return kick;
    }
    public Unban unban(BanType type) {
        return unban(type,"");
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
        return unban(type,reason,message,points,reasonID,staff.toString());
    }

    public Unban unban(BanType type, String reason, String message, int points, int reasonID, String staff) {
        return unban(type,reason,message,-1,points,reasonID,staff,new Document());
    }

    public Unban unban(BanType type, String reason, String message, int points, int reasonID, UUID staff, Document properties) {
        return unban(new Unban(this.uuid,this.lastIP,reason,message,System.currentTimeMillis(),-1,points,reasonID,staff.toString(),properties,type));
    }

    public Unban unban(BanType type, String reason, String message, int id, int points, int reasonID, String staff, Document properties) {
        return unban(new Unban(this.uuid,this.lastIP,reason,message,System.currentTimeMillis(),id,points,reasonID,staff,properties,type));
    }

    public Unban unban(BanType type, UnbanReason reason){
        return unban(type,reason,"Console");
    }

    public Unban unban(BanType type, UnbanReason reason,UUID staff){
        return unban(type,reason,staff.toString());
    }

    public Unban unban(BanType type, UnbanReason reason, String staff){
        return unban(type,reason,"",staff.toString());
    }

    public Unban unban(BanType type, UnbanReason reason,String message,UUID staff){
        return unban(type,reason,message,staff.toString());
    }
    public Unban unban(BanType type, UnbanReason reason,String message,String staff){
        return unban(reason.toUnban(type,this,message,staff));
    }
    public Unban unban(Unban unban) {
        unban.setID(addToHistory(unban));
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
        BanSystem.getInstance().getReportManager().report(report);
        return report;
    }
    public void processOpenReports(NetworkPlayer staff){
        BanSystem.getInstance().getReportManager().processOpenReports(this,staff);
    }
    public void deleteReports(){
        BanSystem.getInstance().getReportManager().deleteReports(this);
    }
    public void resetHistory(){
        this.history = new History();
        BanSystem.getInstance().getStorage().resetHistory(this.uuid);
    }
    public void resetHistory(int id){
        this.history.getRawEntries().remove(id);
        BanSystem.getInstance().getStorage().resetHistory(this.uuid,id);
    }
    public int addToHistory(HistoryEntry entry) {
        this.history.getRawEntries().put(entry.getID(),entry);
        return BanSystem.getInstance().getStorage().addHistoryEntry(this.uuid,entry);
    }
/*
        RuntimeTypeAdapterFactory<Animal> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(Animal.class, "type")
                .registerSubtype(Dog.class, "dog")
                .registerSubtype(Cat.class, "cat");

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(runtimeTypeAdapterFactory)
                .create();

    }

    /*
    RuntimeTypeAdapterFactory<Animal> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
.of(Animal.class, "type")
.registerSubtype(Dog.class, "dog")
.registerSubtype(Cat.class, "cat");

Gson gson = new GsonBuilder()
    .registerTypeAdapterFactory(runtimeTypeAdapterFactory)
    .create();


     */

}
