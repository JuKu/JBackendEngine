package com.jukusoft.jbackendengine.database.impl;

import com.jukusoft.jbackendengine.database.IDatabaseConnector;
import com.jukusoft.jbackendengine.database.IDatabaseConnectorManager;
import com.jukusoft.jbackendengine.database.exception.IDatabaseConnectorNotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Justin on 03.07.2015.
 */
public class DefaultDatabaseConnectorManager implements IDatabaseConnectorManager {

    private Map<String,IDatabaseConnector> connectorMap = new HashMap<String,IDatabaseConnector>();

    @Override
    public void registerConnector(String name, IDatabaseConnector connector) {
        this.connectorMap.put(name, connector);
    }

    @Override
    public void removeConnector(String name) {
        this.connectorMap.remove(name);
    }

    @Override
    public boolean contains(String name) {
        return this.connectorMap.containsKey(name);
    }

    @Override
    public IDatabaseConnector getConnector(String name) throws IDatabaseConnectorNotFoundException {
        if (this.contains(name)) {
            return this.connectorMap.get(name);
        } else {
            throw new IDatabaseConnectorNotFoundException("connector " + name + " not found.");
        }
    }

}
