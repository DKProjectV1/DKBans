/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 08.11.19, 22:06
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

package ch.dkrieger.bansystem.extension.commandsonban;

import ch.dkrieger.bansystem.lib.BanSystem;

import java.util.Arrays;
import java.util.List;

public class CommandsOnBanConfig {

    public boolean executeOnAllServers;
    public List<String> commands;

    public CommandsOnBanConfig() {
        executeOnAllServers = BanSystem.getInstance().getConfig().addAndGetBooleanValue("extension.commandOnBan.executeOnAllServers",false);
        commands = BanSystem.getInstance().getConfig().addAndGetStringListValue("extension.commandOnBan.commands", Arrays.asList("say Hey from Ban"));
        BanSystem.getInstance().getConfig().save();
    }

}
