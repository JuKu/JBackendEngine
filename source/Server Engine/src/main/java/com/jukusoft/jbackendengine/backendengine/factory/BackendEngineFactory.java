package com.jukusoft.jbackendengine.backendengine.factory;

import com.jukusoft.jbackendengine.backendengine.IBackendEngine;
import com.jukusoft.jbackendengine.backendengine.IEditableBackendEngine;
import com.jukusoft.jbackendengine.backendengine.builder.DefaultBackendEngineBuilder;
import com.jukusoft.jbackendengine.backendengine.builder.HazelcastBackendEngineBuilder;

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
