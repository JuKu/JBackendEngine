package com.jukusoft.jbackendengine;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.jukusoft.jbackendengine.impl.DefaultBackendEngine;
import com.jukusoft.jbackendengine.module.IModuleManager;
import com.jukusoft.jbackendengine.module.exception.ModuleNotFoundException;
import com.jukusoft.jbackendengine.servermodule.socketio.SocketIOModule;
import com.jukusoft.jbackendengine.servermodule.socketio.socketio.ISocketIOService;
import com.jukusoft.jbackendengine.service.exception.IServiceNotFoundException;

import java.io.File;
import java.io.IOException;

/**
 * Created by Justin on 05.07.2015.
 */
public class ServerEngineMain {

    public static void main (String[] args) {
        DefaultBackendEngine defaultBackendEngine = new DefaultBackendEngine();
        IModuleManager moduleManager = defaultBackendEngine.getModuleManager();

        try {
            //load all modules from modules directory
            moduleManager.loadModulesFromDir(new File("./modules"));

            //start all modules
            moduleManager.startAllModules();

            //get socket.io service, socket.io works like a background service in this engine
            ISocketIOService socketIOService = (ISocketIOService) defaultBackendEngine.getService("socketio");
            
            socketIOService.addConnectListener(new ConnectListener() {
                @Override
                public void onConnect(SocketIOClient socketIOClient) {
                    defaultBackendEngine.getLoggerManager().debug("A socket.io client connected to server.");
                }
            });
            
            socketIOService.addDisconnectListener(new DisconnectListener() {
                @Override
                public void onDisconnect(SocketIOClient socketIOClient) {
                    defaultBackendEngine.getLoggerManager().debug("A socket.io client disconnected.");
                }
            });
            
            socketIOService.addEventListener("eventname", String.class, new DataListener<String>() {
                @Override
                public void onData(SocketIOClient socketIOClient, String str, AckRequest ackRequest) throws Exception {
                    defaultBackendEngine.getLoggerManager().debug("Received string: " + str);

                    //send a string to the client
                    socketIOClient.sendEvent("newEventName", "Answer String");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IServiceNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("JBackendEngine Version " + defaultBackendEngine.getBackendEngineVersion() + "");
    }

}
