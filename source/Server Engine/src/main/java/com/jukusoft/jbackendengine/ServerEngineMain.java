package com.jukusoft.jbackendengine;

import com.jukusoft.jbackendengine.impl.DefaultBackendEngine;

import java.io.File;
import java.io.IOException;

/**
 * Created by Justin on 05.07.2015.
 */
public class ServerEngineMain {

    public static void main (String[] args) {
        DefaultBackendEngine defaultBackendEngine = new DefaultBackendEngine();
        defaultBackendEngine.getModuleManager().loadAndStart(new File("./modules"));

        System.out.println("JBackendEngine Version " + defaultBackendEngine.getBackendEngineVersion() + "");
    }

}
