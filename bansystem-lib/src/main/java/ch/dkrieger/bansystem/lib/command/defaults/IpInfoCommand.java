package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.BanType;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

public class IpInfoCommand extends NetworkCommand {

    public IpInfoCommand() {
        super("ipinfo");
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length > 1){
            sender.sendMessage(Messages.IPINFO_HEADER.replace("[prefix]",getPrefix()));
            return;
        }
        if(GeneralUtil.isIP4Address(args[0])){
            sender.sendMessage(Messages.IPINFO_IP_HEADER.replace("[prefix]",getPrefix()));
            GeneralUtil.iterateForEach(BanSystem.getInstance().getPlayerManager().getPlayers(args[0]), object -> {
                TextComponent component = new TextComponent(Messages.IPINFO_IP_LIST
                        .replace("[status]",(object.isBanned(BanType.NETWORK)?Messages.IPINFO_PLAYER_BANNED
                                :(object.isBanned(BanType.CHAT)?Messages.IPINFO_PLAYER_MUTED
                                :object.isOnline()?Messages.IPINFO_PLAYER_ONLINE:Messages.IPINFO_PLAYER_OFFLINE)))
                        .replace("[player]",object.getColoredName()).replace("[prefix]",getPrefix()));
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/playerinfo "+object.getName()));
                sender.sendMessage(component);
            });
            return;
        }else{
            sender.sendMessage(Messages.IPINFO_PLAYER_HEADER.replace("[prefix]",getPrefix()));
            NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
            if(player == null){
                sender.sendMessage(Messages.PLAYER_NOT_FOUND.replace("[player]",args[0]).replace("[prefix]",getPrefix()));
                return;
            }
            for(String ip : player.getIPs()){
                TextComponent component = new TextComponent(Messages.IPINFO_IP_LIST.replace("[ip]",ip).replace("[prefix]",getPrefix()));
                component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/ipinfo "+ip));
                sender.sendMessage(component);
            }
            return;
        }
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
