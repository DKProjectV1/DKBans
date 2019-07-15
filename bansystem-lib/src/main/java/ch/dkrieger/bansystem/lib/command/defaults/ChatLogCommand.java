/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 15.07.19 11:31
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
import ch.dkrieger.bansystem.lib.filter.FilterType;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.chatlog.ChatLog;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChatLogCommand extends NetworkCommand {

    private static final List<String> ARGUMENTS = Arrays.asList("player","server");

    public ChatLogCommand() {
        super("chatlog","","dkbans.chatlog","","chatlogs");
        setPrefix(Messages.PREFIX_CHATLOG);
    }

    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length >= 2){
            if(GeneralUtil.equalsOne(args[0],"player","players","p","-p")){
                NetworkPlayer player = BanSystem.getInstance().getPlayerManager().getPlayer(args[1]);
                if(player == null){
                    sender.sendMessage(Messages.PLAYER_NOT_FOUND.replace("[prefix]",getPrefix()));
                    return;
                }
                ChatLog chatlog = BanSystem.getInstance().getPlayerManager().getChatLog(player);
                if(chatlog == null){
                    sender.sendMessage(Messages.CHATLOG_NOTFOUND.replace("[prefix]",getPrefix()));
                    return;
                }
                sender.sendMessage(Messages.CHATLOG_PLAYER_HEADER
                        .replace("[player]",player.getColoredName())
                        .replace("[prefix]",getPrefix()));
                GeneralUtil.iterateForEach(chatlog.getEntries(filter(args)), object -> {
                    String message = Messages.CHATLOG_PLAYER_LIST_NORMAL;
                    if(object.isBlocked()) message = Messages.CHATLOG_PLAYER_LIST_BLOCKED;
                    sender.sendMessage(message
                            .replace("[message]",object.getMessage())
                            .replace("[time]",""+BanSystem.getInstance().getConfig().dateFormat.format(object.getTime()))
                            .replace("[server]",object.getServer())
                            .replace("[filter]",(object.getFilter()!=null?object.getFilter().toString():"No"))
                            .replace("[prefix]",getPrefix()));
                });
                return;
            }else if(GeneralUtil.equalsOne(args[0],"server","servers","s","-s")){
                ChatLog chatlog = BanSystem.getInstance().getPlayerManager().getChatLog(args[1]);
                if(chatlog == null){
                    sender.sendMessage(Messages.CHATLOG_NOTFOUND.replace("[prefix]",getPrefix()));
                    return;
                }
                sender.sendMessage(Messages.CHATLOG_SERVER_HEADER
                        .replace("[server]",args[1])
                        .replace("[prefix]",getPrefix()));
                GeneralUtil.iterateForEach(chatlog.getEntries(filter(args)), object -> {
                    String message = Messages.CHATLOG_SERVER_LIST_NORMAL;
                    if(object.isBlocked()) message = Messages.CHATLOG_SERVER_LIST_BLOCKED;
                    sender.sendMessage(message
                            .replace("[message]",object.getMessage())
                            .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(object.getTime()))
                            .replace("[player]",""+object.getPlayer().getColoredName())
                            .replace("[server]",object.getServer())
                            .replace("[filter]",(object.getFilter()!=null?object.getFilter().toString():"No"))
                            .replace("[prefix]",getPrefix()));
                });
                return;
            }
        }
        sender.sendMessage(Messages.CHATLOG_HELP.replace("[prefix]",getPrefix()));
        /*

        /chatlog player/p <player>
        /chatlog server/s <server>

         */
    }

    private ChatLog.Filter filter(String[] args){
        ChatLog.Filter filter = new ChatLog.Filter();
        String typeKey = "";
        for(String arg : args){
            if(typeKey.equalsIgnoreCase("--server")) filter.setServer(arg);
            else if(typeKey.equalsIgnoreCase("--from")){
                if(GeneralUtil.isNumber(arg)){
                    filter.setFrom(System.currentTimeMillis()-(TimeUnit.DAYS.toMillis(Integer.valueOf(arg))));
                }else{
                    try{
                        filter.setFrom(new Date(arg).getTime());
                    }catch (Exception exception){}
                }
            }else if(typeKey.equalsIgnoreCase("--to")){
                if(GeneralUtil.isNumber(arg)){
                    filter.setTo(System.currentTimeMillis()+(TimeUnit.DAYS.toMillis(Integer.valueOf(arg))));
                }else{
                    try{
                        filter.setTo(new Date(arg).getTime());
                    }catch (Exception exception){}
                }
            }else if(typeKey.equalsIgnoreCase("--filter")){
                try{
                    filter.setFilter(FilterType.valueOf(arg));
                }catch (Exception exception){}
            }
            typeKey = arg;
        }
        return filter;
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        if(args.length == 1) return GeneralUtil.calculateTabComplete(args[0],null,ARGUMENTS);
        else if(args.length == 2){
            if(args[0].equalsIgnoreCase("player")){
                return GeneralUtil.calculateTabComplete(args[1],null,BanSystem.getInstance().getNetwork().getPlayersOnServer(sender.getServer()));
            }
        }
        return null;
    }
}
