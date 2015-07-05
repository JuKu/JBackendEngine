package com.jukusoft.jbackendengine;

import com.jukusoft.jbackendengine.impl.DefaultBackendEngine;

/**
 * Created by Justin on 05.07.2015.
 */
public class ServerEngineMain {

    public static void main (String[] args) {
        DefaultBackendEngine defaultBackendEngine = new DefaultBackendEngine();
        System.out.println("JBackendEngine Version " + defaultBackendEngine.getBackendEngineVersion() + "");
    }

}
