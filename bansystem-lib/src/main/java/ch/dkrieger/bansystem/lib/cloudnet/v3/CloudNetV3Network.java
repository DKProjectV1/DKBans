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

package ch.dkrieger.bansystem.lib.cloudnet.v3;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.DKNetwork;
import ch.dkrieger.bansystem.lib.JoinMe;
import ch.dkrieger.bansystem.lib.broadcast.Broadcast;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import de.dytanic.cloudnet.common.document.Document;
import de.dytanic.cloudnet.wrapper.Wrapper;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.*;

public abstract class CloudNetV3Network implements DKNetwork {

    private Map<UUID,JoinMe> joinmes;

    public CloudNetV3Network() {
        this.joinmes = new HashMap<>();
    }
    public void insertJoinMe(JoinMe joinme){
        this.joinmes.put(joinme.getUUID(),joinme);
    }

    @Override
    public JoinMe getJoinMe(OnlineNetworkPlayer player) {
        return this.joinmes.get(player.getUUID());
    }

    @Override
    public JoinMe getJoinMe(NetworkPlayer player) {
        return this.joinmes.get(player.getUUID());
    }

    @Override
    public JoinMe getJoinMe(UUID uuid) {
        return this.joinmes.get(uuid);
    }

    @Override
    public List<String> getPlayersOnServer(String server) {
        List<String> players = new ArrayList<>();
        GeneralUtil.iterateAcceptedForEach(BanSystem.getInstance().getPlayerManager().getOnlinePlayers()
                , object -> object.getServer().equalsIgnoreCase(server), object -> players.add(object.getName()));
        return players;
    }

    @Override
    public void broadcast(String message) {
        broadcast(new TextComponent(ChatColor.translateAlternateColorCodes('&',message)));
    }

    @Override
    public void broadcast(TextComponent component) {
        Wrapper.getInstance().sendChannelMessage("DKBans","broadcast",new Document().append("message",component));
    }

    @Override
    public void broadcast(Broadcast broadcast) {
        Wrapper.getInstance().sendChannelMessage("DKBans","broadcast",new Document().append("message",broadcast));
    }

    @Override
    public void sendJoinMe(JoinMe joinMe) {
        Wrapper.getInstance().sendChannelMessage("DKBans","sendJoinMe",new Document().append("joinme",joinMe));
    }

    @Override
    public void sendTeamMessage(String message) {
        sendTeamMessage(new TextComponent(message));
    }

    @Override
    public void sendTeamMessage(String message, boolean onlyLogin) {
        sendTeamMessage(new TextComponent(message),onlyLogin);
    }

    @Override
    public void sendTeamMessage(TextComponent component) {
        sendTeamMessage(component,false);
    }

    @Override
    public void sendTeamMessage(TextComponent component, boolean onlyLogin) {
        Wrapper.getInstance().sendChannelMessage("DKBans","sendTeamMessage",new Document().append("message",component)
                .append("onlyLogin",onlyLogin));
    }

    @Override
    public void reloadFilter() {
        Wrapper.getInstance().sendChannelMessage("DKBans","reloadFilter",new Document());
    }

    @Override
    public void reloadBroadcast() {
        Wrapper.getInstance().sendChannelMessage("DKBans","reloadBroadcast",new Document());
    }

    @Override
    public void syncSetting(String name) {
        Wrapper.getInstance().sendChannelMessage("DKBans","syncSetting",new Document("name",name));
    }
}
