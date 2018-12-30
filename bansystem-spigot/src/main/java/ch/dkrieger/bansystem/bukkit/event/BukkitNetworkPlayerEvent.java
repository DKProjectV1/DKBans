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

package ch.dkrieger.bansystem.bukkit.event;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class BukkitNetworkPlayerEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private UUID uuid;
    private long timeStamp;
    private boolean onThisServer;

    public BukkitNetworkPlayerEvent(UUID uuid, long timeStamp, boolean onThisServer) {
        this.uuid = uuid;
        this.timeStamp = timeStamp;
        this.onThisServer = onThisServer;
    }
    public UUID getUUID() {
        return uuid;
    }
    public long getTimeStamp() {
        return timeStamp;
    }
    public NetworkPlayer getPlayer(){
        return BanSystem.getInstance().getPlayerManager().getPlayer(uuid);
    }
    public boolean isOnThisServer() {
        return onThisServer;
    }

    public OnlineNetworkPlayer getOnlinePlayer(){
        return BanSystem.getInstance().getPlayerManager().getOnlinePlayer(uuid);
    }
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
