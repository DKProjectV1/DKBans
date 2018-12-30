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
import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.chat.TextComponent;


import java.util.List;

public class DKBansCommand extends NetworkCommand {

    public DKBansCommand() {
        super("dkbans");
        getAliases().add("dkban");
        setPrefix(Messages.PREFIX_NETWORK);
    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        sender.sendMessage(GeneralUtil.createLinkedMCText(Messages.PREFIX_NETWORK+"§7BanSystem §cv"+ BanSystem.getInstance().getVersion()+" §7by §cDavide Wietlisbach","https://www.spigotmc.org/resources/dkbans-bansystem-playermangement-bukkit-english-german.52570/"));
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        return null;
    }
}
