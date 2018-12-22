package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.broadcast.BroadcastManager;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

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
            return;
        }else if(args.length < 2){
            sender.sendMessage(Messages.BROADCAST_HELP
                    .replace("[prefix]",getPrefix()));
            return;
        }else if(args[0].equalsIgnoreCase("direct")){
            String message = "";
            for(int i = 1;i < args.length;i++) message = args[i];
            BanSystem.getInstance().getNetwork().broadcast(Messages.BROADCAST_FORMAT_DIRECT
                    .replace("[message]",message)
                    .replace("[prefix]",getPrefix()));
            return;
        }else if(args[0].equalsIgnoreCase("create")){
            String message = "";
            for(int i = 1;i < args.length;i++) message = args[i];
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
            sender.sendMessage(Messages.BROADCAST_NOTFOUND_BROADCAST.replace("[prefix]",getPrefix()));
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
            BanSystem.getInstance().getNetwork().broadcast(
                    GeneralUtil.replaceTextComponent(Messages.BROADCAST_FORMAT_SEND
                            .replace("[prefix]",getPrefix()),"[message]",broadcast.build()));
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
            String message = "";
            for(int i = 2;i < args.length;i++) message = args[i];

            BanSystem.getInstance().getBroadcastManager().createBroadcast(message);
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
                    .replace("[click]",""+broadcast.getClick().getMessage())
                    .replace("[message]",broadcast.getMessage())
                    .replace("[prefix]",getPrefix()));
            return;
        }else if(args[1].equalsIgnoreCase("sethover")){
            String message = "";
            for(int i = 2;i < args.length;i++) message = args[i];
            broadcast.setHover(message);
            BanSystem.getInstance().getBroadcastManager().updateBroadcast(broadcast);
            sender.sendMessage(Messages.BROADCAST_CHANGED_HOVER
                    .replace("[id]",""+broadcast.getID())
                    .replace("[hover]",""+broadcast.getHover())
                    .replace("[message]",broadcast.getMessage())
                    .replace("[prefix]",getPrefix()));
            return;
        }else if(args[1].equalsIgnoreCase("setmessage")){
            String message = "";
            for(int i = 2;i < args.length;i++) message = args[i];
            broadcast.setMessage(message);
            BanSystem.getInstance().getBroadcastManager().updateBroadcast(broadcast);
            sender.sendMessage(Messages.BROADCAST_CHANGED_MESSAGE
                    .replace("[message]",broadcast.getMessage())
                    .replace("[prefix]",getPrefix()));
            return;
        }else if(args[1].equalsIgnoreCase("addmessage")){
            String message = "";
            for(int i = 2;i < args.length;i++) message = args[i];
            broadcast.setMessage(broadcast.getMessage()+" "+message);
            BanSystem.getInstance().getBroadcastManager().updateBroadcast(broadcast);
            sender.sendMessage(Messages.BROADCAST_CHANGED_MESSAGE
                    .replace("[message]",broadcast.getMessage())
                    .replace("[prefix]",getPrefix()));
            return;
        }else if(args[1].equalsIgnoreCase("setauto")){
            broadcast.setAuto(Boolean.valueOf(args[2]));
            BanSystem.getInstance().getBroadcastManager().updateBroadcast(broadcast);
            if(broadcast.isAuto()) sender.sendMessage(Messages.BROADCAST_CHANGED_AUTO_ENABLED.replace("[prefix]",getPrefix()));
            else sender.sendMessage(Messages.BROADCAST_CHANGED_AUTO_DISABLED.replace("[prefix]",getPrefix()));
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
