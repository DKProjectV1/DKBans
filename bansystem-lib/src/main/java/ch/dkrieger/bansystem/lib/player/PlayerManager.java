package ch.dkrieger.bansystem.lib.player;

import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.UUID;

public class PlayerManager {

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

    public NetworkPlayer getPlayer(int id){

    }
    public NetworkPlayer getPlayer(UUID uuid){

    }
    public NetworkPlayer getPlayerSave(UUID uuid) throws Exception{

    }
    public NetworkPlayer getPlayer(String name){

    }
    public OnlineNetworkPlayer getOnlinePlayer(int id){

    }
    public OnlineNetworkPlayer getOnlinePlayer(UUID uuid){

    }
    public OnlineNetworkPlayer getOnlinePlayer(String name){

    }
    public NetworkPlayer createPlayer(UUID uuid, String name){

    }

}
