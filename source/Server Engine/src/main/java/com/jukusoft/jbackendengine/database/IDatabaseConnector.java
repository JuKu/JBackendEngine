package com.jukusoft.jbackendengine.database;

import com.jukusoft.jbackendengine.IBackendEngine;
import com.jukusoft.jbackendengine.serversettings.IServerSettings;

/**
 * Created by Justin on 02.07.2015.
 */
public interface IDatabaseConnector {

    public Object connect (IBackendEngine backendEngine, IServerSettings serverSettings);
    public void start ();
    public IDatabaseConnectorListener getConnectorListener ();
    public void setConnectorListener (IDatabaseConnectorListener listener);
    public Object getInstance ();
    public boolean isConnected ();
    public void close ();

}
