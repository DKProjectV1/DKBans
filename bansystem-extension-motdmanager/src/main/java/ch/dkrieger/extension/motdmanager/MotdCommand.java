/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 19.01.19 11:32
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

package ch.dkrieger.extension.motdmanager;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;
import java.util.Map;

public class MotdCommand extends NetworkCommand {

    private final MotdConfig config;
    private DKMotd motd;

    public MotdCommand(MotdConfig config, DKMotd motd) {
        super("motd","","dkbans.motd");
        setPrefix(Messages.PREFIX_NETWORK);
        this.config = config;
        this.motd = motd;
    }

    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length > 0){
            if(args[0].equalsIgnoreCase("removeMessage") && GeneralUtil.isNumber(args[1])){
                String message = motd.getMessage(Integer.valueOf(args[1]));
                if(message != null) motd.removeMessage(Integer.valueOf(args[1]));
                else message = "Unknown";
                sender.sendMessage(new TextComponent(config.commandRemoveMessage
                        .replace("[id]",args[1])
                        .replace("[message]",message)
                        .replace("[prefix]",getPrefix())));
                save();
                return;
            }else if(args[0].equalsIgnoreCase("clear")){
                motd.clearMessages();
                sender.sendMessage(new TextComponent(config.commandClear.replace("[prefix]",getPrefix())));
                save();
                return;
            }else if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("info")){
                sender.sendMessage(new TextComponent(config.commandListHeader.replace("[prefix]",getPrefix())));

                sender.sendMessage(new TextComponent(config.commandListMain
                        .replace("[message]", motd.getMain())
                        .replace("[prefix]",getPrefix())));

                for(Map.Entry<Integer,String> entry : motd.getMessages().entrySet()){
                    sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&',config.commandListMessage)
                            .replace("[id]",""+entry.getKey())
                            .replace("[message]",entry.getValue())
                            .replace("[prefix]",getPrefix())));
                }
                return;
            }else if(args.length > 1){

                String message = "";
                for(int i = 1;i < args.length;i++) message += args[i]+" ";
                message = message.substring(0,message.length()-1);

                if(args[0].equalsIgnoreCase("setMain") || args[0].equalsIgnoreCase("setLine1")){
                    motd.setMain(message);
                    sender.sendMessage(new TextComponent(config.commandSetMain
                            .replace("[message]",motd.getMain())
                            .replace("[prefix]",getPrefix())));
                    save();
                    return;
                }else if(args[0].equalsIgnoreCase("addMessage") || args[0].equalsIgnoreCase("addLine2")){
                    int id = motd.addMessage(message);
                    sender.sendMessage(new TextComponent(config.commandAddMessage
                            .replace("[id]",""+id)
                            .replace("[message]",ChatColor.translateAlternateColorCodes('&',message))
                            .replace("[prefix]",getPrefix())));
                    save();
                    return;
                }
            }
        }
        sender.sendMessage(new TextComponent(config.commandHelp.replace("[prefix]",getPrefix())));
    }

    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }

    private void save(){
        BanSystem.getInstance().getSettingProvider().save("motd",new Document().append("motd",motd));
    }
}
