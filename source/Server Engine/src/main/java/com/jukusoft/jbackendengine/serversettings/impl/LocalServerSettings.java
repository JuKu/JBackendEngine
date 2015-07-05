package com.jukusoft.jbackendengine.serversettings.impl;

import com.jukusoft.jbackendengine.json.JSONConvertable;
import com.jukusoft.jbackendengine.serversettings.IServerSettings;
import org.ini4j.Ini;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Justin on 02.07.2015.
 */
public class LocalServerSettings implements IServerSettings {

    private Map<String,String> localSettings = new HashMap<String,String>();

    public LocalServerSettings (File configDir) {
        if (!configDir.exists()) {
            configDir.mkdirs();
        }

        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".ini") || name.toLowerCase().endsWith(".cfg");
            }
        };

        File[] files = configDir.listFiles(filenameFilter);

        for (File configFile : files) {
            try {
                this.loadSettingsFromFile(configFile, true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public LocalServerSettings () {
        //
    }

    public void loadSettingsFromFile (File configFile, boolean override) throws IOException {
        Ini ini = new Ini(configFile);

        Set<String> sections = ini.keySet();

        for (String key : sections) {
            Ini.Section section = ini.get(key);

            for (String key1 : section.keySet()) {
                String newKey = key + "." + key1;
                String value = section.get(key1);

                this.setSetting(newKey, value);
            }
        }
    }

    public void loadSettingsFromFile (File configFile) throws IOException {
        this.loadSettingsFromFile(configFile, true);
    }

    public void loadSettingsFromFile (String configFile, boolean override) throws IOException {
        this.loadSettingsFromFile(new File(configFile), override);
    }

    public void loadSettingsFromFile (String configFile) throws IOException {
        this.loadSettingsFromFile(new File(configFile));
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
