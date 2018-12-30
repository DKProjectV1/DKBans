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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Query {

    public static List<String> DEFAULT_END_OPTIONS;

    static {
        DEFAULT_END_OPTIONS = new LinkedList<>();
        DEFAULT_END_OPTIONS.addAll(QueryOption.getPossibleEndOptions());
    }

    protected SQL sql;
    protected PreparedStatement preparedStatement;
    protected String query;
    protected boolean firstvalue;
    protected boolean and;
    protected boolean comma;
    protected List<Object> values;
    protected Map<String, String> endOptions;

    public Query(SQL sql, String query){
        this.sql = sql;
        this.query = query;
        this.firstvalue = true;
        this.comma = false;
        this.and = false;
        this.values = new LinkedList<>();
        this.endOptions = new LinkedHashMap<>();
    }

    public Connection getConnection() {
        return this.sql.getConnection();
    }

    public List<Object> getValues(){
        return this.values;
    }

    public String toString(){
        return this.query;
    }
}
