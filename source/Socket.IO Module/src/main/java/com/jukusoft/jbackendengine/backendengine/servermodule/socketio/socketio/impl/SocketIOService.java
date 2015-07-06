package com.jukusoft.jbackendengine.backendengine.servermodule.socketio.socketio.impl;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.jukusoft.jbackendengine.backendengine.IBackendEngine;
import com.jukusoft.jbackendengine.backendengine.servermodule.socketio.socketio.ISocketIOService;

/**
 * Created by Justin on 04.07.2015.
 */
public class SocketIOService implements ISocketIOService {

    private IBackendEngine backendEngine = null;
    private SocketIOServer server = null;

    public SocketIOService (IBackendEngine backendEngine, Configuration config) {
        this.backendEngine = backendEngine;
        this.server = new SocketIOServer(config);
    }

    @Override
    public void run() {
        //start server in own thread
        this.server.start();
    }

    @Override
    public <T> void addEventListener(String event, Class<T> className, DataListener<T> dataListener) {
        server.addEventListener(event, className, dataListener);
    }

    @Override
    public void addConnectListener(ConnectListener connectListener) {
        this.server.addConnectListener(connectListener);
    }

    @Override
    public void addDisconnectListener(DisconnectListener disconnectListener) {
        this.server.addDisconnectListener(disconnectListener);
    }

    @Override
    public synchronized SocketIOServer getSocketIOServer() {
        return this.server;
    }
}
