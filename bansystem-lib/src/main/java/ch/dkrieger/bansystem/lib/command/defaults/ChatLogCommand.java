package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.filter.FilterType;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.chatlog.ChatLog;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChatLogCommand extends NetworkCommand {

    public ChatLogCommand() {
        super("chatlog","","dkbans.chatlog","","chatlogs");
    }

    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(GeneralUtil.equalsOne(args[0],"player","players","p","-p")){
            NetworkPlayer player = BanSystem.getInstance().getPlayerManager().getPlayer(args[1]);
            if(player == null){
                sender.sendMessage(Messages.PLAYER_NOT_FOUND.replace("[prefix]",getPrefix()));
                return;
            }
            ChatLog chatlog = BanSystem.getInstance().getPlayerManager().getChatLog(player.getUUID());
            if(chatlog == null){
                sender.sendMessage(Messages.CHATLOG_NOTFOUND.replace("[prefix]",getPrefix()));
                return;
            }
            GeneralUtil.iterateForEach(chatlog.getEntries(filter(args)), object -> {
                String message = Messages.CHATLOG_PLAYER_LIST_NORMAL;
                if(object.isBlocked()) message = Messages.CHATLOG_PLAYER_LIST_BLOCKED;
                sender.sendMessage(message
                        .replace("[message]",object.getMessage())
                        .replace("[time]",""+object.getTime())
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
            GeneralUtil.iterateForEach(chatlog.getEntries(filter(args)), object -> {
                String message = Messages.CHATLOG_SERVER_LIST_NORMAL;
                if(object.isBlocked()) message = Messages.CHATLOG_SERVER_LIST_BLOCKED;
                sender.sendMessage(message
                        .replace("[message]",object.getMessage())
                        .replace("[time]",""+object.getTime())
                        .replace("[player]",""+object.getPlayer().getColoredName())
                        .replace("[server]",object.getServer())
                        .replace("[filter]",(object.getFilter()!=null?object.getFilter().toString():"No"))
                        .replace("[prefix]",getPrefix()));
            });
            return;
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
        return null;
    }
}
