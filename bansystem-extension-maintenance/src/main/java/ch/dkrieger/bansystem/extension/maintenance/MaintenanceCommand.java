/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 13.01.19 12:27
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

package ch.dkrieger.bansystem.extension.maintenance;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

public class MaintenanceCommand extends NetworkCommand {

    private MaintenanceConfig config;
    private Maintenance maintenance;

    public MaintenanceCommand(MaintenanceConfig config, Maintenance maintenance) {
        super("maintenance","","dkbans.maintenance.change","","wartung");
        this.config = config;
        this.maintenance = maintenance;
        setPrefix(Messages.PREFIX_NETWORK);
    }

    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(args.length >= 1){
            if(args[0].equalsIgnoreCase("enable")){
                maintenance.setEnabled(true);
                sender.sendMessage(config.commandEnabled.replace("[prefix]",getPrefix()));
                save();
                return;
            }else if(args[0].equalsIgnoreCase("disable")){
                maintenance.setEnabled(false);
                sender.sendMessage(config.commandDisabled.replace("[prefix]",getPrefix()));
                save();
                return;
            }else if(args[0].equalsIgnoreCase("info")){
                sender.sendMessage(config.commandInfo
                        .replace("[reason]",maintenance.getReason())
                        .replace("[enabled]",""+maintenance.isRawEnabled())
                        .replace("[remaining]",""+maintenance.formatRemaining(false))
                        .replace("[remaining-short]",""+maintenance.formatRemaining(true))
                        .replace("[timeOut]",BanSystem.getInstance().getConfig().dateFormat.format(maintenance.getTimeOut()))
                        .replace("[prefix]",getPrefix()));
                return;
            }else if(args[0].equalsIgnoreCase("list")){
                sender.sendMessage(config.commandWhitelistListHeader.replace("[prefix]",getPrefix()));
                for(UUID uuid : maintenance.getWhitelist()){
                    NetworkPlayer player = BanSystem.getInstance().getPlayerManager().getPlayer(uuid);
                    if(player != null) sender.sendMessage(config.commandWhitelistList.replace("[player]",player.getColoredName()));
                }
                return;
            }else if (args.length >=2){
                if(args[0].equalsIgnoreCase("setDuration") && GeneralUtil.isNumber(args[1])){
                    maintenance.setTimeOut(System.currentTimeMillis()+GeneralUtil.convertToMillis(Long.valueOf(args[1]),(args.length >2?args[2]:null)));
                    sender.sendMessage(config.commandTimeOut
                            .replace("[timeOut]",BanSystem.getInstance().getConfig().dateFormat.format(maintenance.getTimeOut()))
                            .replace("[prefix]",getPrefix()));
                    save();
                    return;
                }else if(args[0].equalsIgnoreCase("setReason")){
                    String reason = "";
                    for(int i = 1;i<args.length;i++) reason+=args[i]+" ";
                    maintenance.setReason(reason);
                    sender.sendMessage(config.commandReason.replace("[reason]",reason).replace("[prefix]",getPrefix()));
                    save();
                    return;
                }else if(args[0].equalsIgnoreCase("add")){
                    NetworkPlayer player = BanSystem.getInstance().getPlayerManager().getPlayer(args[1]);
                    if(player == null){
                        sender.sendMessage(Messages.PLAYER_NOT_FOUND.replace("[player]",args[1]).replace("[prefix]",getPrefix()));
                        return;
                    }
                    maintenance.getWhitelist().add(player.getUUID());
                    save();
                    sender.sendMessage(config.commandWhitelistAdd.replace("[player]",player.getColoredName()).replace("[prefix]",getPrefix()));
                    return;
                }else if(args[0].equalsIgnoreCase("remove")){
                    NetworkPlayer player = BanSystem.getInstance().getPlayerManager().getPlayer(args[1]);
                    if(player == null){
                        sender.sendMessage(Messages.PLAYER_NOT_FOUND.replace("[player]",args[1]).replace("[prefix]",getPrefix()));
                        return;
                    }
                    maintenance.getWhitelist().remove(player.getUUID());
                    save();
                    sender.sendMessage(config.commandWhitelistRemove.replace("[player]",player.getColoredName()).replace("[prefix]",getPrefix()));
                    return;
                }
                if(args.length >=3){
                    if(args[0].equalsIgnoreCase("setEnd")){
                        try {
                            System.out.println(args[1]);
                            maintenance.setTimeOut(BanSystem.getInstance().getConfig().dateFormat.parse(args[1]+" "+args[2]).getTime());
                            sender.sendMessage(config.commandTimeOut
                                    .replace("[timeOut]",BanSystem.getInstance().getConfig().dateFormat.format(maintenance.getTimeOut()))
                                    .replace("[prefix]",getPrefix()));
                            save();
                        } catch (ParseException e) {
                            sender.sendMessage(config.commandInvalidDate.replace("[prefix]",getPrefix()));
                        }
                        return;
                    }else if(args[0].equalsIgnoreCase("set") && args.length >= 4){
                        try {
                            String reason = "";
                            for(int i = 3;i<args.length;i++) reason+=args[i]+" ";
                            maintenance.setReason(reason);
                            maintenance.setEnabled(true);
                            maintenance.setTimeOut(BanSystem.getInstance().getConfig().dateFormat.parse(args[1]+" "+args[2]).getTime());
                            sender.sendMessage(config.commandReasonAndTimeOut
                                    .replace("[reason]",maintenance.getReason())
                                    .replace("[timeOut]",BanSystem.getInstance().getConfig().dateFormat.format(maintenance.getTimeOut()))
                                    .replace("[prefix]",getPrefix()));
                            save();
                        } catch (ParseException e) {
                            sender.sendMessage(config.commandInvalidDate.replace("[prefix]",getPrefix()));
                        }
                        return;
                    }
                }
            }
        }
        sender.sendMessage(config.commandHelp.replace("[prefix]",getPrefix()));
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
    private void save(){
        BanSystem.getInstance().getSettingProvider().save("maintenance",new Document().append("maintenance",maintenance));
    }
}
