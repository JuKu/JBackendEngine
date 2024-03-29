package com.jukusoft.jbackendengine.backendengine.builder;

import com.jukusoft.jbackendengine.backendengine.IBackendEngine;
import com.jukusoft.jbackendengine.backendengine.impl.HazelcastBackendEngine;

import java.io.File;

/**
 * Created by Justin on 04.07.2015.
 */
public class HazelcastBackendEngineBuilder {

    public IBackendEngine buildHazelcastBackendEngine (File configDir) {
        HazelcastBackendEngine hazelcastBackendEngine = new HazelcastBackendEngine(configDir);
        return hazelcastBackendEngine;
    }

    public static HazelcastBackendEngineBuilder builder () {
        HazelcastBackendEngineBuilder builder = new HazelcastBackendEngineBuilder();
        return builder;
    }

}
