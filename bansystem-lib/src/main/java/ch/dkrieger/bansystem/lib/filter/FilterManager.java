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

package ch.dkrieger.bansystem.lib.filter;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FilterManager {

    private Map<Integer,Filter> filters;

    public FilterManager() {
        this.filters = new HashMap<>();
        reloadLocal();
    }
    public Filter getFilter(int id){
        return filters.get(id);
    }
    public List<Filter> getFilters(){
        return new LinkedList<>(filters.values());
    }
    public List<Filter> getFilters(FilterType type){
        List<Filter> filters = new LinkedList<>();
        GeneralUtil.iterateAcceptedForEach(this.filters.values(),object->object.getType().equals(type),filters::add);
        return filters;
    }
    public boolean isBlocked(FilterType type, String message){
        return GeneralUtil.iterateOne(this.filters.values(),object->object.getType().equals(type) && object.isBocked(message)) != null;
    }
    public Filter createFilterType(FilterType type,FilterOperation operation, String message){
        Filter filter = new Filter(-1,message,operation,type);
        filter.setID(BanSystem.getInstance().getStorage().createFilter(filter));
        this.filters.put(filter.getID(),filter);
        BanSystem.getInstance().getNetwork().reloadFilter();
        return filter;
    }
    public void deleteFilter(Filter filter){
        deleteFilter(filter.getID());
    }
    public void deleteFilter(int id){
        this.filters.remove(id);
        BanSystem.getInstance().getStorage().deleteFilter(id);
        BanSystem.getInstance().getNetwork().reloadFilter();
    }
    public void reloadLocal(){
        this.filters.clear();
        GeneralUtil.iterateForEach(BanSystem.getInstance().getStorage().loadFilters(),object ->filters.put(object.getID(),object));
    }
}
