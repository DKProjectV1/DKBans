/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 30.12.18 14:39
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

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;

import java.util.List;

public class TeamChatCommand extends NetworkCommand {

    public TeamChatCommand() {
        super("teamchat","","dkbans.team","","tc","@team","tchat","teamc");
        setPrefix(Messages.PREFIX_TEAMCHAT);
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 1){
            sender.sendMessage(Messages.TEAMCHAT_HELP
                    .replace("[prefix]",getPrefix()));
            return;
        }
        NetworkPlayer player = sender.getAsNetworkPlayer();
        if(args[0].equalsIgnoreCase("logout")) changeLogin(sender,player,false);
        else if(args[0].equalsIgnoreCase("login")) changeLogin(sender,player,true);
        else if(args[0].equalsIgnoreCase("toggle")) changeLogin(sender,player,!player.isTeamChatLoggedIn());
        else{
            if(!sender.hasPermission("dkbans.teamchat.send") && ! sender.hasPermission("dkbans.*")){
                sender.sendMessage(Messages.NOPERMISSIONS.replace("[prefix]",getPrefix()));
                return;
            }
            if(!player.isTeamChatLoggedIn()){
                sender.sendMessage(Messages.STAFF_STATUS_NOT
                        .replace("[status]",Messages.STAFF_STATUS_LOGIN)
                        .replace("[prefix]",getPrefix()));
                return;
            }
            String message = "";
            for(int i = 0; i < args.length;i++)  message += args[i]+" "+Messages.TEAMCHAT_MESSAGE_COLOR;
            BanSystem.getInstance().getNetwork().sendTeamMessage(Messages.TEAMCHAT_MESSAGE_FORMAT
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",player.getColoredName())
                    .replace("[message]",message.replace('§', '&')),true);
        }
    }
    private void changeLogin(NetworkCommandSender sender, NetworkPlayer player, boolean login){
        if(player.isTeamChatLoggedIn() == login){
            sender.sendMessage(Messages.STAFF_STATUS_ALREADY
                    .replace("[status]",(login?Messages.STAFF_STATUS_LOGIN:Messages.STAFF_STATUS_LOGOUT))
                    .replace("[prefix]",getPrefix()));
        }else{
            sender.sendMessage(Messages.STAFF_STATUS_CHANGE
                    .replace("[status]",(login?Messages.STAFF_STATUS_LOGIN:Messages.STAFF_STATUS_LOGOUT))
                    .replace("[prefix]",getPrefix()));
        }
        player.setTeamChatLogin(login);
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
