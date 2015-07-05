package com.jukusoft.jbackendengine.database;

import com.jukusoft.jbackendengine.database.exception.IDatabaseConnectorNotFoundException;

/**
 * Created by Justin on 03.07.2015.
 */
public interface IDatabaseConnectorManager {

    public void registerConnector (String name, IDatabaseConnector connector);
    public void removeConnector (String name);
    public boolean contains (String name);
    public IDatabaseConnector getConnector (String name) throws IDatabaseConnectorNotFoundException;

}
