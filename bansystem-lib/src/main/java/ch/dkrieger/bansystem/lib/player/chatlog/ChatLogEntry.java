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

package ch.dkrieger.bansystem.lib.player.chatlog;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.filter.FilterType;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;

import java.util.UUID;

public class ChatLogEntry {

    private UUID uuid;
    private String message, server;
    private long time;
    private FilterType filter;

    public ChatLogEntry(UUID uuid, String message, String server, long time, FilterType filter) {
        this.uuid = uuid;
        this.message = message;
        this.server = server;
        this.time = time;
        this.filter = filter;
    }

    public UUID getUUID() {
        return uuid;
    }
    public NetworkPlayer getPlayer(){
        return BanSystem.getInstance().getPlayerManager().getPlayer(this.uuid);
    }

    public String getMessage() {
        return message;
    }

    public String getServer() {
        return server;
    }

    public long getTime() {
        return time;
    }

    public FilterType getFilter() {
        return filter;
    }

    public boolean isBlocked(){
        return this.filter != null;
    }
}
