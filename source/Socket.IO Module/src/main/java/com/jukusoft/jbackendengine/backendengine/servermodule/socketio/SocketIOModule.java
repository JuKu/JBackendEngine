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
        IServerSettings serverSettings = this.getBackendEngine().getLocalSettings();

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

            SocketIOService socketIOService = new SocketIOService(getBackendEngine(), config);
            backendEngine.addService("socketio", socketIOService);
        }
    }

    @Override
    public void stop() {
        //
    }

}
