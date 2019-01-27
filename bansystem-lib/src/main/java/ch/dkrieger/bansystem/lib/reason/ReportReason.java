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

package ch.dkrieger.bansystem.lib.reason;

import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.report.Report;
import ch.dkrieger.bansystem.lib.utils.Document;

import java.util.List;
import java.util.UUID;

public class ReportReason extends KickReason{

    private int forBan;

    public ReportReason(int id, String name, String display, String permission, boolean hidden, List<String> aliases, Document properties, int forban) {
        super(id,null, name, display, permission, hidden, aliases,properties);
        this.forBan = forban;
    }

    public int getForBan() {
        return forBan;
    }

    public Report toReport(NetworkPlayer player, UUID reporter, String message, String server){
        return new Report(player.getUUID(),null,reporter,getRawDisplay(),message,server,System.currentTimeMillis(),getID());
    }
}
