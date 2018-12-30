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
