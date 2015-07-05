package com.jukusoft.jbackendengine.servermodule.socketio.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.jukusoft.jbackendengine.service.IService;

/**
 * Created by Justin on 04.07.2015.
 */
public interface ISocketIOService extends IService {

    public void addEventListener (String event, Class<Object> className, DataListener<Object> dataListener);
    public void addConnectListener (ConnectListener connectListener);
    public void addDisconnectListener (DisconnectListener disconnectListener);
    public SocketIOServer getSocketIOServer ();

}
