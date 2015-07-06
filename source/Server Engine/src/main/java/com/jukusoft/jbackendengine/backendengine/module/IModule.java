package com.jukusoft.jbackendengine.backendengine.module;

import com.jukusoft.jbackendengine.backendengine.IBackendEngine;

/**
 * Created by Justin on 03.07.2015.
 */
public interface IModule {

    public void initialize (IBackendEngine backendEngine);
    public boolean isInitialized ();
    public void start (IBackendEngine backendEngine);
    public void stop ();

}
