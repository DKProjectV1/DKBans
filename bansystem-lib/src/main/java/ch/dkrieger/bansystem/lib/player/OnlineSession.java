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

package ch.dkrieger.bansystem.lib.player;

public class OnlineSession {

    private String ip, country, lastServer, proxy, clientLanguage;
    private int clientVersion;
    private long connected, disconnected;

    public OnlineSession(String ip, String country, String lastServer, String proxy, String clientLanguage,int clientVersion, long connected, long disconnected) {
        this.ip = ip;
        this.country = country;
        this.lastServer = lastServer;
        this.proxy = proxy;
        this.clientLanguage = clientLanguage;
        this.clientVersion = clientVersion;
        this.connected = connected;
        this.disconnected = disconnected;
    }

    public int getClientVersion() {
        return clientVersion;
    }

    public String getIp() {
        return ip;
    }

    public String getCountry() {
        return country;
    }

    public String getLastServer() {
        return lastServer;
    }

    public String getProxy() {
        return proxy;
    }

    public String getClientLanguage() {
        return clientLanguage;
    }

    public long getConnected() {
        return connected;
    }

    public long getDisconnected() {
        return disconnected;
    }
    public long getDuration(){
        return disconnected-connected;
    }

    public void setLastServer(String lastServer) {
        this.lastServer = lastServer;
    }

    public void setClientLanguage(String clientLanguage) {
        this.clientLanguage = clientLanguage;
    }

    public void setDisconnected(long disconnected) {
        this.disconnected = disconnected;
    }
}
