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
import ch.dkrieger.bansystem.lib.config.mode.ReasonMode;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.Unban;
import ch.dkrieger.bansystem.lib.reason.UnbanReason;
import ch.dkrieger.bansystem.lib.utils.Duration;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class UnbanCommand extends NetworkCommand {

    private ReasonMode unbanMode;

    public UnbanCommand() {
        super("unban","","dkbans.unban","","unmute");
        setPrefix(Messages.PREFIX_BAN);
        this.unbanMode = BanSystem.getInstance().getConfig().unbanMode;
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        int messageStart = 1;
        if(args.length < 1) {
            sendReasons(sender);
            return;
        }
        UnbanReason reason = null;
        if(unbanMode != ReasonMode.SELF){
            if(args.length >= 2) reason = BanSystem.getInstance().getReasonProvider().searchUnbanReason(args[1]);
            if(reason == null){
                sendReasons(sender);
                return;
            }
            messageStart++;
        }
        NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
        if(player == null){
            sender.sendMessage(Messages.PLAYER_NOT_FOUND
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",args[0]));
            return;
        }
        if(!player.isBanned()){
            sender.sendMessage(Messages.PLAYER_NOT_BANNED
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",args[0]));
            return;
        }
        BanType type = null;
        if((unbanMode ==ReasonMode.SELF && args.length >= 2) || (unbanMode == ReasonMode.TEMPLATE && args.length >= 3)){//unban dkrieger 1 network das war ein test
            type = BanType.parse(args[messageStart].toUpperCase());
            messageStart++;
        }
        String message = "";
        for(int i = messageStart;i < args.length;i++) message += args[i]+" ";
        if(type == null && player.isBanned(BanType.NETWORK) && player.isBanned(BanType.CHAT)){
            sender.sendMessage(Messages.PLAYER_HAS_MOREBANS_HEADER
                    .replace("[player]",player.getColoredName()).replace("[prefix]",getPrefix()));
            TextComponent network = new TextComponent(player.getBan(BanType.NETWORK).replace(Messages.PLAYER_HAS_MOREBANS_NETWORK,false));
            network.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/unban "+args[0]+" NETWORK "+message));
            TextComponent chat = new TextComponent(player.getBan(BanType.CHAT).replace(Messages.PLAYER_HAS_MOREBANS_NETWORK,false));
            chat.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/unban "+args[0]+" CHAT "+message));
            sender.sendMessage(network);
            sender.sendMessage(chat);
            return;
        }else type = player.isBanned(BanType.NETWORK)?BanType.NETWORK:BanType.CHAT;
        if(!player.isBanned(type)){
            sender.sendMessage(Messages.PLAYER_NOT_BANNED
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",args[0]));
            return;
        }
        if(!sender.hasPermission("dkbans.unban.all") && !sender.hasPermission("dkbans.*")){
            Ban ban = player.getBan(type);
            if(!ban.getStaff().equals(sender.getUUID().toString())){
                sender.sendMessage(Messages.UNBAN_NOTALLOWED
                        .replace("[player]",player.getColoredName())
                        .replace("[prefix]",getPrefix()));
                return;
            }
        }
        if(this.unbanMode != ReasonMode.SELF){
            if(reason.getBanType() != null && !type.equals(reason.getBanType())){
                sender.sendMessage(Messages.UNBAN_NOTFOTHISTYPE.replace("[prefix]",getPrefix()));
                return;
            }
            if(reason.getMaxPoints() > 0 && (BanSystem.getInstance().getConfig().banPointsSeparateChatAndNetwork
                    ?player.getHistory().getPoints(type):player.getHistory().getPoints()) >= reason.getMaxPoints()){
                sender.sendMessage(Messages.UNBAN_TOMANYPOINTS.replace("[prefix]",getPrefix()));
                return;
            }
            Ban ban = player.getBan(type);

            Duration newDuration = new Duration(ban.getDuration(),TimeUnit.MILLISECONDS);
            if(reason.getRemoveDuration().getTime() > 0){
                newDuration.setTime(newDuration.getMillisTime()-reason.getRemoveDuration().getMillisTime());
            }
            if(reason.getDurationDivider() > 0 && newDuration.getTime() > 0){
                newDuration.setTime((long) (newDuration.getMillisTime()/reason.getDurationDivider()));
            }
            if(reason.isRemoveAllPoints()) ban.setPoints(0,"Change from unban reason",sender.getUUID());
            else if(reason.getPoints().getPoints() > 0 || reason.getPointsDivider() > 0){
                int points = ban.getPoints().getPoints()-reason.getPoints().getPoints();
                if(points > 0 && reason.getPointsDivider() > 0) points = (int) (points/reason.getPointsDivider());
                if(points < 0) points = 0;
                ban.setPoints(points,"Change from unban reason",sender.getUUID());
            }
            if(newDuration.getMillisTime() > 10 && newDuration.getTime() != ban.getDuration()){
                ban.setTimeOut(System.currentTimeMillis()+newDuration.getMillisTime(),message,sender.getUUID());
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
        }
        if(type == BanType.NETWORK){
            Unban unban;
            if(this.unbanMode == ReasonMode.SELF) unban = player.unban(BanType.NETWORK,message,sender.getUUID());
            else unban = player.unban(BanType.NETWORK,reason,message,sender.getUUID());
            sender.sendMessage(Messages.PLAYER_UNBANNED
                    .replace("[prefix]",getPrefix())
                    .replace("[reason]",unban.getReason())
                    .replace("[message]",unban.getMessage())
                    .replace("[staff]",unban.getStaffName())
                    .replace("[id]",""+unban.getID())
                    .replace("[points]",""+unban.getPoints())
                    .replace("[player]",args[0]));
        }else{
            Unban unban;
            if(this.unbanMode == ReasonMode.SELF) unban = player.unban(BanType.CHAT,message,sender.getUUID());
            else unban = player.unban(BanType.CHAT,reason,message,sender.getUUID());
            sender.sendMessage(Messages.PLAYER_UNMUTED
                    .replace("[prefix]",getPrefix())
                    .replace("[reason]",unban.getReason())
                    .replace("[message]",unban.getMessage())
                    .replace("[staff]",unban.getStaffName())
                    .replace("[id]",""+unban.getID())
                    .replace("[points]",""+unban.getPoints())
                    .replace("[player]",args[0]));
        }
    }
    private void sendReasons(NetworkCommandSender sender){
        if(BanSystem.getInstance().getConfig().unbanMode != ReasonMode.SELF) {
            sender.sendMessage(Messages.UNBAN_HELP_HEADER.replace("[prefix]",getPrefix()));
            for(UnbanReason reason : BanSystem.getInstance().getReasonProvider().getUnbanReasons()){
                if(!reason.isHidden() && (sender.hasPermission(reason.getPermission()) || sender.hasPermission("dkbans.*"))){
                    sender.sendMessage(Messages.UNBAN_HELP_REASON
                            .replace("[bynType]",reason.getBanType()==null?"All":reason.getBanType().getDisplay())
                            .replace("[reason]",reason.getDisplay())
                            .replace("[prefix]",getPrefix())
                            .replace("[id]",""+reason.getID())
                            .replace("[name]",reason.getDisplay())
                            .replace("[maxPoints]",""+reason.getMaxPoints())
                            .replace("[maxDuration]",reason.getMaxDuration().getFormattedTime(true))
                            .replace("[points]",""+reason.getPoints()));
                }
            }
        }
        sender.sendMessage(Messages.UNBAN_HELP_HELP.replace("[prefix]",getPrefix()));
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        if(args.length == 1) return GeneralUtil.calculateTabComplete(args[0],sender.getName(),BanSystem.getInstance().getNetwork().getPlayersOnServer(sender.getServer()));
        return null;
    }
}
