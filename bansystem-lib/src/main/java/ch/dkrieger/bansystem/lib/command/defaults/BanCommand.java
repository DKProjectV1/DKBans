package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.config.mode.BanMode;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.reason.BanReason;
import ch.dkrieger.bansystem.lib.reason.BanReasonValue;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

public class BanCommand extends NetworkCommand {

    public BanCommand() {
        super("ban","","dkbans.ban");
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length < 1){
            sendReasons(sender);
            return;
        }
        if(BanSystem.getInstance().getConfig().banMode == BanMode.SELF){
            sender.executeCommand("tempban "+GeneralUtil.arrayToString(args," "));
            return;
        }
        NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
        if(player != null){
            sender.sendMessage(Messages.PLAYER_NOT_FOUND
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",args[0]));
            return;
        }
        if(player.hasBypass() && !(sender.hasPermission("dkbans.bypass.ignore"))){
            sender.sendMessage(Messages.BAN_BYPASS
                    .replace("[prefix]",getPrefix())
                    .replace("[player]",player.getColoredName()));
            return;
        }

        BanReason reason = BanSystem.getInstance().getReasonProvider().searchBanReason(args[1]);
        if(reason == null){
            sendReasons(sender);
            return;
        }
        if(!sender.hasPermission(reason.getPermission())){
            sender.sendMessage(Messages.REASON_NO_PERMISSION
                    .replace("[prefix]",getPrefix())
                    .replace("[reason]",reason.getDisplay()));
            return;
        }
        BanReasonValue value = reason.getNextDuration(player);
        if(value == null){
            sender.sendMessage(Messages.ERROR
                    .replace("[prefix]",getPrefix()));
            return;
        }
        String message = "";
        boolean overwrite = false;

        if(args.length > 2) for(int i = 2; i< args.length;i++) {
            if(args[i].equalsIgnoreCase("--overwrite")) overwrite = true;
            else message += args[i] + " ";
        }
        if(player.isBanned(value.getType())){
            if(overwrite && sender.hasPermission("dkbans.ban.overwrite")
                    && !(sender.hasPermission("dkbans.ban.overwrite.all")
                    || player.getBan(value.getType()).getStaff().equalsIgnoreCase(sender.getUUID().toString()))){
                    sender.sendMessage(Messages.BAN_OVERWRITE_NOTALLOWED
                            .replace("[prefix]",getPrefix())
                            .replace("[player]",player.getColoredName()));
                    return;
            }else{
                if(value.getType() == BanType.NETWORK){
                    sender.sendMessage(Messages.PLAYER_ALREADY_BANNED
                            .replace("[prefix]",getPrefix())
                            .replace("[player]",player.getColoredName()));
                }else{
                    sender.sendMessage(Messages.PLAYER_ALREADY_MUTED
                            .replace("[prefix]",getPrefix())
                            .replace("[player]",player.getColoredName()));
                }
                if(sender.hasPermission("dkbans.ban.overwrite")
                        && (sender.hasPermission("dkbans.ban.overwrite.all")
                        || player.getBan(value.getType()).getStaff().equalsIgnoreCase(sender.getUUID().toString()))){
                    TextComponent component = new TextComponent(Messages.BAN_OVERWRITE_INFO
                            .replace("[prefix]",getPrefix())
                            .replace("[player]",player.getColoredName()));
                    component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND
                            ,"/ban "+args[0]+" "+args[1]+" "+message+" --overwrite"));
                    sender.sendMessage(component);
                }
                return;
            }
        }
        Ban ban = player.ban(reason,sender.getUUID());
        sender.sendMessage(Messages.BAN_SUCCESS
                .replace("[prefix]",getPrefix())
                .replace("[player]",player.getColoredName())
                .replace("[type]",ban.getBanType().getDisplay())
                .replace("[reason]",ban.getReason())
                .replace("[points]",String.valueOf(ban.getPoints()))
                .replace("[staff]",ban.getStaffName())
                .replace("[reasonID]",String.valueOf(ban.getReasonID()))
                .replace("[ip]",ban.getIp())
                .replace("[duration-short]", GeneralUtil.calculateTime(ban.getRemaining(),true))
                .replace("[duration]", GeneralUtil.calculateTime(ban.getRemaining(),false)));
    }
    private void sendReasons(NetworkCommandSender sender){
        sender.sendMessage(Messages.BAN_HELP_HEADER);
        for(BanReason reason : BanSystem.getInstance().getReasonProvider().getBanReasons()){
            if(!sender.hasPermission(reason.getPermission())) continue;
            sender.sendMessage(Messages.REASON_HELP
                    .replace("[prefix]",getPrefix())
                    .replace("[id]",""+reason.getID())
                    .replace("[name]",reason.getDisplay())
                    .replace("[historyType]",reason.getHistoryType().getDisplay())
                    .replace("[banType]",reason.getBanType().getDisplay())
                    .replace("[points]",""+reason.getPoints()));
        }
        sender.sendMessage(Messages.BAN_HELP_HELP);
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
