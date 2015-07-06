package com.jukusoft.jbackendengine.backendengine;

import com.jukusoft.jbackendengine.backendengine.authentification.IUserManager;
import com.jukusoft.jbackendengine.backendengine.authentification.UserAuthentificationMode;
import com.jukusoft.jbackendengine.backendengine.module.IModuleManager;
import com.jukusoft.jbackendengine.backendengine.notification.INotificationManager;
import com.jukusoft.jbackendengine.backendengine.serversettings.IServerSettings;
import com.jukusoft.jbackendengine.backendengine.sessionstore.ISessionStore;
import com.jukusoft.jbackendengine.backendengine.database.IDatabaseConnectorManager;
import com.jukusoft.jbackendengine.backendengine.logger.ILoggerManager;
import com.jukusoft.jbackendengine.backendengine.task.ITaskManager;
import com.jukusoft.jbackendengine.backendengine.uniqueID.IUniqueUserIDGenerator;

/**
 * Created by Justin on 05.07.2015.
 */
public interface IEditableBackendEngine {

    public IUserManager getUserManager (UserAuthentificationMode authentificationMode);
    public void setUserManager (IUserManager userManager);
    public ISessionStore getSessionStore ();
    public void setSessionStore (ISessionStore sessionStore);
    public IServerSettings getLocalSettings ();
    public void setLocalSettings (IServerSettings serverSettings);
    public IServerSettings getDistributedSettings ();
    public void setDistributedSettings (IServerSettings serverSettings);
    public ITaskManager getTaskManager ();
    public void setTaskManager (ITaskManager taskManager);
    public IUniqueUserIDGenerator getUniqueUserIDGenerator ();
    public void setUniqueUserIDGenerator (IUniqueUserIDGenerator uniqueUserIDGenerator);
    public INotificationManager getNotificationManager ();
    public void setNotificationManager (INotificationManager notificationManager);
    public Long getServerID ();
    public void setServerID (Long serverID);
    public IModuleManager getModuleManager ();
    public void setModuleManager (IModuleManager moduleManager);
    public IDatabaseConnectorManager getDatabaseConnectorManager ();
    public void setDatabaseConnectorManager (IDatabaseConnectorManager databaseConnectorManager);
    public ILoggerManager getLoggerManager ();
    public void setLoggerManager (ILoggerManager loggerManager);

}
