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
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.OnlineNetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import de.dytanic.cloudnet.common.document.Document;
import de.dytanic.cloudnet.wrapper.Wrapper;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CloudNetV3OnlinePlayer implements OnlineNetworkPlayer {

    public static Map<UUID,Integer> PINGGETTER;

    static{
        PINGGETTER = new HashMap<>();
    }
    private UUID uuid;
    private String name, server, proxy;

    public CloudNetV3OnlinePlayer(UUID uuid, String name, String server, String proxy) {
        this.uuid = uuid;
        this.name = name;
        this.server = server;
        this.proxy = proxy;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getProxy() {
        return proxy;
    }

    @Override
    public String getServer() {
        return server;
    }

    @Override
    public int getPing() {
        PINGGETTER.remove(getUUID());
        Wrapper.getInstance().sendChannelMessage("DKBans","getPing"
                ,new Document().append("uuid",getUUID()));
        int timeOut = 0;

        while(!PINGGETTER.containsKey(getUUID()) && timeOut < 1000){
            timeOut++;
        }
        if(PINGGETTER.containsKey(getUUID())) return PINGGETTER.get(getUUID());
        return -1;
    }

    @Override
    public NetworkPlayer getPlayer() {
        return BanSystem.getInstance().getPlayerManager().getPlayer(getUUID());
    }

    @Override
    public void sendMessage(String message) {
        sendMessage(new TextComponent(message));
    }

    @Override
    public void sendMessage(TextComponent component) {
        Wrapper.getInstance().sendChannelMessage("DKBans","sendMessage",new Document()
                .append("uuid",getUUID()).append("message",component));
    }

    @Override
    public void connect(String server) {
        Wrapper.getInstance().sendChannelMessage("DKBans","connect",new Document()
                .append("uuid",getUUID()).append("server",server));
    }

    @Override
    public void executeCommand(String command) {
        Wrapper.getInstance().sendChannelMessage("DKBans","executeCommand",new Document()
                .append("uuid",getUUID()).append("command",command));
    }

    @Override
    public void sendBan(Ban ban) {
        Wrapper.getInstance().sendChannelMessage("DKBans","sendBan",new Document()
                .append("uuid",getUUID()).append("ban",ban));
    }

    @Override
    public void kick(Kick kick) {
        Wrapper.getInstance().sendChannelMessage("DKBans","sendKick",new Document()
                .append("uuid",getUUID()).append("kick",kick));
    }

    public void setServer(String server) {
        this.server = server;
    }
}
