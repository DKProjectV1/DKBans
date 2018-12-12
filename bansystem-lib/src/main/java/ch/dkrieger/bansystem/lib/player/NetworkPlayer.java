package ch.dkrieger.bansystem.lib.player;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.History;
import ch.dkrieger.bansystem.lib.player.history.value.Ban;
import ch.dkrieger.bansystem.lib.player.history.value.Kick;
import ch.dkrieger.bansystem.lib.player.history.value.Unban;
import ch.dkrieger.bansystem.lib.reason.BanReason;
import ch.dkrieger.bansystem.lib.reason.KickReason;
import ch.dkrieger.bansystem.lib.reason.ReportReason;
import ch.dkrieger.bansystem.lib.reason.UnbanReason;
import ch.dkrieger.bansystem.lib.report.Report;
import ch.dkrieger.bansystem.lib.utils.Document;
import com.sun.org.apache.regexp.internal.RE;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class NetworkPlayer {

    private int id;
    private String name, color;
    private UUID uuid;
    private long lastLogin, firstLogin, onlineTime;
    private boolean bypass, teamChatLogin, reportLogin;
    private History history;
    private Document properties;

    private List<OnlineSession> onlineSessions;

    public NetworkPlayer() {
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

    /*
    RuntimeTypeAdapterFactory<Animal> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
.of(Animal.class, "type")
.registerSubtype(Dog.class, "dog")
.registerSubtype(Cat.class, "cat");

Gson gson = new GsonBuilder()
    .registerTypeAdapterFactory(runtimeTypeAdapterFactory)
    .create();
     */


    /*

    History
     - Last Bans
     - Last Kicks
     -

     */

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
        return null;
    }
    public String getIP(){
        return null;
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

    public boolean isReportLogin() {
        return reportLogin;
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

    }
    public Report getOpenReport(){
        
    }
    public Report getReport(UUID reporter){

    }
    public boolean hasReport(UUID uuid){

    }

    public Ban getBan(){

    }
    public Ban getBan(BanType type){

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
        return ban(type,duration,unit,reason,reasonID,null);
    }

    public Ban ban(BanType type, long duration, TimeUnit unit, String reason, UUID staff){
        return ban(type,duration,unit,reason,-1,staff);
    }

    public Ban ban(BanType type, long duration, TimeUnit unit,String reason, int reasonID, UUID staff){

    }


    public Ban ban(BanReason reason, UUID staff){

    }

    public Ban ban(BanReason reason){

    }

    public void ban(Ban ban){

    }
    public Kick kick(KickReason reason){
        return kick(reason,null);
    }

    public Kick kick(KickReason reason, UUID staff){

    }

    public Kick kick(Kick kick){


    }

    public Unban unban(BanType type, UnbanReason reason){

    }
    public Unban unban(BanType type){

    }

    public Report report(ReportReason reason){
        return report(reason,null);
    }
    public Report report(ReportReason reason, UUID reporter){
        return report(reporter,reason.getDisplay(),reason.getID());
    }
    public Report report(ReportReason reason,String lastServer, UUID reporter){
        return report(reporter,reason.getDisplay(),reason.getID(),lastServer);
    }
    public Report report(String reason){
        return report(null,reason);
    }
    public Report report(UUID reporter,String reason){
        return report(reporter,reason,-1);
    }
    public Report report(UUID reporter,String reason, String lastServer){
        return report(reporter,reason,-1,lastServer);
    }
    public Report report(UUID reporter,String reason, int reasonID ){
        OnlineNetworkPlayer online = getOnlinePlayer();
        if(online != null) return report(reporter,reason,reasonID,online.getServer());
        return report(reporter,reason,reasonID,"Unknown");
    }

    public Report report(UUID reporter, String reason, int reasonID, String lastServer){
        Report report = new Report(null,null,reporter,reason,lastServer,System.currentTimeMillis(),reasonID);
        return report;
    }
    public void processOpenReports(NetworkPlayer player){
        processOpenReports(player.getUUID());
    }
    public void processOpenReports(UUID staff){

    }
    public void resetHistory(){

    }
    public void resetHistory(int id){

    }



}
