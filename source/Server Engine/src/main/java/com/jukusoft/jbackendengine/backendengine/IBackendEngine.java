package com.jukusoft.jbackendengine.backendengine;

import com.jukusoft.jbackendengine.backendengine.authentification.IUserManager;
import com.jukusoft.jbackendengine.backendengine.authentification.UserAuthentificationMode;
import com.jukusoft.jbackendengine.backendengine.manager.IManager;
import com.jukusoft.jbackendengine.backendengine.manager.exception.IManagerNotFoundException;
import com.jukusoft.jbackendengine.backendengine.module.IModule;
import com.jukusoft.jbackendengine.backendengine.module.IModuleManager;
import com.jukusoft.jbackendengine.backendengine.notification.INotificationManager;
import com.jukusoft.jbackendengine.backendengine.serversettings.IServerSettings;
import com.jukusoft.jbackendengine.backendengine.service.IService;
import com.jukusoft.jbackendengine.backendengine.sessionstore.ISessionStore;
import com.jukusoft.jbackendengine.backendengine.database.IDatabaseConnectorManager;
import com.jukusoft.jbackendengine.backendengine.logger.ILoggerManager;
import com.jukusoft.jbackendengine.backendengine.service.exception.IServiceNotFoundException;
import com.jukusoft.jbackendengine.backendengine.task.ITaskManager;
import com.jukusoft.jbackendengine.backendengine.uniqueID.IUniqueUserIDGenerator;
import com.jukusoft.jbackendengine.backendengine.validation.exception.ModuleAccessNotPermittedException;

/**
 * Created by Justin on 02.07.2015.
 */
public interface IBackendEngine extends IEditableBackendEngine {

    public int getBuildNumber ();
    public void setBuildNumber (int buildNumber);
    public String getBackendEngineVersion ();
    public IUserManager getUserManager (UserAuthentificationMode authentificationMode);
    public ISessionStore getSessionStore ();
    public IServerSettings getLocalSettings ();
    public IServerSettings getDistributedSettings ();
    public ITaskManager getTaskManager ();
    public IUniqueUserIDGenerator getUniqueUserIDGenerator ();
    public INotificationManager getNotificationManager ();
    public Long getServerID ();
    public IModuleManager getModuleManager ();
    public IDatabaseConnectorManager getDatabaseConnectorManager ();
    public IEditableBackendEngine requestEditableBackendEngine (IModule module) throws ModuleAccessNotPermittedException;
    public ILoggerManager getLoggerManager ();
    public void registerManager (String name, IManager manager);
    public void removeManager (String name);
    public IManager getManager (String name) throws IManagerNotFoundException;
    public void setData (String key, Object value);
    public void removeData (String key);
    public boolean containsData (String key);
    public Object getData (String key);
    public String getDataAsString (String key);
    public void addService (String name, IService service);
    public void removeService (String name);
    public IService getService (String name) throws IServiceNotFoundException;
    public void shutdown ();
    public void startEngine (IEditableBackendEngine backendEngine);

}
