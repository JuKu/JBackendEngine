package com.jukusoft.jbackendengine.backendengine.notification.impl;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ITopic;
import com.jukusoft.jbackendengine.backendengine.notification.INotificationListener;

/**
 * Created by Justin on 02.07.2015.
 */
public class RedirectNotificationToHazelcastTopicListener<T> implements INotificationListener<T> {

    private ITopic<T> topic = null;

    public RedirectNotificationToHazelcastTopicListener(HazelcastInstance hazelcastInstance, String topicName) {
        this.topic = hazelcastInstance.getTopic(topicName);
    }

    @Override
    public void onReceive(T obj) {
        this.topic.publish(obj);
    }
}
