# JBackendEngine
JBackendEngine is a flexible, full customizable and easy to use backend engine for your needs.
You can replace every manager or module.

The databases Hazelcast (Hazelcast.org), Cassandra and MySQL are supported by default (You have to put the modules jar file into the modules directory, so the backend engine can find them).

## Features
 - Full Module System (you can package modules and load them at runtime to extend the backend engine)
 - Fully configurable
 - Configuration Management (local and distributed(1))
 - Session Store
 - User Management(1)
 - flexible Manager Architecture
 - global Task Manager to execute tasks asynchronous, schedule tasks every period (for example every 5 minutes)(2)
 - execute Tasks asynchronous
 - Create a Socket.IO Server very fast (uses the com.corundumstudio.socketio netty-socketio project)
 - NotificationService (modules or your classes can communicate together or you can use this service to exchange messages around the server cluster with Hazelcast(1))
 - UniqueUserIDGeneration (currenty only with Hazelcast by default, but you can add support for this feature with your own module)
 - Service Support (In Android they are like background services, which are running in the background, this backend engine also adds this option to run services in the background and access or manage them)
 
 (1) for this feature you need a module, which adds support for this option
 (2) Task Manager is using 2 Executor Service for this. You can change corePoolSize and maxPoolSize in the configuration files.

## Planed Features / Comming soon
 - A Hazelcast / Cassandra Hybrid solution to store data in hazelcast and automatically put a backup to cassandra
 - many other things

## First Start
Look to https://github.com/JuKu/JBackendEngine/wiki/First-Start .

## Module Socket.IO
With this module, you can create a socket.io server very fast (uses the com.corundumstudio.socketio netty-socketio project).

`public static void main (String[] args) {
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
 }`
