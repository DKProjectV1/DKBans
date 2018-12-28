package ch.dkrieger.bansystem.bungeecord;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.api.network.packet.out.PacketOutLogoutPlayer;
import de.dytanic.cloudnet.bridge.CloudProxy;
import de.dytanic.cloudnet.lib.player.CloudPlayer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CloudNetExtension {

    public static void loginFix(UUID uuid){
        ProxyServer.getInstance().getScheduler().schedule(BungeeCordBanSystemBootstrap.getInstance(), () -> {
            ProxiedPlayer player = BungeeCord.getInstance().getPlayer(uuid);
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
