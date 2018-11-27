package ch.dkrieger.bansystem.lib;

import ch.dkrieger.bansystem.lib.command.NetworkCommandManager;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;

import java.io.File;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 17.11.18 11:19
 *
 */

public interface DKBansPlatform {

    public String getPlatformName();

    public String getServerVersion();

    public File getFolder();

    public NetworkCommandManager getCommandManager();

    public String getColor(NetworkPlayer player);

}
