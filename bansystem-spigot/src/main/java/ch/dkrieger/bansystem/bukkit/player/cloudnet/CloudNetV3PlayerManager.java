package ch.dkrieger.bansystem.bukkit.player.cloudnet;

import ch.dkrieger.bansystem.bukkit.BukkitBanSystemBootstrap;
import ch.dkrieger.bansystem.bukkit.event.BukkitOnlineNetworkPlayerUpdateEvent;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.cloudnet.v3.CloudNetV3OnlinePlayer;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.NetworkPlayerUpdateCause;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.PlayerManager;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import com.google.gson.reflect.TypeToken;
import de.dytanic.cloudnet.ext.bridge.bukkit.event.BukkitBridgeProxyPlayerDisconnectEvent;
import de.dytanic.cloudnet.ext.bridge.bukkit.event.BukkitBridgeProxyPlayerServerSwitchEvent;
import de.dytanic.cloudnet.ext.bridge.bukkit.event.BukkitChannelMessageReceiveEvent;
import de.dytanic.cloudnet.wrapper.Wrapper;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class CloudNetV3PlayerManager extends PlayerManager implements Listener {

    private Map<UUID,CloudNetV3OnlinePlayer> externalPlayers;

    public CloudNetV3PlayerManager() {
        this.externalPlayers = new HashMap<>();
        Wrapper.getInstance().sendChannelMessage("DKBans","getOnlinePlayers",new de.dytanic.cloudnet.common.document.Document());
    }
    @Override
    public OnlineNetworkPlayer getOnlinePlayer(int id) {
        NetworkPlayer networkPlayer = getPlayer(id);
        if(networkPlayer == null) return null;
        return getOnlinePlayer(networkPlayer.getUUID());
    }

    @Override
    public OnlineNetworkPlayer getOnlinePlayer(UUID uuid) {
        return externalPlayers.get(uuid);
    }
    @Override
    public OnlineNetworkPlayer getOnlinePlayer(String name) {
        return GeneralUtil.iterateOne(externalPlayers.values(), object -> object.getName().equalsIgnoreCase(name));
    }
    @Override
    public List<OnlineNetworkPlayer> getOnlinePlayers() {
        return new ArrayList<>(this.externalPlayers.values());
    }

    /*

    If you have a good Idea for a new future, create a
     */
    @Override
    public void removeOnlinePlayerFromCache(OnlineNetworkPlayer player) {
        this.externalPlayers.remove(player.getUUID());
    }

    @Override
    public void removeOnlinePlayerFromCache(UUID uuid) {
        this.externalPlayers.remove(uuid);
    }

    @Override
    public void updatePlayer(NetworkPlayer player, NetworkPlayerUpdateCause cause, Document properties) {
        de.dytanic.cloudnet.common.document.Document data = de.dytanic.cloudnet.common.document.Document.newDocument(properties.toJson());
        Wrapper.getInstance().sendChannelMessage("DKBans","updatePlayer",data
                .append("uuid",player.getUUID()).append("sender",Wrapper.getInstance().getServiceId().getName()).append("cause",cause).append("properties",properties));
        BukkitBanSystemBootstrap.getInstance().executePlayerUpdateEvents(player.getUUID(),cause,properties,true);
    }

    @Override
    public void updateOnlinePlayer(OnlineNetworkPlayer player) {}

    @Override
    public int getOnlineCount() {
        return this.externalPlayers.size();
    }
    @EventHandler
    public void onServerSwitch(BukkitBridgeProxyPlayerServerSwitchEvent event){
        CloudNetV3OnlinePlayer player = externalPlayers.get(event.getNetworkConnectionInfo().getUniqueId());
        if(player == null){
            player = new CloudNetV3OnlinePlayer(event.getNetworkConnectionInfo().getUniqueId(),event.getNetworkConnectionInfo().getName()
                    ,event.getNetworkServiceInfo().getServerName(),"Unknown");
            this.externalPlayers.put(player.getUUID(),player);
        }else player.setServer(event.getNetworkServiceInfo().getServerName());
        Bukkit.getPluginManager().callEvent(new BukkitOnlineNetworkPlayerUpdateEvent(player.getUUID(),System.currentTimeMillis(),false));
    }
    @EventHandler
    public void onDisconnect(BukkitBridgeProxyPlayerDisconnectEvent event){
        this.externalPlayers.remove(event.getNetworkConnectionInfo().getUniqueId());
    }
    @EventHandler
    public void onMessageReceive(BukkitChannelMessageReceiveEvent event){
        if(event.getChannel().equalsIgnoreCase("DKBans")){
            if(event.getMessage().equalsIgnoreCase("updatePlayer") &&
                    !Wrapper.getInstance().getServiceId().getName().equalsIgnoreCase(event.getData().getString("sender"))){
                UUID uuid = event.getData().get("uuid",UUID.class);
                BanSystem.getInstance().getPlayerManager().removePlayerFromCache(uuid);
                BukkitBanSystemBootstrap.getInstance().executePlayerUpdateEvents(uuid
                        ,event.getData().get("cause", NetworkPlayerUpdateCause.class)
                        ,Document.loadData(event.getData().toJson()),false);
            }else if(event.getMessage().equalsIgnoreCase("pingResult")){
                UUID uuid = event.getData().get("uuid",UUID.class);
                CloudNetV3OnlinePlayer.PINGGETTER.put(uuid,event.getData().getInt("ping"));
            }else if(event.getMessage().equalsIgnoreCase("reloadFilter")){
                BanSystem.getInstance().getFilterManager().reloadLocal();
            }else if(event.getMessage().equalsIgnoreCase("reloadBroadcast")){
                BanSystem.getInstance().getBroadcastManager().reloadLocal();
            }else if(event.getMessage().equalsIgnoreCase("syncOnlinePlayers")){
                List<CloudNetV3OnlinePlayer> players = event.getData().get("players"
                        ,new TypeToken<List<CloudNetV3OnlinePlayer>>(){}.getType());
                for(CloudNetV3OnlinePlayer player : players) this.externalPlayers.put(player.getUUID(),player);
            }
        }
    }
}
