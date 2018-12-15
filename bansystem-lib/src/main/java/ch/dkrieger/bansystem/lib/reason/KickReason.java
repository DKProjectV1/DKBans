package ch.dkrieger.bansystem.lib.reason;

import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;

import java.util.List;

public class KickReason {

    private int id, points;
    private String name, display, permission;
    private boolean hidden;
    private List<String> aliases;

    public KickReason(int id, int points, String name, String display,String permission, boolean hidden, List<String> aliases) {
        this.id = id;
        this.points = points;
        this.name = name;
        this.display = display;
        this.permission = permission;
        this.hidden = hidden;
        this.aliases = aliases;
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
        return display;
    }

    public String getPermission() {
        return permission;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public boolean isHidden() {
        return hidden;
    }
    public boolean hasAlias(String alias){
        return this.name.equalsIgnoreCase(alias) ||this.aliases.contains(alias);
    }
    public Kick toKick(NetworkPlayer player, String staff){
        return null;
    }
}
