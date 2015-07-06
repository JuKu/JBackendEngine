package com.jukusoft.jbackendengine.backendengine;

import com.jukusoft.jbackendengine.backendengine.factory.BackendEngineFactory;
import com.jukusoft.jbackendengine.backendengine.impl.DefaultBackendEngine;

import java.io.File;

/**
 * Created by Justin on 05.07.2015.
 */
public class ServerEngineMain {

    public static void main (String[] args) {
        IEditableBackendEngine backendEngine = BackendEngineFactory.createNewDefaultBackendEngine();

        System.out.println("JBackendEngine Version " + backendEngine.getBackendEngineVersion() + "");

        backendEngine.shutdown();
    }

}
