package ch.dkrieger.bansystem.lib.filter;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import de.dytanic.cloudnet.lib.map.Maps;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FilterManager {

    private Map<Integer,Filter> filters;

    public FilterManager() {
        this.filters = new HashMap<>();
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
        return GeneralUtil.iterateOne(this.filters.values(), object -> object.getType().equals(type) && object.isBocked(message)) != null;
    }
    public Filter createFilterType(FilterType type,FilterOperation operation, String message){
        Filter filter = new Filter(-1,message,operation,type);
        filter.setID(BanSystem.getInstance().getStorage().createFilter(filter));
        this.filters.put(filter.getID(),filter);
        return filter;
    }
    public void deleteFilter(Filter filter){
        deleteFilter(filter.getID());
    }
    public void deleteFilter(int id){
        this.filters.remove(id);
        BanSystem.getInstance().getStorage().delteFilter(id);
    }
    public void reloadLocal(){
        this.filters.clear();
        GeneralUtil.iterateForEach(BanSystem.getInstance().getStorage().loadFilters(),object ->filters.put(object.getID(),object));
    }
}
