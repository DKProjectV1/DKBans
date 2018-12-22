package ch.dkrieger.bansystem.lib.storage.sql.query;

import ch.dkrieger.bansystem.lib.storage.sql.SQL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

/*
 * Copyright (c) 2018 Dkrieger on 16.05.18 15:49
 * Copyright (c) 2018 Philipp Elvin Friedhoff 05.12.18 14:58
 */

public class SelectQuery extends Query {


    public SelectQuery(SQL sql, String query) {
        super(sql, query);
    }

    public SelectQuery where(String key, Object value) {
        if(!and) {
            query += " WHERE";
            and = true;
        }else query += " AND";
        query += " `"+key+"`=?";
        values.add(value);
        return this;
    }

    public SelectQuery whereWithOr(String key, Object value) {
        if(!and){
            query += " WHERE";
            and = true;
        }else query += " or";
        query += " `"+key+"`=?";
        values.add(value);
        return this;
    }

    /*public ResultSet execute() {

    }*/


    public ResultSet execute(String... fields) {
        Map<String, Object> result = new LinkedHashMap<>();
        if(this.sql.isIgnoreCase() && this.sql.supportNoCase() && !this.query.contains("COLLATE NOCASE")) noCase();
        try {
            final PreparedStatement preparedStatement = getConnection().prepareStatement(query);
            int i = 1;
            for (Object object : values) {
                preparedStatement.setObject(i, object);
                i++;
            }
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public SelectQuery noCase() {
        this.query += " COLLATE NOCASE";
        return this;
    }
}