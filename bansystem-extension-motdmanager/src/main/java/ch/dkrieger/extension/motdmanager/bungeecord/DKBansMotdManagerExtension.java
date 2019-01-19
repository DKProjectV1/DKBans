/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 19.01.19 11:40
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

package ch.dkrieger.extension.motdmanager.bungeecord;

import ch.dkrieger.bansystem.bungeecord.event.ProxiedDKBansSettingUpdateEvent;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.extension.motdmanager.DKMotd;
import ch.dkrieger.extension.motdmanager.MotdCommand;
import ch.dkrieger.extension.motdmanager.MotdConfig;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class DKBansMotdManagerExtension extends Plugin implements Listener {

    private DKMotd motd;

    @Override
    public void onEnable() {
        MotdConfig config = new MotdConfig();

        Document data = BanSystem.getInstance().getSettingProvider().get("motd");
        if(data != null && data.contains("motd")) motd = data.getObject("motd",DKMotd.class);
        else motd = new DKMotd();
        ProxyServer.getInstance().getPluginManager().registerListener(this,this);
        BanSystem.getInstance().getCommandManager().registerCommand(new MotdCommand(config,motd));
    }
    @EventHandler
    public void onSettingUpdate(ProxiedDKBansSettingUpdateEvent event){
        if(event.getName().equalsIgnoreCase("motd")) this.motd = event.getSettings().getObject("motd",DKMotd.class);
    }

    @EventHandler(priority=80)
    public void onPing(ProxyPingEvent event){
        System.out.println(event.getConnection().getName()+" | "+event.getConnection().getUniqueId());
        event.getResponse().setDescriptionComponent(new TextComponent(motd.getMain()+"\n"+motd.getRandomMessage()));
    }
}
