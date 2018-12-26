package ch.dkrieger.bansystem.lib.storage.sql.query;

import ch.dkrieger.bansystem.lib.storage.sql.MySQL;
import ch.dkrieger.bansystem.lib.storage.sql.SQL;

import java.sql.Connection;
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

    private boolean addNoCase;

    public SelectQuery(SQL sql, String query) {
        super(sql, query);
        addNoCase = false;
    }

    public SelectQuery where(String key, Object value) {
        addNoCase = true;
        if(!and) {
            query += " WHERE";
            and = true;
        }else query += " AND";
        query += " `"+key+"`=?";
        values.add(value);
        return this;
    }

    public SelectQuery whereWithOr(String key, Object value) {
        addNoCase = true;
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

    public <R> R execute(ConsumerReturn<ResultSet,R> consumer) {
        if(this.sql.isIgnoreCase() && addNoCase&& this.sql.supportNoCase() && !this.query.contains("COLLATE NOCASE")) noCase();
        Connection connection = getConnection();
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            int i = 1;
            for (Object object : values) {
                preparedStatement.setObject(i, object);
                i++;
            }
            return consumer.accept(preparedStatement.executeQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if(sql instanceof MySQL)connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public SelectQuery noCase() {
        this.query += " COLLATE NOCASE";
        return this;
    }
    public interface ConsumerReturn<T,R> {

        public R accept(T object);

    }
}