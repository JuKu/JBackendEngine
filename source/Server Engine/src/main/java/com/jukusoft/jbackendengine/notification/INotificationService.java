package com.jukusoft.jbackendengine.notification;

/**
 * Created by Justin on 02.07.2015.
 */
public interface INotificationService<T> {

    public void registerNotificationListener (INotificationListener<T> listener);
    public void removeNotificationListener (INotificationListener<T> listener);
    public void publish (T obj);

}
