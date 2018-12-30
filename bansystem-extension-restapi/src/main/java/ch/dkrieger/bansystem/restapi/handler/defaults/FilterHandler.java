package ch.dkrieger.bansystem.restapi.handler.defaults;

import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.filter.Filter;
import ch.dkrieger.bansystem.lib.filter.FilterOperation;
import ch.dkrieger.bansystem.lib.filter.FilterType;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;
import ch.dkrieger.bansystem.restapi.ResponseCode;
import ch.dkrieger.bansystem.restapi.handler.RestApiHandler;

public class FilterHandler extends RestApiHandler {

    public FilterHandler() {
        super("filter/");
    }

    @Override
    public void onRequest(Query query, Document response) {
        if(query.contains("action")){
            if(query.get("action").equalsIgnoreCase("list")){
                response.append("filters",BanSystem.getInstance().getFilterManager().getFilters());
                return;
            }else if(query.get("action").equalsIgnoreCase("create") && query.contains("message")&& query.contains("type")){
                FilterType type = FilterType.ParseNull(query.get("type"));
                FilterOperation operation;
                if(query.contains("operation")) operation = FilterOperation.ParseNull(query.get("operation"));
                else operation = FilterOperation.CONTAINS;

                if(type == null){
                    response.append("code", ResponseCode.BAD_REQUEST).append("message","Invalid filter type");
                    return;
                }
                if(operation == null){
                    response.append("code", ResponseCode.BAD_REQUEST).append("message","Invalid operation type");
                    return;
                }
                Filter filter = BanSystem.getInstance().getFilterManager().createFilterType(type,operation,query.get("message"));
                response.append("filter",filter);
                return;
            }else if(query.get("action").equalsIgnoreCase("delete") && query.contains("id") && GeneralUtil.isNumber(query.get("id"))){
                BanSystem.getInstance().getFilterManager().deleteFilter(Integer.valueOf(query.get("id")));
                response.append("message","Filter deleted");
                return;
            }
        }
        response.append("code", ResponseCode.BAD_REQUEST).append("message","Invalid request");
    }
}
