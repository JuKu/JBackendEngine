package com.jukusoft.jbackendengine.backendengine.servermodule.socketio.requesthandler.impl;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.jukusoft.jbackendengine.backendengine.IBackendEngine;
import com.jukusoft.jbackendengine.backendengine.servermodule.socketio.requesthandler.IRequestHandler;
import com.jukusoft.jbackendengine.backendengine.sessionstore.ISession;
import com.jukusoft.jbackendengine.backendengine.sessionstore.exception.SessionNotFoundException;
import org.json.JSONObject;

/**
 * Created by Justin on 11.07.2015.
 */
public abstract class JSONRequestHandler implements IRequestHandler {

    @Override
    public void onRequest(String eventname, SocketIOClient client, String data, IBackendEngine backendEngine, AckRequest ackRequest) {
        JSONObject jsonObject = new JSONObject(data);
        ISession session = null;

        try {
            session = backendEngine.getSessionStore().getSession(client.getSessionId().toString());
        } catch (SessionNotFoundException e) {
            session = backendEngine.getSessionStore().createNewSession(client.getSessionId().toString());
        }

        this.onJSONRequest(jsonObject, client, session, ackRequest);
    }

    public abstract void onJSONRequest (JSONObject jsonObject, SocketIOClient client, ISession session, AckRequest ackRequest);

}
