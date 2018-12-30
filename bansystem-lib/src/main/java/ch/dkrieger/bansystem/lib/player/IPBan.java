/*
 * (C) Copyright 2018 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 30.12.18 20:22
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

package ch.dkrieger.bansystem.lib.player;

import java.util.UUID;

public class IPBan {

    private UUID lastPlayer;
    private String ip;
    private long timeStamp, timeOut;

    public IPBan(UUID lastPlayer, String ip, long timeStamp, long timeOut) {
        this.lastPlayer = lastPlayer;
        this.ip = ip;
        this.timeStamp = timeStamp;
        this.timeOut = timeOut;
    }

    public UUID getLastPlayer() {
        return lastPlayer;
    }

    public String getIp() {
        return ip;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public long getTimeOut() {
        return timeOut;
    }
    public long getDuration(){
        return timeOut-timeStamp;
    }
    public long getRemaining(){
        return timeOut-System.currentTimeMillis();
    }
}
