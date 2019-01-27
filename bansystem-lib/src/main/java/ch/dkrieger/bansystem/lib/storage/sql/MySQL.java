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

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class MySQL extends SQL {

    private DataSource dataSource;
    private String host, port, database, user, password;
    private boolean ssl;
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

    public MySQL(String host, String port, String database, String user, String password, boolean ssl) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
        this.dataSourceProperties = new LinkedHashMap<>();
        this.ssl = ssl;
        setSupportNoCase(false);
        setOptionsOnEnd(true);
    }

    public MySQL(String host, int port, String database, String user, String password, boolean ssl) {
        this(host, String.valueOf(port), database, user, password,ssl);
    }

    public Map<String, String> getDataSourceProperties() {
        return dataSourceProperties;
    }

    @Override
	public boolean connect() {
        loadDriver();
        if(ssl) dataSourceProperties.put("ssl","true");
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