package ch.dkrieger.bansystem.lib.player;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.filter.FilterType;
import ch.dkrieger.bansystem.lib.player.chatlog.ChatLog;
import ch.dkrieger.bansystem.lib.player.chatlog.ChatLogEntry;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class PlayerManager {

    private Map<UUID,NetworkPlayer> loadedPlayers;
    private int cachedRegisteredCount, cachedCountryCount;

    public PlayerManager() {
        this.loadedPlayers = new HashMap<>();
        this.cachedRegisteredCount = -1;
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
        if(this.cachedRegisteredCount < 0) return this.cachedRegisteredCount;
        else return BanSystem.getInstance().getStorage().getRegisteredPlayerCount();
    }
    public int getCountryCount(){
        if(this.cachedCountryCount < 0) return this.cachedCountryCount;
        else return BanSystem.getInstance().getStorage().getCountryCount();
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
        long start = System.currentTimeMillis();
        if(player == null){
            try{
                player = getPlayerSave(uuid);
            }catch (Exception exception){}
        }
        System.out.println("Loaded player in "+(System.currentTimeMillis()-start)+"ms");
        return player;
    }
    public NetworkPlayer getPlayerSave(UUID uuid) throws Exception{
        System.out.println("get save");
        NetworkPlayer player = BanSystem.getInstance().getStorage().getPlayer(uuid);
        if(player != null) this.loadedPlayers.put(uuid,player);
        return player;
    }
    public NetworkPlayer getPlayer(String name){
        long start = System.currentTimeMillis();
        NetworkPlayer player = GeneralUtil.iterateOne(this.loadedPlayers.values(), object -> object.getName().equalsIgnoreCase(name));
        if(player == null){
            try{
                player = BanSystem.getInstance().getStorage().getPlayer(name);
                if(player != null) this.loadedPlayers.put(player.getUUID(),player);
            }catch (Exception exception){}
        }
        System.out.println("Loaded player in "+(System.currentTimeMillis()-start)+"ms");
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

    public NetworkPlayer createPlayer(UUID uuid, String name, String ip){
        NetworkPlayer player = new NetworkPlayer(uuid,name,ip,"//Implement");
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
        this.cachedCountryCount = -1;
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
