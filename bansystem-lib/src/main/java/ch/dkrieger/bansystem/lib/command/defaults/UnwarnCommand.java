/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 01.02.19 17:15
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
import ch.dkrieger.bansystem.lib.player.history.entry.Unwarn;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.List;

public class UnwarnCommand extends NetworkCommand {

    public UnwarnCommand() {
        super("unwarn","","dkbans.unwarn");
        getAliases().add("unwarning");
        setPrefix(Messages.PREFIX_BAN);
    }

    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length >= 1){
            NetworkPlayer player = BanSystem.getInstance().getPlayerManager().getPlayer(args[0]);
            if(player == null){
                sender.sendMessage(Messages.PLAYER_NOT_FOUND.replace("[prefix]",getPrefix()));
                return;
            }
            int warnId = -1;
            String reason = "";

            if(args.length >= 2 && GeneralUtil.isNumber(args[1])) warnId = Integer.valueOf(args[1]);

            for(int i = (warnId>0?2:1);i< args.length;i++) reason +=args[i]+" ";

            Unwarn unwarn = player.unwarn(warnId,reason,sender.getUUID());
            if(warnId > 0){
                sender.sendMessage(Messages.UNWARN_SUCCESS_DEFINED
                        .replace("[warnId]",""+warnId)
                        .replace("[player]",player.getColoredName())
                        .replace("[reason]",unwarn.getMessage())
                        .replace("[prefix]",getPrefix()));
            }else{
                sender.sendMessage(Messages.UNWARN_SUCCESS_ALL
                        .replace("[warnId]",""+warnId)
                        .replace("[player]",player.getColoredName())
                        .replace("[reason]",unwarn.getMessage())
                        .replace("[prefix]",getPrefix()));
            }
        }else sender.sendMessage(Messages.UNWARN_HELP.replace("[prefix]",getPrefix()));
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        if(args.length == 1) return GeneralUtil.calculateTabComplete(args[0],sender.getName(), BanSystem.getInstance().getNetwork().getPlayersOnServer(sender.getServer()));
        return null;
    }
}
