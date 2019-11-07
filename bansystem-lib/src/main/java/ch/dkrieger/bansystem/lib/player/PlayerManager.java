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

package ch.dkrieger.bansystem.lib.player;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.filter.FilterType;
import ch.dkrieger.bansystem.lib.player.chatlog.ChatLog;
import ch.dkrieger.bansystem.lib.player.chatlog.ChatLogEntry;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;

public abstract class PlayerManager {

    private Map<UUID,NetworkPlayer> loadedPlayers;
    private long cachedRegisteredCountTime;
    private int cachedRegisteredCount;

    public PlayerManager() {
        this.loadedPlayers = new HashMap<>();
        this.cachedRegisteredCount = -1;
        this.cachedRegisteredCountTime = -1;
    }
    public Map<UUID, NetworkPlayer> getLoadedPlayers() {
        return loadedPlayers;
    }
    public NetworkPlayer searchPlayer(String parser){
        try{
            if(parser.length() > 25){
                return getPlayer(UUID.fromString(parser));
            }
            else {
                NetworkPlayer player = null;
                if(parser.length() < 18) player = getPlayer(parser);
                if(player == null && GeneralUtil.isNumber(parser)) return getPlayer(Integer.valueOf(parser));
                return player;
            }
        }catch (Exception exception){}
        return null;
    }
    public OnlineNetworkPlayer searchOnlinePlayer(String parser){
        try{
            if(parser.length() > 35) return getOnlinePlayer(UUID.fromString(parser));
            else {
                OnlineNetworkPlayer player = null;
                if(parser.length() < 18) player = getOnlinePlayer(parser);
                if(player == null && GeneralUtil.isNumber(parser)) return getPlayer(Integer.valueOf(parser)).getOnlinePlayer();
                return player;
            }
        }catch (Exception exception){}
        return null;
    }
    public int getRegisteredCount(){
        if(this.cachedRegisteredCount > 0 && cachedRegisteredCountTime > System.currentTimeMillis()) return this.cachedRegisteredCount;
        this.cachedRegisteredCount = BanSystem.getInstance().getStorage().getRegisteredPlayerCount();
        cachedRegisteredCountTime = System.currentTimeMillis()+ TimeUnit.MINUTES.toMillis(15);
        return cachedRegisteredCount;
    }
    public List<NetworkPlayer> getPlayers(String ip){
        return BanSystem.getInstance().getStorage().getPlayersByIp(ip);
    }

    public NetworkPlayer getPlayer(int id){
        NetworkPlayer player = GeneralUtil.iterateOne(this.loadedPlayers.values(), object -> object.getID() == id);
        if(player == null){
            try{
                player = BanSystem.getInstance().getStorage().getPlayer(id);
            }catch (Exception exception){}
            if(player != null) this.loadedPlayers.put(player.getUUID(),player);
        }
        return player;
    }
    public NetworkPlayer getPlayer(UUID uuid){
        NetworkPlayer player = loadedPlayers.get(uuid);
        if(player == null){
            try{
                player = getPlayerSave(uuid);
            }catch (Exception exception){}
        }
        return player;
    }
    public NetworkPlayer getPlayerSave(UUID uuid) throws Exception{
        NetworkPlayer player = BanSystem.getInstance().getStorage().getPlayer(uuid);
        if(player != null) this.loadedPlayers.put(uuid,player);
        return player;
    }
    public NetworkPlayer getPlayer(String name){
        NetworkPlayer player = GeneralUtil.iterateOne(this.loadedPlayers.values(), object -> object.getName().equalsIgnoreCase(name));
        if(player == null){
            try{
                player = BanSystem.getInstance().getStorage().getPlayer(name);
                if(player != null) this.loadedPlayers.put(player.getUUID(),player);
            }catch (Exception exception){
                UUID uuid = GeneralUtil.getMojangUUIDByName(name);
                if(uuid != null) {
                    player = new NetworkPlayer(uuid, name, "unknown", "unknown");
                    int id = BanSystem.getInstance().getStorage().createPlayer(player);
                    player.setID(id);
                }
            }
        }
        return player;
    }
    public ChatLog getChatLog(NetworkPlayer player){
        return getChatLog(player.getUUID());
    }
    public ChatLog getChatLog(UUID uuid){
        return BanSystem.getInstance().getStorage().getChatLog(uuid);
    }
    public ChatLog getChatLog(String server){
        return BanSystem.getInstance().getStorage().getChatLog(server);
    }
    public ChatLog getChatLog(UUID uuid, String server){
        return BanSystem.getInstance().getStorage().getChatLog(uuid,server);
    }
    public IPBan banIp(String ip){
        return banIp(ip,-1,TimeUnit.DAYS);
    }
    public IPBan banIp(String ip,long duration,TimeUnit unit){
        return banIp(ip,duration, unit,null);
    }
    public IPBan banIp(String ip,long duration,TimeUnit unit, UUID lastPlayer){
        return banIp(new IPBan(lastPlayer,ip,System.currentTimeMillis(),duration<=0?-1:System.currentTimeMillis()+unit.toMillis(duration)));
    }
    public IPBan banIp(IPBan ipban){
        BanSystem.getInstance().getStorage().banIp(ipban);
        return ipban;
    }
    public void unbanIp(String ip){
        BanSystem.getInstance().getStorage().unbanIp(ip);
    }
    public void unbanIp(UUID lastIp){
        BanSystem.getInstance().getStorage().unbanIp(lastIp);
    }
    public IPBan getIpBan(String ip){
        IPBan ban = BanSystem.getInstance().getStorage().getIpBan(ip);
        if(ban != null && (ban.getTimeOut() > 0 && ban.getTimeOut() <= System.currentTimeMillis())){
            BanSystem.getInstance().getStorage().unbanIp(ip);
            return null;
        }
        else return ban;
    }
    public boolean isIPBanned(String ip){
        return getIpBan(ip) != null;
    }

    public NetworkPlayer createPlayer(UUID uuid, String name, String ip){
        resetCachedCounts();
        NetworkPlayer player = new NetworkPlayer(uuid,name,ip,getCountry(ip));
        player.setID(BanSystem.getInstance().getStorage().createPlayer(player));
        this.loadedPlayers.put(uuid,player);
        return player;
    }
    public void removePlayerFromCache(NetworkPlayer player){
        removePlayerFromCache(player.getUUID());
    }
    public void createChatLogEntry(UUID uuid, String message, String server){
        createChatLogEntry(uuid,message,server,null);
    }
    public void createChatLogEntry(UUID uuid, String message, String server, FilterType filter){
        createChatLogEntry(new ChatLogEntry(uuid, message, server,System.currentTimeMillis(), filter));
    }
    public void createChatLogEntry(ChatLogEntry entry){
        BanSystem.getInstance().getStorage().createChatLogEntry(entry);
    }

    public void removePlayerFromCache(UUID uuid){
        this.loadedPlayers.remove(uuid);
    }

    public void resetCachedCounts(){
        this.cachedRegisteredCount = -1;
    }
    public String getCountry(String ip){
        try{
            InputStream stream = new URL("http://ip-api.com/json/"+ip+"?fields=country").openStream();
            Scanner scanner = new Scanner(stream, "UTF-8").useDelimiter("\\A");
            Document document = Document.loadData(scanner.next());
            return document.contains("country")?document.getString("country"): Messages.UNKNOWN;
        }catch (Exception exception){}
        return Messages.UNKNOWN;
    }

    public abstract OnlineNetworkPlayer getOnlinePlayer(int id);

    public abstract OnlineNetworkPlayer getOnlinePlayer(UUID uuid);

    public abstract OnlineNetworkPlayer getOnlinePlayer(String name);

    public abstract List<OnlineNetworkPlayer> getOnlinePlayers();

    public abstract void removeOnlinePlayerFromCache(OnlineNetworkPlayer player);

    public abstract void removeOnlinePlayerFromCache(UUID uuid);

    public abstract void updatePlayer(NetworkPlayer player, NetworkPlayerUpdateCause cause, Document properties);

    public abstract void updateOnlinePlayer(OnlineNetworkPlayer player);

    public abstract int getOnlineCount();
}
