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

    @Override
    public void onInitialize(IBackendEngine backendEngine) {
        PostgreSQLConnector connector = new PostgreSQLConnector(backendEngine);
        backendEngine.getDatabaseConnectorManager().registerConnector("postgresql", connector);
    }

    @Override
    public void start(IBackendEngine backendEngine) {
        //
    }

    @Override
    public void stop() {
        //
    }

}
