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
import ch.dkrieger.bansystem.lib.config.mode.BanMode;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MuteCommand extends NetworkCommand {

    public MuteCommand() {
        super("mute");
        setPrefix(Messages.PREFIX_BAN);
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(BanSystem.getInstance().getConfig().banMode == BanMode.SELF){
            if(!sender.hasPermission(this.permission)&& !sender.hasPermission("dkbans.*")){
                sender.sendMessage(Messages.REASON_NO_PERMISSION
                        .replace("[prefix]",getPrefix())
                        .replace("[reason]",reason.getDisplay()));
                return;
            }
            if(sender.getName().equalsIgnoreCase(args[0])){
                sender.sendMessage(Messages.BAN_SELF.replace("[prefix]",getPrefix()));
                return;
            }
            NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
            if(player == null){
                sender.sendMessage(Messages.PLAYER_NOT_FOUND
                        .replace("[prefix]",getPrefix())
                        .replace("[player]",args[0]));
                return;
            }
            if(player.hasBypass() && !(sender.hasPermission("dkbans.bypass.ignore"))){
                sender.sendMessage(Messages.BAN_BYPASS
                        .replace("[prefix]",getPrefix())
                        .replace("[player]",player.getColoredName()));
                return;
            }
            StringBuilder message = new StringBuilder();
            for(int i = 1; i< args.length;i++) message.append(args[i]).append(" ");
            BanCommand.sendBanMessage(sender,player,player.ban(BanType.CHAT,-1,TimeUnit.MICROSECONDS,message.toString()));
        } else sender.executeCommand("ban " + GeneralUtil.arrayToString(args, " "));
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        if(args.length == 1) return GeneralUtil.calculateTabComplete(args[0],sender.getName(),BanSystem.getInstance().getNetwork().getPlayersOnServer(sender.getServer()));
        return null;
    }
}
