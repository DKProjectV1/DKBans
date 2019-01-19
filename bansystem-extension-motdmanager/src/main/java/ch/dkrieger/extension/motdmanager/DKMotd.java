/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 19.01.19 11:33
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

package ch.dkrieger.extension.motdmanager;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import net.md_5.bungee.api.ChatColor;

import java.util.*;

public class DKMotd {

    private String main;
    private Map<Integer,String> messages;

    public DKMotd() {
        this("&e&lExample.net &8- &4DKNetwork &8[&a1.8 &8- &a1.13&8]",new LinkedHashMap<>());
    }

    public DKMotd(String main, Map<Integer,String> messages) {
        this.main = main;
        this.messages = messages;
    }

    public String getMain() {
        return ChatColor.translateAlternateColorCodes('&',main);
    }

    public Map<Integer,String> getMessages() {
        return messages;
    }
    public String getMessage(int id) {
        String message = this.messages.get(id);
        if(message != null) return ChatColor.translateAlternateColorCodes('&',message);
        return null;
    }
    public String getRandomMessage() {
        if(this.messages.size() < 1) return "§7This server is running §cDKBans v"+ BanSystem.getInstance().getVersion();
        return ChatColor.translateAlternateColorCodes('&',this.messages.values().toArray(new String[0])[GeneralUtil.RANDOM.nextInt(messages.size())]);
    }

    public void setMain(String main) {
        this.main = main;
    }

    public void setMessages(Map<Integer, String> messages) {
        this.messages = messages;
    }

    public void clearMessages(){
        messages.clear();
    }
    public int addMessage(String message){
        int i = 1;
        while(this.messages.containsKey(i)) i++;
        this.messages.put(i,message);
        return i;
    }
    public void removeMessage(int id){
        this.messages.remove(id);
    }
}
