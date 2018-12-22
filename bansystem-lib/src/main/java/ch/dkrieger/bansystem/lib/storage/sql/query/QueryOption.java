package ch.dkrieger.bansystem.lib.storage.sql.query;

/*
 *
 *  * Copyright (c) 2018 Davide Wietlisbach on 19.09.18 08:01
 *
 */

import java.util.LinkedList;
import java.util.List;

public enum QueryOption {

    NOT_NULL("NOT NULL", false),
    UNIQUE("UNIQUE", true),
    PRIMARY_KEY("PRIMARY KEY", true),
    AUTO_INCREMENT("AUTO_INCREMENT",false);

    private String code;
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
