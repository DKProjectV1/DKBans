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

package ch.dkrieger.bansystem.lib;

import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;
import java.util.UUID;

public interface DKNetwork {

    public JoinMe getJoinMe(OnlineNetworkPlayer player);

    public JoinMe getJoinMe(NetworkPlayer player);

    public JoinMe getJoinMe(UUID uuid);

    public List<String> getPlayersOnServer(String server);

    public List<String> getGroupServers(String group);

    public void broadcast(String message);

    public void broadcast(TextComponent component);

    public void broadcast(Broadcast broadcast);

    public void broadcastLocal(Broadcast broadcast);

    public void sendJoinMe(JoinMe joinMe);

    public void sendTeamMessage(String message);

    public void sendTeamMessage(String message, boolean onlyLogin);

    public void sendTeamMessage(TextComponent component);

    public void sendTeamMessage(TextComponent component,boolean onlyLogin);

    public void reloadFilter();

    public void reloadBroadcast();

    public void syncSetting(String name);

}
