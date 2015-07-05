package com.jukusoft.jbackendengine.factory;

import com.jukusoft.jbackendengine.IBackendEngine;
import com.jukusoft.jbackendengine.builder.HazelcastBackendEngineBuilder;

import java.io.File;

/**
 * Created by Justin on 04.07.2015.
 */
public class BackendEngineFactory {

    public static IBackendEngine createNewDefaultHazelcastBackendEngine () {
        HazelcastBackendEngineBuilder builder = HazelcastBackendEngineBuilder.builder();
        return builder.buildHazelcastBackendEngine(new File("./config/"));
    }

}
