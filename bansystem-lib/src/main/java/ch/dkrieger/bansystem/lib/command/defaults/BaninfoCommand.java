/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 09.06.19 12:15
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
import ch.dkrieger.bansystem.lib.player.history.entry.HistoryEntry;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

public class BaninfoCommand extends NetworkCommand {

    public BaninfoCommand() {
        super("baninfo","","dkbans.baninfo","","pcheck");
        setPrefix(Messages.PREFIX_BAN);
    }

    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 1){
            return;
        }
        if(GeneralUtil.isNumber(args[0])){
            HistoryEntry entry = BanSystem.getInstance().getHistoryManager().getHistoryEntry(Integer.valueOf(args[0]));
            if(entry instanceof Ban){
                NetworkPlayer player = entry.getPlayer();
                if(player != null && ((Ban) entry).getBanType() != null){
                    Ban currentBan = player.getBan(((Ban) entry).getBanType());
                    if(currentBan != null && currentBan.equals(entry)){
                        sender.sendMessage(entry.getInfoMessage());
                        return;
                    }
                }
            }
            sender.sendMessage(Messages.BAN_NOTFOUND.replace("[prefix]",getPrefix()));
        }else{
            NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
            if(player == null){
                sender.sendMessage(Messages.BAN_NOTFOUND.replace("[prefix]",getPrefix()));
                return;
            }
            if(player.isBanned(BanType.NETWORK) && player.isBanned(BanType.CHAT)){
                sender.sendMessage(Messages.PLAYER_HAS_MOREBANS_HEADER
                        .replace("[player]",player.getColoredName())
                        .replace("[prefix]",getPrefix()));
                TextComponent network = new TextComponent(Messages.PLAYER_HAS_MOREBANS_NETWORK
                        .replace("[prefix]",getPrefix())
                        .replace("[duration]",GeneralUtil.calculateDuration(player.getBan(BanType.NETWORK).getDuration()))
                        .replace("[remaining]",GeneralUtil.calculateRemaining(player.getBan(BanType.NETWORK).getDuration(),false))
                        .replace("[remaining-short]",GeneralUtil.calculateRemaining(player.getBan(BanType.NETWORK).getDuration(),true))
                        .replace("[id]",""+player.getBan(BanType.NETWORK).getID())
                        .replace("[reason]",player.getBan(BanType.NETWORK).getReason())
                        .replace("[type]",player.getBan(BanType.NETWORK).getTypeName())
                        .replace("[points]",""+player.getBan(BanType.NETWORK).getPoints())
                        .replace("[message]",player.getBan(BanType.NETWORK).getMessage())
                        .replace("[date]",""+player.getBan(BanType.NETWORK).getTimeStamp())
                        .replace("[ip]",player.getBan(BanType.NETWORK).getIp())
                        .replace("[staff]",player.getBan(BanType.NETWORK).getStaffName())
                        .replace("[player]",player.getColoredName()));
                network.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/history "+player.getUUID()+" "+player.getBan(BanType.NETWORK).getID()));
                TextComponent chat = new TextComponent(Messages.PLAYER_HAS_MOREBANS_CHAT
                        .replace("[prefix]",getPrefix())
                        .replace("[duration]",GeneralUtil.calculateDuration(player.getBan(BanType.CHAT).getDuration()))
                        .replace("[remaining]",GeneralUtil.calculateRemaining(player.getBan(BanType.CHAT).getDuration(),false))
                        .replace("[remaining-short]",GeneralUtil.calculateRemaining(player.getBan(BanType.CHAT).getDuration(),true))
                        .replace("[id]",""+player.getBan(BanType.CHAT).getID())
                        .replace("[reason]",player.getBan(BanType.CHAT).getReason())
                        .replace("[type]",player.getBan(BanType.CHAT).getTypeName())
                        .replace("[points]",""+player.getBan(BanType.CHAT).getPoints())
                        .replace("[message]",player.getBan(BanType.CHAT).getMessage())
                        .replace("[date]",""+player.getBan(BanType.CHAT).getTimeStamp())
                        .replace("[ip]",player.getBan(BanType.CHAT).getIp())
                        .replace("[staff]",player.getBan(BanType.CHAT).getStaffName())
                        .replace("[player]",player.getColoredName()));
                chat.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/history "+player.getUUID()+" "+player.getBan(BanType.CHAT).getID()));
                sender.sendMessage(network);
                sender.sendMessage(chat);
                return;
            }else{
                Ban ban = player.getBan(BanType.NETWORK);
                if(ban == null) ban = player.getBan(BanType.CHAT);
                if(ban == null){
                    sender.sendMessage(Messages.BAN_NOTFOUND.replace("[prefix]",getPrefix()));
                    return;
                }
                sender.sendMessage(ban.getInfoMessage());
            }
        }
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
