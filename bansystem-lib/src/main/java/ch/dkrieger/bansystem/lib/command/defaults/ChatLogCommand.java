package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.chatlog.ChatLog;
import ch.dkrieger.bansystem.lib.player.chatlog.ChatLogEntry;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.List;

public class ChatLogCommand extends NetworkCommand {

    public ChatLogCommand() {
        super("chatlog","","dkbans.chatlog","","chatlogs");
    }

    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(GeneralUtil.equalsOne(args[0],"player","players","p","-p")){
            ChatLog chatlog = null;
            GeneralUtil.iterateForEach(chatlog.getEntries(filter(args)), new GeneralUtil.ForEach<ChatLogEntry>() {
                @Override
                public void forEach(ChatLogEntry object) {
                    sender.sendMessage(Messages.CHATLOG_PLAYER_LIST
                            .replace("[message]",object.getMessage())
                            .replace("[time]",""+object.getTime())
                            .replace("[server]",object.getServer())
                            .replace("[prefix]",getPrefix()));
                }
            });

        }else if(GeneralUtil.equalsOne(args[0],"server","servers","s","-s")){

        }

        /*

        /chatlog player/p <player>
        /chatlog server/s <server>

         */
    }
    private ChatLog.Filter filter(String[] args){

    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
