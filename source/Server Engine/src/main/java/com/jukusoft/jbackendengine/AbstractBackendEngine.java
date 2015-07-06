package com.jukusoft.jbackendengine;

import com.jukusoft.jbackendengine.authentification.IUserManager;
import com.jukusoft.jbackendengine.authentification.UserAuthentificationMode;
import com.jukusoft.jbackendengine.database.IDatabaseConnectorManager;
import com.jukusoft.jbackendengine.database.impl.DefaultDatabaseConnectorManager;
import com.jukusoft.jbackendengine.logger.ILoggerManager;
import com.jukusoft.jbackendengine.logger.impl.DefaultLoggerManager;
import com.jukusoft.jbackendengine.manager.IManager;
import com.jukusoft.jbackendengine.manager.exception.IManagerNotFoundException;
import com.jukusoft.jbackendengine.module.IModule;
import com.jukusoft.jbackendengine.module.IModuleManager;
import com.jukusoft.jbackendengine.module.impl.DefaultModuleManager;
import com.jukusoft.jbackendengine.notification.INotificationManager;
import com.jukusoft.jbackendengine.notification.impl.DefaultNotificationManager;
import com.jukusoft.jbackendengine.serversettings.IServerSettings;
import com.jukusoft.jbackendengine.serversettings.impl.LocalServerSettings;
import com.jukusoft.jbackendengine.service.IService;
import com.jukusoft.jbackendengine.service.exception.IServiceNotFoundException;
import com.jukusoft.jbackendengine.sessionstore.ISessionStore;
import com.jukusoft.jbackendengine.sessionstore.impl.LocalSessionStore;
import com.jukusoft.jbackendengine.task.ITaskManager;
import com.jukusoft.jbackendengine.task.impl.DefaultTaskManager;
import com.jukusoft.jbackendengine.uniqueID.IUniqueUserIDGenerator;
import com.jukusoft.jbackendengine.validation.exception.ModuleAccessNotPermittedException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Justin on 02.07.2015.
 */
public abstract class AbstractBackendEngine implements IBackendEngine {

    protected int BUILD_NUMBER = 2;
    private final String backendEngineVersion = "1.0.0";

    protected IUserManager userManager = null;
    private ISessionStore sessionStore = null;
    private IServerSettings localSettings = null;
    private IServerSettings distributedSettings = null;
    private ITaskManager taskManager = null;
    private IUniqueUserIDGenerator uniqueUserIDGenerator = null;
    private INotificationManager notificationManager = null;
    private IModuleManager moduleManager = null;
    private IDatabaseConnectorManager databaseConnectorManager = null;
    private ILoggerManager loggerManager = null;
    private Map<String,IManager> managerMap = new HashMap<String,IManager>();
    private Map<String,IService> serviceMap = new HashMap<String,IService>();
    private Long serverID = 0l;
    private Map<String,Object> dataMap = new HashMap<String,Object>();

    public AbstractBackendEngine(IServerSettings localSettings) {
        this.localSettings = localSettings;

        int corePoolSize = this.localSettings.getInteger("TaskManager.corePoolSize", 4);
        int maxPoolSize = this.localSettings.getInteger("TaskManager.maxPoolSize", 8);
        int schedulerPoolSize = this.localSettings.getInteger("TaskManager.schedulerPoolSize", 4);

        this.taskManager = new DefaultTaskManager(corePoolSize, maxPoolSize, schedulerPoolSize);
        this.sessionStore = new LocalSessionStore(this);
        this.notificationManager = new DefaultNotificationManager();
        this.moduleManager = new DefaultModuleManager(this);
        this.databaseConnectorManager = new DefaultDatabaseConnectorManager();
        this.loggerManager = new DefaultLoggerManager();
    }

    public AbstractBackendEngine(File configDir) {
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        this.localSettings = new LocalServerSettings(configDir);

        int corePoolSize = this.localSettings.getInteger("TaskManager.corePoolSize", 4);
        int maxPoolSize = this.localSettings.getInteger("TaskManager.maxPoolSize", 8);
        int schedulerPoolSize = this.localSettings.getInteger("TaskManager.schedulerPoolSize", 4);

        this.taskManager = new DefaultTaskManager(corePoolSize, maxPoolSize, schedulerPoolSize);
        this.sessionStore = new LocalSessionStore(this);
        this.notificationManager = new DefaultNotificationManager();
        this.moduleManager = new DefaultModuleManager(this);
        this.databaseConnectorManager = new DefaultDatabaseConnectorManager();
        this.loggerManager = new DefaultLoggerManager();
    }

    @Override
    public int getBuildNumber() {
        return this.BUILD_NUMBER;
    }

    @Override
    public void setBuildNumber(int BUILD_NUMBER) {
        this.BUILD_NUMBER = BUILD_NUMBER;
    }

    @Override
    public String getBackendEngineVersion() {
        return this.backendEngineVersion;
    }

    @Override
    public IUserManager getUserManager(UserAuthentificationMode authentificationMode) {
        return this.userManager;
    }

    @Override
    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public ISessionStore getSessionStore() {
        return this.sessionStore;
    }

    @Override
    public void setSessionStore(ISessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    @Override
    public IServerSettings getLocalSettings() {
        return this.localSettings;
    }

    @Override
    public void setLocalSettings(IServerSettings serverSettings) {
        this.localSettings = serverSettings;
    }

    @Override
    public IServerSettings getDistributedSettings() {
        return this.distributedSettings;
    }

    @Override
    public void setDistributedSettings(IServerSettings serverSettings) {
        this.distributedSettings = serverSettings;
    }

    @Override
    public ITaskManager getTaskManager() {
        return this.taskManager;
    }

    @Override
    public void setTaskManager(ITaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public IUniqueUserIDGenerator getUniqueUserIDGenerator() {
        return this.uniqueUserIDGenerator;
    }

    @Override
    public void setUniqueUserIDGenerator(IUniqueUserIDGenerator uniqueUserIDGenerator) {
        this.uniqueUserIDGenerator = uniqueUserIDGenerator;
    }

    @Override
    public INotificationManager getNotificationManager() {
        return this.notificationManager;
    }

    @Override
    public void setNotificationManager(INotificationManager notificationManager) {
        this.notificationManager = notificationManager;
    }

    @Override
    public Long getServerID() {
        return this.serverID;
    }

    @Override
    public void setServerID(Long serverID) {
        this.serverID = serverID;
    }

    @Override
    public IModuleManager getModuleManager() {
        return this.moduleManager;
    }

    @Override
    public void setModuleManager(IModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    @Override
    public IDatabaseConnectorManager getDatabaseConnectorManager() {
        return this.databaseConnectorManager;
    }

    @Override
    public IEditableBackendEngine requestEditableBackendEngine(IModule module) throws ModuleAccessNotPermittedException {
        return null;
    }

    @Override
    public void setDatabaseConnectorManager(IDatabaseConnectorManager databaseConnectorManager) {
        this.databaseConnectorManager = databaseConnectorManager;
    }

    @Override
    public ILoggerManager getLoggerManager() {
        return this.loggerManager;
    }

    @Override
    public void setLoggerManager(ILoggerManager loggerManager) {
        this.loggerManager = loggerManager;
    }

    @Override
    public void registerManager(String name, IManager manager) {
        this.managerMap.put(name, manager);
    }

    @Override
    public void removeManager(String name) {
        this.managerMap.remove(name);
    }

    @Override
    public IManager getManager(String name) throws IManagerNotFoundException {
        if (this.managerMap.containsKey(name)) {
            return this.managerMap.get(name);
        } else {
            throw new IManagerNotFoundException("IManager " + name + " isnt registered.");
        }
    }

    @Override
    public void setData(String key, Object value) {
        this.dataMap.put(key, value);
    }

    @Override
    public void removeData(String key) {
        this.dataMap.remove(key);
    }

    @Override
    public boolean containsData(String key) {
        return this.dataMap.containsKey(key);
    }

    @Override
    public Object getData(String key) {
        if (this.dataMap.containsKey(key)) {
            return this.dataMap.get(key);
        } else {
            return null;
        }
    }

    @Override
    public String getDataAsString(String key) {
        return (String) this.getData(key);
    }

    @Override
    public void addService(String name, IService service) {
        Thread thread = new Thread(service);
        thread.setDaemon(true);
        thread.start();
        this.serviceMap.put(name, service);
    }

    @Override
    public void removeService(String name) {
        this.serviceMap.remove(name);
    }

    @Override
    public IService getService(String name) throws IServiceNotFoundException {
        if (this.serviceMap.containsKey(name)) {
            return this.serviceMap.get(name);
        } else {
            throw new IServiceNotFoundException("service " + name + " not found.");
        }
    }

    @Override
    public abstract void startEngine(IEditableBackendEngine backendEngine);

    public final void startEngine () {
        this.startEngine(this);
    }
}
