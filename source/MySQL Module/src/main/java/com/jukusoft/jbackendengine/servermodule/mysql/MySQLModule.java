package com.jukusoft.jbackendengine.servermodule.mysql;

import com.jukusoft.jbackendengine.IBackendEngine;
import com.jukusoft.jbackendengine.module.ModuleInfo;
import com.jukusoft.jbackendengine.module.impl.Module;
import com.jukusoft.jbackendengine.servermodule.mysql.connector.MySQLConnector;

/**
 * Created by Justin on 05.07.2015.
 */
@ModuleInfo(
        moduleName = "mysql-connector",
        version = "1.0.0",
        description = "adds a mysql connector to the backend engine",
        author = "Service4All",
        priority = ModuleInfo.Priority.DB,
        dependencies = {}
)
public class MySQLModule extends Module {

    @Override
    public void onInitialize(IBackendEngine backendEngine) {
        MySQLConnector connector = new MySQLConnector(backendEngine);
        backendEngine.getDatabaseConnectorManager().registerConnector("mysql", connector);
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
