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
                sender.sendMessage(Messages.FILTER_LIST_HEADER.replace("[prefix]",getPrefix()));
                for(Filter filter : filters){
                    sender.sendMessage(Messages.FILTER_LIST_HEADER
                            .replace("[type]",filter.getType().toString())
                            .replace("[operation]",filter.getOperation().toString())
                            .replace("[id]",""+filter.getID())
                            .replace("[word]",args[2])
                            .replace("[prefix]",getPrefix()));
                }
            }else if(args[0].equalsIgnoreCase("add")){
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
            }else if(args[0].equalsIgnoreCase("remove")){
                Filter filter = BanSystem.getInstance().getFilterManager().getFilter(Integer.valueOf(args[1]));
                if(filter == null){
                    sender.sendMessage(Messages.FILTER_NOTFOUND.replace("[prefix]",getPrefix()));
                    return;
                }
                BanSystem.getInstance().getFilterManager().deleteFilter(filter);
                sender.sendMessage(Messages.FILTER_DELETE.replace("[id]",args[1]).replace("[prefix]",getPrefix()));
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
