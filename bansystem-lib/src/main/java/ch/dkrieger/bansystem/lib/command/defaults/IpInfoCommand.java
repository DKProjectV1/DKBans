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
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

public class IpInfoCommand extends NetworkCommand {

    public IpInfoCommand() {
        super("ipinfo","","dkperms.ipinfo");
        setPrefix(Messages.PREFIX_BAN);
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 1){
            sender.sendMessage(Messages.IPINFO_HELP.replace("[prefix]",getPrefix()));
            return;
        }
        if(GeneralUtil.isIP4Address(args[0])){
            sender.sendMessage(Messages.IPINFO_PLAYER_HEADER
                    .replace("[ip]",args[0])
                    .replace("[prefix]",getPrefix()));
            GeneralUtil.iterateForEach(BanSystem.getInstance().getPlayerManager().getPlayers(args[0]), object -> {
                TextComponent component = new TextComponent(Messages.IPINFO_PLAYER_LIST
                        .replace("[status]",(object.isBanned(BanType.NETWORK)?Messages.IPINFO_PLAYER_BANNED
                                :(object.isBanned(BanType.CHAT)?Messages.IPINFO_PLAYER_MUTED
                                :object.isOnline()?Messages.IPINFO_PLAYER_ONLINE:Messages.IPINFO_PLAYER_OFFLINE)))
                        .replace("[player]",object.getColoredName()).replace("[prefix]",getPrefix()));
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/playerinfo "+object.getName()));
                sender.sendMessage(component);
            });
            return;
        }else{
            NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
            if(player == null){
                sender.sendMessage(Messages.PLAYER_NOT_FOUND.replace("[player]",args[0]).replace("[prefix]",getPrefix()));
                return;
            }
            sender.sendMessage(Messages.IPINFO_IP_HEADER.replace("[player]",player.getColoredName()).replace("[prefix]",getPrefix()));
            for(String ip : player.getIPs()){
                TextComponent component = new TextComponent(Messages.IPINFO_IP_LIST.replace("[ip]",ip).replace("[prefix]",getPrefix()));
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/ipinfo "+ip));
                sender.sendMessage(component);
            }
            return;
        }
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        if(args.length == 1) return GeneralUtil.calculateTabComplete(args[0],sender.getName(),BanSystem.getInstance().getNetwork().getPlayersOnServer(sender.getServer()));
        return null;
    }
}
