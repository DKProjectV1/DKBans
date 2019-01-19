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
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TempmuteCommand extends NetworkCommand {

    public TempmuteCommand() {
        super("tempmute","","dkbans.ban.temp.mute","","tmute");
        setPrefix(Messages.PREFIX_BAN);
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 3 || !(GeneralUtil.isNumber(args[1]))){
            sender.sendMessage(Messages.TEMPMUTE_HELP.replace("[prefix]",getPrefix()));
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
        long millis = TimeUnit.DAYS.toMillis(Long.valueOf(args[1]));
        int reasonStart = 3;
        try{
            millis = GeneralUtil.convertToMillis(Long.valueOf(args[1]),args[2]);
            if(args.length < 4){
                sender.sendMessage(Messages.TEMPBAN_HELP.replace("[prefix]",getPrefix()));
                return;
            }
        }catch (Exception exception){
            reasonStart = 2;
        }
        if(player.isBanned(BanType.NETWORK)){
            sender.sendMessage(Messages.PLAYER_ALREADY_BANNED
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",player.getColoredName()));
            return;
        }
        String reason = "";
        for(int i = reasonStart;i<args.length;i++) reason+= args[i]+" ";


        Ban ban = player.ban(BanType.CHAT,millis,TimeUnit.MILLISECONDS,reason.substring(0,reason.length()-1),-1,sender.getUUID());
        sender.sendMessage(Messages.BAN_SUCCESS
                .replace("[prefix]",getPrefix())
                .replace("[player]",player.getColoredName())
                .replace("[type]",ban.getBanType().getDisplay())
                .replace("[reason]",ban.getReason())
                .replace("[points]",String.valueOf(ban.getPoints()))
                .replace("[staff]",ban.getStaffName())
                .replace("[reasonID]",String.valueOf(ban.getReasonID()))
                .replace("[ip]",ban.getIp())
                .replace("[duration]",GeneralUtil.calculateDuration(ban.getDuration()))
                .replace("[remaining]",GeneralUtil.calculateRemaining(ban.getDuration(),false))
                .replace("[remaining-short]",GeneralUtil.calculateRemaining(ban.getDuration(),true)));
    }

    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        if(args.length == 1) return GeneralUtil.calculateTabComplete(args[0],sender.getName(),BanSystem.getInstance().getNetwork().getPlayersOnServer(sender.getServer()));
        return null;
    }
}
