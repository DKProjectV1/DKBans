/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 02.01.19 20:43
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
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BroadcastJumpCommand extends NetworkCommand {

    public final static Map<String,Long> SERVER_WHITELISTS = new LinkedHashMap<>();

    public BroadcastJumpCommand() {
        super("broadcastjump");
        setPrefix(Messages.PREFIX_NETWORK);
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length > 1){
            if(SERVER_WHITELISTS.containsKey(args[1]) && (System.currentTimeMillis()+ TimeUnit.MINUTES.toMillis(10)) > SERVER_WHITELISTS.get(args[1])){
                if(args[0].equalsIgnoreCase("group")){
                    List<String> servers = BanSystem.getInstance().getNetwork().getGroupServers(args[1]);
                    if(servers.size() > 0) sender.getAsOnlineNetworkPlayer().connect(servers.get(GeneralUtil.RANDOM.nextInt(servers.size())));
                    else sender.sendMessage(Messages.SERVER_NOT_FOUND.replace("[prefix]",getPrefix()));
                }else sender.getAsOnlineNetworkPlayer().connect(args[1]);
                return;
            }
            SERVER_WHITELISTS.remove(args[1]);
        }
    }

    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
