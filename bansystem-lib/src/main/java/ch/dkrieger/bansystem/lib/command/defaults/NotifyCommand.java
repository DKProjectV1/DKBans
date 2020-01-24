/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 08.11.19, 22:06
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

package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;

import java.util.Arrays;
import java.util.List;

public class NotifyCommand extends NetworkCommand {

    public NotifyCommand() {
        super("notify","","dkbans.notify");
        setPrefix(Messages.PREFIX_NETWORK);
    }

    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length == 1) {
            NetworkPlayer player = sender.getAsNetworkPlayer();
            if(player == null) {
                sender.sendMessage(Messages.NOTIFY_HELP.replace("prefix", getPrefix()));
                return;
            }
            if(args[0].equalsIgnoreCase("on")) {
                changeLogin(sender, player, true, true);
                return;
            } else if(args[0].equalsIgnoreCase("off")) {
                changeLogin(sender, player, false, false);
                return;
            } else if(args[0].equalsIgnoreCase("toggle")) {
                changeLogin(sender, player, !player.isTeamChatLoggedIn(), !player.isReportLoggedIn());
                return;
            }
        }
        sender.sendMessage(Messages.NOTIFY_HELP.replace("prefix", getPrefix()));
    }

    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        if(args.length == 0) return Arrays.asList("on", "off", "toggle");
        return null;
    }

    private void changeLogin(NetworkCommandSender sender, NetworkPlayer player, boolean teamChatLogin, boolean reportLogin) {
        if(player.isTeamChatLoggedIn() == teamChatLogin){
            sender.sendMessage(Messages.STAFF_STATUS_ALREADY
                    .replace("[status]",(teamChatLogin?Messages.STAFF_STATUS_LOGIN:Messages.STAFF_STATUS_LOGOUT))
                    .replace("[prefix]", Messages.PREFIX_TEAMCHAT));
        }else{
            sender.sendMessage(Messages.STAFF_STATUS_CHANGE
                    .replace("[status]",(teamChatLogin?Messages.STAFF_STATUS_LOGIN:Messages.STAFF_STATUS_LOGOUT))
                    .replace("[prefix]", Messages.PREFIX_TEAMCHAT));
        }
        player.setTeamChatLogin(teamChatLogin);
        if(player.isReportLoggedIn() == reportLogin){
            sender.sendMessage(Messages.STAFF_STATUS_ALREADY
                    .replace("[status]",(reportLogin?Messages.STAFF_STATUS_LOGIN:Messages.STAFF_STATUS_LOGOUT))
                    .replace("[prefix]", Messages.PREFIX_REPORT));
        }else{
            sender.sendMessage(Messages.STAFF_STATUS_CHANGE
                    .replace("[status]",(reportLogin?Messages.STAFF_STATUS_LOGIN:Messages.STAFF_STATUS_LOGOUT))
                    .replace("[prefix]", Messages.PREFIX_REPORT));
        }
        player.setReportLogin(reportLogin);
    }
}
