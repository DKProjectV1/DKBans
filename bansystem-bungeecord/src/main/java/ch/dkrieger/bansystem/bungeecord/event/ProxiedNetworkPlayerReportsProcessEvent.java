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

package ch.dkrieger.bansystem.bungeecord.event;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.report.Report;

import java.util.List;
import java.util.UUID;

public class ProxiedNetworkPlayerReportsProcessEvent extends ProxiedDKBansNetworkPlayerEvent {

    private final UUID staff;

    public ProxiedNetworkPlayerReportsProcessEvent(UUID uuid, long timeStamp, boolean onThisServer,UUID staff) {
        super(uuid, timeStamp,onThisServer);
        this.staff = staff;
    }
    public UUID getStaffUUID(){
        return staff;
    }
    public NetworkPlayer getStaff(){
        return BanSystem.getInstance().getPlayerManager().getPlayer(this.staff);
    }
    public List<Report> getReport() {
        return getPlayer().getReports();
    }
}
