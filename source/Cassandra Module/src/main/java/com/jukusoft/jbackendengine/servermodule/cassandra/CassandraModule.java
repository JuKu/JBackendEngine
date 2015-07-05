package com.jukusoft.jbackendengine.servermodule.cassandra;

import com.jukusoft.jbackendengine.IBackendEngine;
import com.jukusoft.jbackendengine.module.ModuleInfo;
import com.jukusoft.jbackendengine.module.impl.Module;
import com.jukusoft.jbackendengine.servermodule.cassandra.connector.CassandraConnector;

/**
 * Created by Justin on 04.07.2015.
 */
@ModuleInfo(
        moduleName = "cassandra-connector",
        version = "1.0.0",
        description = "adds a cassandra connector to the backend engine",
        author = "Service4All",
        priority = ModuleInfo.Priority.DB,
        dependencies = {}
)
public class CassandraModule extends Module {

    @Override
    public void onInitialize(IBackendEngine backendEngine) {
        CassandraConnector connector = new CassandraConnector(backendEngine);
        backendEngine.getDatabaseConnectorManager().registerConnector("cassandra", connector);
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
