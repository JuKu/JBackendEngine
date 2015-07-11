package com.jukusoft.jbackendengine.backendengine.sessionstore.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.jukusoft.jbackendengine.backendengine.IBackendEngine;
import com.jukusoft.jbackendengine.backendengine.sessionstore.ISession;
import com.jukusoft.jbackendengine.backendengine.sessionstore.ISessionStore;
import com.jukusoft.jbackendengine.backendengine.sessionstore.exception.SessionNotFoundException;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Created by Justin on 06.07.2015.
 */
public class LocalSessionStore implements ISessionStore {

    private Cache<String,ISession> sessionCache = null;

    public LocalSessionStore (IBackendEngine backendEngine, int maxSize, int TTL) {
        this.sessionCache = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .expireAfterAccess(TTL, TimeUnit.SECONDS)
                .recordStats()
                .build();

        this.addCleanupTask(backendEngine);
    }

    public LocalSessionStore (IBackendEngine backendEngine) {
        this.sessionCache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(60, TimeUnit.MINUTES)
                .build();

        this.addCleanupTask(backendEngine);
    }

    private void addCleanupTask (IBackendEngine backendEngine) {
        backendEngine.getTaskManager().schedulePeriodically(new Runnable() {
            @Override
            public void run() {
                synchronized (sessionCache) {
                    sessionCache.cleanUp();
                }
            }
        }, 0l, 60l, TimeUnit.SECONDS);
    }

    @Override
    public boolean contains(String sessionKey) {
        return this.sessionCache.asMap().containsKey(sessionKey);
    }

    @Override
    public ISession getSession(String sessionKey) throws SessionNotFoundException {
        if (!this.contains(sessionKey)) {
            throw new SessionNotFoundException("session with sessionKey " + sessionKey + " not found.");
        }

        return this.sessionCache.getIfPresent(sessionKey);
    }

    @Override
    public ISession createNewSession() {
        UUID uuid = UUID.randomUUID();
        ISession session = new Session(this, uuid.toString());
        return session;
    }

    @Override
    public ISession createNewSession(Long TTL) {
        //Guava Cache doesnt support TTL for each entry
        return this.createNewSession();
    }

    @Override
    public ISession createNewSession(String sessionKey) {
        return null;
    }

    @Override
    public void putSession(String sessionKey, ISession session) {
        this.sessionCache.put(sessionKey, session);
    }

    @Override
    public void putSession(String sessionKey, ISession session, Long TTL) {
        this.sessionCache.put(sessionKey, session);
    }

    @Override
    public void removeSession(String sessionKey) {
        if (this.contains(sessionKey)) {
            this.sessionCache.put(sessionKey, null);
        }
    }

}
