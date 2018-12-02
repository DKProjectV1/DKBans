package ch.dkrieger.bansystem.lib;

import ch.dkrieger.bansystem.lib.player.NetworkPlayer;

import java.util.UUID;

public class JoinMe {

    private UUID player;
    private String server;
    private long timeOut;

    public JoinMe(NetworkPlayer player, String server, long timeOut) {
        this(player.getUUID(),server,timeOut);
    }
    public JoinMe(UUID player, String server, long timeOut) {
        this.player = player;
        this.server = server;
        this.timeOut = timeOut;
    }
    public UUID getPlayerUUID() {
        return player;
    }
    public NetworkPlayer getPlayer(){
        return BanSystem.getInstance().getPlayerManager().getPlayer(this.player);
    }
    public String getServer() {
        return server;
    }

    public long getTimeOut() {
        return timeOut;
    }
}
