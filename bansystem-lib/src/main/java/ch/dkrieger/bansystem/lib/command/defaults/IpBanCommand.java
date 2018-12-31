/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 30.12.18 19:47
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
import ch.dkrieger.bansystem.lib.player.IPBan;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class IpBanCommand extends NetworkCommand {

    public IpBanCommand() {
        super("ipban","","dkbans.ipban");
        getAliases().add("ipblock");
        getAliases().add("banip");
        setPrefix(Messages.PREFIX_BAN);
    }

    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length <  1){
            sender.sendMessage(Messages.IPBAN_HELP.replace("[prefix]",getPrefix()));
            return;
        }
        if(args[0].equalsIgnoreCase("info")){
            if(args.length < 2){
                sender.sendMessage(Messages.IPBAN_HELP.replace("[prefix]",getPrefix()));
                return;
            }
            IPBan ban = BanSystem.getInstance().getPlayerManager().getIpBan(args[1]);
            if(ban == null){
                sender.sendMessage(Messages.IPBAN_NOT_BANNED
                        .replace("[ip]",args[1])
                        .replace("[prefix]",getPrefix()));
                return;
            }
            sender.sendMessage(Messages.IPBAN_INFO
                    .replace("[ip]",args[1])
                    .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(ban.getTimeStamp()))
                    .replace("[timeOut]",BanSystem.getInstance().getConfig().dateFormat.format(ban.getTimeOut()))
                    .replace("[duration]",GeneralUtil.calculateDuration(ban.getDuration()))
                    .replace("[remaining]",GeneralUtil.calculateRemaining(ban.getRemaining(),false))
                    .replace("[remaining-short]",GeneralUtil.calculateRemaining(ban.getRemaining(),true))
                    .replace("[prefix]",getPrefix()));
            return;
        }
        String ip;
        UUID lastPlayer = null;
        if(GeneralUtil.isIP4Address(args[0])) ip = args[0];
        else{
            NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
            if(player == null){
                sender.sendMessage(Messages.PLAYER_NOT_FOUND
                        .replace("[prefix]",getPrefix())
                        .replace("[player]",args[0]));
                return;
            }
            ip = player.getIP();
            lastPlayer = player.getUUID();
        }
        if(BanSystem.getInstance().getPlayerManager().isIPBanned(ip)){
            sender.sendMessage(Messages.IPBAN_ALREADY_BANNED
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",args[0]));
            return;
        }
        long duration = -1;
        if(args.length >= 2){
            if(args.length >= 3) duration = GeneralUtil.convertToMillis(Long.valueOf(args[1]),args[2]);
            else duration = TimeUnit.DAYS.toMillis(Long.valueOf(args[1]));
        }
        IPBan ban = BanSystem.getInstance().getPlayerManager().banIp(ip,duration,TimeUnit.DAYS,lastPlayer);
        sender.sendMessage(Messages.IPBAN_SUCCESS
                .replace("[ip]",ip)
                .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(ban.getTimeStamp()))
                .replace("[timeOut]",BanSystem.getInstance().getConfig().dateFormat.format(ban.getTimeOut()))
                .replace("[duration]",GeneralUtil.calculateDuration(ban.getDuration()))
                .replace("[remaining]",GeneralUtil.calculateRemaining(ban.getRemaining(),false))
                .replace("[remaining-short]",GeneralUtil.calculateRemaining(ban.getRemaining(),true))
                .replace("[prefix]",getPrefix()));
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
