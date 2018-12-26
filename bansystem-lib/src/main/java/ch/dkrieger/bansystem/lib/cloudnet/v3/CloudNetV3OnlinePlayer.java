package ch.dkrieger.bansystem.lib.cloudnet.v3;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import de.dytanic.cloudnet.common.document.Document;
import de.dytanic.cloudnet.wrapper.Wrapper;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CloudNetV3OnlinePlayer implements OnlineNetworkPlayer {

    public static Map<UUID,Integer> PINGGETTER;

    static{
        PINGGETTER = new HashMap<>();
    }
    private de.dytanic.cloudnet.ext.bridge.player.NetworkPlayer player;

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
        return player.getProxy().getServerName();
    }

    @Override
    public String getServer() {
        return player.getServer().getServerName();
    }

    @Override
    public int getPing() {
        PINGGETTER.remove(getUUID());
        Wrapper.getInstance().sendChannelMessage("DKBans","getPing"
                ,new Document().append("uuid",getUUID()));
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
        sendMessage(new TextComponent(message));
    }

    @Override
    public void sendMessage(TextComponent component) {
        Wrapper.getInstance().sendChannelMessage("DKBans","sendMessage",new Document()
                .append("message",component));
    }

    @Override
    public void connect(String server) {
        Wrapper.getInstance().sendChannelMessage("DKBans","connect",new Document()
                .append("server",server));
    }

    @Override
    public void executeCommand(String command) {
        Wrapper.getInstance().sendChannelMessage("DKBans","executeCommand",new Document()
                .append("command",command));
    }

    @Override
    public void sendBan(Ban ban) {
        Wrapper.getInstance().sendChannelMessage("DKBans","sendBan",new Document()
                .append("ban",ban));
    }

    @Override
    public void kick(Kick kick) {
        Wrapper.getInstance().sendChannelMessage("DKBans","sendKick",new Document()
                .append("kick",kick));
    }
}
