/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 19.01.19 14:15
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

package ch.dkrieger.extension.motdmanager.bukkit;

import ch.dkrieger.bansystem.bukkit.event.BukkitDKBansSettingUpdateEvent;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.extension.motdmanager.DKMotd;
import ch.dkrieger.extension.motdmanager.MotdCommand;
import ch.dkrieger.extension.motdmanager.MotdConfig;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DKBansMotdManagerExtension extends JavaPlugin implements Listener {

    private DKMotd motd;

    @Override
    public void onEnable() {
        MotdConfig config = new MotdConfig();
        Document data = BanSystem.getInstance().getSettingProvider().get("motd");
        if(data != null && data.contains("motd")) motd = data.getObject("motd",DKMotd.class);
        else motd = new DKMotd();
        Bukkit.getPluginManager().registerEvents(this,this);
        BanSystem.getInstance().getCommandManager().registerCommand(new MotdCommand(config,motd));
    }
    @EventHandler
    public void onSettingUpdate(BukkitDKBansSettingUpdateEvent event){
        if(event.getName().equalsIgnoreCase("mots")) this.motd = event.getSettings().getObject("motd",DKMotd.class);
    }

    @EventHandler(priority= EventPriority.HIGHEST)
    public void onPing(ServerListPingEvent event){
        event.setMotd(motd.getMain()+"\n"+motd.getRandomMessage());
    }
}
