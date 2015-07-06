package com.jukusoft.jbackendengine.sessionstore.impl;

import com.jukusoft.jbackendengine.sessionstore.ISession;
import org.json.JSONObject;

/**
 * Created by Justin on 06.07.2015.
 */
public class Session implements ISession {
    @Override
    public String getSessionKey() {
        return null;
    }

    @Override
    public void access() {

    }

    @Override
    public Long getInsertedDate() {
        return null;
    }

    @Override
    public boolean containsData(String key) {
        return false;
    }

    @Override
    public void putData(String key, String value) {

    }

    @Override
    public String getData(String key) {
        return null;
    }

    @Override
    public void commitData() {

    }

    @Override
    public String toJSON() {
        return null;
    }

    @Override
    public JSONObject getJSONObject() {
        return null;
    }
}
