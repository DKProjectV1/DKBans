/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 01.01.19 12:16
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

package ch.dkrieger.bansystem.bukkit.event;

import ch.dkrieger.bansystem.lib.player.NetworkPlayer;

import java.util.UUID;

public class BukkitNetworkPlayerOfflinePermissionCheckEvent extends BukkitDKBansNetworkPlayerEvent {

    private final NetworkPlayer player;
    private String permission;
    private boolean hasPermission;

    public BukkitNetworkPlayerOfflinePermissionCheckEvent(UUID uuid, long timeStamp, boolean onThisServer, NetworkPlayer player, String permission) {
        super(uuid, timeStamp, onThisServer);
        this.player = player;
        this.permission = permission;
        this.hasPermission = false;
    }

    public String getPermission() {
        return permission;
    }

    public boolean hasPermission() {
        return hasPermission;
    }

    public void setHasPermission(boolean hasPermission) {
        this.hasPermission = hasPermission;
    }

    @Override
    public NetworkPlayer getPlayer() {
        return player;
    }
}
