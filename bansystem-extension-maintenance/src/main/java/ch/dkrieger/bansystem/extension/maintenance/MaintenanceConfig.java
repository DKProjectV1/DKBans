/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 13.01.19 11:54
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
import ch.dkrieger.bansystem.lib.config.MessageConfig;

import java.util.Arrays;
import java.util.List;

public class MaintenanceConfig {

    public List<String> playerInfo;
    public String versionInfo, motdLine1, motdLine2, joinMessage, commandEnabled, commandDisabled, commandInfo
            ,commandTimeOut, commandReason, commandReasonAndTimeOut, commandInvalidDate, commandWhitelistAdd
            ,commandWhitelistRemove, commandWhitelistListHeader, commandWhitelistList, commandHelp;

    public MaintenanceConfig() {
        MessageConfig config = BanSystem.getInstance().getMessageConfig();
        motdLine1 = config.addAndGetMessageValue("dkbans.extension.maintenance.motd.line1","&e&lExample.net &8- &4DKNetwork &8[&a1.8 &8- &a1.13&8]");
        motdLine2 = config.addAndGetMessageValue("dkbans.extension.maintenance.motd.line2"," &8&l» &c&lMaintenance &8- &7[reason]");
        versionInfo = config.addAndGetMessageValue("dkbans.extension.maintenance.info.version","&4&l➜ &a&l[remaining] &7&lremain");
        playerInfo = config.addAndGetMessageListValue("dkbans.extension.maintenance.info.player", Arrays.asList("&7","&7Reason&8: &c[reason]","&7End&8: &c[TimeOut]","&7"));
        joinMessage = config.addAndGetMessageValue("dkbans.extension.maintenance.joinmessage","&e&lexample.net\n&5\n&cWe are currently in maintenance.\n&7\n&8» &7Reason&8: &c[reason]\n&8» &7End&8: &c[timeOut]\n&8» &7Remaining&8: &c[remaining]\n&7");
        commandEnabled = config.addAndGetMessageValue("dkbans.extension.maintenance.command.enabled","[prefix]&7The Maintenance mode is now &aenabled&7.");
        commandDisabled = config.addAndGetMessageValue("dkbans.extension.maintenance.command.disabled","[prefix]&7The Maintenance mode is now &cdisabled&7.");
        commandTimeOut = config.addAndGetMessageValue("dkbans.extension.maintenance.command.timeout","[prefix]&7Changed the maintenance end to &c[timeOut]&7.");
        commandReason = config.addAndGetMessageValue("dkbans.extension.maintenance.command.reason","[prefix]&7Changed the maintenance reason to &c[reason]&7.");
        commandReasonAndTimeOut = config.addAndGetMessageValue("dkbans.extension.maintenance.command.change","[prefix]&7The network is now in &7maintenance &7mode &7for &c[reason] &7until &c[timeOut]&7.");
        commandInfo = config.addAndGetMessageValue("dkbans.extension.maintenance.command.info","[prefix]&7Maintenance\n&8» &7Enabled &c[enabled]\n&8» &7Reason &c[reason]\n&8» &7End &c[timeOut]");
        commandInvalidDate = config.addAndGetMessageValue("dkbans.extension.maintenance.invaliddateformat","[prefix]&cInvalid date format &8(&7dd.MM.yyyy HH:mm&8)");
        commandWhitelistAdd = config.addAndGetMessageValue("dkbans.whitelist.add","[prefix]&7Added [player] to the maintenance whitelist.");
        commandWhitelistRemove = config.addAndGetMessageValue("dkbans.whitelist.remove","[prefix]&7Removed [player] from the maintenance whitelist.");
        commandWhitelistListHeader = config.addAndGetMessageValue("dkbans.whitelist.list.header","[prefix]&7Whitelisted players");
        commandWhitelistList = config.addAndGetMessageValue("dkbans.whitelist.list.list","&8» [player]");
        commandHelp = config.addAndGetMessageValue("dkbans.extension.maintenance.command.help","[prefix]&7Maintenance" +
                "\n&8» &e/maintenance enabled\n&8» &e/maintenance disable\n&8» &e/maintenance info\n&8» &e/maintenance add <player>" +
                "\n&8» &e/maintenance remove <player>\n&8» &e/maintenance list\n&8» &e/maintenance setEnd <dd.MM.yyyy> <HH:mm>" +
                "\n&8» &e/maintenance setDuration <time> {unit}\n&8» &e/maintenance setReason <reason>\n&8» &e/maintenance set <dd.MM.yyyy> <HH:mm> <reason>");
        config.save();
    }
}
