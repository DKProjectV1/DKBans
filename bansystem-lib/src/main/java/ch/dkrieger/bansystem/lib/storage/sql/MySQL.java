package ch.dkrieger.bansystem.lib.storage.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/*
 * Copyright (c) 2018 Philipp Elvin Friedhoff on 04.12.1018 21:18
 */

public class MySQL extends SQL {

    private DataSource dataSource;
    private String host, port, database, user, password;
    private Map<String, String> dataSourceProperties;
    public static Map<String, String> DEFAULT_DATASOURCE_PROPERTIES;

    static {
        DEFAULT_DATASOURCE_PROPERTIES = new LinkedHashMap<>();
        DEFAULT_DATASOURCE_PROPERTIES.put("cachePrepStmts", "true");
        DEFAULT_DATASOURCE_PROPERTIES.put("prepStmtCacheSize", "250");
        DEFAULT_DATASOURCE_PROPERTIES.put("prepStmtCacheSqlLimit", "2048");
        DEFAULT_DATASOURCE_PROPERTIES.put("autoReconnect", "true");
        DEFAULT_DATASOURCE_PROPERTIES.put("allowMultiQueries", "true");
        DEFAULT_DATASOURCE_PROPERTIES.put("reconnectAtTxEnd", "true");
    }

    public MySQL(String host, String port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
        this.dataSourceProperties = new LinkedHashMap<>();
        setSupportNoCase(false);
        setOptionsOnEnd(true);
    }

    public MySQL(String host, int port, String database, String user, String password) {
        this(host, String.valueOf(port), database, user, password);
    }

    public Map<String, String> getDataSourceProperties() {
        return dataSourceProperties;
    }

    @Override
	public boolean connect() {
        loadDriver();
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://"+this.host+":"+this.port+"/"+this.database);
        config.setUsername(this.user);
        config.setPassword(this.password);
        config.setMaximumPoolSize(10);
        //config.setAutoCommit(false);
        DEFAULT_DATASOURCE_PROPERTIES.forEach(config::addDataSourceProperty);
        this.dataSourceProperties.forEach(config::addDataSourceProperty);
        this.dataSource = new HikariDataSource(config);
        return true;
	}

    @Override
    public void disconnect() {
        try {
            getDataSource().getConnection().close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    @Override
	public void loadDriver(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			System.out.println("Could not load MySQL driver");
		}
	}
}