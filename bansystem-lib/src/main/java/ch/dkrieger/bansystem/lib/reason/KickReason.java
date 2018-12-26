package ch.dkrieger.bansystem.lib.reason;

import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import ch.dkrieger.bansystem.lib.utils.Document;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class KickReason {

    private int id, points;
    private String name, display, permission;
    private boolean hidden;
    private List<String> aliases;
    private Document properties;

    public KickReason(int id, int points, String name, String display,String permission, boolean hidden, List<String> aliases) {
        this.id = id;
        this.points = points;
        this.name = name;
        this.display = display;
        this.permission = permission;
        this.hidden = hidden;
        this.aliases = new ArrayList<>(aliases);
        this.properties = new Document();
    }

    public int getID() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

    public String getDisplay() {
        return ChatColor.translateAlternateColorCodes('&',display);
    }

    public String getPermission() {
        return permission;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public Document getProperties() {
        return properties;
    }

    public boolean isHidden() {
        return hidden;
    }
    public boolean hasAlias(String alias){
        return this.name.equalsIgnoreCase(alias) ||this.aliases.contains(alias);
    }
    public Kick toKick(NetworkPlayer player,String message,String server, String staff){
        return new Kick(player.getUUID(),player.getIP(),getDisplay(),message,System.currentTimeMillis(),-1,points,getID(),staff,new Document(),server);
    }
}
