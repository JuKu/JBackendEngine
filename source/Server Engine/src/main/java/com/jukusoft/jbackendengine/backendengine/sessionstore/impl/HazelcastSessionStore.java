package com.jukusoft.jbackendengine.backendengine.sessionstore.impl;

import com.hazelcast.cache.ICache;
import com.hazelcast.core.HazelcastInstance;
import com.jukusoft.jbackendengine.backendengine.sessionstore.ISession;
import com.jukusoft.jbackendengine.backendengine.sessionstore.ISessionStore;
import com.jukusoft.jbackendengine.backendengine.sessionstore.exception.SessionNotFoundException;
import org.json.JSONObject;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Justin on 02.07.2015.
 */
public class HazelcastSessionStore implements ISessionStore {

    private HazelcastInstance hazelcastInstance = null;
    private ICache<String,String> sessionCache = null;

    public HazelcastSessionStore (HazelcastInstance hazelcastInstance, Long TTL) {
        this.hazelcastInstance = hazelcastInstance;

        CacheManager manager = Caching.getCachingProvider().getCacheManager();
        MutableConfiguration<String, String> configuration = new MutableConfiguration<String, String>();

        configuration.setExpiryPolicyFactory(AccessedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, TTL)));

        Cache<String,String> cache = manager.createCache("sessionCache", configuration);
        this.sessionCache = cache.unwrap(ICache.class);
    }

    @Override
    public boolean contains(String sessionKey) {
        return this.sessionCache.containsKey(sessionKey);
    }

    @Override
    public ISession getSession(String sessionKey) throws SessionNotFoundException {
        if (!this.contains(sessionKey)) {
            throw new SessionNotFoundException("session with sessionKey " + sessionKey + " not found.");
        }

        HazelcastSession session = new HazelcastSession(this, sessionKey);
        String value = null;

        try {
            value = this.sessionCache.getAsync(sessionKey).get(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new SessionNotFoundException(e.getStackTrace() + ".");
        } catch (ExecutionException e) {
            e.printStackTrace();
            throw new SessionNotFoundException(e.getStackTrace() + ".");
        } catch (TimeoutException e) {
            e.printStackTrace();
            throw new SessionNotFoundException(e.getStackTrace() + ".");
        }

        JSONObject jsonObject = new JSONObject(value);
        session.loadFromJSON(jsonObject);

        return session;
    }

    @Override
    public ISession createNewSession() {
        return null;
    }

    @Override
    public ISession createNewSession(Long TTL) {
        return null;
    }

    @Override
    public void putSession(String sessionKey, ISession session) {
        this.sessionCache.putAsync(sessionKey, session.toJSON());
    }

    @Override
    public void putSession(String sessionKey, ISession session, Long TTL) {
        this.sessionCache.putAsync(sessionKey, session.toJSON(), AccessedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, TTL)).create());
    }

    @Override
    public void removeSession(String sessionKey) {
        this.sessionCache.removeAsync(sessionKey);
    }
}
