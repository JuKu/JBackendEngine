package com.jukusoft.jbackendengine.backendengine.serversettings.impl;

import com.jukusoft.jbackendengine.backendengine.json.JSONConvertable;
import com.jukusoft.jbackendengine.backendengine.serversettings.IServerSettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Justin on 05.07.2015.
 */
public class CustomServerSettings implements IServerSettings {

    private Map<String,String> localSettings = new HashMap<String,String>();

    public CustomServerSettings () {
        //
    }

    @Override
    public String getSetting(String section, String key) {
        return this.getSetting(section + "." + key);
    }

    @Override
    public String getSetting(String key) {
        if (this.contains(key)) {
            return this.localSettings.get(key);
        } else {
            return "";
        }
    }

    @Override
    public String getSettingOrDefaultValue(String key, String defaultValue) {
        if (this.contains(key)) {
            return this.getSetting(key);
        } else {
            return defaultValue;
        }
    }

    @Override
    public int getInteger(String key, int defaultValue) {
        if (this.contains(key)) {
            return this.getInteger(key);
        } else {
            return defaultValue;
        }
    }

    @Override
    public int getInteger(String key) {
        return Integer.parseInt(this.getSetting(key));
    }

    @Override
    public Long getLong(String key) {
        return Long.parseLong(this.getSettingOrDefaultValue(key, "0l"));
    }

    @Override
    public boolean getBoolean(String key) {
        return this.getSetting(key).equals("true");
    }

    @Override
    public boolean contains(String key) {
        return this.localSettings.containsKey(key);
    }

    @Override
    public void setSetting(String section, String key, String value) {
        this.setSetting(section + "." + key, value);
    }

    @Override
    public void setSetting(String key, String value) {
        this.localSettings.put(key, value);
    }

    @Override
    public void setSetting(String section, String key, JSONConvertable jsonConvertable) {
        this.setSetting(section + "." + key, jsonConvertable);
    }

    @Override
    public void setSetting(String key, JSONConvertable jsonConvertable) {
        this.setSetting(key, jsonConvertable.toJSON());
    }

    @Override
    public boolean multiValuesAllowed() {
        return false;
    }

    @Override
    public List<String> getValues(String key) {
        return null;
    }

    @Override
    public int size() {
        return this.localSettings.size();
    }
}
