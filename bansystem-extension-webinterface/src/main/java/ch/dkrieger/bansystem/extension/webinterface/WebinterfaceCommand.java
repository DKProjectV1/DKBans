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

package ch.dkrieger.bansystem.extension.webinterface;

import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.List;

public class WebinterfaceCommand extends NetworkCommand {

    private DKBansWebinterfaceConfig config;

    public WebinterfaceCommand(DKBansWebinterfaceConfig config) {
        super("dkbanswebinterface","","dkbans.webinterface","","dwi","dkbanswi","dwebinterface");
        this.config = config;
        setPrefix(Messages.PREFIX_NETWORK);
    }

    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length > 1){
            if(args[0].equalsIgnoreCase("setpassword")){
                if(args[1].length() < config.minPasswordLenght){
                    sender.sendMessage(config.passwordToShort.replace("[prefix]",getPrefix()));
                    return;
                }
                NetworkPlayer player = sender.getAsNetworkPlayer();
                player.getProperties().append("password", GeneralUtil.encodeMD5(args[1]));
                player.saveProperties();
                String password = "";
                for(int i = 0;i <args[1].length();i++) password+="*";
                sender.sendMessage(config.passwordChangedMessage.replace("[prefix]",getPrefix()).replace("[password]",password));
                return;
            }
        }
        sender.sendMessage(config.commandHelp.replace("[prefix]",getPrefix()));
    }
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
