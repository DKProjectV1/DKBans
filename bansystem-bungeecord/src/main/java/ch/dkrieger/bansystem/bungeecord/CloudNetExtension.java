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

package ch.dkrieger.bansystem.bungeecord;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.api.network.packet.out.PacketOutLogoutPlayer;
import de.dytanic.cloudnet.bridge.CloudProxy;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CloudNetExtension {

    public static void loginFix(UUID uuid){
        ProxyServer.getInstance().getScheduler().schedule(BungeeCordBanSystemBootstrap.getInstance(), () -> {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
            if(player == null){
                CloudPlayer cloudPlayer = CloudProxy.getInstance().getCloudPlayers().get(uuid);
                if(cloudPlayer != null) {
                    CloudAPI.getInstance().getNetworkConnection().sendPacket(new PacketOutLogoutPlayer(cloudPlayer, uuid));
                }
                CloudProxy.getInstance().getCloudPlayers().remove(uuid);
                ProxyServer.getInstance().getScheduler().schedule(de.dytanic.cloudnet.bridge.CloudProxy.getInstance().getPlugin(),
                        () -> CloudProxy.getInstance().update(), 250L, TimeUnit.MILLISECONDS);
            }
        }, 550L, TimeUnit.MILLISECONDS);
    }
}
