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
import ch.dkrieger.bansystem.lib.filter.Filter;
import ch.dkrieger.bansystem.lib.filter.FilterOperation;
import ch.dkrieger.bansystem.lib.filter.FilterType;

import java.util.List;

public class FilterCommand extends NetworkCommand {

    public FilterCommand() {
        super("filter","","dkbans.filter");
        setPrefix(Messages.PREFIX_NETWORK);
    }

    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length >= 1){
            if(args[0].equalsIgnoreCase("reload")){
                BanSystem.getInstance().getNetwork().reloadFilter();
                sender.sendMessage(Messages.FILTER_RELOAD.replace("[prefix]",getPrefix()));
                return;
            }else if(args[0].equalsIgnoreCase("list")){
                List<Filter> filters = null;
                FilterType type = null;
                if(args.length >= 2){
                    try{
                        type = FilterType.valueOf(args[1].toUpperCase());
                        filters = BanSystem.getInstance().getFilterManager().getFilters(type);
                    }catch (Exception exception){
                        sender.sendMessage(Messages.FILTER_TYPE_NOTFOUND.replace("[prefix]",getPrefix()));
                        return;
                    }
                }else filters = BanSystem.getInstance().getFilterManager().getFilters();
                sender.sendMessage(Messages.FILTER_LIST_HEADER
                        .replace("[type]",type==null?"ALL":type.toString())
                        .replace("[prefix]",getPrefix()));
                for(Filter filter : filters){
                    sender.sendMessage(Messages.FILTER_LIST_LIST
                            .replace("[type]",filter.getType().toString())
                            .replace("[operation]",filter.getOperation().toString())
                            .replace("[id]",""+filter.getID())
                            .replace("[word]",filter.getMessage())
                            .replace("[prefix]",getPrefix()));
                }
                return;
            }else if(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("create")){
                FilterType type = null;
                FilterOperation operation = FilterOperation.CONTAINS;
                try{
                    type = FilterType.valueOf(args[1].toUpperCase());
                }catch (Exception exception){
                    sender.sendMessage(Messages.FILTER_TYPE_NOTFOUND.replace("[prefix]",getPrefix()));
                    return;
                }
                if(args.length >= 4){
                    try{
                        operation = FilterOperation.valueOf(args[3].toUpperCase());
                    }catch (Exception exception){
                        sender.sendMessage(Messages.FILTER_OPERATION_NOTFOUND.replace("[prefix]",getPrefix()));
                        return;
                    }
                }
                Filter filter = BanSystem.getInstance().getFilterManager().createFilterType(type,operation,args[2]);
                sender.sendMessage(Messages.FILTER_CREATE
                        .replace("[type]",filter.getType().toString())
                        .replace("[operation]",filter.getOperation().toString())
                        .replace("[id]",""+filter.getID())
                        .replace("[word]",args[2])
                        .replace("[prefix]",getPrefix()));
                return;
            }else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")){
                Filter filter = BanSystem.getInstance().getFilterManager().getFilter(Integer.valueOf(args[1]));
                if(filter == null){
                    sender.sendMessage(Messages.FILTER_NOTFOUND.replace("[prefix]",getPrefix()));
                    return;
                }
                BanSystem.getInstance().getFilterManager().deleteFilter(filter);
                sender.sendMessage(Messages.FILTER_DELETE
                        .replace("[type]",filter.getType().toString())
                        .replace("[word]",filter.getMessage())
                        .replace("[id]",args[1]).replace("[prefix]",getPrefix()));
                return;
            }
        }
        sender.sendMessage(Messages.FILTER_HELP.replace("[prefix]",getPrefix()));
        /*
        /filter reload
        /Filter list {type}
        /filter add <type> <word> {operation}
        /filter remove <id>

         */
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
