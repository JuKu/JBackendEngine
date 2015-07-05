package com.jukusoft.jbackendengine.module;

/**
 * Created by Justin on 03.07.2015.
 */
public class ModuleDependencie {

    private String moduleName = "";
    private String version = "";
    private int buildNumber = 1;

    public ModuleDependencie (String moduleName, String version, int buildNumber) {
        this.moduleName = moduleName;
        this.version = version;
        this.buildNumber = buildNumber;
    }

    public ModuleDependencie (String module, String version) {
        this.moduleName = module;
        this.version = version;
    }

    public ModuleDependencie (String module) {
        this.moduleName = module;
        this.version = "*.*.*";
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getVersion() {
        return version;
    }

    public int getBuildNumber() {
        return buildNumber;
    }
}
