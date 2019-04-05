/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 05.04.19 22:47
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
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.reason.BanReason;
import ch.dkrieger.bansystem.lib.reason.BanReasonEntry;
import ch.dkrieger.bansystem.lib.reason.KickReason;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class BanCommand extends NetworkCommand {

    private static final Function<BanReason,String> REASON_FORMATTER = KickReason::getName;

    public BanCommand() {
        super("ban","","dkbans.ban");
        setPrefix(Messages.PREFIX_BAN);
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 2) {
            sendReasons(sender);
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

        if(BanSystem.getInstance().getConfig().banMode == BanMode.SELF){
            StringBuilder message = new StringBuilder();
            for(int i = 1; i< args.length;i++) message.append(args[i]).append(" ");
            sendBanMessage(sender,player,player.ban(BanType.NETWORK,-1,TimeUnit.MICROSECONDS,message.toString()));
            return;
        }

        BanReason reason = BanSystem.getInstance().getReasonProvider().searchBanReason(args[1]);
        if(reason == null){
            sendReasons(sender);
            return;
        }
        if(!sender.hasPermission(reason.getPermission())) {
            sender.sendMessage(Messages.REASON_NO_PERMISSION
                    .replace("[prefix]",getPrefix())
                    .replace("[reason]",reason.getDisplay()));
            return;
        }
        BanReasonEntry value = reason.getNextDuration(player);
        if(value == null){
            sender.sendMessage(Messages.ERROR
                    .replace("[prefix]",getPrefix()));
            return;
        }
        StringBuilder message = new StringBuilder();
        boolean overwrite = false;

        if(args.length > 2) for(int i = 2; i< args.length;i++) {
            if(args[i].equalsIgnoreCase("--overwrite")) overwrite = true;
            else message.append(args[i]).append(" ");
        }
        if(player.isBanned(value.getType())){
            if(overwrite){
                if(!(sender.hasPermission("dkbans.ban.overwrite") && (sender.hasPermission("dkbans.ban.overwrite.all")
                        || player.getBan(value.getType()).getStaff().equalsIgnoreCase(sender.getUUID().toString())))){
                    sender.sendMessage(Messages.BAN_OVERWRITE_NOTALLOWED
                            .replace("[prefix]",getPrefix())
                            .replace("[player]",player.getColoredName()));
                    return;
                }
            }else{
                if(value.getType() == BanType.NETWORK){
                    sender.sendMessage(Messages.PLAYER_ALREADY_BANNED
                            .replace("[prefix]",getPrefix())
                            .replace("[player]",player.getColoredName()));
                }else{
                    sender.sendMessage(Messages.PLAYER_ALREADY_MUTED
                            .replace("[prefix]",getPrefix())
                            .replace("[player]",player.getColoredName()));
                }
                if(sender.hasPermission("dkbans.ban.overwrite") && (sender.hasPermission("dkbans.ban.overwrite.all")
                        || player.getBan(value.getType()).getStaff().equalsIgnoreCase(sender.getUUID().toString()))){
                    TextComponent component = new TextComponent(Messages.BAN_OVERWRITE_INFO
                            .replace("[prefix]",getPrefix())
                            .replace("[player]",player.getColoredName()));
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND
                            ,"/ban "+args[0]+" "+args[1]+" "+message+" --overwrite"));
                    sender.sendMessage(component);
                }
                return;
            }
        }
        sendBanMessage(sender,player,player.ban(reason, message.toString(),sender.getUUID()));
    }

    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        if(args.length == 1) return GeneralUtil.calculateTabComplete(args[0],sender.getName(),BanSystem.getInstance().getNetwork().getPlayersOnServer(sender.getServer()));
        else if(args.length == 2 && BanSystem.getInstance().getConfig().banMode != BanMode.SELF)
            return GeneralUtil.calculateTabComplete(args[1],null,BanSystem.getInstance().getReasonProvider().getBanReasons(),REASON_FORMATTER);
        return null;
    }
    private void sendReasons(NetworkCommandSender sender){
        if(BanSystem.getInstance().getConfig().banMode != BanMode.SELF){
            sender.sendMessage(Messages.BAN_HELP_HEADER.replace("[prefix]",getPrefix()));
            for(BanReason reason : BanSystem.getInstance().getReasonProvider().getBanReasons()){
                if(!reason.isHidden() && (!BanSystem.getInstance().getConfig().reasonShowOnlyPermitted
                        || sender.hasPermission(reason.getPermission()) || sender.hasPermission("dkbans.*"))){
                    sender.sendMessage(Messages.BAN_HELP_REASON
                            .replace("[prefix]",getPrefix())
                            .replace("[id]",""+reason.getID())
                            .replace("[name]",reason.getDisplay())
                            .replace("[historyType]",reason.getHistoryType().getDisplay())
                            .replace("[banType]",reason.getBanType().getDisplay())
                            .replace("[reason]",reason.getDisplay())
                            .replace("[points]",""+reason.getPoints()));
                }
            }
        }
        sender.sendMessage(Messages.BAN_HELP_HELP.replace("[prefix]",getPrefix()));
    }
    public static void sendBanMessage(NetworkCommandSender sender,NetworkPlayer player,Ban ban){
        sender.sendMessage((ban.getBanType()==BanType.CHAT?Messages.BAN_CHAT_SUCCESS:Messages.BAN_NETWORK_SUCCESS)
                .replace("[prefix]",Messages.PREFIX_BAN)
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
}