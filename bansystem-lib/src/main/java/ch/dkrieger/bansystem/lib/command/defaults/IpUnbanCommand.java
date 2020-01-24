/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 30.12.18 21:11
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
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.List;

public class IpUnbanCommand extends NetworkCommand {

    public IpUnbanCommand() {
        super("ipunban","","dkbans.ipunban");
        getAliases().add("ipunblock");
        getAliases().add("unbanip");
        setPrefix(Messages.PREFIX_BAN);
    }

    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 1){
            sender.sendMessage(Messages.IPUNBAN_HELP.replace("[prefix]",getPrefix()));
            return;
        }
        if(GeneralUtil.isIP4Address(args[0]) && BanSystem.getInstance().getPlayerManager().isIPBanned(args[0])) {
            BanSystem.getInstance().getPlayerManager().unbanIp(args[0]);
            sender.sendMessage(Messages.IPUNBAN_SUCCESS.replace("[ip]",args[0]).replace("[prefix]",getPrefix()));
            return;
        } else {
            NetworkPlayer networkPlayer = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
            if(networkPlayer != null) {
                BanSystem.getInstance().getPlayerManager().unbanIp(args[0]);
                sender.sendMessage(Messages.IPUNBAN_SUCCESS.replace("[ip]",args[0]).replace("[prefix]",getPrefix()));
                return;
            }
        }
        sender.sendMessage(Messages.IPBAN_NOT_BANNED
                .replace("[prefix]",getPrefix())
                .replace("[player]",args[0]));
    }

    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
