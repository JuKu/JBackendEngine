package com.jukusoft.jbackendengine;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.jukusoft.jbackendengine.impl.DefaultBackendEngine;
import com.jukusoft.jbackendengine.module.IModuleManager;
import com.jukusoft.jbackendengine.module.exception.ModuleNotFoundException;
import com.jukusoft.jbackendengine.servermodule.socketio.SocketIOModule;
import com.jukusoft.jbackendengine.servermodule.socketio.socketio.ISocketIOService;
import com.jukusoft.jbackendengine.service.exception.IServiceNotFoundException;

import java.io.File;
import java.io.IOException;

/**
 * Created by Justin on 05.07.2015.
 */
public class ServerEngineMain {

    public static void main (String[] args) {
        DefaultBackendEngine defaultBackendEngine = new DefaultBackendEngine();
        System.out.println("JBackendEngine Version " + defaultBackendEngine.getBackendEngineVersion() + "");
    }

}
