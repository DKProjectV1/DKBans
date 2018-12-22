package ch.dkrieger.bansystem.lib.storage.sql.query;

import ch.dkrieger.bansystem.lib.storage.sql.SQL;

import java.sql.PreparedStatement;
import java.util.function.Consumer;

/*
 * Copyright (c) 2018 Dkrieger on 16.05.18 15:49
 * Copyright (c) 2018 Philipp Elvin Friedhoff 05.12.18 14:32
 */

public class InsertQuery extends ExecuteQuery {

    public InsertQuery(SQL sql, String query) {
        super(sql, query);
    }

    public InsertQuery insert(String insert) {
        query += "`"+insert+"`,";
        return this;
    }

    public InsertQuery value(Object value) {
        query = query.substring(0, query.length() - 1);
        if(firstvalue){
            query += ") VALUES (?)";
            firstvalue = false;
        }else query += ",?)";
        values.add(value);
        return this;
    }

    public void executeAndGetKeyAsIntAsync(Consumer<Integer> consumer) {
        consumer.accept((Integer) executeAndGetKey());
    }

    public Object executeAndGetKey() {
        return execute(PreparedStatement.RETURN_GENERATED_KEYS);
    }
    public int executeAndGetKeyAsInt(){
        return (int) executeAndGetKey();
    }
}
