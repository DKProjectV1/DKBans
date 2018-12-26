package ch.dkrieger.bansystem.lib.cloudnet.v2;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import de.dytanic.cloudnet.lib.utility.document.Document;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CloudNetV2OnlinePlayer implements OnlineNetworkPlayer {

    public static Map<UUID,Integer> PINGGETTER;

    static{
        PINGGETTER = new HashMap<>();
    }

    private CloudPlayer player;

    public CloudNetV2OnlinePlayer(CloudPlayer player) {
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
        return player.getProxy();
    }

    @Override
    public String getServer() {
        return player.getServer();
    }

    @Override
    public int getPing() {
        PINGGETTER.remove(getUUID());
        CloudAPI.getInstance().sendCustomSubProxyMessage("DKBans","getPing"
                ,new Document().append("uuid",getUUID()),getProxy());
        int timeOut = 0;
        while(!PINGGETTER.containsKey(getUUID()) && timeOut < 600) timeOut++;
        if(PINGGETTER.containsKey(getUUID())) return PINGGETTER.get(getUUID());
        return -1;
    }

    @Override
    public NetworkPlayer getPlayer() {
        return BanSystem.getInstance().getPlayerManager().getPlayer(getUUID());
    }

    @Override
    public void sendMessage(String message) {
        player.getPlayerExecutor().sendMessage(player,message);
    }

    @Override
    public void sendMessage(TextComponent component) {
        CloudAPI.getInstance().sendCustomSubProxyMessage("DKBans","sendMessage"
                ,new Document().append("uuid",getUUID()).append("message",component),getProxy());
    }

    @Override
    public void connect(String server) {
        CloudAPI.getInstance().sendCustomSubProxyMessage("DKBans","connect"
                ,new Document().append("uuid",getUUID()).append("server",server),getProxy());
    }

    @Override
    public void executeCommand(String command) {
        CloudAPI.getInstance().sendCustomSubProxyMessage("DKBans","executeCommand"
                ,new Document().append("uuid",getUUID()).append("command",command),getProxy());
    }

    @Override
    public void sendBan(Ban ban) {
        CloudAPI.getInstance().sendCustomSubProxyMessage("DKBans","sendBan"
                ,new Document().append("uuid",getUUID()).append("ban",ban),getProxy());
    }

    @Override
    public void kick(Kick kick) {
        CloudAPI.getInstance().sendCustomSubProxyMessage("DKBans","kick"
                ,new Document().append("uuid",getUUID()).append("kick",kick),getProxy());
    }

    public void setCloudPlayer(CloudPlayer player) {
        this.player = player;
    }
}
