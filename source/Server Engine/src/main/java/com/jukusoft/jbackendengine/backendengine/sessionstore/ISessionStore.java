package com.jukusoft.jbackendengine.backendengine.sessionstore;

import com.jukusoft.jbackendengine.backendengine.sessionstore.exception.SessionNotFoundException;

/**
 * Created by Justin on 02.07.2015.
 */
public interface ISessionStore {

    public boolean contains (String sessionKey);
    public ISession getSession (String sessionKey) throws SessionNotFoundException;
    public ISession createNewSession ();
    public ISession createNewSession (Long TTL);
    public void putSession (String sessionKey, ISession session);
    public void putSession (String sessionKey, ISession session, Long TTL);
    public void removeSession (String sessionKey);

}
