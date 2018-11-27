package ch.dkrieger.bansystem.lib.command;

import java.util.Arrays;
import java.util.List;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 16.11.18 17:47
 *
 */

public abstract class NetworkCommand {

    private String name, prefix, description, permission, usage;
    private List<String> aliases;

    public NetworkCommand(String name) {
        this(name,"none");
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
        this.aliases = aliases;
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