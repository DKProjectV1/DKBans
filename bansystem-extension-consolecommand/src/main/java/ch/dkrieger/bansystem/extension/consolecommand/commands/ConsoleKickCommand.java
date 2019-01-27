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

package ch.dkrieger.bansystem.extension.consolecommand.commands;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.reason.BanReason;
import ch.dkrieger.bansystem.lib.reason.KickReason;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.List;

public class ConsoleKickCommand extends NetworkCommand {

    public ConsoleKickCommand() {
        super("ckick");
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
                    KickReason reason = BanSystem.getInstance().getReasonProvider().getKickReason(Integer.valueOf(args[1]));
                    if(reason == null){
                        System.out.println(Messages.SYSTEM_PREFIX+"Kick reason not found.");
                        return;
                    }
                    player.kick(reason,message,args[2]);
                    System.out.println(Messages.SYSTEM_PREFIX+player.getName()+" was kicked for "+reason.getName());
                }else{
                    player.kick(args[1],message,args[2]);
                    System.out.println(Messages.SYSTEM_PREFIX+player.getName()+" was kicked for "+args[1]);
                }
            }else{
                System.out.println(Messages.SYSTEM_PREFIX+"This is a simple addon for banning, kicking or unbanning a " +
                        "player from the console with a special staff member (Example: AntiCheat)\n\n\t-> /cKick <player> <reason> <staffName> <message>" +
                        "\n\nAs reason you can use a id or a custom reason\n\n\t-> Do not forgot syncing all reasons in you network (Config files).");
            }
        }else sender.sendMessage(Messages.CHAT_FILTER_COMMAND.replace("[prefix]",Messages.PREFIX_NETWORK));
    }

    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
