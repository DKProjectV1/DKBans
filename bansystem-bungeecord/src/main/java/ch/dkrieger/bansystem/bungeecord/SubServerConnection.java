package ch.dkrieger.bansystem.bungeecord;

import ch.dkrieger.bansystem.bungeecord.event.ProxiedDKBansMessageReceiveEvent;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.JoinMe;
import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.player.NetworkPlayerUpdateCause;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import ch.dkrieger.bansystem.lib.utils.Document;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.*;
import java.util.UUID;

public class SubServerConnection implements Listener {

    private boolean active;

    public SubServerConnection() {
        active = false;
    }

    @EventHandler
    public void onPluginMessageReceive(PluginMessageEvent event){
        if(event.getTag().equalsIgnoreCase("DKBans:DKBans") && event.getSender() instanceof Server){
            ByteArrayInputStream b = new ByteArrayInputStream(event.getData());
            DataInputStream in = new DataInputStream(b);

            BungeeCord.getInstance().getScheduler().runAsync(BungeeCordBanSystemBootstrap.getInstance(),()->{
                try{
                    Document document = Document.loadData(in.readUTF());
                    if(document.getString("action").equalsIgnoreCase("updatePlayer")){
                        UUID uuid = document.getObject("uuid",UUID.class);
                        BanSystem.getInstance().getPlayerManager().removePlayerFromCache(uuid);
                        BungeeCordBanSystemBootstrap.getInstance().executePlayerUpdateEvents(uuid
                                ,document.getObject("cause", NetworkPlayerUpdateCause.class)
                                ,document.getObject("properties",Document.class),false);
                    }else if(document.getString("action").equalsIgnoreCase("sendMesssage")){
                        ProxiedPlayer player = BungeeCord.getInstance().getPlayer(document.getObject("uuid",UUID.class));
                        if(player != null) player.sendMessage(document.getObject("message", TextComponent.class));
                    }else if(document.getString("action").equalsIgnoreCase("connect")){
                        ProxiedPlayer player = BungeeCord.getInstance().getPlayer(document.getObject("uuid",UUID.class));
                        if(player != null){
                            ServerInfo server = BungeeCord.getInstance().getServerInfo("server");
                            if(server != null && player.getServer().getInfo() != server) player.connect(server);
                        }
                    }else if(document.getString("action").equalsIgnoreCase("executeCommand")){
                        ProxiedPlayer player = BungeeCord.getInstance().getPlayer(document.getObject("uuid",UUID.class));
                        if(player != null) BungeeCord.getInstance().getPluginManager().dispatchCommand(player,document.getString("command"));
                    }else if(document.getString("action").equalsIgnoreCase("kick")){
                        ProxiedPlayer player = BungeeCord.getInstance().getPlayer(document.getObject("uuid",UUID.class));
                        if(player != null){
                            Kick kick = document.getObject("kick", Kick.class);
                            player.disconnect(kick.toMessage());
                        }
                    }else if(document.getString("action").equalsIgnoreCase("sendBan")){
                        ProxiedPlayer player = BungeeCord.getInstance().getPlayer(document.getObject("uuid",UUID.class));
                        if(player != null){
                            Ban ban = document.getObject("ban", Ban.class);
                            if(ban.getBanType() == BanType.NETWORK) player.disconnect(ban.toMessage());
                            else player.sendMessage(ban.toMessage());;
                        }
                    }else if(document.getString("action").equalsIgnoreCase("broadcast")){
                        if(document.contains("message")){
                            BungeeCord.getInstance().broadcast(document.getObject("message",TextComponent.class));
                        }else BanSystem.getInstance().getNetwork().broadcastLocal(document.getObject("broadcast", Broadcast.class));
                    }else if(document.getString("action").equalsIgnoreCase("sendJoinMe")){
                        BanSystem.getInstance().getNetwork().sendJoinMe(document.getObject("joinme", JoinMe.class));
                    }else if(document.getString("action").equalsIgnoreCase("sendTeamMessage")){
                        BanSystem.getInstance().getNetwork().sendTeamMessage(document.getObject("message",TextComponent.class)
                                ,document.getBoolean("onlyLogin"));
                    }else if(document.getString("action").equalsIgnoreCase("reloadFilter")){
                        BanSystem.getInstance().getFilterManager().reloadLocal();
                    }else if(document.getString("action").equalsIgnoreCase("reloadBroadcast")){
                        BanSystem.getInstance().getBroadcastManager().reloadLocal();
                    }else BungeeCord.getInstance().getPluginManager().callEvent(new ProxiedDKBansMessageReceiveEvent(document));
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            });
        }
    }
    public void sendToAll(String action,Document document){
        sendToAll(action,document,null);
    }
    public void sendToAll(String action,Document document, String from){
        for(ServerInfo server : BungeeCord.getInstance().getServers().values()){
            if(from != null && server.getName().equalsIgnoreCase(from)) return;
            send(server,action,document);
        }
    }
    public void enable(){
        active = true;
        BungeeCord.getInstance().registerChannel("DKBans:DKBans");
    }
    public void send(ServerInfo server,String action, Document document){
        if(!active) throw new IllegalArgumentException("SubServerConnection is not enabled");
        try {
            document.append("action",action);
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);

            out.writeUTF(document.toJson());
            server.sendData("DKBans:DKBans",b.toByteArray());
        }catch (IOException exception){
            exception.printStackTrace();
        }

    }
}
