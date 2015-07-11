package com.jukusoft.jbackendengine.backendengine.servermodule.socketio.socketio.impl;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.corundumstudio.socketio.listener.ExceptionListener;
import com.jukusoft.jbackendengine.backendengine.IBackendEngine;
import com.jukusoft.jbackendengine.backendengine.servermodule.socketio.requesthandler.IRequestHandler;
import com.jukusoft.jbackendengine.backendengine.servermodule.socketio.requesthandler.IRequestHandlerManager;
import com.jukusoft.jbackendengine.backendengine.servermodule.socketio.requesthandler.impl.DefaultRequestHandlerManager;
import com.jukusoft.jbackendengine.backendengine.servermodule.socketio.socketio.ISocketIOService;
import com.jukusoft.jbackendengine.backendengine.serversettings.IServerSettings;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * Created by Justin on 04.07.2015.
 */
public class SocketIOService implements ISocketIOService {

    private IBackendEngine backendEngine = null;
    private SocketIOServer server = null;
    private IRequestHandlerManager requestHandlerManager = new DefaultRequestHandlerManager();

    public SocketIOService (IBackendEngine backendEngine, Configuration config) {
        this.backendEngine = backendEngine;
        this.server = new SocketIOServer(config);
    }

    public SocketIOService (IBackendEngine backendEngine) {
        this.backendEngine = backendEngine;
        IServerSettings serverSettings = backendEngine.getLocalSettings();

        if (!serverSettings.contains("SocketIO.hostname") || !serverSettings.contains("SocketIO.port")) {
            this.getBackendEngine().getLoggerManager().warn("Socket.IO Server couldnt start, no hostname and port in configuration found.");
        } else {
            Configuration config = new Configuration();
            config.setHostname(serverSettings.getSetting("SocketIO.hostname"));
            config.setPort(serverSettings.getInteger("SocketIO.port"));
            config.setAuthorizationListener(new AuthorizationListener() {
                @Override
                public boolean isAuthorized(HandshakeData handshakeData) {
                    getBackendEngine().getLoggerManager().debug("AuthorizationListener isAuthorized().");
                    return true;
                }
            });

            if (!serverSettings.contains("SocketIO.tcpNoDelay") || serverSettings.getSetting("SocketIO.tcpNoDelay").equals("true")) {
                config.getSocketConfig().setTcpNoDelay(true);
            } else {
                config.getSocketConfig().setTcpNoDelay(false);
            }

            if (!serverSettings.contains("SocketIO.reuseAddress") || serverSettings.getSetting("SocketIO.reuseAddress").equals("true")) {
                config.getSocketConfig().setReuseAddress(true);
            } else {
                config.getSocketConfig().setReuseAddress(false);
            }

            if (!serverSettings.contains("SocketIO.tcpKeepAlive") || serverSettings.getSetting("SocketIO.tcpKeepAlive").equals("false")) {
                config.getSocketConfig().setTcpKeepAlive(false);
            } else {
                config.getSocketConfig().setTcpKeepAlive(true);
            }

            if (!serverSettings.contains("SocketIO.addVersionHeader") || serverSettings.getSetting("SocketIO.addVersionHeader").equals("true")) {
                config.setAddVersionHeader(true);
            } else {
                config.setAddVersionHeader(false);
            }

            if (!serverSettings.contains("SocketIO.allowCustomRequests") || serverSettings.getSetting("SocketIO.allowCustomRequests").equals("true")) {
                config.setAllowCustomRequests(true);
            } else {
                config.setAllowCustomRequests(false);
            }

            config.setExceptionListener(new ExceptionListener() {
                @Override
                public void onEventException(Exception e, List<Object> objects, SocketIOClient socketIOClient) {
                    getBackendEngine().getLoggerManager().warn("onEventException() on client with clientID " + socketIOClient.getSessionId() + ", " + e.getStackTrace() + ", " + objects.toString() + ".");

                    e.printStackTrace();
                }

                @Override
                public void onDisconnectException(Exception e, SocketIOClient socketIOClient) {
                    getBackendEngine().getLoggerManager().warn("onDisconnectException() on client with clientID " + socketIOClient.getSessionId() + ": " + e.getStackTrace() + ".");
                }

                @Override
                public void onConnectException(Exception e, SocketIOClient socketIOClient) {
                    getBackendEngine().getLoggerManager().warn("onConnectException() on client with clientID " + socketIOClient.getSessionId() + ": " + e.getStackTrace() + ".");
                }

                @Override
                public void onMessageException(Exception e, String s, SocketIOClient socketIOClient) {
                    getBackendEngine().getLoggerManager().warn("onMessageException() on client with clientID " + socketIOClient.getSessionId() + ": " + e.getStackTrace() + ".");
                }

                @Override
                public void onJsonException(Exception e, Object o, SocketIOClient socketIOClient) {
                    getBackendEngine().getLoggerManager().warn("onJsonException() on client with clientID " + socketIOClient.getSessionId() + ": " + e.getStackTrace() + ".");
                }

                @Override
                public boolean exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
                    return false;
                }
            });

            this.server = new SocketIOServer(config);
        }
    }

    public IBackendEngine getBackendEngine () {
        return this.backendEngine;
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
    public void registerRequestHandler(String eventname, IRequestHandler requestHandler) {
        requestHandlerManager.registerRequestHandler(eventname, requestHandler);

        this.addEventListener(eventname, String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String s, AckRequest ackRequest) throws Exception {
                requestHandlerManager.request(eventname, client, s, backendEngine, ackRequest);
            }
        });
    }

    @Override
    public void removeRequestHandler(String eventname, IRequestHandler requestHandler) {
        requestHandlerManager.removeRequestHandler(eventname, requestHandler);
    }

    @Override
    public synchronized SocketIOServer getSocketIOServer() {
        return this.server;
    }
}
