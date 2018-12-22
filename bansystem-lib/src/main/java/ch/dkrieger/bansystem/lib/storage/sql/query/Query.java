package ch.dkrieger.bansystem.lib.storage.sql.query;

import ch.dkrieger.bansystem.lib.storage.sql.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * Copyright (c) 2018 Dkrieger on 16.05.18 15:49
 */

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
