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
import ch.dkrieger.bansystem.lib.command.NetworkCommand;
import ch.dkrieger.bansystem.lib.command.NetworkCommandSender;
import ch.dkrieger.bansystem.lib.config.mode.BanMode;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.List;

public class MuteCommand extends NetworkCommand {

    public MuteCommand() {
        super("mute");

    }
    @Override
    public void onExecute(NetworkCommandSender sender, String[] args) {
        if(BanSystem.getInstance().getConfig().banMode == BanMode.SELF) sender.executeCommand("tempmute " + GeneralUtil.arrayToString(args, " "));
        else sender.executeCommand("ban " + GeneralUtil.arrayToString(args, " "));
    }
    @Override
    public List<String> onTabComplete(NetworkCommandSender sender, String[] args) {
        if(args.length == 1) return GeneralUtil.calculateTabComplete(args[0],sender.getName(),BanSystem.getInstance().getNetwork().getPlayersOnServer(sender.getServer()));
        return null;
    }
}
