package ch.dkrieger.bansystem.lib.player;

import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

public interface OnlineNetworkPlayer {

    public UUID getUUID();

    public String getName();

    public String getServer();

    public void sendMessage(String message);

    public void sendMessage(TextComponent component);

    public void connect(String server);

    public void executeCommand(String command);

    public void kick();

    public void ban();

}
