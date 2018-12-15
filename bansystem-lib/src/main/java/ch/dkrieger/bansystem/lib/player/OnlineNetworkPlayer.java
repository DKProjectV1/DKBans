package ch.dkrieger.bansystem.lib.player;

import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

public interface OnlineNetworkPlayer {

    public UUID getUUID();

    public String getName();

    public String getProxy();

    public String getServer();

    public int getPing();

    public NetworkPlayer getPlayer();

    public void sendMessage(String message);

    public void sendMessage(TextComponent component);

    public void connect(String server);

    public void executeCommand(String command);

    public void kickForBan(Ban ban);

    public void kick(String reason, int reasonID);

    public void kick(NetworkPlayer staff,int reasonID);

    public void kick(NetworkPlayer staff, String reason, int reasonID);

    public void kick(Kick kick);

}
