package ch.dkrieger.bansystem.bukkit.player.bungeecord;

import ch.dkrieger.bansystem.bukkit.BukkitBanSystemBootstrap;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import ch.dkrieger.bansystem.lib.utils.Document;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

public class BungeeCordOnlinePlayer implements OnlineNetworkPlayer {

    private UUID uuid;
    private String name, server, proxy;

    public BungeeCordOnlinePlayer(UUID uuid, String name, String server, String proxy) {
        this.uuid = uuid;
        this.name = name;
        this.server = server;
        this.proxy = proxy;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public String getProxy() {
        return proxy;
    }
    @Override
    public String getServer() {
        return server;
    }
    @Override
    public int getPing() {
        return -1;
    }

    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public NetworkPlayer getPlayer() {
        return BanSystem.getInstance().getPlayerManager().getPlayer(uuid);
    }
    @Override
    public void sendMessage(String message) {
        sendMessage(new TextComponent(message));
    }
    @Override
    public void sendMessage(TextComponent component) {
        BukkitBanSystemBootstrap.getInstance().getBungeeCordConnection().send("sendMesssage"
                ,new Document().append("uuid",uuid).append("message",component));
    }
    @Override
    public void connect(String server) {
        BukkitBanSystemBootstrap.getInstance().getBungeeCordConnection().send("connect"
                ,new Document().append("uuid",uuid).append("server",server));
    }
    @Override
    public void executeCommand(String command) {
        BukkitBanSystemBootstrap.getInstance().getBungeeCordConnection().send("executeCommand"
                ,new Document().append("uuid",uuid).append("command",command));
    }
    @Override
    public void sendBan(Ban ban) {
        BukkitBanSystemBootstrap.getInstance().getBungeeCordConnection().send("sendBan"
                ,new Document().append("uuid",uuid).append("ban",ban));
    }
    @Override
    public void kick(Kick kick) {
        System.out.println("kick to proxy");
        BukkitBanSystemBootstrap.getInstance().getBungeeCordConnection().send("kick"
                ,new Document().append("uuid",uuid).append("kick",kick));
    }
}
