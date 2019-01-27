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

import ch.dkrieger.bansystem.lib.storage.sql.MySQL;
import ch.dkrieger.bansystem.lib.storage.sql.SQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
    public void execute() {
        execute(object -> null);
    }
    public <R> R execute(String query, ConsumerReturn<ResultSet,R> consumer) {
        this.query = query;
        this.addNoCase = false;
        return execute(consumer);
    }
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
            } catch (SQLException e) {}
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