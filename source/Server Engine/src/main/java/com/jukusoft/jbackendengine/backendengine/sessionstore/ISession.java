package com.jukusoft.jbackendengine.backendengine.sessionstore;

import com.jukusoft.jbackendengine.backendengine.json.JSONConvertable;

/**
 * Created by Justin on 02.07.2015.
 */
public interface ISession extends JSONConvertable {

    public String getSessionKey ();
    public void access ();
    public Long getInsertedDate ();
    public boolean containsData (String key);
    public void putData (String key, String value);
    public void putData (String key, Object obj);
    public String getData (String key);
    public Object getObject (String key);
    public void commitData ();

}
