/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 04.01.19 13:35
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

public class EditBanCommand extends NetworkCommand {

    public EditBanCommand() {
        super("editban","","dkbans.editban");
        getAliases().add("changeban");
        setPrefix(Messages.PREFIX_BAN);
    }

    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        /*

        editban <player/id> setReason <reason> CHAT/network {message}

        editban <player/id> addDuration <duration> <unit> {message}

        editban <player/id> setDuration <duration> <unit> {message}

         */

        if(args.length >= 3){
            BanType type = null;
            Ban ban = null;
            NetworkPlayer player = null;
            if(GeneralUtil.isNumber(args[0])){
                HistoryEntry entry = BanSystem.getInstance().getHistoryManager().getHistoryEntry(Integer.valueOf(args[0]));
                if(entry instanceof Ban){
                    ban = (Ban) entry;
                    player = entry.getPlayer();
                }
            }else player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
            if(player == null){
                sender.sendMessage(new TextComponent(Messages.BAN_NOTFOUND.replace("[prefix]",getPrefix())));
                return;
            }
            if(ban == null){
                if(player.isBanned(BanType.CHAT) && player.isBanned(BanType.NETWORK)){
                    type = BanType.parseNull(args[args.length-1]);
                    if(type != null) ban = player.getBan(type);
                    else{
                        String command = "";
                        for(int i = 0;i < args.length;i++) command += args[i]+" ";
                        sender.sendMessage(Messages.PLAYER_HAS_MOREBANS_HEADER
                                .replace("[player]",player.getColoredName()).replace("[prefix]",getPrefix()));
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
                        network.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/editban "+command+"NETWORK"));
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
                        chat.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/editban "+command+"CHAT"));
                        sender.sendMessage(network);
                        sender.sendMessage(chat);
                        return;
                    }
                }else{
                    ban = player.isBanned(BanType.CHAT)?player.getBan(BanType.CHAT):player.getBan(BanType.NETWORK);
                    if(ban == null){
                        sender.sendMessage(new TextComponent(Messages.BAN_NOTFOUND.replace("[prefix]",getPrefix())));
                        return;
                    }
                }
            }
            if(!player.getBan(ban.getBanType()).equals(ban)){
                sender.sendMessage(new TextComponent(Messages.BAN_NOTFOUND.replace("[prefix]",getPrefix())));
                return;
            }
            if(!sender.hasPermission("dkbans.editban.all") && !sender.hasPermission("dkbans.*") && !ban.getStaff().equals(sender.getUUID().toString())){
                sender.sendMessage(Messages.EDITBAN_NOTALLOWED
                        .replace("[player]",player.getColoredName())
                        .replace("[prefix]",getPrefix()));
                return;
            }
            if(args[1].equalsIgnoreCase("setReason")){
                String message = "";
                for(int i = 3;i < (type==null?args.length:args.length-1);i++) message += args[i]+" ";
                ban.setReason(args[2],message,sender.getUUID());
            }else if(args[1].equalsIgnoreCase("setMessage")){
                String message = "";
                for(int i = 2;i < (type==null?args.length:args.length-1);i++) message += args[i]+" ";
                ban.setMessage(message,"",sender.getUUID());
            }else if(args[1].equalsIgnoreCase("setPoints") && GeneralUtil.isNumber(args[2])){
                String message = "";
                for(int i = 3;i < (type==null?args.length:args.length-1);i++) message += args[i]+" ";
                ban.setPoints(Integer.valueOf(args[2]),message,sender.getUUID());
            }else if(args[1].equalsIgnoreCase("addDuration") && GeneralUtil.isNumber(args[2])){
                String message = "";
                for(int i = 4;i < (type==null?args.length:args.length-1);i++) message += args[i]+" ";
                long time = GeneralUtil.convertToMillis(Long.valueOf(args[2]),args.length>3?args[3]:null);
                ban.setTimeOut(ban.getTimeOut()+time,message,sender.getUUID());
            }else if(args[1].equalsIgnoreCase("setDuration")){
                String message = "";
                for(int i = 4;i < (type==null?args.length:args.length-1);i++) message += args[i]+" ";
                long time = GeneralUtil.convertToMillis(Long.valueOf(args[2]),args.length>3?args[3]:null);
                ban.setTimeOut(System.currentTimeMillis()+time,message,sender.getUUID());
            }else{
                sender.sendMessage(Messages.EDITBAN_HELP.replace("[prefix]",getPrefix()));
                return;
            }
            sender.sendMessage(Messages.EDITBAN_CHANGED
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
            return;
        }
        sender.sendMessage(Messages.EDITBAN_HELP.replace("[prefix]",getPrefix()));
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
