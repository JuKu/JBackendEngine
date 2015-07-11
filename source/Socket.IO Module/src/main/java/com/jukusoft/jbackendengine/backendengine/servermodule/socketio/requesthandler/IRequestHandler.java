package com.jukusoft.jbackendengine.backendengine.servermodule.socketio.requesthandler;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.jukusoft.jbackendengine.backendengine.IBackendEngine;

/**
 * Created by Justin on 11.07.2015.
 */
public interface IRequestHandler {

    public void onRequest (String eventname, SocketIOClient client, String data, IBackendEngine backendEngine, AckRequest ackRequest);

}
