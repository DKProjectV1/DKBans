package ch.dkrieger.bansystem.lib.command.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.JoinMe;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JoinMeCommand extends NetworkCommand {

    private Map<UUID,Long> cooldown;

    public JoinMeCommand() {
        super("joinme");
        this.cooldown = new LinkedHashMap<>();
        setPrefix(Messages.PREFIX_NETWORK);
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length > 0){
            NetworkPlayer player = BanSystem.getInstance().getPlayerManager().searchPlayer(args[0]);
            if(player != null){
                JoinMe joinMe  = BanSystem.getInstance().getNetwork().getJoinMe(player);
                if(joinMe != null){
                    OnlineNetworkPlayer online = sender.getAsOnlineNetworkPlayer();
                    if(online.getServer().equalsIgnoreCase(joinMe.getServer())) sender.sendMessage(Messages.SERVER_ALREADY
                            .replace("[server]",joinMe.getServer())
                            .replace("[prefix]",getPrefix()));
                    else{
                        sender.sendMessage(Messages.SERVER_CONNECTING
                                .replace("[server]",joinMe.getServer())
                                .replace("[prefix]",getPrefix()));
                        online.connect(joinMe.getServer());
                    }
                    return;
                }
            }
        }
        if(args.length == 0 && sender.hasPermission("dkbans.joinme")){
            if(this.cooldown.containsKey(sender.getUUID()) && this.cooldown.get(sender.getUUID()) > (System.currentTimeMillis())){
                sender.sendMessage(Messages.JOINME_COOLDOWN.replace("[prefix]",getPrefix()));
                return;
            }
            for(String server : BanSystem.getInstance().getConfig().joinMeDisabledServerList){
                if((BanSystem.getInstance().getConfig().joinMeDisabledServerEquals && sender.getServer().equalsIgnoreCase(server))
                        || (!BanSystem.getInstance().getConfig().joinMeDisabledServerEquals && sender.getServer().contains(server))){
                    sender.sendMessage(Messages.JOINME_NOTALLOWEDONSERVER.replace("[server]",sender.getServer())
                            .replace("[prefix]",getPrefix()));
                    return;
                }
            }
            OnlineNetworkPlayer player = sender.getAsOnlineNetworkPlayer();
            BanSystem.getInstance().getNetwork().sendJoinMe(new JoinMe(sender.getUUID(),player.getServer()
                    ,System.currentTimeMillis()+BanSystem.getInstance().getConfig().joinMeTimeOut));
            return;
        }
        sender.sendMessage(Messages.JOINME_NOTFOUND
                .replace("[prefix]",getPrefix()));
    }

    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
