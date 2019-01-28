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

package ch.dkrieger.bansystem.lib.config;

import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class SimpleConfig {

    private transient final File file;
    private transient Configuration config;

    public SimpleConfig(File file) {
        this.file = file;

        try{
            file.getParentFile().mkdirs();
            if(!file.exists()) this.file.createNewFile();
        }catch (Exception exception){
            System.out.println(Messages.SYSTEM_PREFIX+"Could not create config file.");
            System.out.println(Messages.SYSTEM_PREFIX+"Error: "+exception.getMessage());
        }
    }
    public File getFile() {
        return file;
    }
    public void reloadConfig(){
        loadConfig();
    }
    public void loadConfig(){
        load();
        onLoad();
        save();
    }
    public void save(){
        if(file == null || !file.exists()) return;
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.config,file);
        }catch (Exception exception) {
            System.out.println(Messages.SYSTEM_PREFIX+"Could not save config file.");
            System.out.println(Messages.SYSTEM_PREFIX+"Error: "+exception.getMessage());
        }
    }
    public void load(){
        if(file == null) return;
        try{
            if(file.exists()) file.createNewFile();
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        }catch (Exception exception){
            System.out.println(Messages.SYSTEM_PREFIX+"Could not load config file.");
            System.out.println(Messages.SYSTEM_PREFIX+"Error: "+exception.getMessage());
        }
    }
    public void setValue(String path, Object value){
        this.config.set(path,value);
    }
    public void addValue(String path, Object value){
        if(!this.config.contains(path))this.config.set(path,value);
    }
    public String getStringValue(String path){
        return this.config.getString(path);
    }
    public String getMessageValue(String path){
        return ChatColor.translateAlternateColorCodes('&',getStringValue(path));
    }
    public int getIntValue(String path){
        return this.config.getInt(path);
    }
    public double getDoubleValue(String path){
        return this.config.getDouble(path);
    }
    public long getLongValue(String path){
        return this.config.getLong(path);
    }
    public boolean getBooleanValue(String path){
        return this.config.getBoolean(path);
    }
    public List<String> getStringListValue(String path){
        return this.config.getStringList(path);
    }
    public List<String> getMessageListValue(String path) {
        List<String> messages = new LinkedList<>();
        getStringListValue(path).forEach((message)-> messages.add(ChatColor.translateAlternateColorCodes('&', message)));
        return messages;
    }
    public List<Integer> getIntListValue(String path){
        return this.config.getIntList(path);
    }
    public List<Double> getDoubleListValue(String path){
        return this.config.getDoubleList(path);
    }
    public List<Long> getLongListValue(String path){
        return this.config.getLongList(path);
    }
    public List<Boolean> getBooleanListValue(String path){
        return this.config.getBooleanList(path);
    }
    public Object getValue(String path){
        return this.config.getStringList(path);
    }

    public String addAndGetStringValue(String path, Object object){
        addValue(path,object);
        return this.config.getString(path);
    }
    public String addAndGetMessageValue(String path,Object object){
        addValue(path,object);
        String result = getStringValue(path);
        if(result == null) return "";
        return GeneralUtil.buildNextLineColor(ChatColor.translateAlternateColorCodes('&',result));
    }
    public int addAndGetIntValue(String path,Object object){
        addValue(path,object);
        return this.config.getInt(path);
    }
    public double addAndGetDoubleValue(String path,Object object){
        addValue(path,object);
        return this.config.getDouble(path);
    }
    public long addAndGetLongValue(String path,Object object){
        addValue(path,object);
        return this.config.getLong(path);
    }
    public boolean addAndGetBooleanValue(String path,Object object){
        addValue(path,object);
        return this.config.getBoolean(path);
    }
    public List<String> addAndGetStringListValue(String path,Object object){
        addValue(path,object);
        return this.config.getStringList(path);
    }
    public List<String> addAndGetMessageListValue(String path, Object object) {
        addValue(path, object);
        return getMessageListValue(path);
    }
    public List<Integer> addAndGetIntListValue(String path,Object object){
        addValue(path,object);
        return this.config.getIntList(path);
    }
    public List<Double> addAndGetDoubleListValue(String path,Object object){
        addValue(path,object);
        return this.config.getDoubleList(path);
    }

    public List<Long> addAndGetLongListValue(String path,Object object){
        addValue(path,object);
        return this.config.getLongList(path);
    }
    public List<Boolean> addAndGetBooleanListValue(String path,Object object){
        addValue(path,object);
        return this.config.getBooleanList(path);
    }
    public char addAndGetCharValue(String path,Object object){
        addValue(path,object);
        return this.config.getChar(path);
    }
    public Object addAndGetValue(String path,Object object){
        addValue(path,object);
        return this.config.getStringList(path);
    }

    public Collection<String> getKeys(String path){
        Configuration config = this.config.getSection(path);
        if(config != null) return config.getKeys();
        else return new LinkedList<>();
    }
    public Boolean contains(String path){
        return this.config.contains(path);
    }
    public abstract void onLoad();
}
