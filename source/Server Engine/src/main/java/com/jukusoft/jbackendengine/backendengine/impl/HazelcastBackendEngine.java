package com.jukusoft.jbackendengine.backendengine.impl;

import com.hazelcast.core.HazelcastInstance;
import com.jukusoft.jbackendengine.backendengine.AbstractBackendEngine;
import com.jukusoft.jbackendengine.backendengine.IBackendEngine;
import com.jukusoft.jbackendengine.backendengine.IEditableBackendEngine;
import com.jukusoft.jbackendengine.backendengine.database.IDatabaseConnector;
import com.jukusoft.jbackendengine.backendengine.database.IDatabaseConnectorListener;
import com.jukusoft.jbackendengine.backendengine.database.exception.IDatabaseConnectorNotFoundException;

import java.io.File;

/**
 * Created by Justin on 02.07.2015.
 */
public class HazelcastBackendEngine extends AbstractBackendEngine {

    private HazelcastInstance hazelcastInstance = null;

    public HazelcastBackendEngine (File configDir) {
        super(configDir);
    }

    @Override
    public void startEngine(IEditableBackendEngine backendEngine) {
        //connect to hazelcast
        if (backendEngine.getModuleManager().contains("hazelcast")) {
            try {
                IDatabaseConnector connector = backendEngine.getDatabaseConnectorManager().getConnector("hazelcast-3.5");
                connector.setConnectorListener(new IDatabaseConnectorListener() {
                    @Override
                    public void onConnect(Object... objects) {
                        HazelcastBackendEngine.this.hazelcastInstance = (HazelcastInstance) objects[0];
                    }

                    @Override
                    public void onDisconnect(Object... objects) {
                        HazelcastBackendEngine.this.hazelcastInstance = null;
                    }
                });

                connector.connect((IBackendEngine) backendEngine, backendEngine.getLocalSettings());
                connector.start();
            } catch (IDatabaseConnectorNotFoundException e) {
                backendEngine.getLoggerManager().warn("Couldnt find hazelcast connector " + e.getStackTrace() + ".ss");
                e.printStackTrace();
            }
        }
    }

}
