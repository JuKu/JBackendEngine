package com.jukusoft.jbackendengine.serverbackendengine.servermodule.postgresql.connector;

import com.jukusoft.jbackendengine.backendengine.IBackendEngine;
import com.jukusoft.jbackendengine.backendengine.database.impl.DatabaseConnector;
import com.jukusoft.jbackendengine.backendengine.serversettings.IServerSettings;

/**
 * Created by Justin on 10.07.2015.
 */
public class PostgreSQLConnector extends DatabaseConnector {

    private IBackendEngine backendEngine = null;

    public PostgreSQLConnector (IBackendEngine backendEngine) {
        super(backendEngine);
        this.backendEngine = backendEngine;
    }

    @Override
    public Object connect(IBackendEngine backendEngine, IServerSettings serverSettings) {
        return null;
    }

    @Override
    public Object getInstance() {
        return null;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void close() {
        //
    }

}
