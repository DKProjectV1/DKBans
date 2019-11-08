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

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.entry.HistoryEntry;
import ch.dkrieger.bansystem.lib.player.history.entry.Unwarn;
import ch.dkrieger.bansystem.lib.player.history.entry.Warn;
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

            if(args.length >= 2 && GeneralUtil.isNumber(args[1])) warnId = Integer.parseInt(args[1]);

            for(int i = (warnId>0?2:1);i< args.length;i++) reason +=args[i]+" ";

            if(warnId > 0){
                HistoryEntry entry = player.getHistory().getEntry(warnId);
                if(!(entry instanceof Warn)){
                    sender.sendMessage(Messages.UNWARN_NOT_WARN
                            .replace("[warnId]",""+warnId)
                            .replace("[player]",player.getColoredName())
                            .replace("[prefix]",getPrefix()));
                    return;
                }
            }else if(player.getHistory().getWarnCountSinceLastBan() == 0){
                    sender.sendMessage(Messages.UNWARN_NO_WARNS
                        .replace("[player]",player.getColoredName())
                        .replace("[prefix]",getPrefix()));
                return;
            }

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
