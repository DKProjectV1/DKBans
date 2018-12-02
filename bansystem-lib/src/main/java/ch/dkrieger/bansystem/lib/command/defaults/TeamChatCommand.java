package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;

import java.util.List;

public class TeamChatCommand extends NetworkCommand {

    public TeamChatCommand() {
        super("teamchat","","dkbans.teamchat.send","","tc","team","@team","tchat","teamc");
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 1){
            sender.sendMessage(Messages.TEAMCHAT_HELP
                    .replace("[prefix]",getPrefix()));
            return;
        }
        NetworkPlayer player = sender.getAsNetworkPlayer();
        if(args[0].equalsIgnoreCase("logout")) changeLogin(sender,player,false);
        else if(args[0].equalsIgnoreCase("login")) changeLogin(sender,player,true);
        else if(args[0].equalsIgnoreCase("toggle")) changeLogin(sender,player,!player.isTeamChatLoggedIn());
        else{
            if(!player.isTeamChatLoggedIn()){
                sender.sendMessage(Messages.STAFF_STATUS_NOT
                        .replace("[status]",Messages.STAFF_STATUS_LOGIN)
                        .replace("[prefix]",getPrefix()));
                return;
            }
            String message = "";
            for(int i = 0; i < args.length;i++)  message += args[i]+" "+Messages.TEAMCHAT_MESSAGE_COLOR;
            BanSystem.getInstance().getNetwork().sendTeamMessage(Messages.TEAMCHAT_MESSAGE_FORMAT
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",player.getColoredName())
                    .replace("[message]",message),true);
        }
    }
    private void changeLogin(NetworkCommandSender sender, NetworkPlayer player, boolean login){
        if(player.isTeamChatLoggedIn() == login){
            sender.sendMessage(Messages.STAFF_STATUS_ALREADY
                    .replace("[status]",(login?Messages.STAFF_STATUS_LOGIN:Messages.STAFF_STATUS_LOGOUT))
                    .replace("[prefix]",getPrefix()));
        }else{
            sender.sendMessage(Messages.STAFF_STATUS_CHANGE
                    .replace("[status]",(login?Messages.STAFF_STATUS_LOGIN:Messages.STAFF_STATUS_LOGOUT))
                    .replace("[prefix]",getPrefix()));
            player.setTeamChatLogin(login);
        }
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
