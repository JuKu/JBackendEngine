package com.jukusoft.jbackendengine.sessionstore.impl;

import com.jukusoft.jbackendengine.sessionstore.ISession;
import com.jukusoft.jbackendengine.sessionstore.ISessionStore;
import com.jukusoft.jbackendengine.sessionstore.exception.SessionNotFoundException;

/**
 * Created by Justin on 06.07.2015.
 */
public class LocalSessionStore implements ISessionStore {
    @Override
    public boolean contains(String sessionKey) {
        return false;
    }

    @Override
    public ISession getSession(String sessionKey) throws SessionNotFoundException {
        return null;
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

    }

    @Override
    public void putSession(String sessionKey, ISession session, Long TTL) {

    }

    @Override
    public void removeSession(String sessionKey) {

    }
}
