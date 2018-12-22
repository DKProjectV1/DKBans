package ch.dkrieger.bansystem.lib.storage.sql.query;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/*
 * Copyright (c) 2018 Dkrieger on 16.05.18 15:49
 * Copyright (c) 2018 Philipp Elvin Friedhoff 06.12.18 15:51
 */

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
