/*
 * (C) Copyright 2019 The DKBans Project (Davide Wietlisbach)
 *
 * @author Davide Wietlisbach
 * @since 01.01.19 13:25
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

package ch.dkrieger.bansystem.extension.restapi.handler.defaults;

import ch.dkrieger.bansystem.extension.restapi.ResponseCode;
import ch.dkrieger.bansystem.extension.restapi.handler.RestApiHandler;
import ch.dkrieger.bansystem.lib.BanSystem;
import ch.dkrieger.bansystem.lib.filter.Filter;
import ch.dkrieger.bansystem.lib.filter.FilterOperation;
import ch.dkrieger.bansystem.lib.filter.FilterType;
import ch.dkrieger.bansystem.lib.utils.Document;
import ch.dkrieger.bansystem.lib.utils.GeneralUtil;

public class FilterHandler extends RestApiHandler {

    public FilterHandler() {
        super("filter/");
    }

    @Override
    public void onRequest(Document query, Document response) {
        if(query.contains("action")){
            if(query.getString("action").equalsIgnoreCase("list")){
                if(query.contains("type")){
                    FilterType type = FilterType.ParseNull(query.getString("type"));
                    if(type == null){
                        response.append("code", ResponseCode.BAD_REQUEST).append("message","Invalid filter type");
                        return;
                    }
                    response.append("filters",BanSystem.getInstance().getFilterManager().getFilters(type));
                }else response.append("filters",BanSystem.getInstance().getFilterManager().getFilters());
                return;
            }else if(query.getString("action").equalsIgnoreCase("create") && query.contains("message")&& query.contains("type")){
                FilterType type = FilterType.ParseNull(query.getString("type"));
                FilterOperation operation;
                if(query.contains("operation")) operation = FilterOperation.ParseNull(query.getString("operation"));
                else operation = FilterOperation.CONTAINS;

                if(type == null){
                    response.append("code", ResponseCode.BAD_REQUEST).append("message","Invalid filter type");
                    return;
                }
                if(operation == null){
                    response.append("code", ResponseCode.BAD_REQUEST).append("message","Invalid operation type");
                    return;
                }
                Filter filter = BanSystem.getInstance().getFilterManager().createFilterType(type,operation,query.getString("message"));
                response.append("filter",filter);
                return;
            }else if(query.getString("action").equalsIgnoreCase("delete") && query.contains("id") && GeneralUtil.isNumber(query.getString("id"))){
                BanSystem.getInstance().getFilterManager().deleteFilter(Integer.valueOf(query.getString("id")));
                response.append("message","Filter deleted");
                return;
            }
        }
        response.append("code", ResponseCode.BAD_REQUEST).append("message","Invalid request");
    }
}
