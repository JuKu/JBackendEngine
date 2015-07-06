package com.jukusoft.jbackendengine;

import com.jukusoft.jbackendengine.authentification.IUserManager;
import com.jukusoft.jbackendengine.authentification.UserAuthentificationMode;
import com.jukusoft.jbackendengine.database.IDatabaseConnectorManager;
import com.jukusoft.jbackendengine.logger.ILoggerManager;
import com.jukusoft.jbackendengine.manager.IManager;
import com.jukusoft.jbackendengine.manager.exception.IManagerNotFoundException;
import com.jukusoft.jbackendengine.module.IModule;
import com.jukusoft.jbackendengine.module.IModuleManager;
import com.jukusoft.jbackendengine.notification.INotificationManager;
import com.jukusoft.jbackendengine.serversettings.IServerSettings;
import com.jukusoft.jbackendengine.service.IService;
import com.jukusoft.jbackendengine.service.exception.IServiceNotFoundException;
import com.jukusoft.jbackendengine.sessionstore.ISessionStore;
import com.jukusoft.jbackendengine.task.ITaskManager;
import com.jukusoft.jbackendengine.uniqueID.IUniqueUserIDGenerator;
import com.jukusoft.jbackendengine.validation.exception.ModuleAccessNotPermittedException;

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
