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

package ch.dkrieger.bansystem.lib.cloudnet.v2;

import ch.dkrieger.bansystem.lib.DKNetwork;
import ch.dkrieger.bansystem.lib.JoinMe;
import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.api.player.PlayerExecutorBridge;
import de.dytanic.cloudnet.lib.server.info.ServerInfo;
import de.dytanic.cloudnet.lib.utility.document.Document;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.*;

public abstract class CloudNetV2Network implements DKNetwork {

    private Map<UUID,JoinMe> joinmes;

    public CloudNetV2Network() {
        this.joinmes = new HashMap<>();
    }
    public void insertJoinMe(JoinMe joinme){
        this.joinmes.put(joinme.getUUID(),joinme);
    }

    @Override
    public JoinMe getJoinMe(OnlineNetworkPlayer player) {
        return joinmes.get(player.getUUID());
    }

    @Override
    public JoinMe getJoinMe(NetworkPlayer player) {
        return joinmes.get(player.getUUID());
    }

    @Override
    public JoinMe getJoinMe(UUID uuid) {
        return joinmes.get(uuid);
    }

    @Override
    public List<String> getPlayersOnServer(String server) {
        ServerInfo serverInfo = CloudAPI.getInstance().getServerInfo(server);
        if(serverInfo != null) return serverInfo.getPlayers();
        return new ArrayList<>();
    }

    @Override
    public void broadcast(String message) {
        broadcast(new TextComponent(ChatColor.translateAlternateColorCodes('&',message)));
    }

    @Override
    public void broadcast(TextComponent component) {
        CloudAPI.getInstance().sendCustomSubProxyMessage("DKBans","broadcast",new Document("message",component));
    }

    @Override
    public void broadcast(Broadcast broadcast) {
        CloudAPI.getInstance().sendCustomSubProxyMessage("DKBans","broadcast",new Document("broadcast",broadcast));
    }

    @Override
    public void sendJoinMe(JoinMe joinMe) {
        CloudAPI.getInstance().sendCustomSubProxyMessage("DKBans","sendJoinMe",new Document("joinme",joinMe));
    }

    @Override
    public void sendTeamMessage(String message) {
        sendTeamMessage(message,false);
    }

    @Override
    public void sendTeamMessage(String message, boolean onlyLogin) {
        sendTeamMessage(new TextComponent(message),false);
    }

    @Override
    public void sendTeamMessage(TextComponent component) {
        sendTeamMessage(component,false);
    }

    @Override
    public void sendTeamMessage(TextComponent component, boolean onlyLogin) {
        CloudAPI.getInstance().sendCustomSubProxyMessage("DKBans","sendTeamMessage", new Document().append("message",component)
                .append("onlyLogin",onlyLogin));
    }

    @Override
    public void reloadFilter() {
        CloudAPI.getInstance().sendCustomSubProxyMessage("DKBans","reloadFilter",new Document());
        CloudAPI.getInstance().sendCustomSubServerMessage("DKBans","reloadFilter",new Document());
    }

    @Override
    public void reloadBroadcast() {
        CloudAPI.getInstance().sendCustomSubProxyMessage("DKBans","reloadBroadcast",new Document());
        CloudAPI.getInstance().sendCustomSubServerMessage("DKBans","reloadBroadcast",new Document());
    }
}
