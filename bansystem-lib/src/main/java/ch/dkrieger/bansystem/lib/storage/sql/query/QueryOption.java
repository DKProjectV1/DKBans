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

import java.util.LinkedList;
import java.util.List;

public enum QueryOption {

    NOT_NULL("NOT NULL", false),
    UNIQUE("UNIQUE", true),
    PRIMARY_KEY("PRIMARY KEY", true),
    AUTO_INCREMENT("AUTO_INCREMENT",false);

    private String code, extraForSqlite;
    private boolean possibleEndOfQuery;

    QueryOption(String code, boolean possibleEndOfQuery) {
        this.code = code;
        this.possibleEndOfQuery = possibleEndOfQuery;
    }

    public String getCode() {
        return code;
    }

    public boolean isPossibleEndOfQuery() {
        return possibleEndOfQuery;
    }

    public static String[] getAsStringArray(QueryOption... queryOptions) {
        List<String> options = new LinkedList<>();
        for(QueryOption option : queryOptions) options.add(option.getCode());
        return options.toArray(new String[queryOptions.length]);
    }

    public static List<String> getPossibleEndOptions() {
        List<String> options = new LinkedList<>();
        for(QueryOption queryOption : QueryOption.values()) if(queryOption.isPossibleEndOfQuery()) options.add(queryOption.getCode());
        return options;
    }
}
