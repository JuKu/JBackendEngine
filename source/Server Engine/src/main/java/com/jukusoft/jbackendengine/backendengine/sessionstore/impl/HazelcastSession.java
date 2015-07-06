package com.jukusoft.jbackendengine.backendengine.sessionstore.impl;

import com.jukusoft.jbackendengine.backendengine.sessionstore.ISession;
import com.jukusoft.jbackendengine.backendengine.sessionstore.ISessionStore;
import com.jukusoft.jbackendengine.backendengine.sessionstore.exception.SessionNotFoundException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Justin on 02.07.2015.
 */
public class HazelcastSession implements ISession {

    private ISessionStore sessionStore = null;
    private String sessionKey = "";
    private Map<String,String> dataMap = new HashMap<String,String>();
    private Long dbInserted = 0l;

    public HazelcastSession (ISessionStore sessionStore, String sessionKey) {
        this.sessionStore = sessionStore;
        this.sessionKey = sessionKey;
    }

    public HazelcastSession () {
        //
    }

    public void loadFromJSON (JSONObject json) {
        //
    }

    @Override
    public String getSessionKey() {
        return this.sessionKey;
    }

    /*
     * Provides additional functionality to set a new expire date
     */
    @Override
    public void access() {
        if (this.sessionStore != null) {
            try {
                this.sessionStore.getSession(this.sessionKey);
            } catch (SessionNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Long getInsertedDate() {
        return this.dbInserted;
    }

    @Override
    public boolean containsData(String key) {
        return this.dataMap.containsKey(key);
    }

    @Override
    public void putData(String key, String value) {
        this.dataMap.put(key, value);
    }

    @Override
    public String getData(String key) {
        return this.dataMap.get(key);
    }

    @Override
    public void commitData() {
        //send data to server
        this.sessionStore.putSession(this.sessionKey, this);
    }

    @Override
    public String toJSON() {
        return this.getJSONObject().toString();
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionKey", this.sessionKey);

        Calendar calendar = new GregorianCalendar();
        jsonObject.put("dbInserted", calendar.getTimeInMillis());

        JSONArray jsonArray = new JSONArray();

        for (Map.Entry<String,String> entry : this.dataMap.entrySet()) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("key", entry.getKey());
            jsonObject1.put("value", entry.getValue());

            jsonArray.put(jsonObject1.toString());
        }

        jsonObject.put("data", jsonArray.toString());

        return jsonObject;
    }
}
