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

import ch.dkrieger.bansystem.lib.player.history.entry.Ban;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import ch.dkrieger.bansystem.lib.player.history.entry.Warn;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.UUID;

public interface OnlineNetworkPlayer {

    public UUID getUUID();

    public String getName();

    public String getProxy();

    public String getServer();

    public int getPing();

    public NetworkPlayer getPlayer();

    public void sendMessage(String message);

    public void sendMessage(TextComponent component);

    public void connect(String server);

    public void executeCommand(String command);

    public void sendBan(Ban ban);

    public void sendWarn(Warn warn);

    public void sendKick(Kick kick);

    void kickToFallback(String message);
}
