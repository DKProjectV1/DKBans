package ch.dkrieger.bansystem.lib.command;

import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 16.11.18 17:47
 *
 */

public interface NetworkCommandSender {

    public String getName();

    public UUID getUUID();

    public NetworkPlayer getAsNetworkPlayer();

    public OnlineNetworkPlayer getAsOnlineNetworkPlayer();

    public boolean hasPermission(String permission);

    public void sendMessage(String message);

    public void sendMessage(TextComponent component);

    public void executeCommand(String command);


}
