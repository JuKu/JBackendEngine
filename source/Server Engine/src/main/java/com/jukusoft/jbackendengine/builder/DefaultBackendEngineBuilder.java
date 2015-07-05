package com.jukusoft.jbackendengine.builder;

import com.jukusoft.jbackendengine.IBackendEngine;
import com.jukusoft.jbackendengine.impl.DefaultBackendEngine;

/**
 * Created by Justin on 04.07.2015.
 */
public class DefaultBackendEngineBuilder {

    public IBackendEngine buildBackendEngine () {
        DefaultBackendEngine backendEngine = new DefaultBackendEngine();
        return backendEngine;
    }

    public static DefaultBackendEngineBuilder builder () {
        DefaultBackendEngineBuilder builder = new DefaultBackendEngineBuilder();
        return builder;
    }

}
