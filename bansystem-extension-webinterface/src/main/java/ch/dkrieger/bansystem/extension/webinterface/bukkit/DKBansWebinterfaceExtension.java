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

package ch.dkrieger.bansystem.extension.webinterface.bukkit;

import ch.dkrieger.bansystem.extension.webinterface.DKBansWebinterfaceConfig;
import ch.dkrieger.bansystem.extension.webinterface.WebinterfaceCommand;
import ch.dkrieger.bansystem.extension.webinterface.WebinterfaceHandler;
import ch.dkrieger.bansystem.extension.webinterface.bungeecord.BukkitDKBansNetworkPlayerAccessWebinterface;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.extension.restapi.DKBansRestAPIServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DKBansWebinterfaceExtension extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this,()->{
            DKBansWebinterfaceConfig config = new DKBansWebinterfaceConfig() {
                public boolean canAccess(NetworkPlayer player) {
                    BukkitDKBansNetworkPlayerAccessWebinterface event = new BukkitDKBansNetworkPlayerAccessWebinterface(player.getUUID(),System.currentTimeMillis(),false);
                    return !event.isCanceled();
                }
            };
            DKBansRestAPIServer.getInstance().registerRestApiHandler(new WebinterfaceHandler(config));
            BanSystem.getInstance().getCommandManager().registerCommand(new WebinterfaceCommand(config));
        },20);
    }
}
