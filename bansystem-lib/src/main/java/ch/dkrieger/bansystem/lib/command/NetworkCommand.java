/*
 * (C) Copyright 2020 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 24.01.20, 21:13
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

package ch.dkrieger.bansystem.lib.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class NetworkCommand {

    private String name, prefix, description, permission, usage;
    private List<String> aliases;

    public NetworkCommand(String name) {
        this(name,"unknown");
    }

    public NetworkCommand(String name, String description) {
        this(name,description,null);
    }

    public NetworkCommand(String name, String description, String permission) {
        this(name,description,permission,name);
    }

    public NetworkCommand(String name, String description, String permission, String usage, String... aliases) {
        this(name, description, permission, usage, Arrays.asList(aliases));
    }

    public NetworkCommand(String name, String description, String permission, String usage, List<String> aliases) {
        this.name = name;
        this.description = description;
        this.permission = permission;
        this.usage = usage;
        this.aliases = new ArrayList<>(aliases);
    }
    public String getName() {
        return this.name;
    }
    public String getPrefix() {
        return this.prefix;
    }
    public String getDescription() {
        return this.description;
    }
    public String getPermission() {
        return this.permission;
    }
    public String getUsage() {
        return usage;
    }
    public boolean hasUsage() {
        return usage != null;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public List<String> getAliases() {
        return this.aliases;
    }
    public Boolean hasAliase(String name){
        if(this.name.equalsIgnoreCase(name) || this.aliases.contains(name.toLowerCase())) return true;
        return false;
    }
    public NetworkCommand setUsage(String usage) {
        this.usage = usage;
        return this;
    }
    public NetworkCommand setDescription(String description) {
        this.description = description;
        return this;
    }
    public NetworkCommand setPermission(String permission) {
        this.permission = permission;
        return this;
    }
    public NetworkCommand addAlias(String... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
        return this;
    }
    public abstract void onExecute(NetworkCommandSender sender, String[] args);
    public abstract List<String> onTabComplete(NetworkCommandSender sender, String[] args);
}
