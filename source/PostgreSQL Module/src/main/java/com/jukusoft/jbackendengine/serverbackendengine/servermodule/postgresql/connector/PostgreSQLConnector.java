package com.jukusoft.jbackendengine.serverbackendengine.servermodule.postgresql.connector;

import com.jukusoft.jbackendengine.backendengine.IBackendEngine;
import com.jukusoft.jbackendengine.backendengine.database.impl.DatabaseConnector;
import com.jukusoft.jbackendengine.backendengine.serversettings.IServerSettings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Justin on 10.07.2015.
 */
public class PostgreSQLConnector extends DatabaseConnector {

    private IBackendEngine backendEngine = null;
    private Connection connection = null;

    public PostgreSQLConnector (IBackendEngine backendEngine) {
        super(backendEngine);
        this.backendEngine = backendEngine;
    }

    @Override
    public Object connect(IBackendEngine backendEngine, IServerSettings serverSettings) {
        try {
            Class.forName("org.postgresql.Driver");

            String host = serverSettings.getSettingOrDefaultValue("PostgreSQL.host", "localhost");
            int port = serverSettings.getInteger("PostgreSQL.port", 5432);

            String url = "jdbc:postgresql://" + host + ":" + port + "";
            String user = "";
            String password = "";

            if (serverSettings.contains("PostgreSQL.database")) {
                url = url + "/" + serverSettings.getSetting("PostgreSQL.database");
            }

            if (serverSettings.contains("PostgreSQL.user")) {
                user = serverSettings.getSettingOrDefaultValue("PostgreSQL.user", "root");
            }

            if (serverSettings.contains("PostgreSQL.password")) {
                password = serverSettings.getSetting("PostgreSQL.password");
            }

            this.connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            backendEngine.getLoggerManager().warn("SQLException while connecting to database " + e.getStackTrace() + ".");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            backendEngine.getLoggerManager().error("ClassNotFoundException in PostgreSQLConnector " + e.getStackTrace() + ".");
            e.printStackTrace();
        }

        return this.connection;
    }

    @Override
    public Object getInstance() {
        return this.connection;
    }

    @Override
    public boolean isConnected() {
        return this.connection != null;
    }

    @Override
    public void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
