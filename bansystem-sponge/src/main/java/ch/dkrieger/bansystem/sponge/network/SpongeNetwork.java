/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 10.08.19, 21:12
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

package ch.dkrieger.bansystem.sponge.network;

import ch.dkrieger.bansystem.lib.DKNetwork;
import ch.dkrieger.bansystem.lib.JoinMe;
import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;
import java.util.UUID;

public class SpongeNetwork implements DKNetwork {

    @Override
    public JoinMe getJoinMe(OnlineNetworkPlayer player) {
        return null;
    }

    @Override
    public JoinMe getJoinMe(NetworkPlayer player) {
        return null;
    }

    @Override
    public JoinMe getJoinMe(UUID uuid) {
        return null;
    }

    @Override
    public List<String> getPlayersOnServer(String server) {
        return null;
    }

    @Override
    public List<String> getGroupServers(String group) {
        return null;
    }

    @Override
    public void broadcast(String message) {

    }

    @Override
    public void broadcast(TextComponent component) {

    }

    @Override
    public void broadcast(Broadcast broadcast) {

    }

    @Override
    public void broadcastLocal(Broadcast broadcast) {

    }

    @Override
    public void sendJoinMe(JoinMe joinMe) {

    }

    @Override
    public void sendTeamMessage(String message) {

    }

    @Override
    public void sendTeamMessage(String message, boolean onlyLogin) {

    }

    @Override
    public void sendTeamMessage(TextComponent component) {

    }

    @Override
    public void sendTeamMessage(TextComponent component, boolean onlyLogin) {

    }

    @Override
    public void reloadFilter() {

    }

    @Override
    public void reloadBroadcast() {

    }

    @Override
    public void syncSetting(String name) {

    }
}
