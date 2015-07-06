package com.jukusoft.jbackendengine.backendengine.module.impl;

import com.jukusoft.jbackendengine.backendengine.IBackendEngine;
import com.jukusoft.jbackendengine.backendengine.module.IModule;

/**
 * Created by Justin on 03.07.2015.
 */
public abstract class Module implements IModule {

    private IBackendEngine backendEngine = null;
    private boolean initialized = false;

    public IBackendEngine getBackendEngine () {
        return this.backendEngine;
    }

    @Override
    public void initialize(IBackendEngine backendEngine) {
        this.backendEngine = backendEngine;
        this.onInitialize(this.backendEngine);
        this.initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }

    public abstract void onInitialize (IBackendEngine backendEngine);

    @Override
    public abstract void start(IBackendEngine backendEngine);

    @Override
    public abstract void stop();

}
