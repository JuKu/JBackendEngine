package com.jukusoft.jbackendengine.backendengine.database;

/**
 * Created by Justin on 02.07.2015.
 */
public interface IDatabaseConnectorListener {

    public void onConnect (Object... objects);
    public void onDisconnect (Object... objects);

}
