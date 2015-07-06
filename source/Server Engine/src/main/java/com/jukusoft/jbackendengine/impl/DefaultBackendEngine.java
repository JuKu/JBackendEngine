package com.jukusoft.jbackendengine.impl;

import com.jukusoft.jbackendengine.AbstractBackendEngine;
import com.jukusoft.jbackendengine.IEditableBackendEngine;
import com.jukusoft.jbackendengine.IRunAtBackendEngineStartRunnable;

import java.io.File;

/**
 * Created by Justin on 02.07.2015.
 */
public class DefaultBackendEngine extends AbstractBackendEngine {

    public DefaultBackendEngine (IRunAtBackendEngineStartRunnable runAtBackendEngineStartRunnable) {
        super(new File("./config/"));
        runAtBackendEngineStartRunnable.run(this);
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
