/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 05.04.19 22:47
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

package ch.dkrieger.bansystem.lib.reason;

import ch.dkrieger.bansystem.lib.player.NetworkPlayer;
import ch.dkrieger.bansystem.lib.player.history.HistoryPoints;
import ch.dkrieger.bansystem.lib.player.history.entry.Kick;
import ch.dkrieger.bansystem.lib.utils.Document;
import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class KickReason {

    private int id;
    private String name, display, permission;
    private boolean hidden;
    private List<String> aliases;
    private HistoryPoints points;
    private Document properties;

    public KickReason(int id,HistoryPoints points, String name, String display,String permission, boolean hidden, List<String> aliases, Document properties) {
        this.id = id;
        this.points = points;
        this.name = name;
        this.display = display;
        this.permission = permission;
        this.hidden = hidden;
        this.properties = properties;
        this.aliases = new ArrayList<>(aliases);
    }

    public int getID() {
        return id;
    }

    public HistoryPoints getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

    public String getDisplay() {
        return ChatColor.translateAlternateColorCodes('&',display);
    }
    public String getRawDisplay(){
        return display;
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
        return this.name.equalsIgnoreCase(alias) || this.aliases.contains(alias);
    }
    public Kick toKick(NetworkPlayer player,String message,String server, String staff){
        return new Kick(player.getUUID(),player.getIP(),getRawDisplay(),message,System.currentTimeMillis(),-1,points,getID(),staff,new Document(),server);
    }
}
