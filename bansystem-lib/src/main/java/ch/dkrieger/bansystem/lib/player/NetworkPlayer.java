/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 14.03.19 19:43
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

package ch.dkrieger.bansystem.lib.player;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.config.Config;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.History;
import ch.dkrieger.bansystem.lib.player.history.HistoryPoints;
import ch.dkrieger.bansystem.lib.player.history.entry.*;
import ch.dkrieger.bansystem.lib.reason.*;
import ch.dkrieger.bansystem.lib.report.Report;
import ch.dkrieger.bansystem.lib.stats.PlayerStats;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class NetworkPlayer {

    private int id;
    private UUID uuid, watchingReportedPlayer;
    private String name, color, lastIP, lastCountry;
    private long lastLogin, firstLogin, onlineTime;
    private boolean bypass, teamChatLogin, reportLogin;
    private History history;
    private Document properties;
    private PlayerStats stats;
    private List<OnlineSession> onlineSessions;
    private List<Report> reports;

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
    public NetworkPlayer(int id, UUID uuid, String name, String color, String lastIP, String lastCountry, long lastLogin, long firstLogin, long onlineTime, boolean bypass, boolean teamChatLogin, boolean reportLogin, History history, Document properties, PlayerStats stats, List<OnlineSession> onlineSessions, List<Report> reports) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.color = color;
        this.lastIP = lastIP;
        this.lastCountry = lastCountry;
        this.lastLogin = lastLogin;
        this.firstLogin = firstLogin;
        this.onlineTime = onlineTime;
        this.bypass = bypass;
        this.teamChatLogin = teamChatLogin;
        this.reportLogin = reportLogin;
        this.history = history;
        this.properties = properties;
        this.stats = stats;
        this.onlineSessions = onlineSessions;
        this.reports = reports;
    }

    /**
     * Every player has a unique ide which is given by DKBans.
     *
     * @return The id of the player
     */
    public int getID() {
        return id;
    }

    /**
     * @return The minecraft player name
     */
    public String getName() {
        return name;
    }

    /**
     * Every player has a color which is set in the config or in the player set event. (Only for Design)
     * The color is usually set by the rank (permission).
     *
     * @return The player color
     */
    public String getColor() {
        String color = BanSystem.getInstance().getPlatform().getColor(this);
        if(color == null) color = this.color;
        return ChatColor.translateAlternateColorCodes('&',color);
    }

    /**
     *
     * @return The minecraft name an the color before the name
     */
    public String getColoredName(){
        return getColor()+this.name;
    }

    /**
     * Get the player's last country.
     *
     * @return The last country from the player
     */
    public String getCountry(){
        return this.lastCountry;
    }

    /**
     * If the ip saving is disabled in the config, no ip will be available.
     *
     * @return The last ip from the player
     */
    public String getIP(){
        return this.lastIP;
    }

    /**
     *
     * @return The minecraft uuid from the player
     */
    public UUID getUUID(){
        return this.uuid;
    }

    /**
     *
     * @return The last login time from the player in milliseconds
     */
    public long getLastLogin() {
        return lastLogin;
    }

    /**
     *
     * @return The last login time from the player in milliseconds
     */
    public long getFirstLogin() {
        return firstLogin;
    }

    /**
     * This is for receiving teamChat messages or disabling team messages for each player (Used for staff members).
     *
     * @return If the player received team messages
     */
    public boolean isTeamChatLoggedIn() {
        return teamChatLogin;
    }

    /**
     * This is for disabling/enabling receiving report messages for each player (Used for staff members).
     *
     * @return If the player receives reports
     */
    public boolean isReportLoggedIn() {
        return reportLogin;
    }

    /**
     * Get stats from the player to show his activity
     *
     * @return Statistics from the player
     */
    public PlayerStats getStats() {
        return stats;
    }

    /**
     * The online time will be calculated live
     *
     * @return The online time from the player in milliseconds
     */
    public long getOnlineTime(){
        return getOnlineTime(true);
    }

    /**
     * Get the online time from a player which live calculating option.
     *
     * @param live If this is true and the player is online, the system while calculate the new online time live.
     *             If this is false you will get the online time from the last finished online time session.
     *
     * @return The online time from the player in milliseconds
     */
    public long getOnlineTime(boolean live) {
        if(live){
            OnlineNetworkPlayer player = getOnlinePlayer();
            if(player != null){
                long yet = (System.currentTimeMillis()-lastLogin);
                if(yet > 0) return onlineTime+yet;
            }
        }
        return onlineTime;
    }

    /**
     *
     * @return The history (bans/Mutes usw.) from the player
     */
    public History getHistory() {
        return history;
    }

    /**
     * Add custome properties to very player, don't forget to save with @saveProperties
     *
     * @return The player properties
     */
    public Document getProperties() {
        return properties;
    }

    /**
     * Get the online sessions from the player, we recommend not saving to much sessions.
     *
     * @return All available online sessions as list
     */
    public List<OnlineSession> getOnlineSessions() {
        return onlineSessions;
    }

    /**
     *
     * @param teamChatLogin If the player received team messages
     */
    public void setTeamChatLogin(boolean teamChatLogin) {
        this.teamChatLogin = teamChatLogin;
        saveStaffSettings();
    }

    /**
     *
     * @param reportLogin If the player receives report messages
     */
    public void setReportLogin(boolean reportLogin) {
        this.reportLogin = reportLogin;
        saveStaffSettings();
    }

    /**
     * Get all report which are created fo this player
     *
     * @return All report for this player.
     */
    public List<Report> getReports(){
        return this.reports;
    }

    /**
     * The ips are from the online sessions
     *
     * @return all available ips as list
     */
    public List<String> getIPs(){
        List<String> ips = new ArrayList<>();
        GeneralUtil.iterateAcceptedForEach(this.onlineSessions, object -> !ips.contains(object.getIp()), object ->ips.add(object.getIp()));
        return ips;
    }

    public UUID getWatchingReportedPlayer() {
        return watchingReportedPlayer;
    }

    /**
     * An report is open, when no staff is processing this report (staff uuid is null).
     *
     * @return All open reports for this player as list (No staff is checking this)
     */
    public List<Report> getOpenReports(){
        return GeneralUtil.iterateAcceptedReturn(this.reports, object -> object.getStaff() == null);
    }

    /**
     * An report is in processing state the a stuff member is checking this player (staff uuid is not null).
     *
     * @return All reports which are processing by a staff member
     */
    public List<Report> getProcessingReports(){
        return GeneralUtil.iterateAcceptedReturn(this.reports, object -> object.getStaff() != null);
    }

    /**
     * Get one random processing report.
     *
     * @return A random processing report
     */
    public Report getProcessingReport(){
        List<Report> reports = getProcessingReports();
        if(reports.size() > 0) return reports.get(0);
        return null;
    }

    /**
     * Get one random open report
     *
     * @return A random open report.
     */
    public Report getOpenReport(){
        List<Report> reports = getOpenReports();
        if(reports.size() >= 1) return reports.get(0);
        return null;
    }

    /**
     * Get the staff, which is processing the reports for this player.
     *
     * @return The uuid of from the staff member
     */
    public UUID getReportStaff(){
        Report report = getProcessingReport();
        if(report != null) return report.getStaff();
        return null;
    }

    /**
     * Returns only one open report, when no other report is in processing state.
     *
     * @return An open report when no other report is in processing state
     */
    public Report getOpenReportWhenNoProcessing(){
        List<Report> reports = getReports();
        if(reports.size() >= 1 && reports.size() == this.reports.size()) return reports.get(0);
        return null;
    }

    /**
     * Get the report for this player from a player.
     *
     * @param reporter The reporter
     * @return The report, which is created by this reporter
     */
    public Report getReport(NetworkPlayer reporter) {
        return getReport(reporter.getUUID());
    }

    /**
     * Get the report for this player from a player.
     *
     * @param reporter The UUID of the reporter
     * @return The report, which is created by this reporter
     */
    public Report getReport(UUID reporter){
        return GeneralUtil.iterateOne(this.reports, object -> object.getReporterUUID().equals(reporter));
    }

    /**
     * Check if a player has created an report for this player.
     *
     * @param reporter the reporter
     * @return if rhe reporter created a report
     */
    public boolean hasReport(NetworkPlayer reporter){
        return getReport(reporter) != null;
    }

    /**
     * Check if a player has created an report for this player.
     *
     * @param reporter the uuid from the reporter
     * @return if rhe reproter created a report
     */
    public boolean hasReport(UUID reporter){
        return getReport(reporter) != null;
    }

    /**
     * Get the active ban of this player.
     *
     * @param type The banType (Chat or Network)
     * @return The current Ban
     */
    public Ban getBan(BanType type){
        return history.getBan(type);
    }

    /**
     *
     * @return The online player which the same uuid
     */
    public OnlineNetworkPlayer getOnlinePlayer(){
        return BanSystem.getInstance().getPlayerManager().getOnlinePlayer(this.uuid);
    }

    /**
     * If this player has bypass, this is used for ban or report bypassing.
     *
     * @return if the player has bypass
     */
    public boolean hasBypass() {
        return bypass;
    }

    /**
     * This requires an integration to you permission system or on bukkit vault with
     * a permission system that supports vault.
     *
     * Use this only if your player is not online or you have no other way to get his permission.
     *
     * @param permission The permission which should checked.
     * @return If the player has this permission (Only when found a supported permission system).
     */
    public boolean hasPermission(String permission){
        return BanSystem.getInstance().getPlatform().checkPermissionInternally(this,permission);
    }

    /**
     *
     * @return if this player is online or not
     */
    public boolean isOnline(){
        return getOnlinePlayer() != null;
    }

    /**
     *
     * @return if this player is banned or not (Every type)
     */
    public boolean isBanned(){
        return history.isBanned();
    }

    /**
     *
     * @param type The ban type (Chat or Network)
     * @return if this player is banned
     */
    public boolean isBanned(BanType type){
        return getBan(type) != null;
    }

    /**
     * Ban a player
     *
     * @param type The type of the ban (Chat or Network)
     * @param duration How long the player is banned
     * @param unit Which time unit should taken to calculate the duration
     * @param reason The reason of the ban
     * @return The created ban
     */
    public Ban ban(BanType type, long duration, TimeUnit unit, String reason){
        return ban(type,duration,unit,reason,-1);
    }

    /**
     *
     * @param type The type of the ban (Chat or Network)
     * @param duration How long the player is banned
     * @param unit Which time unit should taken to calculate the duration
     * @param reason The reason of the ban
     * @param reasonID The id of the reason, this is only to identify the ban
     * @return The created ban
     */
    public Ban ban(BanType type, long duration, TimeUnit unit,String reason, int reasonID){
        return ban(type,duration,unit,reason,reasonID,"Console");
    }

    /**
     * Ban a player
     *
     * @param type The type of the ban (Chat or Network)
     * @param duration How long the player is banned
     * @param unit Which time unit should taken to calculate the duration
     * @param reason The reason of the ban
     * @param reasonID The id of the reason, this is only to identify the ban
     * @param staff The player which has banned this player (uuid)
     * @return The created ban
     */
    public Ban ban(BanType type, long duration, TimeUnit unit,String reason, int reasonID, UUID staff){
        return ban(type, duration,unit,reason,reasonID,staff==null?"Console":staff.toString());
    }
    /**
     * Ban a player
     *
     * @param type The type of the ban (Chat or Network)
     * @param duration How long the player is banned
     * @param unit Which time unit should taken to calculate the duration
     * @param reason The reason of the ban
     * @param reasonID The id of the reason, this is only to identify the ban
     * @param staff The instance which has banned this player, for example Console or AntiCheat (string)
     * @return The created ban
     */
    public Ban ban(BanType type, long duration, TimeUnit unit,String reason, int reasonID, String staff){
        return ban(type, duration,unit,reason,"",reasonID,staff);
    }
    public Ban ban(BanType type, long duration, TimeUnit unit,String reason,String message, int reasonID, UUID staff){
        return ban(type, duration,unit,reason,message,new HistoryPoints(0,BanType.NETWORK),reasonID,staff);
    }

    public Ban ban(BanType type, long duration, TimeUnit unit,String reason,String message, int reasonID, String staff){
        return ban(type, duration,unit,reason,message,new HistoryPoints(0,BanType.NETWORK),reasonID,staff);
    }

    public Ban ban(BanType type, long duration, TimeUnit unit,String reason,String message,HistoryPoints points, int reasonID, UUID staff){
        return ban(type, duration,unit,reason,message,points,reasonID,staff==null?"Console":staff.toString());
    }

    public Ban ban(BanType type, long duration, TimeUnit unit,String reason,String message,HistoryPoints points, int reasonID, String staff){
        return ban(type, duration,unit,reason,message,points,reasonID,staff,new Document());
    }

    public Ban ban(BanType type, long duration, TimeUnit unit,String reason,String message,HistoryPoints points, int reasonID, UUID staff, Document properties){
        return ban(type, duration,unit,reason,message,points,reasonID,staff==null?"Console":staff.toString(),properties);
    }

    public Ban ban(BanType type, long duration, TimeUnit unit,String reason,String message,HistoryPoints points, int reasonID, String staff, Document properties){
        return ban(new Ban(this.uuid,this.lastIP,reason,message,System.currentTimeMillis(),-1,points,reasonID,staff,properties
                ,duration==-1?-1:System.currentTimeMillis()+unit.toMillis(duration),type));
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
        return ban(ban,false);
    }
    public Ban ban(Ban ban, boolean noAltBan){
        final List<Report> reports = getReports();
        if(ban.getBanType() == BanType.CHAT) BanSystem.getInstance().getTempSyncStats().addMutes();
        else BanSystem.getInstance().getTempSyncStats().addBans();
        BanSystem.getInstance().getHistoryManager().clearCache();
        BanSystem.getInstance().getPlatform().getTaskManager().runTaskAsync(()->{
            NetworkPlayer staff = ban.getStaffAsPlayer();
            if(staff != null){
                if(ban.getBanType() == BanType.CHAT) staff.addStatsMuted();
                else staff.addStatsBaned();
            }
            for(Report report : reports){
                BanSystem.getInstance().getTempSyncStats().addReportsAccepted();
                report.getReporter().addStatsReportAccepted();
            }
        });
        if(ban.getBanType() == BanType.NETWORK && !(noAltBan) && BanSystem.getInstance().getConfig().ipBanOnBanEnabled
                && !(lastIP.equalsIgnoreCase("Unknown")) && !(lastIP.equalsIgnoreCase(Messages.UNKNOWN))){
            BanSystem.getInstance().getPlatform().getTaskManager().runTaskAsync(()->{
                if(!BanSystem.getInstance().getPlayerManager().isIPBanned(lastIP))
                    BanSystem.getInstance().getPlayerManager().banIp(lastIP,BanSystem.getInstance().getConfig().ipBanOnBanDuration,TimeUnit.MILLISECONDS,uuid);
            });
        }
        BanSystem.getInstance().getStorage().deleteReports(this);
        ban.setID(addToHistory(ban,NetworkPlayerUpdateCause.BAN,new Document().append("ban",ban).append("reports",this.reports)));
        this.reports.clear();
        OnlineNetworkPlayer player = getOnlinePlayer();
        if(player != null) player.sendBan(ban);
        return ban;
    }
    public Kick kick(String reason, String message){
        return kick(reason,message,new HistoryPoints(0,BanType.NETWORK));
    }
    public Kick kick(String reason, String message, UUID staff){
        return kick(reason,message,new HistoryPoints(0,BanType.NETWORK),1,staff==null?"Console":staff.toString());
    }
    public Kick kick(String reason, String message, String staff){
        return kick(reason,message,new HistoryPoints(0,BanType.NETWORK),1,staff);
    }
    public Kick kick(String reason, String message,HistoryPoints points){
        return kick(reason,message,points,-1);
    }

    public Kick kick(String reason, String message,HistoryPoints points, int reasonID){
        return kick(reason,message,points,reasonID,"Console");
    }

    public Kick kick(String reason, String message,HistoryPoints points, int reasonID, UUID staff){
        return kick(reason,message,points,reasonID,staff==null?"Console":staff.toString());
    }

    public Kick kick(String reason, String message,HistoryPoints points, int reasonID, String staff){
        OnlineNetworkPlayer online = getOnlinePlayer();
        return kick(reason,message,points,reasonID,staff,new Document(),online!=null?online.getServer():"");
    }

    public Kick kick(String reason, String message,HistoryPoints points, int reasonID, UUID staff, Document properties){
        return kick(reason,message,points,reasonID,staff==null?"Console":staff.toString(),properties);
    }

    public Kick kick(String reason, String message,HistoryPoints points, int reasonID, String staff, Document properties){
        OnlineNetworkPlayer online = getOnlinePlayer();
        return kick(reason,message,points,reasonID,staff,properties,online!=null?online.getServer():"");
    }

    public Kick kick(String reason, String message,HistoryPoints points, int reasonID, UUID staff, Document properties, String server){
        return kick(new Kick(this.uuid,this.lastIP,reason,message,System.currentTimeMillis(),-1,points,reasonID,staff==null?"Console":staff.toString(),properties,server));
    }

    public Kick kick(String reason, String message,HistoryPoints points, int reasonID, String staff, Document properties, String server){
        return kick(new Kick(this.uuid,this.lastIP,reason,message,System.currentTimeMillis(),-1,points,reasonID,staff,properties,server));
    }

    public Kick kick(KickReason reason){
        return kick(reason,(String)null);
    }
    public Kick kick(KickReason reason,UUID staff){
        return kick(reason,staff==null?"Console":staff.toString());
    }
    public Kick kick(KickReason reason, String staff){
        return kick(reason,"",staff);
    }
    public Kick kick(KickReason reason,String message, UUID staff){
        return kick(reason,message,staff==null?"Console":staff.toString());
    }
    public Kick kick(KickReason reason,String message, String staff){
        OnlineNetworkPlayer online = getOnlinePlayer();
        return kick(reason.toKick(this,message,staff,online!=null?online.getServer(): Messages.UNKNOWN));
    }
    public Kick kick(KickReason reason,String message, UUID staff, String server){
        return kick(reason,message,staff==null?"Console":staff.toString(),server);
    }
    public Kick kick(KickReason reason,String message, String staff, String server){
        return kick(reason.toKick(this,message,staff,server));
    }
    public Kick kick(Kick kick){
        BanSystem.getInstance().getPlatform().getTaskManager().runTaskAsync(()->{
            NetworkPlayer staff = kick.getStaffAsPlayer();
            if(staff != null) staff.addStatsKicked();
        });
        BanSystem.getInstance().getTempSyncStats().addKicks();
        kick.setID(addToHistory(kick,NetworkPlayerUpdateCause.KICK));
        OnlineNetworkPlayer player = getOnlinePlayer();
        if(player != null) player.sendKick(kick);
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

    public Unban unban(BanType type, String reason, String message, UUID staff) {
        return unban(type,reason,message,0,-1,staff==null?"Console":staff.toString());
    }

    public Unban unban(BanType type, String reason, String message,String staff) {
        return unban(type,reason,message,0,-1,staff);
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
        return unban(new Unban(this.uuid,this.lastIP,reason,message,System.currentTimeMillis(),-1,new HistoryPoints(points,BanType.NETWORK),reasonID,staff==null?"Console":staff.toString(),properties,type));
    }

    public Unban unban(BanType type, String reason, String message, int id, int points, int reasonID, String staff, Document properties) {
        return unban(new Unban(this.uuid,this.lastIP,reason,message,System.currentTimeMillis(),id,new HistoryPoints(points,BanType.NETWORK),reasonID,staff,properties,type));
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
        return unban(reason.toUnban(type,this,message,staff,getBan(type).getPoints()));
    }
    public Unban unban(BanType type, UnbanReason reason,String message,String staff, HistoryPoints lastPoints){
        return unban(reason.toUnban(type,this,message,staff,lastPoints));
    }
    public Unban unban(Unban unban) {
        BanSystem.getInstance().getPlatform().getTaskManager().runTaskAsync(()->{
            NetworkPlayer staff = unban.getStaffAsPlayer();
            if(staff != null) staff.addStatsUnbanned();
        });
        BanSystem.getInstance().getPlatform().getTaskManager().runTaskAsync(()->{
            BanSystem.getInstance().getPlayerManager().unbanIp(uuid);
        });
        BanSystem.getInstance().getTempSyncStats().addUnbans();
        unban.setID(addToHistory(unban,NetworkPlayerUpdateCause.UNBAN));
        return unban;
    }
    public Warn warn(String reason){
        return warn(reason,"");
    }
    public Warn warn(String reason, String message){
        return warn(reason,message,(UUID) null);
    }
    public Warn warn(String reason, String message,UUID staff){
        return warn(reason,message,staff==null?"Console":staff.toString());
    }
    public Warn warn(String reason, String message,String staff){
        return warn(reason,message,new HistoryPoints(0,BanType.NETWORK),-1,staff);
    }
    public Warn warn(String reason, String message, HistoryPoints points, int reasonID, UUID staff){
        return warn(reason,message,points,reasonID,staff,new Document());
    }
    public Warn warn(String reason, String message, HistoryPoints points, int reasonID, String staff){
        return warn(reason,message,points,reasonID,staff,new Document());
    }
    public Warn warn(String reason, String message, HistoryPoints points, int reasonID, UUID staff, Document properties){
        return warn(reason,message,points,reasonID,staff==null?"Console":staff.toString(),properties);
    }
    public Warn warn(String reason, String message, HistoryPoints points, int reasonID, String staff, Document properties){
        return warn(new Warn(uuid,lastIP,reason,message,System.currentTimeMillis(),-1,new HistoryPoints(0,BanType.NETWORK),reasonID,staff,properties));
    }
    public Warn warn(WarnReason reason){
        return warn(reason,"");
    }
    public Warn warn(WarnReason reason, String message){
        return warn(reason,message,(UUID)null);
    }
    public Warn warn(WarnReason reason, String message, UUID staff){
        return warn(reason,message,staff==null?"Console":staff.toString());
    }
    public Warn warn(WarnReason reason, String message, String staff){
        return warn(reason.toWarn(this,message,staff));
    }
    public Warn warn(Warn warn){
        warn.setID(addToHistory(warn,NetworkPlayerUpdateCause.WARN));
        OnlineNetworkPlayer player = getOnlinePlayer();
        if(player != null) player.sendWarn(warn);
        BanSystem.getInstance().getPlatform().getTaskManager().runTaskAsync(()->{
            WarnReason reason = BanSystem.getInstance().getReasonProvider().getWarnReason(warn.getReasonID());
            if(reason != null && getHistory().getWarnCountSinceLastBan(warn.getReasonID()) >= reason.getAutoBanCount()){
                BanReason banReason = BanSystem.getInstance().getReasonProvider().getBanReason(reason.getForBan());
                if(banReason != null) ban(banReason.toBan(this,"",BanSystem.getInstance().getConfig().warnStaffName));
            }else if(getHistory().getWarnCountSinceLastBan() >= BanSystem.getInstance().getConfig().warnAutoBanCount){
                if(reason != null && BanSystem.getInstance().getConfig().warnAutoBanBanForLastReason){
                    BanReason banReason = BanSystem.getInstance().getReasonProvider().getBanReason(reason.getForBan());
                    if(banReason != null) ban(banReason.toBan(this,"","WarnManager"));
                }else{
                    Config config = BanSystem.getInstance().getConfig();
                    ban(config.warnAutoBanBanType,config.warnAutoBanCustomDuration,TimeUnit.MILLISECONDS,config.warnAutoBanCustomReason,"",
                            new HistoryPoints(config.warnAutoBanCustomPoints,config.warnAutoBanBanType),1,config.warnStaffName);
                }
            }
        });
        BanSystem.getInstance().getPlatform().getTaskManager().runTaskAsync(()->{
            NetworkPlayer staff = warn.getStaffAsPlayer();
            if(staff != null) staff.addStatsWarned();
        });
        BanSystem.getInstance().getTempSyncStats().addWarns();
        return warn;
    }

    public Unwarn unwarn(){
        return unwarn(-1);
    }

    public Unwarn unwarn(int warnId){
        return unwarn(warnId,"");
    }


    public Unwarn unwarn(int warnId,String reason){
        return unwarn(warnId,reason,"");
    }

    public Unwarn unwarn(int warnId,String reason, UUID staff){
        return unwarn(warnId,reason,"",new HistoryPoints(0,BanType.NETWORK),staff);
    }

    public Unwarn unwarn(int warnId,String reason,String message){
        return unwarn(warnId,reason,message,new HistoryPoints(0,BanType.NETWORK));
    }

    public Unwarn unwarn(int warnId,String reason,String message, HistoryPoints points){
        return unwarn(warnId,reason,message,points,(UUID)null);
    }

    public Unwarn unwarn(int warnId,String reason,String message, HistoryPoints points, UUID staff){
        return unwarn(warnId,reason,message,points,staff!=null?staff.toString():"",new Document());
    }

    public Unwarn unwarn(int warnId,String reason,String message, HistoryPoints points, String staff){
        return unwarn(warnId,reason,message,points,staff,new Document());
    }

    public Unwarn unwarn(int warnId,String reason,String message, HistoryPoints points, UUID staff, Document properties){
        return unwarn(warnId,reason,message,points,staff!=null?staff.toString():"",properties);
    }

    public Unwarn unwarn(int warnId,String reason,String message, HistoryPoints points, String staff, Document properties){
        return unwarn(new Unwarn(this.uuid,this.lastIP,reason,message,System.currentTimeMillis(),-1,points,-1,staff,properties,warnId));
    }

    public Unwarn unwarn(Unwarn unwarn){
        unwarn.setID(addToHistory(unwarn));
        return unwarn;
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
        return report(reporter,reason,message,reasonID,online!=null?online.getServer():Messages.UNKNOWN);
    }

    public Report report(UUID reporter, String reason, String message, int reasonID, String lastServer){
        return report(new Report(this.uuid,null,reporter,reason,message,lastServer,System.currentTimeMillis(),reasonID));
    }

    public Report report(ReportReason reason){
        return report(reason,null);
    }

    public Report report(ReportReason reason,UUID reporter) {
        OnlineNetworkPlayer online = getOnlinePlayer();
        return report(reason.toReport(this,reporter,"",online!=null?online.getServer():Messages.UNKNOWN));
    }

    public Report report(ReportReason reason,String message,UUID reporter) {
        OnlineNetworkPlayer online = getOnlinePlayer();
        return report(reason.toReport(this,reporter,message,online!=null?online.getServer():Messages.UNKNOWN));
    }

    public Report report(ReportReason reason,String message,UUID reporter, String lastServer) {
        return report(reason.toReport(this,reporter,message,lastServer));
    }
    public Report report(Report report) {
        if(report.getUUID() != this.uuid) throw new IllegalArgumentException("This reports ist not from this player");
        BanSystem.getInstance().getTempSyncStats().addReports();
        this.reports.add(report);
        if(getReportStaff() != null) report.setStaff(getReportStaff());
        BanSystem.getInstance().getStorage().createReport(report);
        stats.addReportsReceived();
        BanSystem.getInstance().getPlatform().getTaskManager().runTaskAsync(() ->{
            BanSystem.getInstance().getStorage().updatePlayerStats(uuid,stats);
            report.getReporter().addStatsReportSent();
        });
        update(NetworkPlayerUpdateCause.REPORTSEND,new Document().append("report",report));
        return report;
    }
    public void processOpenReports(NetworkPlayer staff){
        GeneralUtil.iterateForEach(this.reports, object -> object.setStaff(staff.getUUID()));
        BanSystem.getInstance().getStorage().processReports(this,staff);
        update(NetworkPlayerUpdateCause.REPORTPROCESS,new Document().append("staff",staff.getUUID()));
        staff.setWatchingReportedPlayer(this.uuid);
    }
    public void deleteReports(){
        this.reports.clear();
        BanSystem.getInstance().getStorage().deleteReports(this);
        update(NetworkPlayerUpdateCause.REPORTDELETE);
    }
    public void denyReports(){
        BanSystem.getInstance().getStorage().deleteReports(this);
        update(NetworkPlayerUpdateCause.REPORTDENY,new Document().append("reports",this.reports));
        BanSystem.getInstance().getPlatform().getTaskManager().runTaskAsync(() ->{
            NetworkPlayer staff = BanSystem.getInstance().getPlayerManager().getPlayer(getReportStaff());
            if(staff != null) staff.setWatchingReportedPlayer(null);
        });
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
        int id = BanSystem.getInstance().getStorage().createHistoryEntry(this,entry);
        entry.setID(id);
        this.history.getRawEntries().put(id,entry);
        update(cause,properties);
        return id;
    }
    private void saveStaffSettings(){
        BanSystem.getInstance().getStorage().saveStaffSettings(this.uuid,reportLogin,teamChatLogin);
        update(NetworkPlayerUpdateCause.STAFFSETTINGS);
    }
    public void setColor(String color){
        if(color == null) return;
        this.color = color;
        BanSystem.getInstance().getStorage().setColor(this.uuid,color);
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
    public void addStatsReportSent(){
        this.stats.addReports();
        updatePlayerStatsAsync();
    }
    public void setWatchingReportedPlayer(UUID uuid){
        this.watchingReportedPlayer = uuid;
        BanSystem.getInstance().getStorage().updateWatchReportPlayer(this.uuid,uuid);
        update(NetworkPlayerUpdateCause.REPORTTAKE);
    }
    public void addStatsReportAccepted(){
        this.stats.addReportsAccepted();
        updatePlayerStatsAsync();
    }
    public void addStatsBaned(){
        this.stats.addBans();
        updatePlayerStatsAsync();
    }
    public void addStatsMuted(){
        this.stats.addMutes();
        updatePlayerStatsAsync();
    }
    public void addStatsKicked(){
        this.stats.addKicks();
        updatePlayerStatsAsync();
    }
    public void addStatsWarned(){
        this.stats.addWarns();
        updatePlayerStatsAsync();
    }
    public void addStatsUnbanned(){
        this.stats.addUnbans();
        updatePlayerStatsAsync();
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
    public void setOnlineSessions(List<OnlineSession> sessions) {
        this.onlineSessions = sessions;
    }
    public void playerLogin(String name, String ip,int clientVersion, String clientLanguage, String proxy, String color ,boolean bypass){
        this.watchingReportedPlayer = null;
        this.reports.clear();
        this.name = name;
        if(color != null) this.color = color;
        this.bypass = bypass;
        this.lastIP = ip;
        this.lastCountry = BanSystem.getInstance().getPlayerManager().getCountry(ip);
        if(!BanSystem.getInstance().getConfig().playerSaveIP) this.lastIP = Messages.UNKNOWN;
        this.lastLogin = System.currentTimeMillis();
        if(BanSystem.getInstance().getConfig().playerOnlineSessionSaving){
            OnlineSession session = new OnlineSession(ip,lastCountry,Messages.UNKNOWN,proxy,clientLanguage,clientVersion,lastLogin,lastLogin);
            this.onlineSessions.add(session);
            BanSystem.getInstance().getStorage().createOnlineSession(this,session);
        }
        BanSystem.getInstance().getStorage().updatePlayerLoginInfos(this.uuid,name,this.lastLogin,this.color,bypass,ip,lastCountry,stats.getLogins()+1);
        BanSystem.getInstance().getStorage().deleteReports(this);
        update(NetworkPlayerUpdateCause.LOGIN);
    }
    public void playerLogout(String color ,boolean bypass,String lastServer,int messages){
        if(color != null) this.color = color;
        final UUID watching = this.watchingReportedPlayer;
        this.watchingReportedPlayer = null;
        this.bypass = bypass;
        OnlineSession session = GeneralUtil.iterateOne(this.onlineSessions, object -> object.getConnected() == lastLogin);
        if(session!= null){
            session.setDisconnected(System.currentTimeMillis());
            session.setLastServer(lastServer);
        }
        if(BanSystem.getInstance().getConfig().playerOnlineSessionSaving){
            BanSystem.getInstance().getStorage().finishStartedOnlineSession(uuid,lastLogin,System.currentTimeMillis(),lastServer);
        }
        this.onlineTime += (System.currentTimeMillis()-lastLogin);
        this.lastLogin = System.currentTimeMillis();
        BanSystem.getInstance().getStorage().updatePlayerLogoutInfos(this.uuid,this.lastLogin,onlineTime,this.color,bypass,stats.getMessages()+messages);
        BanSystem.getInstance().getStorage().deleteReports(this);
        stats.setMessages(stats.getMessages()+messages);
        update(NetworkPlayerUpdateCause.LOGOUT,new Document().append("reports",reports).append("watchingReportedPlayer",watching));
        this.reports.clear();
    }
    public void updatePlayerStatsAsync(){
        BanSystem.getInstance().getPlatform().getTaskManager().runTaskAsync(this::updatePlayerStats);
    }
    public void updatePlayerStats(){
        BanSystem.getInstance().getStorage().updatePlayerStats(this.uuid,stats);
        update(NetworkPlayerUpdateCause.PLAYERSTATS);
    }
    public void saveProperties(){
        BanSystem.getInstance().getStorage().updatePlayerProperties(this.uuid,this.properties);
        update(NetworkPlayerUpdateCause.PROPERTIES);
    }
}
