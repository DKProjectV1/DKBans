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

package ch.dkrieger.bansystem.lib.storage.sql.table;

import ch.dkrieger.bansystem.lib.storage.sql.SQL;
import ch.dkrieger.bansystem.lib.storage.sql.query.*;

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
    public void execute(String query){
        new ExecuteQuery(sql,query).execute();
    }

    public DeleteQuery delete(){
        return new DeleteQuery(sql, "DELETE FROM `"+this.name+"`");
    }
}
