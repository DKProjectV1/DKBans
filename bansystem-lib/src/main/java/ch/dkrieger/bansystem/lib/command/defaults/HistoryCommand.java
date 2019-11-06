/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 06.11.19, 20:31
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
import ch.dkrieger.bansystem.lib.player.history.History;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.HistoryEntry;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

public class HistoryCommand extends NetworkCommand {

    public HistoryCommand() {
        super("history","","dkbans.history");
        setPrefix(Messages.PREFIX_BAN);
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 1){
            sender.sendMessage(Messages.HISTORY_HELP.replace("[prefix]",getPrefix()));
            return;
        }
        NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
        if(player == null){
            sender.sendMessage(Messages.PLAYER_NOT_FOUND
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",args[0]));
            return;
        }
        History history = player.getHistory();
        if(history == null || history.size() == 0){
            sender.sendMessage(Messages.HISTORY_NOTFOUND
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",player.getColoredName()));
            return;
        }
        if(args.length > 1 && GeneralUtil.isNumber(args[1])){
            HistoryEntry entry = history.getEntry(Integer.parseInt(args[1]));
            if(entry != null){
                if(args.length > 2 && entry instanceof Ban){
                    if(args[2].equalsIgnoreCase("list")){
                        sender.sendMessage(Messages.HISTORY_INFO_BAN_VERSION_LIST_HEADER
                                .replace("[id]",""+entry.getID())
                                .replace("[player]",player.getColoredName())
                                .replace("[prefix]",getPrefix()));

                        TextComponent first = new TextComponent(Messages.HISTORY_INFO_BAN_VERSION_LIST_FIRST
                                .replace("[id]",""+entry.getID())
                                .replace("[player]",player.getColoredName())
                                .replace("[time]",BanSystem.getInstance().getConfig().dateFormat.format(entry.getTimeStamp())));
                        first.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/history "+player.getUUID()+" "+entry.getID()+" first"));
                        sender.sendMessage(first);

                        for(Ban.BanEditVersion version :((Ban) entry).getVersionsSorted()){
                            TextComponent component = new TextComponent(version.getListMessage()
                                    .replace("[id]",""+version.getID())
                                    .replace("[entryID]",""+entry.getID())
                                    .replace("[type]",((Ban) entry).getBanType().getDisplay())
                                    .replace("[player]",player.getColoredName()));
                            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/history "+player.getUUID()+" "+entry.getID()+" "+version.getID()));
                            sender.sendMessage(component);
                        }
                        return;
                    }else if(args[2].equalsIgnoreCase("first")){
                        sender.sendMessage(((Ban) entry).getInfo(true));
                        return;
                    }else if(GeneralUtil.isNumber(args[2])){
                        Ban.BanEditVersion version = ((Ban) entry).getVersion(Integer.valueOf(args[2]));
                        if(version != null){
                            sender.sendMessage(new TextComponent(version.getInfoMessage()
                                    .replace("[id]",""+version.getID())
                                    .replace("[entryID]",""+entry.getID())
                                    .replace("[type]",((Ban) entry).getBanType().getDisplay())
                                    .replace("[player]",player.getColoredName())));
                            return;
                        }
                    }
                }
                sender.sendMessage(entry.getInfoMessage());
            }
            return;
        }
        sender.sendMessage(Messages.HISTORY_LIST_HEADER
                .replace("[player]",player.getColoredName())
                .replace("[size]",""+history.size())
                .replace("[prefix]",getPrefix()));
        for(HistoryEntry value : history.getEntriesSorted()){
            TextComponent component = value.getListMessage();
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/history "+player.getUUID()+" "+value.getID()));
            sender.sendMessage(component);
        }

        //change to page System
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        if(args.length == 1) return GeneralUtil.calculateTabComplete(args[0],sender.getName(),BanSystem.getInstance().getNetwork().getPlayersOnServer(sender.getServer()));
        return null;
    }
}
