package com.jukusoft.jbackendengine.backendengine.notification.impl;

import com.jukusoft.jbackendengine.backendengine.notification.INotificationListener;
import com.jukusoft.jbackendengine.backendengine.notification.INotificationService;

import java.util.*;

/**
 * Created by Justin on 02.07.2015.
 */
public class DefaultNotificationService<T> implements INotificationService {

    private List<INotificationListener<T>> listenerList = new ArrayList<INotificationListener<T>>();

    @Override
    public void registerNotificationListener(INotificationListener listener) {
        this.listenerList.add(listener);
    }

    @Override
    public void removeNotificationListener(INotificationListener listener) {
        this.listenerList.remove(listener);
    }

    @Override
    public void publish(Object obj) {
        List<INotificationListener<T>> copyList = Collections.synchronizedList(this.listenerList);

        try {
            for (INotificationListener<T> listener : copyList) {
                listener.onReceive((T) obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
