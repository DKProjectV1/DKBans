package ch.dkrieger.bansystem.lib.command.defaults;

/*
 * (C) Copyright 2020 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 24.01.20, 19:57
 * @Website https://github.com/DevKrieger/DKBans
 *
 * The DKBans Project is under the Apache License, version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;

import java.util.List;

public class FallbackKickCommand extends NetworkCommand {

    public FallbackKickCommand() {
        super("fallbackkick", "", "dkbans.kick.fallback");
        setPrefix(Messages.PREFIX_NETWORK);
    }

    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 1) {
            sender.sendMessage(Messages.FALLBACK_KICK_HELP.replace("[prefix]", getPrefix()));
            return;
        }
        OnlineNetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchOnlinePlayer(args[0]);
        if(player == null) {
            sender.sendMessage(Messages.PLAYER_NOT_FOUND);
            return;
        }
        String message = args.length >= 2 ? getFallbackKickMessage(args) : null;
        sender.sendMessage(Messages.FALLBACK_KICK_SUCCESS.replace("[prefix]", getPrefix()));
        player.kickToFallback(message);
    }

    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }

    private String getFallbackKickMessage(String[] args) {
        StringBuilder message = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            message.append(args[i]);
        }
        return message.toString();
    }
}
