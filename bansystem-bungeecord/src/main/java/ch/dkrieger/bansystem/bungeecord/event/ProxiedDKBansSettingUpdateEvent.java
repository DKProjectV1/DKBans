/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 13.01.19 11:08
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

package ch.dkrieger.bansystem.bungeecord.event;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.utils.Document;
import net.md_5.bungee.api.plugin.Event;

public class ProxiedDKBansSettingUpdateEvent extends Event {

    private final String name;
    private final long timeStamp;
    private final boolean onThisServer;

    public ProxiedDKBansSettingUpdateEvent(String name, long timeStamp, boolean onThisServer) {
        this.name = name;
        this.timeStamp = timeStamp;
        this.onThisServer = onThisServer;
    }

    public String getName() {
        return name;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public boolean isOnThisServer() {
        return onThisServer;
    }

    public Document getSettings(){
        return BanSystem.getInstance().getSettingProvider().get(name);
    }
}
