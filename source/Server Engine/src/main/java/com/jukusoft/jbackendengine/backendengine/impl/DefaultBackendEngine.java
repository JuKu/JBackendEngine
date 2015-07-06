package com.jukusoft.jbackendengine.backendengine.impl;

import com.jukusoft.jbackendengine.backendengine.AbstractBackendEngine;
import com.jukusoft.jbackendengine.backendengine.IEditableBackendEngine;
import com.jukusoft.jbackendengine.backendengine.IRunAtBackendEngineStartRunnable;
import com.jukusoft.jbackendengine.backendengine.serversettings.IServerSettings;

import java.io.File;

/**
 * Created by Justin on 02.07.2015.
 */
public class DefaultBackendEngine extends AbstractBackendEngine {

    public DefaultBackendEngine (IRunAtBackendEngineStartRunnable runAtBackendEngineStartRunnable) {
        super(new File("./config/"));
        runAtBackendEngineStartRunnable.run(this);
    }

    public DefaultBackendEngine (IServerSettings serverSettings) {
        super(serverSettings);
    }

    public DefaultBackendEngine (File configDir) {
        super(configDir);
    }

    public DefaultBackendEngine () {
        super(new File("./config/"));
    }

    @Override
    public void startEngine(IEditableBackendEngine backendEngine) {
        //
    }

}
