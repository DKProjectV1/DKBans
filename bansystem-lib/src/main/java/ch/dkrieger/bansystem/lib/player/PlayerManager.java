package ch.dkrieger.bansystem.lib.player;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.filter.FilterType;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import sun.nio.ch.Net;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class PlayerManager {

    private Map<UUID,NetworkPlayer> loadedPlayers;

    public PlayerManager() {
        this.loadedPlayers = new HashMap<>();
    }
    public Map<UUID, NetworkPlayer> getLoadedPlayers() {
        return loadedPlayers;
    }
    public NetworkPlayer searchPlayer(String parser){
        try{
            if(parser.length() > 35) return getPlayer(UUID.fromString(parser));
            else {
                NetworkPlayer player = null;
                if(parser.length() < 18) player = getPlayer(parser);
                if(player == null && GeneralUtil.isNumber(parser)) return getPlayer(Integer.valueOf(parser));
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
            }
        }catch (Exception exception){}
        return null;
    }
    public int getRegisteredCount(){

    }
    public int getCountryCount(){

    }

    public List<NetworkPlayer> getPlayers(String ip){

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
            }catch (Exception exception){}
        }
        return player;
    }
    public NetworkPlayer createPlayer(UUID uuid, String name){
        NetworkPlayer player = new NetworkPlayer();
        BanSystem.getInstance().getStorage().createPlayer(player);
        this.loadedPlayers.put(uuid,player);
        return player;
    }
    public void removePlayerFromCache(NetworkPlayer player){
        removePlayerFromCache(player.getUUID());
    }
    public void createChatLog(UUID uuid, String message, String server){
        createChatLog(uuid,message,server,null);
    }
    public void createChatLog(UUID uuid, String message, String server, FilterType filteredBy){

    }

    public void removePlayerFromCache(UUID uuid){
        this.loadedPlayers.remove(uuid);
    }
    public abstract OnlineNetworkPlayer getOnlinePlayer(int id);

    public abstract OnlineNetworkPlayer getOnlinePlayer(UUID uuid);

    public abstract OnlineNetworkPlayer getOnlinePlayer(String name);

    public abstract List<OnlineNetworkPlayer> getOnlinePlayers();

    public abstract void removeOnlinePlayerFromCache(OnlineNetworkPlayer player);

    public abstract void removeOnlinePlayerFromCache(UUID uuid);

    public abstract int getOnlineCount();
}
