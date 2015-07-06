package com.jukusoft.jbackendengine.backendengine.servermodule.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.jukusoft.jbackendengine.backendengine.IBackendEngine;
import com.jukusoft.jbackendengine.backendengine.authentification.IUserManager;
import com.jukusoft.jbackendengine.backendengine.authentification.impl.HazelcastUserManager;
import com.jukusoft.jbackendengine.backendengine.database.exception.IDatabaseConnectorNotFoundException;
import com.jukusoft.jbackendengine.backendengine.module.ModuleInfo;
import com.jukusoft.jbackendengine.backendengine.module.impl.Module;
import com.jukusoft.jbackendengine.backendengine.servermodule.hazelcast.serversettings.HazelcastServerSettings;
import com.jukusoft.jbackendengine.backendengine.servermodule.hazelcast.connector.HazelcastConnector;
import com.jukusoft.jbackendengine.backendengine.servermodule.hazelcast.logger.HazelcastTopicLogger;
import com.jukusoft.jbackendengine.backendengine.validation.exception.ModuleAccessNotPermittedException;

/**
 * Created by Justin on 04.07.2015.
 */
@ModuleInfo(
        moduleName = "hazelcast",
        version = "1.0.0",
        description = "adds a hazelcast connector to the backend engine",
        author = "Service4All",
        priority = ModuleInfo.Priority.DB,
        dependencies = {}
)
public class HazelcastModule extends Module {

    @Override
    public void onInitialize(IBackendEngine backendEngine) {
        HazelcastConnector connector = new HazelcastConnector(backendEngine);
        backendEngine.getDatabaseConnectorManager().registerConnector("hazelcast-3.5", connector);
    }

    @Override
    public void start(IBackendEngine backendEngine) {
        if (backendEngine.getLocalSettings() != null && backendEngine.getLocalSettings().contains("HazelcastModule.autoConnect") && backendEngine.getLocalSettings().getSetting("HazelcastModule.autoConnect").equals("true")) {
            //connect to hazelcast database
            try {
                HazelcastConnector connector = (HazelcastConnector) backendEngine.getDatabaseConnectorManager().getConnector("hazelcast-3.5");
                connector.connect(backendEngine, backendEngine.getLocalSettings());
                HazelcastInstance hazelcastInstance = (HazelcastInstance) connector.getInstance();

                if (backendEngine.getLocalSettings().getBoolean("HazelcastModule.generateServerID")) {
                    //generate serverID
                    Long serverID = hazelcastInstance.getIdGenerator("server-id-generator").newId();

                    backendEngine.requestEditableBackendEngine(this).setServerID(serverID);
                }

                if (backendEngine.getLocalSettings().getBoolean("HazelcastModule.addTopicLogger")) {
                    String loggerTopicName = backendEngine.getLocalSettings().getSettingOrDefaultValue("HazelcastModule.loggerTopicName", "logger");
                    String errorLoggerTopicName = backendEngine.getLocalSettings().getSettingOrDefaultValue("HazelcastModule.errorLoggerTopicName", "errorLogger");

                    HazelcastTopicLogger hazelcastTopicLogger = new HazelcastTopicLogger(hazelcastInstance, backendEngine.getServerID(), loggerTopicName, errorLoggerTopicName);
                    backendEngine.getLoggerManager().registerLogger(hazelcastTopicLogger);
                }

                if (backendEngine.getLocalSettings().getBoolean("HazelcastModule.loadDefaultUserManager")) {
                    //create new user manager
                    IUserManager userManager = new HazelcastUserManager(hazelcastInstance, backendEngine, "userlist");

                    backendEngine.requestEditableBackendEngine(this).setUserManager(userManager);
                }

                if (backendEngine.getLocalSettings().getBoolean("HazelcastModule.useHazelcastServerSettings")) {
                    HazelcastServerSettings serverSettings = new HazelcastServerSettings(hazelcastInstance, backendEngine.getLocalSettings().getSettingOrDefaultValue("HazelcastModule.settingsMapName", "settings"));
                    backendEngine.requestEditableBackendEngine(this).setDistributedSettings(serverSettings);
                }
            } catch (IDatabaseConnectorNotFoundException e) {
                backendEngine.getLoggerManager().warn("Couldnt found hazelcast connector in Hazelcast Module " + e.getStackTrace() + ".");
                e.printStackTrace();
            } catch (ModuleAccessNotPermittedException e) {
                backendEngine.getLoggerManager().warn("HazelcastModule needs exclusive backend engine rights to add user manager " + e.getStackTrace() + ".");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {
        //
    }

}
