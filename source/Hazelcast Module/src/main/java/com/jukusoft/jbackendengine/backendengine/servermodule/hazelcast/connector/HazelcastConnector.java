package com.jukusoft.jbackendengine.backendengine.servermodule.hazelcast.connector;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.ListenerConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.LifecycleEvent;
import com.hazelcast.core.LifecycleListener;
import com.jukusoft.jbackendengine.backendengine.IBackendEngine;
import com.jukusoft.jbackendengine.backendengine.database.impl.DatabaseConnector;
import com.jukusoft.jbackendengine.backendengine.serversettings.IServerSettings;

import java.util.List;

/**
 * Created by Justin on 02.07.2015.
 */
public class HazelcastConnector extends DatabaseConnector {

    private HazelcastInstance hazelcastInstance = null;

    public HazelcastConnector (IBackendEngine backendEngine) {
        super(backendEngine);
    }

    @Override
    public Object connect(IBackendEngine backendEngine, IServerSettings serverSettings) {
        //connect to hazelcast in-memory data grid
        ClientConfig clientConfig = new ClientConfig();

        if (serverSettings.contains("Hazelcast.useGroupConfig")) {
            String grpName = serverSettings.getSetting("Hazelcast.grpName");
            String grpPassword = serverSettings.getSetting("Hazelcast.grpPassword");

            clientConfig.getGroupConfig().setName(grpName).setPassword(grpPassword);

            if (serverSettings.multiValuesAllowed()) {
                List<String> addressList = serverSettings.getValues("Hazelcast.address");

                for (String str : addressList) {
                    clientConfig.getNetworkConfig().addAddress(str);
                }
            } else {
                clientConfig.getNetworkConfig().addAddress(serverSettings.getSetting("Hazelcast.address"));
            }

            if (serverSettings.contains("Hazelcast.smartRouting") && serverSettings.getSetting("Hazelcast.smartRouting").equals("true")) {
                clientConfig.getNetworkConfig().setSmartRouting(true);
            } else {
                clientConfig.getNetworkConfig().setSmartRouting(false);
            }
        }

        ListenerConfig listenerConfig = new ListenerConfig(new LifecycleListener() {
            @Override
            public void stateChanged(LifecycleEvent lifecycleEvent) {
                if (lifecycleEvent.getState() == LifecycleEvent.LifecycleState.CLIENT_CONNECTED) {
                    if (HazelcastConnector.this.getConnectorListener() != null) {
                        HazelcastConnector.this.getConnectorListener().onConnect(hazelcastInstance);
                    }
                } else if (lifecycleEvent.getState() == LifecycleEvent.LifecycleState.CLIENT_DISCONNECTED) {
                    if (HazelcastConnector.this.getConnectorListener() != null) {
                        HazelcastConnector.this.getConnectorListener().onDisconnect(lifecycleEvent);
                    }
                }
            }
        });

        hazelcastInstance = HazelcastClient.newHazelcastClient(clientConfig);
        return hazelcastInstance;
    }

    @Override
    public Object getInstance() {
        return this.hazelcastInstance;
    }

    @Override
    public boolean isConnected() {
        if (this.hazelcastInstance != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void close() {
        this.hazelcastInstance.shutdown();
    }

}
