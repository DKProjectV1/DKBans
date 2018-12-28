package ch.dkrieger.bansystem.bukkit.hook;

import ch.dkrieger.bansystem.bukkit.BukkitBanSystemBootstrap;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;

public class PlaceHolderApiHook extends EZPlaceholderHook {

    public PlaceHolderApiHook() {
        super(BukkitBanSystemBootstrap.getInstance(),"dkbans");
    }

    @Override
    public String onPlaceholderRequest(Player requestPlayer, String identifier) {
        if(identifier.startsWith("player_")){
            NetworkPlayer player = BanSystem.getInstance().getPlayerManager().getPlayer(requestPlayer.getUniqueId());
            if(player == null) return "PlayerNotFound";
            if(identifier.endsWith("name")){
                return player.getColoredName();
            }else if(identifier.endsWith("color")){
                return player.getColor();
            }else if(identifier.endsWith("ip")){
                return player.getIP();
            }else if(identifier.endsWith("country")){
                return player.getCountry();
            }else if(identifier.endsWith("id")){
                return String.valueOf(player.getID());
            }else if(identifier.endsWith("uuid")){
                return player.getUUID().toString();
            }else if(identifier.endsWith("report")){
                return (player.isReportLoggedIn()? Messages.STAFF_STATUS_LOGIN:Messages.STAFF_STATUS_LOGOUT);
            }else if(identifier.endsWith("color")){
                return player.getColor();
            }else if(identifier.endsWith("color")){
                return player.getColor();
            }else if(identifier.endsWith("color")){
                return player.getColor();
            }else if(identifier.endsWith("color")){
                return player.getColor();
            }else if(identifier.endsWith("color")){
                return player.getColor();
            }
        }
        return "&cPlaceHolderNotFound";
    }
}
