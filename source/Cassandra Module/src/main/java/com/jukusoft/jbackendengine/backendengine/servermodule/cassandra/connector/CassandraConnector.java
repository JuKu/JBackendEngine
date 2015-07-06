package com.jukusoft.jbackendengine.backendengine.servermodule.cassandra.connector;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;
import com.jukusoft.jbackendengine.backendengine.IBackendEngine;
import com.jukusoft.jbackendengine.backendengine.database.impl.DatabaseConnector;
import com.jukusoft.jbackendengine.backendengine.serversettings.IServerSettings;

/**
 * Created by Justin on 03.07.2015.
 */
public class CassandraConnector extends DatabaseConnector {

    private Cluster cluster = null;
    private Session session = null;

    public CassandraConnector (IBackendEngine backendEngine) {
        super(backendEngine);
    }

    @Override
    public Object connect(IBackendEngine backendEngine, IServerSettings serverSettings) {
        Cluster.Builder builder = null;

        if (!serverSettings.contains("Cassandra.useAuthentification") && !serverSettings.getSetting("useAuthentification").equals("true")) {
            builder = this.cluster.builder();
        } else {
            builder = this.cluster.builder().withCredentials(serverSettings.getSetting("Cassandra.user"), serverSettings.getSetting("Cassandra.password"));
        }

        if (serverSettings.multiValuesAllowed()) {
            for (String address : serverSettings.getValues("Cassandra.contactPoint")) {
                String[] strArray = address.split(":");
                address = strArray[0];

                if (strArray.length > 1) {
                    int port = Integer.parseInt(strArray[1]);
                    builder.addContactPoint(address).withPort(port);
                } else {
                    builder.addContactPoint(address);
                }
            }
        } else {
            if (serverSettings.contains("Cassandra.port")) {
                builder.addContactPoint(serverSettings.getSetting("Cassandra.contactPoint")).withPort(serverSettings.getInteger("Cassandra.port"));
            } else {
                String address = serverSettings.getSetting("Cassandra.contactPoint");
                String[] strArray = address.split(":");
                address = strArray[0];

                if (strArray.length > 1) {
                    int port = Integer.parseInt(strArray[1]);
                    builder.addContactPoint(address).withPort(port);
                } else {
                    builder.addContactPoint(address);
                }
            }
        }

        this.cluster = builder.build();

        final Metadata metadata = cluster.getMetadata();
        backendEngine.getLoggerManager().debug("Connected to Cassandra Cluster: " + metadata.getClusterName() + ".");

        for (Host host : metadata.getAllHosts()) {
            backendEngine.getLoggerManager().debug("Cassandra Datacenter: " + host.getDatacenter() + ", Rack: " + host.getRack() + ", Host: " + host.getAddress() + ".");
        }

        if (serverSettings.contains("Cassandra.keySpace")) {
            this.session = this.cluster.connect(serverSettings.getSetting("Cassandra.keySpace"));
        } else {
            this.session = this.cluster.connect();
        }

        return null;
    }

    public Cluster getCluster () {
        return this.cluster;
    }

    public Session getSession () {
        return this.session;
    }

    @Override
    public Object getInstance() {
        return this.session;
    }

    @Override
    public boolean isConnected() {
        if (this.session != null && !this.session.isClosed()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void close() {
        this.session.close();
        this.cluster.close();
    }

}
