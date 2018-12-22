package ch.dkrieger.bansystem.lib.storage.sql;

/*
 *
 *  * Copyright (c) 2018 Philipp Elvin Friedhoff on 04.12.18 20:58
 *
 */

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLite extends SQL {

    private File path;
    private File file;
    private Connection connection;

    public SQLite(String path, String child) {
        this.path = new File(path);
        this.file = new File(path, child);
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
            e.printStackTrace();
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