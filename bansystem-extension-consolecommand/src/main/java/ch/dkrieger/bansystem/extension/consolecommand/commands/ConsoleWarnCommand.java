/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 31.12.18 18:14
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

package ch.dkrieger.bansystem.extension.consolecommand.commands;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.reason.BanReason;
import ch.dkrieger.bansystem.lib.reason.WarnReason;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ConsoleWarnCommand extends NetworkCommand {

    public ConsoleWarnCommand() {
        super("cwarn");
    }

    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(sender.getUUID() == null){
            if(args.length >= 3){
                NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
                if(player == null){
                    System.out.println(Messages.SYSTEM_PREFIX+"This player was not found.");
                    return;
                }
                String message = "";
                for(int i = 3;i < args.length;i++) message += args[i]+" ";
                if(GeneralUtil.isNumber(args[1])){
                    WarnReason reason = BanSystem.getInstance().getReasonProvider().searchWarnReason(args[1]);
                    if(reason != null){
                        player.warn(reason,message,args[2]);
                        System.out.println(Messages.SYSTEM_PREFIX+player.getName()+" was warned for "+reason.getName());
                        return;
                    }
                }else{
                    player.warn(args[1],message,args[2]);
                    System.out.println(Messages.SYSTEM_PREFIX+player.getName()+" was warned for "+args[1]);
                }
            }
            System.out.println(Messages.SYSTEM_PREFIX+"This is a simple addon for banning, kicking or unbanning a " +
                    "player from the console with a special staff member (Example: AntiCheat)\n\n\t-> /cBan <player> <reasonID> <staffName> <message>" +
                    "\n\t-> /cBan <player> <banType> <reason> <duration> <unit> <staffName> <message>" +
                    "\n\nAs reason you can use a id or a custom reason\n\n\t-> Do not forgot syncing all reasons in you network (Config files).");

        }else sender.sendMessage(Messages.CHAT_FILTER_COMMAND.replace("[prefix]",Messages.PREFIX_NETWORK));
    }

    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
