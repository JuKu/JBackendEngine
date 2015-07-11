package com.jukusoft.jbackendengine.backendengine.servermodule.socketio.requesthandler.impl;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.jukusoft.jbackendengine.backendengine.IBackendEngine;
import com.jukusoft.jbackendengine.backendengine.servermodule.socketio.requesthandler.IRequestHandler;

/**
 * Created by Justin on 11.07.2015.
 */
public interface IRequestHandlerManager {

    public void registerRequestHandler (String eventname, IRequestHandler requestHandler);
    public void removeRequestHandler (String eventname, IRequestHandler requestHandler);
    public void request (String eventname, SocketIOClient client, String data, IBackendEngine backendEngine, AckRequest ackRequest);

}
