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

package ch.dkrieger.bansystem.lib.stats;

public class PlayerStats extends NetworkStats {

    private long reportsReceived;

    public PlayerStats() {
        this.reportsReceived = 0;
    }

    public PlayerStats(long logins, long reports, long reportsAccepted, long messages, long bans, long mutes, long unbans, long kicks, long warns, long reportsReceived) {
        super(logins, reports, reportsAccepted, messages, bans, mutes, unbans, kicks,warns);
        this.reportsReceived = reportsReceived;
    }

    public long getReportsReceived() {
        return reportsReceived;
    }
    public void addReportsReceived(){
        this.reportsReceived++;
    }
}
