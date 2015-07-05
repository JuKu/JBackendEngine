package com.jukusoft.jbackendengine.notification;

import com.jukusoft.jbackendengine.manager.IManager;

/**
 * Created by Justin on 02.07.2015.
 */
public interface INotificationManager extends IManager {

    public <T> INotificationService<T> getService (String name);
    public <T> void addNotificationService (String name, INotificationService<T> service);
    public <T> void publish (String name, T obj);

}
