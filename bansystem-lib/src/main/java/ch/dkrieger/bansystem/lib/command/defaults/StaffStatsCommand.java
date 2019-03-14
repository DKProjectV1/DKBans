/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 14.03.19 19:43
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

public class StaffStatsCommand extends NetworkCommand {

    public StaffStatsCommand() {
        super("staffstats","","dkbans.staffstats");
        setPrefix(Messages.PREFIX_BAN);
    }

    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 1){
            sender.sendMessage(Messages.STAFFSTATS_HELP.replace("[prefix]",getPrefix()));
            return;
        }
        NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
        if(player == null){
            sender.sendMessage(Messages.PLAYER_NOT_FOUND
                    .replace("[player]",args[0])
                    .replace("[prefix]",getPrefix()));
            return;
        }
        sender.sendMessage(Messages.STAFFSTATS_INFO
                .replace("[player]",player.getColoredName())
                .replace("[bans]",String.valueOf(player.getStats().getBans()))
                .replace("[mutes]",String.valueOf(player.getStats().getMutes()))
                .replace("[kicks]",String.valueOf(player.getStats().getKicks()))
                .replace("[unbans]",String.valueOf(player.getStats().getUnbans()))
                .replace("[warns]",String.valueOf(player.getStats().getWarns()))
                .replace("[prefix]",getPrefix()));
    }

    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
