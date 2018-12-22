package ch.dkrieger.bansystem.lib.storage.sql.table;

import ch.dkrieger.bansystem.lib.storage.sql.SQL;
import ch.dkrieger.bansystem.lib.storage.sql.query.*;

/*
 * Copyright (c) 2018 Dkrieger on 16.05.18 15:49
 * Copyright (c) 2018 Philipp Elvin Friedhoff 05.12.18 14:40
 */

public class Table {

    private String name;
    private SQL sql;

    public Table(SQL sql,String name) {
        this.name = name;
        this.sql = sql;
    }

    public String getName(){
        return this.name;
    }

    public SQL getSQL() {
        return this.sql;
    }

    public CreateQuery create(){
        return new CreateQuery(sql,"CREATE TABLE IF NOT EXISTS `"+this.name+"` (");
    }

    public InsertQuery insert(){
        return new InsertQuery(sql,"INSERT INTO `"+this.name+"` (");
    }

    public UpdateQuery update(){
        return new UpdateQuery(sql,"UPDATE `"+this.name+"` SET");
    }

    public SelectQuery selectAll(){
        return select("*");
    }

    public SelectQuery select() {
        return selectAll();
    }

    public SelectQuery select(String selection){
        return new SelectQuery(sql, "SELECT "+selection+" FROM `"+this.name+"`");
    }

    public DeleteQuery delete(){
        return new DeleteQuery(sql, "DELETE FROM `"+this.name+"`");
    }
}
