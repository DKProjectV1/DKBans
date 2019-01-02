/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 01.01.19 12:57
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

import ch.dkrieger.bansystem.bungeecord.event.ProxiedDKBansNetworkPlayerEvent;

import java.util.UUID;

public class ProxiedDKBansNetworkPlayerAccessWebinterface extends ProxiedDKBansNetworkPlayerEvent {

    private boolean canceled;

    public ProxiedDKBansNetworkPlayerAccessWebinterface(UUID uuid, long timeStamp, boolean onThisServer) {
        super(uuid, timeStamp, onThisServer);
        canceled = false;
    }
    public boolean isCanceled() {
        return canceled;
    }
    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
