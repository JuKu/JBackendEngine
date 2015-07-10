package com.jukusoft.jbackendengine.backendengine.servermodule.socketio;

import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ExceptionListener;
import com.jukusoft.jbackendengine.backendengine.IBackendEngine;
import com.jukusoft.jbackendengine.backendengine.servermodule.socketio.socketio.impl.SocketIOService;
import com.jukusoft.jbackendengine.backendengine.module.ModuleInfo;
import com.jukusoft.jbackendengine.backendengine.module.impl.Module;
import com.jukusoft.jbackendengine.backendengine.serversettings.IServerSettings;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * Created by Justin on 04.07.2015.
 */
@ModuleInfo(
        moduleName = "socketio",
        version = "1.0.0",
        author = "Service4All",
        priority = ModuleInfo.Priority.LOW,
        dependencies = {}
)
public class SocketIOModule extends Module {

    @Override
    public void onInitialize(IBackendEngine backendEngine) {
        backendEngine.getLoggerManager().debug("Socket.IO Module initialized.");
    }

    @Override
    public void start(IBackendEngine backendEngine) {
        SocketIOService socketIOService = new SocketIOService(getBackendEngine());
        backendEngine.addService("socketio", socketIOService);
    }

    @Override
    public void stop() {
        //
    }

}
