package com.jukusoft.jbackendengine.backendengine.serversettings;

import com.jukusoft.jbackendengine.backendengine.json.JSONConvertable;

import java.util.List;

/**
 * Created by Justin on 02.07.2015.
 */
public interface IServerSettings {

    public String getSetting (String section, String key);
    public String getSetting (String key);
    public String getSettingOrDefaultValue (String key, String defaultValue);
    public int getInteger (String key, int defaultValue);
    public int getInteger (String key);
    public Long getLong (String key);
    public boolean getBoolean (String key);
    public boolean contains (String key);
    public void setSetting (String section, String key, String value);
    public void setSetting (String key, String value);
    public void setSetting (String section, String key, JSONConvertable jsonConvertable);
    public void setSetting (String key, JSONConvertable jsonConvertable);
    public boolean multiValuesAllowed ();
    public List<String> getValues (String key);
    public int size ();

}
