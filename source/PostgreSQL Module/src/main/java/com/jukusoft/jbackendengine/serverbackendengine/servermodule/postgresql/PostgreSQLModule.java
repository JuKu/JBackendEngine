package com.jukusoft.jbackendengine.serverbackendengine.servermodule.postgresql;

import com.jukusoft.jbackendengine.backendengine.IBackendEngine;
import com.jukusoft.jbackendengine.backendengine.module.ModuleInfo;
import com.jukusoft.jbackendengine.backendengine.module.impl.Module;
import com.jukusoft.jbackendengine.serverbackendengine.servermodule.postgresql.connector.PostgreSQLConnector;

/**
 * Created by Justin on 10.07.2015.
 */
@ModuleInfo(
        moduleName = "postgresql-connector",
        version = "1.0.0",
        description = "A database connector fpr postgresql",
        author = "Service4All",
        priority = ModuleInfo.Priority.DB,
        dependencies = {}
)
public class PostgreSQLModule extends Module {

    private PostgreSQLConnector connector = null;

    @Override
    public void onInitialize(IBackendEngine backendEngine) {
        this.connector = new PostgreSQLConnector(backendEngine);
        backendEngine.getDatabaseConnectorManager().registerConnector("postgresql", connector);
    }

    @Override
    public void start(IBackendEngine backendEngine) {
        if (backendEngine.getLocalSettings().contains("PostgreSQL.autoConnect") && backendEngine.getLocalSettings().getBoolean("PostgreSQL.autoConnect")) {
            this.connector.connect(backendEngine, backendEngine.getLocalSettings());
        }
    }

    @Override
    public void stop() {
        this.connector.close();
    }

}
