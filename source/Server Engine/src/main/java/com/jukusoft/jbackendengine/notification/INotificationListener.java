package com.jukusoft.jbackendengine.notification;

/**
 * Created by Justin on 02.07.2015.
 */
public interface INotificationListener<T> {

    public void onReceive (T obj);

}
