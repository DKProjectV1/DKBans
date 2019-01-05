package ch.dkrieger.bansystem.bukkit.event;

/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Philipp Elvin Friedhoff
 * @since 05.01.19 23:10
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

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.history.History;

import java.util.UUID;

public class BukkitNetworkPlayerHistoryUpdateEvent extends BukkitDKBansNetworkPlayerEvent {

    public BukkitNetworkPlayerHistoryUpdateEvent(UUID uuid, long timeStamp, boolean onThisServer) {
        super(uuid, timeStamp, onThisServer);
    }

    public History getHistory() {
        return BanSystem.getInstance().getPlayerManager().getPlayer(getUUID()).getHistory();
    }
}