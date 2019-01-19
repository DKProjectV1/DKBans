/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 19.01.19 10:59
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
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.History;

import java.util.List;

public class MyHistoryPointsCommand extends NetworkCommand {


    public MyHistoryPointsCommand() {
        super("myhistorypoints","","dkbans.myhistorypoints","/myHistoryPoints","mybanpoints","mymutepoints");
    }

    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        History history = sender.getAsNetworkPlayer().getHistory();
        if(history != null){
            sender.sendMessage(Messages.MYHISTORYPOINTS
                    .replace("[prefix]",getPrefix())
                    .replace("[points_chat]",""+history.getPoints(BanType.CHAT))
                    .replace("[points_network]",""+history.getPoints(BanType.NETWORK))
                    .replace("[points_all]",""+history.getPoints()));
        }else sender.sendMessage(Messages.MYHISTORYPOINTS
                .replace("[prefix]",getPrefix())
                .replace("[points_chat]","0")
                .replace("[points_network]","0")
                .replace("[points_all]","0"));
    }

    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
