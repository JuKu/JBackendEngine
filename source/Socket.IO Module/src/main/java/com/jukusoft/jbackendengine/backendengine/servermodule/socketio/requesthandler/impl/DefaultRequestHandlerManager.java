package com.jukusoft.jbackendengine.backendengine.servermodule.socketio.requesthandler.impl;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.jukusoft.jbackendengine.backendengine.IBackendEngine;
import com.jukusoft.jbackendengine.backendengine.servermodule.socketio.requesthandler.IRequestHandler;
import com.jukusoft.jbackendengine.backendengine.servermodule.socketio.requesthandler.IRequestHandlerManager;

import java.util.*;

/**
 * Created by Justin on 11.07.2015.
 */
public class DefaultRequestHandlerManager implements IRequestHandlerManager {

    private Map<String,List<IRequestHandler>> requestHandlerMap = new HashMap<String,List<IRequestHandler>>();

    @Override
    public void registerRequestHandler(String eventname, IRequestHandler requestHandler) {
        if (!this.requestHandlerMap.containsKey(eventname)) {
            List<IRequestHandler> requestHandlerList = new ArrayList<IRequestHandler>();
            this.requestHandlerMap.put(eventname, requestHandlerList);
        }

        this.requestHandlerMap.get(eventname).add(requestHandler);
    }

    @Override
    public void removeRequestHandler(String eventname, IRequestHandler requestHandler) {
        if (this.requestHandlerMap.containsKey(eventname)) {
            this.requestHandlerMap.get(eventname).remove(requestHandler);
        }
    }

    @Override
    public boolean request(String eventname, SocketIOClient client, String data, IBackendEngine backendEngine, AckRequest ackRequest) {
        if (this.requestHandlerMap.containsKey(eventname)) {
            List<IRequestHandler> copyList = Collections.synchronizedList(requestHandlerMap.get(eventname));

            for (IRequestHandler requestHandler : copyList) {
                requestHandler.onRequest(eventname, client, data, backendEngine, ackRequest);
            }

            return copyList.size() > 0;
        }

        return false;
    }

}
