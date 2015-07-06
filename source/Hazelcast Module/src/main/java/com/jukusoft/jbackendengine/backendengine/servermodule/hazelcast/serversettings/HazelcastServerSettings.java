package com.jukusoft.jbackendengine.backendengine.servermodule.hazelcast.serversettings;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.jukusoft.jbackendengine.backendengine.json.JSONConvertable;
import com.jukusoft.jbackendengine.backendengine.serversettings.IServerSettings;
import org.ini4j.Ini;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Created by Justin on 02.07.2015.
 */
public class HazelcastServerSettings implements IServerSettings {

    private HazelcastInstance hazelcastInstance = null;
    private IMap<String,String> serverSettings = null;

    public HazelcastServerSettings(HazelcastInstance hazelcastInstance, String serverSettingsMapName) {
        this.hazelcastInstance = hazelcastInstance;
        this.serverSettings = this.hazelcastInstance.getMap(serverSettingsMapName);
    }

    public HazelcastServerSettings(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
        this.serverSettings = this.hazelcastInstance.getMap("settings");
    }

    public void uploadFromConfigDir (File configDir, boolean override) throws IOException {
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".ini") || name.toLowerCase().endsWith(".cfg");
            }
        };

        File[] files = configDir.listFiles(filenameFilter);

        for (File configFile : files) {
            this.uploadFromFile(configFile, override);
        }
    }

    public void uploadFromConfigDir (File configDir) throws IOException {
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".ini") || name.toLowerCase().endsWith(".cfg");
            }
        };

        File[] files = configDir.listFiles(filenameFilter);

        for (File configFile : files) {
            this.uploadFromFile(configFile);
        }
    }

    public void uploadFromFile (File configFile, boolean override) throws IOException {
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

    public void uploadFromFile (File configFile) throws IOException {
        this.uploadFromFile(configFile, true);
    }

    public void uploadFromFile (String configFile, boolean override) throws IOException {
        this.uploadFromFile(new File(configFile), override);
    }

    public void uploadFromFile (String configFile) throws IOException {
        this.uploadFromFile(new File(configFile));
    }

    @Override
    public String getSetting(String section, String key) {
        return this.getSetting(section + "." + key);
    }

    @Override
    public String getSetting(String key) {
        return this.serverSettings.get(key);
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
        return Long.parseLong(this.getSetting(key));
    }

    @Override
    public boolean getBoolean(String key) {
        return this.getSetting(key).equals("true");
    }

    @Override
    public boolean contains(String key) {
        return this.serverSettings.containsKey(key);
    }

    @Override
    public void setSetting(String section, String key, String value) {
        this.setSetting(section + "." + key, value);
    }

    @Override
    public void setSetting(String key, String value) {
        this.serverSettings.put(key, value);
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
        return this.serverSettings.size();
    }
}
