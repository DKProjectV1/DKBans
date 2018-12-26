package ch.dkrieger.bansystem.bukkit.network;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.DKNetwork;
import ch.dkrieger.bansystem.lib.JoinMe;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import ch.dkrieger.bansystem.bukkit.BukkitBanSystemBootstrap;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class BukkitNetwork implements DKNetwork {

    private Map<UUID,JoinMe> joinmes;

    public BukkitNetwork() {
        joinmes = new HashMap<>();
    }

    @Override
    public JoinMe getJoinMe(OnlineNetworkPlayer player) {
        return getJoinMe(player.getUUID());
    }

    @Override
    public JoinMe getJoinMe(NetworkPlayer player) {
        return getJoinMe(player.getUUID());
    }

    @Override
    public JoinMe getJoinMe(UUID uuid) {
        return joinmes.get(uuid);
    }

    @Override
    public List<String> getPlayersOnServer(String server) {
        List<String> result = new ArrayList<>();
        for(Player player : Bukkit.getOnlinePlayers()) result.add(player.getName());
        return result;
    }

    @Override
    public void broadcast(String message) {
        Bukkit.broadcastMessage(message);
    }

    @Override
    public void broadcast(TextComponent component) {
        for(Player player : Bukkit.getOnlinePlayers()) BukkitBanSystemBootstrap.getInstance().sendTextComponent(player,component);
    }

    @Override
    public void broadcast(Broadcast broadcast) {
        if(broadcast == null) return;
        for(Player player : Bukkit.getOnlinePlayers()){
            if(broadcast.getPermission() == null || broadcast.getPermission().length() == 0|| player.hasPermission(broadcast.getPermission())) {
                NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
                BukkitBanSystemBootstrap.getInstance().sendTextComponent(player,GeneralUtil.replaceTextComponent(Messages.BROADCAST_FORMAT_SEND.replace("[prefix]",Messages.PREFIX_NETWORK)
                        ,"[message]",broadcast.build(networkPlayer)));
            }
        }
    }

    @Override
    public void broadcastLocal(Broadcast broadcast) {
        broadcast(broadcast);
    }

    @Override
    public void sendJoinMe(JoinMe joinMe) {
        this.joinmes.put(joinMe.getUUID(),joinMe);
        List<TextComponent> components = joinMe.create();
        for(Player player : Bukkit.getOnlinePlayers()){
            for(TextComponent component : components) BukkitBanSystemBootstrap.getInstance().sendTextComponent(player,component);
        }
    }

    @Override
    public void sendTeamMessage(String message) {
        sendTeamMessage(message,false);
    }

    @Override
    public void sendTeamMessage(String message, boolean onlyLogin) {
        sendTeamMessage(new TextComponent(message),onlyLogin);
    }

    @Override
    public void sendTeamMessage(TextComponent component) {
        sendTeamMessage(component,false);
    }

    @Override
    public void sendTeamMessage(TextComponent component, boolean onlyLogin) {
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player.hasPermission("dkbans.team")){
                NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
                if(networkPlayer != null && (!onlyLogin || networkPlayer.isTeamChatLoggedIn())) BukkitBanSystemBootstrap.getInstance().sendTextComponent(player,component);
            }
        }
    }

    @Override
    public void reloadFilter() {
        BanSystem.getInstance().getFilterManager().reloadLocal();
    }

    @Override
    public void reloadBroadcast() {
        BanSystem.getInstance().getBroadcastManager().reloadLocal();
    }
}
