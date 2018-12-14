package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.History;
import ch.dkrieger.bansystem.lib.player.history.value.Ban;
import ch.dkrieger.bansystem.lib.player.history.value.HistoryEntry;
import ch.dkrieger.bansystem.lib.player.history.value.Kick;
import ch.dkrieger.bansystem.lib.player.history.value.Unban;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

public class HistoryCommand extends NetworkCommand {

    public HistoryCommand() {
        super("history","","dkbans.history");
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
            HistoryEntry value = history.getEntry(Integer.valueOf(args[1]));
            if(value != null){
                String message = Messages.HISTORY_INFO_OTHER;
                if(value instanceof Ban){
                    message = Messages.HISTORY_INFO_BAN
                            .replace("[banType]",((Ban)value).getBanType().getDisplay())
                            .replace("[remaining]",GeneralUtil.calculateTime(((Ban)value).getRemaining(),true))
                            .replace("[duration]",GeneralUtil.calculateTime(((Ban)value).getDuration(),true));
                }else if(value instanceof Kick){
                    message = Messages.HISTORY_INFO_KICK.replace("[lastServer]",((Kick)value).getServer());
                }else if(value instanceof Unban){
                    sender.sendMessage(Messages.HISTORY_INFO_UNBAN);
                }else{
                    sender.sendMessage(Messages.HISTORY_INFO_OTHER
                            .replace("[prefix]",getPrefix())
                            .replace("[id]",""+value.getID())
                            .replace("[reason]",value.getReason())
                            .replace("[type]",value.getTypeName())
                            .replace("[points]",""+value.getPoints())
                            .replace("[message]",value.getMessage())
                            .replace("[date]",""+value.getTimeStamp())
                            .replace("[ip]",value.getIp())
                            .replace("[staff]",value.getStaffName())
                            .replace("[player]",player.getColoredName()));
                }
                sender.sendMessage(message
                        .replace("[prefix]",getPrefix())
                        .replace("[id]",""+value.getID())
                        .replace("[reason]",value.getReason())
                        .replace("[type]",value.getTypeName())
                        .replace("[points]",""+value.getPoints())
                        .replace("[message]",value.getMessage())
                        .replace("[date]",""+value.getTimeStamp())
                        .replace("[ip]",value.getIp())
                        .replace("[staff]",value.getStaffName())
                        .replace("[player]",player.getColoredName()));
                return;
            }
        }
        sender.sendMessage(Messages.HISTORY_LIST_HEADER
                .replace("[player]",player.getColoredName())
                .replace("[size]",""+history.size())
                .replace("[prefix]",getPrefix()));
        for(HistoryEntry value : history.getEntries()){
            TextComponent component = new TextComponent(Messages.HISTORY_LIST_LIST
                    .replace("[player]",player.getColoredName())
                    .replace("[id]",""+value.getID())
                    .replace("[reason]",value.getReason())
                    .replace("[reasonID]",""+value.getReasonID())
                    .replace("[time]",""+value.getTimeStamp())
                    .replace("[message]",value.getMessage())
                    .replace("[type]",value.getTypeName())
                    .replace("[staff]",value.getStaffName())
                    .replace("[points]",""+value.getPoints())
                    .replace("[prefix]",getPrefix()));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/history "+player.getUUID()+" "+value.getID()));
            sender.sendMessage(component);
        }

        //change to page System
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
