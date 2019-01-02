/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 01.01.19 13:25
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

package ch.dkrieger.bansystem.extension.restapi.bukkit;

import ch.dkrieger.bansystem.extension.restapi.DKBansRestAPIServer;
import ch.dkrieger.bansystem.extension.restapi.DKBansRestApiConfig;
import org.bukkit.plugin.java.JavaPlugin;

public class DKBansRestAPIExtension extends JavaPlugin {

    private DKBansRestAPIServer server;

    @Override
    public void onEnable() {
        this.server = new DKBansRestAPIServer(new DKBansRestApiConfig());

        this.server.startAsync();
    }
    @Override
    public void onDisable() {
        this.server.shutdown();
    }
}
