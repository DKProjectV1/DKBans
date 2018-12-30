/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 30.12.18 14:39
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

package ch.dkrieger.bansystem.lib.storage;

import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.filter.Filter;
import ch.dkrieger.bansystem.lib.player.IPBan;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineSession;
import ch.dkrieger.bansystem.lib.player.chatlog.ChatLog;
import ch.dkrieger.bansystem.lib.player.chatlog.ChatLogEntry;
import ch.dkrieger.bansystem.lib.player.history.History;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.HistoryEntry;
import ch.dkrieger.bansystem.lib.report.Report;
import ch.dkrieger.bansystem.lib.stats.NetworkStats;
import ch.dkrieger.bansystem.lib.stats.PlayerStats;
import ch.dkrieger.bansystem.lib.utils.Document;

import java.util.List;
import java.util.UUID;

public interface DKBansStorage {

    public boolean connect();

    public boolean isConnected();

    public void disconnect();

    public NetworkPlayer getPlayer(int id) throws Exception;

    public NetworkPlayer getPlayer(String name) throws Exception;

    public NetworkPlayer getPlayer(UUID uuid) throws Exception;

    public List<NetworkPlayer> getPlayersByIp(String ip);

    public int getRegisteredPlayerCount();

    public int createPlayer(NetworkPlayer player);

    public void createOnlineSession(NetworkPlayer player, OnlineSession session);

    public void finishStartedOnlineSession(UUID uuid, long login, long logout, String server);

    public void saveStaffSettings(UUID player, boolean report,boolean teamChat);

    public void setColor(UUID player, String color);

    public void updatePlayerLoginInfos(UUID player,String name, long lastLogin, String color, boolean bypass, String lastIP,String lastCountry, long logins);

    public void updatePlayerLogoutInfos(UUID player, long lastLogin, long onlineTime, String color, boolean bypass, long messages);

    public void updatePlayerStats(UUID uuid, PlayerStats stats);

    public void updatePlayerProperties(UUID uuid, Document properties);

    public ChatLog getChatLog(UUID player);

    public ChatLog getChatLog(String server);

    public ChatLog getChatLog(UUID player, String server);

    public void createChatLogEntry(ChatLogEntry entry);

    public void deleteOldChatLog(long before);

    public History getHistory(UUID player);

    public void clearHistory(NetworkPlayer player);

    public int createHistoryEntry(NetworkPlayer player, HistoryEntry entry);

    public void deleteHistoryEntry(NetworkPlayer player,int id);

    public List<Report> getReports();

    public void createReport(Report report);

    public void processReports(NetworkPlayer player, NetworkPlayer staff);

    public void deleteReports(NetworkPlayer player);

    public HistoryEntry getHistoryEntry(int id);

    @SuppressWarnings("This methode is dangerous, it (can) return many datas and have a long delay.")
    public List<Ban> getNotTimeOutedBans( );

    @SuppressWarnings("This methode is dangerous, it (can) return many datas and have a long delay.")
    public List<Ban> getBans();

    public IPBan getIpBan(String ip);

    public void banIp(IPBan ipBan);

    public void unbanIp(String ip);

    public void unbanIp(UUID lastPlayer);

    public List<Filter> loadFilters();

    public int createFilter(Filter filter);

    public void deleteFilter(int id);

    public List<Broadcast> loadBroadcasts();

    public int createBroadcast(Broadcast broadcast);

    public void updateBroadcast(Broadcast broadcast);

    public void deleteBroadcast(int id);

    public NetworkStats getNetworkStats();

    public void updateNetworkStats(long logins, long reports, long reportsAccepted, long messages, long bans, long mutes, long unbans, long kicks);

}
