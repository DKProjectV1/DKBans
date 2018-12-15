package ch.dkrieger.bansystem.bungeecord;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.DKBansPlatform;
import ch.dkrieger.bansystem.lib.command.NetworkCommandManager;
import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;

public class BungeeCordBanSystemBootstrap extends Plugin implements DKBansPlatform {

    private static BungeeCordBanSystemBootstrap instance;
    private BungeeCordCommandManager commandManager;

    @Override
    public void onLoad() {
        instance = this;
        this.commandManager = new BungeeCordCommandManager();
        new BanSystem(this,new BungeeCordNetwork());
    }
    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        BanSystem.getInstance().shutdown();
    }

    @Override
    public String getPlatformName() {
        return "BungeeCord";
    }

    @Override
    public String getServerVersion() {
        return BungeeCord.getInstance().getVersion()+" | "+BungeeCord.getInstance().getGameVersion();
    }

    @Override
    public File getFolder() {
        return new File("plugins/DKBans/");
    }

    @Override
    public NetworkCommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public String getColor(NetworkPlayer player) {
        return null;
    }

    public static BungeeCordBanSystemBootstrap getInstance() {
        return instance;
    }
    /*
    NetworkPlayerLoginEvent
    NetworkPlayerUpdateEvent
    NetworkPlayerLogoutEvent
    OnlineNetworkPlayerUpdateEvent
    NetworkPlayerBanEvent
    NetworkPlayerKickEvent
    NetworkPlayerReportEvent
    NetworkPlayerReportProcessEvent
    NetworkPlayerReportAcceptEvent
    NetworkPlayerReportDenyEvent

     */
}
