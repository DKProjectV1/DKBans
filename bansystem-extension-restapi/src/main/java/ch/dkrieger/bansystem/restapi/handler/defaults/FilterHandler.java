package ch.dkrieger.bansystem.restapi.handler.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.filter.Filter;
import ch.dkrieger.bansystem.lib.filter.FilterOperation;
import ch.dkrieger.bansystem.lib.filter.FilterType;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.restapi.handler.RestApiHandler;

public class FilterHandler extends RestApiHandler {

    public FilterHandler() {
        super("filter/");
    }

    @Override
    public void onRequest(Query query, Document response) {
        if(query.contains("filter")){
            if(query.get("action").equalsIgnoreCase("list")){
                response.append("filters",BanSystem.getInstance().getFilterManager().getFilters());
            }else if(query.get("action").equalsIgnoreCase("create")){
                FilterType type = FilterType.ParseNull(query.get("type"));
                FilterOperation operation;
                if(query.contains("operation")) operation = FilterOperation.ParseNull(query.get("operation"));
                else operation = FilterOperation.CONTAINS;
                Filter filter = BanSystem.getInstance().getFilterManager().createFilterType(type,operation,query.get("message"));
                response.append("filter",filter);
            }else if(query.get("action").equalsIgnoreCase("delete")){
                BanSystem.getInstance().getFilterManager().deleteFilter(Integer.valueOf(query.get("id")));
                response.append("message","Filter deleted");
            }
        }
    }
}
