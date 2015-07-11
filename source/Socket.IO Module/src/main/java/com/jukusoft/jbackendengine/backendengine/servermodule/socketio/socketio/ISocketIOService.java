package com.jukusoft.jbackendengine.backendengine.servermodule.socketio.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.jukusoft.jbackendengine.backendengine.servermodule.socketio.requesthandler.IRequestHandler;
import com.jukusoft.jbackendengine.backendengine.service.IService;

/**
 * Created by Justin on 04.07.2015.
 */
public interface ISocketIOService extends IService {

    public <T> void addEventListener (String event, Class<T> className, DataListener<T> dataListener);
    public void addConnectListener (ConnectListener connectListener);
    public void addDisconnectListener (DisconnectListener disconnectListener);
    public void registerRequestHandler (String eventname, IRequestHandler requestHandler);
    public void removeRequestHandler (String eventname, IRequestHandler requestHandler);
    public SocketIOServer getSocketIOServer ();

}
