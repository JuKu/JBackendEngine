package com.jukusoft.jbackendengine.notification.impl;

import com.jukusoft.jbackendengine.notification.INotificationManager;
import com.jukusoft.jbackendengine.notification.INotificationService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Justin on 02.07.2015.
 */
public class DefaultNotificationManager implements INotificationManager {

    private Map<String,INotificationService<?>> notificationServiceMap = new HashMap<String,INotificationService<?>>();

    @Override
    public <T> INotificationService<T> getService(String name) {
        if (!this.notificationServiceMap.containsKey(name)) {
            this.addNotificationService(name, new DefaultNotificationService<T>());
            System.out.println("INotificationService " + name + " not found, create a new DefaultNotificationService.");
        }

        return (INotificationService<T>) this.notificationServiceMap.get(name);
    }

    @Override
    public <T> void addNotificationService(String name, INotificationService<T> service) {
        this.notificationServiceMap.put(name, service);
    }

    @Override
    public <T> void publish(String name, T obj) {
        this.getService(name).publish(obj);
    }
}
