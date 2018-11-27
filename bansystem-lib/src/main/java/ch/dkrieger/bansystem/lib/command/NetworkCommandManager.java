package ch.dkrieger.bansystem.lib.command;

import java.util.Collection;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 16.11.18 17:47
 *
 */

public interface NetworkCommandManager {

    public Collection<NetworkCommand> getCommands();

    public NetworkCommand getCommand(String name);

    public void registerCommand(NetworkCommand command);

}
