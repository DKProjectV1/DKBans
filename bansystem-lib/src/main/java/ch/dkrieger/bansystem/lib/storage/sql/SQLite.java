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

package ch.dkrieger.bansystem.lib.storage.sql;

import ch.dkrieger.bansystem.lib.Messages;
import ch.dkrieger.bansystem.lib.storage.sql.query.ColumnType;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite extends SQL {

    private File path;
    private File file;
    private Connection connection;

    public SQLite(File path, String database) {
        this.path = path;
        this.file = new File(path,database);
    }

    @Override
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public void loadDriver() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println(Messages.SYSTEM_PREFIX + "Driver is not available ("+e.getMessage()+")");
        }
    }

    @Override
    public boolean connect() {
        try {
            this.path.mkdirs();
            this.file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            loadDriver();
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.file.getPath());
            ColumnType.INT = "Integer";
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void disconnect() {
        try {
            this.connection.close();
            this.connection = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isConnected() {
        return this.connection != null;
    }
}