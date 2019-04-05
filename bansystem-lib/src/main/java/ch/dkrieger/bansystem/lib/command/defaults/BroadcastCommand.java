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
import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Arrays;
import java.util.List;

public class BroadcastCommand extends NetworkCommand {

    public BroadcastCommand() {
        super("broadcast","","dkbans.broadcast","","bc","alert","rundruf","autobroadcast","autobc");
        setPrefix(Messages.PREFIX_NETWORK);
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 1){
            sender.sendMessage(Messages.BROADCAST_HELP
                    .replace("[prefix]",getPrefix()));
            return;
        }
        if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")){
            BanSystem.getInstance().getNetwork().reloadBroadcast();
            sender.sendMessage(Messages.BROADCAST_RELOADED.replace("[prefix]",getPrefix()));
            return;
        }else if(args[0].equalsIgnoreCase("list")){
            sender.sendMessage(Messages.BROADCAST_LIST_HEADER.replace("[prefix]",getPrefix()));
            for(Broadcast broadcast : BanSystem.getInstance().getBroadcastManager().getBroadcasts()){
                TextComponent component = new TextComponent(Messages.BROADCAST_LIST_LIST
                        .replace("[id]",""+broadcast.getID())
                        .replace("[hover]",broadcast.getMessage())
                        .replace("[message]",broadcast.getMessage())
                        .replace("[prefix]",getPrefix()));
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/broadcast "+broadcast.getID()+" info"));
                sender.sendMessage(component);
            }
            return;
        }else if(args.length < 2){
            sender.sendMessage(Messages.BROADCAST_HELP.replace("[prefix]",getPrefix()));
            return;
        }else if(args[0].equalsIgnoreCase("direct")){
            String message = null;
            for(int i = 1;i < args.length;i++){
                if(message == null) message = args[i];
                else message += " " +args[i];
            }
            BanSystem.getInstance().getNetwork().broadcast(Messages.BROADCAST_FORMAT_DIRECT
                    .replace("[message]",GeneralUtil.buildNextLineColor(ChatColor.translateAlternateColorCodes('&',message)))
                    .replace("[prefix]",getPrefix()));
            return;
        }else if(args[0].equalsIgnoreCase("create")){
            String message = null;
            for(int i = 1;i < args.length;i++){
                if(message == null) message = args[i];
                else message += " "+args[i];
            }
            Broadcast broadcast = BanSystem.getInstance().getBroadcastManager().createBroadcast(message);
            sender.sendMessage(Messages.BROADCAST_CREATED
                    .replace("[id]",""+broadcast.getID())
                    .replace("[message]",broadcast.getMessage())
                    .replace("[prefix]",getPrefix()));
            return;
        }
        Broadcast broadcast = null;
        if(GeneralUtil.isNumber(args[0])) broadcast = BanSystem.getInstance().getBroadcastManager().getBroadcast(Integer.valueOf(args[0]));
        if(broadcast == null){
            sender.sendMessage(Messages.BROADCAST_NOTFOUND_BROADCAST
                    .replace("[id]",args[0])
                    .replace("[prefix]",getPrefix()));
            return;
        }
        if(args[1].equalsIgnoreCase("delete")){
            BanSystem.getInstance().getBroadcastManager().deleteBroadcast(broadcast);
            sender.sendMessage(Messages.BROADCAST_DELETED
                    .replace("[id]",""+broadcast.getID())
                    .replace("[message]",broadcast.getMessage())
                    .replace("[prefix]",getPrefix()));
            return;
        }else if(args[1].equalsIgnoreCase("send")){
            BanSystem.getInstance().getNetwork().broadcast(broadcast);
            return;
        }else if(args[1].equalsIgnoreCase("info")){
            sender.sendMessage(Messages.BROADCAST_INFO.replace("[prefix]",getPrefix())
                    .replace("[message]",broadcast.getMessage())
                    .replace("[id]",""+broadcast.getID())
                    .replace("[hover]",broadcast.getHover())
                    .replace("[auto]",""+broadcast.isAuto())
                    .replace("[clickType]",broadcast.getClick().getType().toString())
                    .replace("[clickMessage]",broadcast.getClick().getMessage()));
            return;
        }else if(args.length < 3){
            sender.sendMessage(Messages.BROADCAST_HELP
                    .replace("[prefix]",getPrefix()));
            return;
        }else if(args[1].equalsIgnoreCase("setclick")){
            if(args.length < 4){
                sender.sendMessage(Messages.BROADCAST_HELP
                        .replace("[prefix]",getPrefix()));
                return;
            }
            String message = null;
            for(int i = 3;i < args.length;i++){
                if(message == null) message = args[i];
                else message += " " +args[i];
            }

            Broadcast.Click click = new Broadcast.Click(message,null);
            try{
                click.setType(Broadcast.ClickType.valueOf(args[2].toUpperCase()));
            }catch (Exception exception){
                sender.sendMessage(Messages.BROADCAST_NOTFOUND_CLICKTYPE
                        .replace("[prefix]",getPrefix()));
                return;
            }
            broadcast.setClick(click);
            BanSystem.getInstance().getBroadcastManager().updateBroadcast(broadcast);
            sender.sendMessage(Messages.BROADCAST_CHANGED_CLICK
                    .replace("[id]",""+broadcast.getID())
                    .replace("[clickType]",""+broadcast.getClick().getType())
                    .replace("[clickMessage]",""+broadcast.getClick().getMessage())
                    .replace("[message]",broadcast.getMessage())
                    .replace("[prefix]",getPrefix()));
            return;
        }else if(args[1].equalsIgnoreCase("sethover")){
            String message = null;
            for(int i = 2;i < args.length;i++){
                if(message == null) message = args[i];
                else message += " " +args[i];
            }
            broadcast.setHover(message);
            BanSystem.getInstance().getBroadcastManager().updateBroadcast(broadcast);
            sender.sendMessage(Messages.BROADCAST_CHANGED_HOVER
                    .replace("[id]",""+broadcast.getID())
                    .replace("[hover]",""+broadcast.getHover())
                    .replace("[message]",broadcast.getMessage())
                    .replace("[prefix]",getPrefix()));
            return;
        }else if(args[1].equalsIgnoreCase("setmessage")){
            String message = null;
            for(int i = 2;i < args.length;i++){
                if(message == null) message = args[i];
                else message += " " +args[i];
            }
            broadcast.setMessage(message);
            BanSystem.getInstance().getBroadcastManager().updateBroadcast(broadcast);
            sender.sendMessage(Messages.BROADCAST_CHANGED_MESSAGE
                    .replace("[message]",broadcast.getMessage())
                    .replace("[id]",""+broadcast.getID())
                    .replace("[id]",""+broadcast.getID())
                    .replace("[prefix]",getPrefix()));
            return;
        }else if(args[1].equalsIgnoreCase("addmessage")){
            String message = null;
            for(int i = 2;i < args.length;i++){
                if(message == null) message = args[i];
                else message += " " +args[i];
            }
            broadcast.setMessage(broadcast.getMessage()+" "+message);
            BanSystem.getInstance().getBroadcastManager().updateBroadcast(broadcast);
            sender.sendMessage(Messages.BROADCAST_CHANGED_MESSAGE
                    .replace("[message]",broadcast.getMessage())
                    .replace("[id]",""+broadcast.getID())
                    .replace("[prefix]",getPrefix()));
            return;
        }else if(args[1].equalsIgnoreCase("setpermission")){
            broadcast.setPermission(args[2]);
            BanSystem.getInstance().getBroadcastManager().updateBroadcast(broadcast);
            sender.sendMessage(Messages.BROADCAST_CHANGED_PERMISSION
                    .replace("[id]",""+broadcast.getID())
                    .replace("[permission]",args[2]).replace("[prefix]",getPrefix()));
            return;
        }else if(args[1].equalsIgnoreCase("setauto")){
            broadcast.setAuto(Boolean.valueOf(args[2]));
            BanSystem.getInstance().getBroadcastManager().updateBroadcast(broadcast);
            if(broadcast.isAuto()) sender.sendMessage(Messages.BROADCAST_CHANGED_AUTO_ENABLED
                    .replace("[id]",""+broadcast.getID())
                    .replace("[prefix]",getPrefix()));
            else sender.sendMessage(Messages.BROADCAST_CHANGED_AUTO_DISABLED
                    .replace("[id]",""+broadcast.getID())
                    .replace("[prefix]",getPrefix()));
            return;
        }
        sender.sendMessage(Messages.BROADCAST_HELP
                .replace("[prefix]",getPrefix()));
        /*
        /bc reload
        /bc direct <message>
        /bc create <message>
        /bc <id> send
        /bc <id> delete
        /bc <id> setClick <type> <message>
        /bc <id> setHover <message>
        /bc <id> setMessage <message>
        /bc <id> addMessage <message>
        /bc <id> setAuto <true/false>
         */
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
