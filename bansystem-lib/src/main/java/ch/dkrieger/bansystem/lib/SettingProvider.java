/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 13.01.19 10:56
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

package ch.dkrieger.bansystem.lib;

import ch.dkrieger.bansystem.lib.utils.Document;

import java.util.LinkedHashMap;
import java.util.Map;

public class SettingProvider {

    private Map<String,Document> cache;

    public SettingProvider() {
        this.cache = new LinkedHashMap<>();
    }
    public Document get(String name){
        Document result = cache.get(name);
        if(result != null)return result;
        result = BanSystem.getInstance().getStorage().getSetting(name);
        if(result != null) this.cache.put(name,result);
        return result;
    }
    public void save(String name, Document document){
        this.cache.put(name,document);
        BanSystem.getInstance().getPlatform().getTaskManager().runTaskAsync(()->{
            BanSystem.getInstance().getStorage().saveSetting(name,document);
            BanSystem.getInstance().getNetwork().syncSetting(name);
        });
    }
    public void delete(String name){
        this.cache.remove(name);
        BanSystem.getInstance().getPlatform().getTaskManager().runTaskAsync(()->{
            BanSystem.getInstance().getStorage().deleteSetting(name);
            BanSystem.getInstance().getNetwork().syncSetting(name);
        });
    }
    public void removeFromCache(String name){
        this.cache.remove(name);
    }
    public void clearCache(){
        this.cache.clear();
    }
}
