package com.jukusoft.jbackendengine.uniqueID;

import com.hazelcast.core.HazelcastInstance;

/**
 * Created by Justin on 02.07.2015.
 */
public class UniqueUserIDGenerator implements IUniqueUserIDGenerator {

    private HazelcastInstance hazelcastInstance = null;

    public UniqueUserIDGenerator (HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public Long generate(Long serverID) {
        return null;
    }

    @Override
    public Long generate() {
        return null;
    }
}
