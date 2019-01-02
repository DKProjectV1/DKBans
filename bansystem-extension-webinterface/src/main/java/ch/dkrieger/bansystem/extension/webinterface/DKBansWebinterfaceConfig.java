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

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;

public abstract class DKBansWebinterfaceConfig {

    public String passwordChangedMessage, passwordToShort, commandHelp;

    public int minPasswordLenght;

    public abstract boolean canAccess(NetworkPlayer player);

    public DKBansWebinterfaceConfig() {
        passwordChangedMessage = BanSystem.getInstance().getMessageConfig().addAndGetMessageValue("extension.webinterface.passwordChange","[prefix]&7The password was changed to &c[password]&7.");
        passwordToShort = BanSystem.getInstance().getMessageConfig().addAndGetMessageValue("extension.webinterface.passwordtoshort","[prefix]&cThe password must be longer than 6 letters.");
        commandHelp = BanSystem.getInstance().getMessageConfig().addAndGetMessageValue("extension.webinterface.commandhelp","[prefix]&cUsage&8: &7/dwi setPassword <password>");
        BanSystem.getInstance().getMessageConfig().save();

        minPasswordLenght = BanSystem.getInstance().getMessageConfig().addAndGetIntValue("extension.webinterface.minpasswordlenght",6);
        BanSystem.getInstance().getMessageConfig().save();
    }
}
