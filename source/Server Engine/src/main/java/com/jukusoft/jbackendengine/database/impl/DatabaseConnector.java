package com.jukusoft.jbackendengine.database.impl;

import com.jukusoft.jbackendengine.IBackendEngine;
import com.jukusoft.jbackendengine.database.IDatabaseConnector;
import com.jukusoft.jbackendengine.database.IDatabaseConnectorListener;
import com.jukusoft.jbackendengine.serversettings.IServerSettings;

/**
 * Created by Justin on 02.07.2015.
 */
public abstract class DatabaseConnector implements IDatabaseConnector {

    private IDatabaseConnectorListener listener = null;
    private IBackendEngine backendEngine = null;

    public DatabaseConnector (IBackendEngine backendEngine) {
        this.backendEngine = backendEngine;
    }

    @Override
    public abstract Object connect(IBackendEngine backendEngine, IServerSettings serverSettings);

    @Override
    public void start() {
        this.backendEngine.getLoggerManager().debug("Connect to database...");
        Object result = this.connect(this.backendEngine, this.backendEngine.getLocalSettings());

        this.getConnectorListener().onConnect(result);

        this.backendEngine.getLoggerManager().debug("Connection to database successful.");
    }

    @Override
    public IDatabaseConnectorListener getConnectorListener() {
        return this.listener;
    }

    public void setConnectorListener (IDatabaseConnectorListener listener) {
        this.listener = listener;
    }

}
