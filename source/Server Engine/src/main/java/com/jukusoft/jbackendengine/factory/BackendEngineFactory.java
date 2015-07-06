package com.jukusoft.jbackendengine.factory;

import com.jukusoft.jbackendengine.IBackendEngine;
import com.jukusoft.jbackendengine.IEditableBackendEngine;
import com.jukusoft.jbackendengine.IRunAtBackendEngineStartRunnable;
import com.jukusoft.jbackendengine.builder.DefaultBackendEngineBuilder;
import com.jukusoft.jbackendengine.builder.HazelcastBackendEngineBuilder;

import java.io.File;

/**
 * Created by Justin on 04.07.2015.
 */
public class BackendEngineFactory {

    public static IBackendEngine createNewHazelcastBackendEngine () {
        HazelcastBackendEngineBuilder builder = HazelcastBackendEngineBuilder.builder();
        return builder.buildHazelcastBackendEngine(new File("./config/"));
    }

    public static IEditableBackendEngine createNewDefaultBackendEngine () {
        DefaultBackendEngineBuilder builder = DefaultBackendEngineBuilder.builder();
        return (IEditableBackendEngine) builder.buildBackendEngine();
    }

}
