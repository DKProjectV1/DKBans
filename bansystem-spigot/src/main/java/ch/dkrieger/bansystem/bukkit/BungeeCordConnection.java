package ch.dkrieger.bansystem.bukkit;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 22.11.18 19:35
 *
 */

import ch.dkrieger.bansystem.bukkit.event.BukkitDKBansMessageReceiveEvent;
import ch.dkrieger.bansystem.bukkit.event.BukkitOnlineNetworkPlayerUpdateEvent;
import ch.dkrieger.bansystem.bukkit.player.bungeecord.BukkitBungeeCordPlayerManager;
import ch.dkrieger.bansystem.bukkit.player.bungeecord.BungeeCordOnlinePlayer;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.player.NetworkPlayerUpdateCause;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.utils.Document;
import com.google.gson.reflect.TypeToken;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;
import java.util.List;
import java.util.UUID;

public class BungeeCordConnection implements PluginMessageListener {

    private boolean active;

    public BungeeCordConnection() {
        active = false;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if(channel.equalsIgnoreCase("DKBans:DKBans")){
            Bukkit.getScheduler().runTaskAsynchronously(BukkitBanSystemBootstrap.getInstance(),()->{
                try{
                    if(Bukkit.getOnlineMode()) return;
                    ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
                    DataInputStream input = new DataInputStream(byteStream);
                    Document document = Document.loadData(input.readUTF());
                    if(document.getString("action").equalsIgnoreCase("updatePlayer")){
                        UUID uuid = document.getObject("uuid",UUID.class);
                        NetworkPlayerUpdateCause cause = document.getObject("cause", NetworkPlayerUpdateCause.class);
                        BanSystem.getInstance().getPlayerManager().removePlayerFromCache(uuid);
                        if(cause == NetworkPlayerUpdateCause.LOGOUT) BanSystem.getInstance().getPlayerManager().removeOnlinePlayerFromCache(uuid);
                        BukkitBanSystemBootstrap.getInstance().executePlayerUpdateEvents(uuid,cause
                                ,document.getObject("properties",Document.class),false);
                    }else if(document.getString("action").equalsIgnoreCase("updateOnlinePlayers")){
                        ((BukkitBungeeCordPlayerManager)BanSystem.getInstance().getPlayerManager())
                                .updateAll(document.getObject("players",new TypeToken<List<BungeeCordOnlinePlayer>>(){}.getType()));
                    }else if(document.getString("action").equalsIgnoreCase("updateOnlinePlayer")){
                        UUID uuid = document.getObject("uuid",UUID.class);
                        OnlineNetworkPlayer online = BanSystem.getInstance().getPlayerManager().getOnlinePlayer(uuid);
                        if(online instanceof BungeeCordOnlinePlayer){
                            ((BungeeCordOnlinePlayer) online).setServer(document.getString("server"));
                        }else{
                            online = new BungeeCordOnlinePlayer(uuid,document.getString("name"),document.getString("server"),"Proxy-1");
                            ((BukkitBungeeCordPlayerManager)BanSystem.getInstance().getPlayerManager()).insertOnlinePlayer(online);
                        }
                        Bukkit.getPluginManager().callEvent(new BukkitOnlineNetworkPlayerUpdateEvent(uuid,System.currentTimeMillis(),false));
                    }else if(document.getString("action").equalsIgnoreCase("reloadFilter")){
                        BanSystem.getInstance().getFilterManager().reloadLocal();
                    }else if(document.getString("action").equalsIgnoreCase("reloadBroadcast")){
                        BanSystem.getInstance().getBroadcastManager().reloadLocal();
                    }else Bukkit.getPluginManager().callEvent(new BukkitDKBansMessageReceiveEvent(document));
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            });
        }
    }
    public void enable(){
        this.active = true;
        Bukkit.getMessenger().registerOutgoingPluginChannel(BukkitBanSystemBootstrap.getInstance(),"DKBans:DKBans");
        Bukkit.getMessenger().registerIncomingPluginChannel(BukkitBanSystemBootstrap.getInstance(),"DKBans:DKBans",this);
    }
    public void send(String action, Document document){
        if(Bukkit.getOnlineMode()) return;
        if(!active) throw new IllegalArgumentException("SubServerConnection is not enabled");
        System.out.println("sending "+action);
        document.append("action",action);
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);

            out.writeUTF(document.toJson());
            if(Bukkit.getOnlinePlayers().size() == 0){
                System.out.println(Messages.SYSTEM_PREFIX+"Updater: Could not send data to bukkit.");
                return;
            }
            for(Player player : Bukkit.getOnlinePlayers()){
                player.sendPluginMessage(BukkitBanSystemBootstrap.getInstance(),"DKBans:DKBans",b.toByteArray());
                return;
            }
        }catch (IOException exception){
            System.out.println(Messages.SYSTEM_PREFIX+"Updater: Could not send data to bukkit.");
            System.out.println(Messages.SYSTEM_PREFIX+"Updater: Error - "+exception.getMessage());
        }
    }
}
