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

package ch.dkrieger.bansystem.lib.storage.sql.query;

import ch.dkrieger.bansystem.lib.storage.sql.SQL;
import ch.dkrieger.bansystem.lib.storage.sql.SQLite;

public class CreateQuery extends ExecuteQuery {

    public CreateQuery(SQL sql, String query) {
        super(sql, query);
        firstvalue = true;
    }

    public CreateQuery create(String field, String type, QueryOption... options) {
        return create(field, type, QueryOption.getAsStringArray(options));
    }

    public CreateQuery create(String field, String type, String... options) {
        return create(field, type, 0, options);
    }

    public CreateQuery create(String field, String type, int size) {
        return create(field, type, size, QueryOption.getAsStringArray());
    }

    public CreateQuery create(String field, String type, int size, QueryOption... options) {
        return create(field, type, size, QueryOption.getAsStringArray(options));
    }

    public CreateQuery create(String field, String type, int size, String... options) {
        StringBuilder builder = new StringBuilder();
        builder.append("`").append(field).append("` ").append(type);
        if(size != 0) builder.append("(").append(size).append(")");
        for(String option : options) {
            if(sql instanceof SQLite && option.equalsIgnoreCase("AUTO_INCREMENT")) option = "AUTOINCREMENT";
            if(this.sql.isOptionsOnEnd() && DEFAULT_END_OPTIONS.contains(option.toUpperCase())) this.endOptions.put(field, option);
            else builder.append(" ").append(option);
        }
        return create(builder.toString());
    }

    public CreateQuery create(String value) {
        if(!firstvalue) query = query.substring(0,query.length()-1)+",";
        else firstvalue = false;
        query += value+")";
        return this;
    }
}