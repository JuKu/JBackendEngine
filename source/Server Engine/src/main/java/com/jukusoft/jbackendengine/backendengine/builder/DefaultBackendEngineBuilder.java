package com.jukusoft.jbackendengine.backendengine.builder;

import com.jukusoft.jbackendengine.backendengine.IBackendEngine;
import com.jukusoft.jbackendengine.backendengine.IEditableBackendEngine;
import com.jukusoft.jbackendengine.backendengine.IRunAtBackendEngineStartRunnable;
import com.jukusoft.jbackendengine.backendengine.impl.DefaultBackendEngine;

import java.io.File;

/**
 * Created by Justin on 04.07.2015.
 */
public class DefaultBackendEngineBuilder {

    public IBackendEngine buildBackendEngine () {
        return buildBackendEngine(new IRunAtBackendEngineStartRunnable() {
            @Override
            public void run(IEditableBackendEngine backendEngine) {
                //
            }
        });
    }

    public IBackendEngine buildBackendEngine (IRunAtBackendEngineStartRunnable runAtBackendEngineStartRunnable) {
        DefaultBackendEngine backendEngine = new DefaultBackendEngine();
        backendEngine.getModuleManager().loadAndStart(new File("./modules"));

        return backendEngine;
    }

    public static DefaultBackendEngineBuilder builder () {
        DefaultBackendEngineBuilder builder = new DefaultBackendEngineBuilder();
        return builder;
    }

}
