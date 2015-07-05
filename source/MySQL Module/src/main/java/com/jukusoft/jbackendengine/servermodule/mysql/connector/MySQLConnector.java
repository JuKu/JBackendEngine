package com.jukusoft.jbackendengine.servermodule.mysql.connector;

import com.jukusoft.jbackendengine.IBackendEngine;
import com.jukusoft.jbackendengine.database.impl.DatabaseConnector;
import com.jukusoft.jbackendengine.serversettings.IServerSettings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Justin on 05.07.2015.
 */
public class MySQLConnector extends DatabaseConnector {

    private Connection connection = null;
    private IBackendEngine backendEngine = null;

    public MySQLConnector (IBackendEngine backendEngine) {
        super(backendEngine);
        this.backendEngine = backendEngine;

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            backendEngine.getLoggerManager().error("Couldnt load com.mysql.jdbc.Driver " + e.getStackTrace() + ".");
            e.printStackTrace();
        }
    }

    @Override
    public Object connect(IBackendEngine backendEngine, IServerSettings serverSettings) {
        try {
            String mySQLUrl = "";

            if (serverSettings.contains("MySQL.url")) {
                mySQLUrl = serverSettings.getSetting("MySQL.url");
            } else {
                String host = serverSettings.getSettingOrDefaultValue("MySQL.host", "localhost");
                int port = serverSettings.getInteger("MySQL.port", 3306);
                String dbName = serverSettings.getSettingOrDefaultValue("MySQL.dbName", "database");

                if (serverSettings.contains("MySQL.user")) {
                    String user = serverSettings.getSettingOrDefaultValue("MySQL.user", "root");
                    String password = serverSettings.getSettingOrDefaultValue("MySQL.password", "");

                    mySQLUrl = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?"
                            + "user=" + user + "&password=" + password + "";
                } else {
                    String user = serverSettings.getSettingOrDefaultValue("MySQL.user", "root");
                    String password = serverSettings.getSettingOrDefaultValue("MySQL.password", "");

                    mySQLUrl = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "";
                }
            }

            this.connection = DriverManager
                    .getConnection(mySQLUrl);

            return this.connection;
        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }
    }

    @Override
    public Object getInstance() {
        return this.connection;
    }

    @Override
    public boolean isConnected() {
        try {
            return this.connection != null && !this.connection.isClosed();
        } catch (SQLException e) {
            this.backendEngine.getLoggerManager().warn("SQLException in MySQLConnector::isConnected() " + e.getStackTrace() + ".");
            e.printStackTrace();

            return true;
        }
    }

    @Override
    public void close() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException e) {
            backendEngine.getLoggerManager().warn("SQLException in MySQLConnector::close() " + e.getStackTrace() + ".");
            e.printStackTrace();
        }
    }

}
