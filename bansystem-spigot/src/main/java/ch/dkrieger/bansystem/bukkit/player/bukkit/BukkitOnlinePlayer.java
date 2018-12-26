package ch.dkrieger.bansystem.bukkit.player.bukkit;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import ch.dkrieger.bansystem.bukkit.BukkitBanSystemBootstrap;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import java.util.UUID;

public class BukkitOnlinePlayer implements OnlineNetworkPlayer {

    private Player player;

    public BukkitOnlinePlayer(Player player) {
        this.player = player;
    }

    @Override
    public UUID getUUID() {
        return player.getUniqueId();
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public String getProxy() {
        return "Proxy-1";
    }

    @Override
    public String getServer() {
        return player.getWorld().getName();
    }

    @Override
    public int getPing() {
        try {
            Object playerHandle = player.getClass().getMethod("getHandle").invoke(player);
            return (int)playerHandle.getClass().getField( "ping").get(playerHandle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public NetworkPlayer getPlayer() {
        return BanSystem.getInstance().getPlayerManager().getPlayer(player.getUniqueId());
    }

    @Override
    public void sendMessage(String message) {
        player.sendMessage(message);
    }

    @Override
    public void sendMessage(TextComponent component) {
        BukkitBanSystemBootstrap.getInstance().sendTextComponent(player,component);
    }

    @Override
    public void connect(String server) {
        World world = Bukkit.getWorld(server);
        if(world != null) player.teleport(world.getSpawnLocation());
    }

    @Override
    public void executeCommand(String command) {
        Bukkit.dispatchCommand(player,command);
    }

    @Override
    public void sendBan(Ban ban) {
        if(ban.getBanType() == BanType.NETWORK) kick(ban.toMessage());
        else BukkitBanSystemBootstrap.getInstance().sendTextComponent(player,ban.toMessage());
    }

    @Override
    public void kick(Kick kick) {
        kick(kick.toMessage());
    }
    public void kick(TextComponent component){
       Bukkit.getScheduler().runTask(BukkitBanSystemBootstrap.getInstance(),()->{ player.kickPlayer(component.toLegacyText());});
    }
}
