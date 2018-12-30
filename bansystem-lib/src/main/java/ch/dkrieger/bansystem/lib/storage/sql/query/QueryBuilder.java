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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class QueryBuilder {

    private List<Query> queries;
    private String query;

    public QueryBuilder(Query... queries){
        this.queries = new LinkedList<>();
        this.queries.addAll(Arrays.asList(queries));
    }

    public QueryBuilder append(Query query){
        this.queries.add(query);
        return this;
    }

    public QueryBuilder remove(Query query){
        this.queries.remove(query);
        return this;
    }

    public QueryBuilder build(){
        for(Query query : this.queries){
            if(this.query == null) this.query = query.toString();
            else this.query += ";"+query.toString();
        }
        return this;
    }

    public void execute(){
        try(PreparedStatement preparedStatement = queries.get(0).getConnection().prepareStatement(this.query)) {
            int i = 1;
            for(Query query : this.queries){
                for(Object object : query.getValues()) {
                    preparedStatement.setString(i,object.toString());
                    i++;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void buildAndExecute(){
        build();
        execute();
    }
}
