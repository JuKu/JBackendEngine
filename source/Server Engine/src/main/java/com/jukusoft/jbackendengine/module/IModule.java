package com.jukusoft.jbackendengine.module;

import com.jukusoft.jbackendengine.IBackendEngine;

/**
 * Created by Justin on 03.07.2015.
 */
public interface IModule {

    public void initialize (IBackendEngine backendEngine);
    public boolean isInitialized ();
    public void start (IBackendEngine backendEngine);
    public void stop ();

}
