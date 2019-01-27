/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 19.01.19 16:00
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

package ch.dkrieger.bansystem.extension.offlinepermissionhook.bukkit;

import ch.dkrieger.bansystem.bukkit.BukkitBanSystemBootstrap;
import ch.dkrieger.bansystem.bukkit.event.BukkitNetworkPlayerOfflinePermissionCheckEvent;
import ch.dkrieger.bansystem.extension.offlinepermissionhook.CPermsHook;
import ch.dkrieger.bansystem.extension.offlinepermissionhook.LuckPermsHook;
import ch.dkrieger.bansystem.extension.offlinepermissionhook.SimplePermissionHook;
import ch.dkrieger.bansystem.lib.Messages;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class DKBansOfflinePermissionHookExtension extends JavaPlugin implements Listener {

    private SimplePermissionHook hook;

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this,()->{
            if(checkPlugin("LuckPerms")) this.hook = new LuckPermsHook();
            else if(BukkitBanSystemBootstrap.getInstance().isCloudNetV2()) this.hook = new CPermsHook();

            if(hook != null){
                System.out.println(Messages.SYSTEM_PREFIX+"Invoked offline permission hook for "+hook.getName());
                Bukkit.getPluginManager().registerEvents(this,this);
            }else System.out.println(Messages.SYSTEM_PREFIX+"Could not invoke a offline permission hook, no permission system found.");
        },60L);
    }
    private boolean checkPlugin(String name){
        Plugin plugin = Bukkit.getPluginManager().getPlugin(name);
        return plugin != null && plugin.getDescription() != null;
    }
    @EventHandler
    public void onCheck(BukkitNetworkPlayerOfflinePermissionCheckEvent event){
        event.setHasPermission(hook.hasPermission(event.getPlayer(),event.getPermission()));
    }
}
