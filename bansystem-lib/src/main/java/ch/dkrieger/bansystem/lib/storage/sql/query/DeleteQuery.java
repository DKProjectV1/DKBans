package ch.dkrieger.bansystem.lib.storage.sql.query;

import ch.dkrieger.bansystem.lib.storage.sql.SQL;

/*
 * Copyright (c) 2018 Dkrieger on 16.05.18 15:49
 * Copyright (c) 2018 Philipp Elvin Friedhoff 05.12.18 14:56
 */

public class DeleteQuery extends ExecuteQuery {

    public DeleteQuery(SQL sql, String query) {
        super(sql, query);
        this.firstvalue = true;
    }
    public DeleteQuery where(String key, Object value) {
        if(and) query += " AND";
        else{
            query += " WHERE";
            and = true;
        }
        query += " "+key+"=";
        values.add(value);
        query += "?";
        return this;
    }
    public DeleteQuery whereLower(String key, Object value) {
        if(and) query += " AND";
        else{
            query += " WHERE";
            and = true;
        }
        query += " "+key+"<";
        values.add(value);
        query += "?";
        return this;
    }
}