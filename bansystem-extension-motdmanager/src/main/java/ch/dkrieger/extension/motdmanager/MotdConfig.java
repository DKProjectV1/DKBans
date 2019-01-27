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
import ch.dkrieger.bansystem.lib.config.MessageConfig;

public class MotdConfig {

    public String commandHelp, commandSetMain, commandAddMessage, commandRemoveMessage, commandClear, commandListHeader, commandListMain, commandListMessage;

    public MotdConfig() {
        MessageConfig config = BanSystem.getInstance().getMessageConfig();

        commandHelp = config.addAndGetMessageValue("extension.motd.help","[prefix]&7Motd" +
                        "\n&8» &e/motd list \n&8» &e/motd setMain <message>\n&8» &e/motd addMessage <message>\n&8» &e/motd removeMessage <id>\n&8» &e/motd clear");
        commandSetMain = config.addAndGetMessageValue("extension.motd.command.setmain","[prefix]&7The main message was changed to &c[message]&7.");
        commandAddMessage = config.addAndGetMessageValue("extension.motd.message.add","[prefix]&7The message &c[id] &8| &c[message] &7was added.");
        commandRemoveMessage = config.addAndGetMessageValue("extension.motd.message.remove","[prefix]&7The message &c[message] &7was removed.");
        commandClear = config.addAndGetMessageValue("extension.motd.message.clear","[prefix]&7Cleared all messages.");
        commandListHeader = config.addAndGetMessageValue("extension.motd.list.header","[prefix]&7Motd\n&7");
        commandListMain = config.addAndGetMessageValue("extension.motd.list.main","&8» &7Main&8: &c[message]\n");
        commandListMessage = config.addAndGetMessageValue("extension.motd.list.message","&8» &c[id] &8| &c[message]");

        config.save();
    }
}
