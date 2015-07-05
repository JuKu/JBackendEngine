package com.jukusoft.jbackendengine.module.impl;

import com.google.common.collect.Lists;
import com.jukusoft.jbackendengine.module.*;
import com.jukusoft.jbackendengine.module.exception.InvalidModuleException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Justin on 03.07.2015.
 */
public class DefaultModuleDependencieManager implements IModuleDependencieManager {

    private Map<String,List<ModuleDependencie>> moduleDependencieMap = new HashMap<String,List<ModuleDependencie>>();
    private String backendEngineVersion = "";
    private Map<String,ModuleInfo> moduleInfoMap = new HashMap<String,ModuleInfo>();

    @Override
    public void addDependencie(String module, ModuleDependencie moduleDependencie) {
        if (!this.moduleDependencieMap.containsKey(module)) {
            List<ModuleDependencie> moduleDependencieList = new ArrayList<ModuleDependencie>();
        }

        this.moduleDependencieMap.get(module).add(moduleDependencie);
    }

    @Override
    public void removeDependencie(String module, ModuleDependencie moduleDependencie) {
        if (this.moduleDependencieMap.containsKey(module)) {
            this.moduleDependencieMap.get(module).remove(moduleDependencie);
        }
    }

    @Override
    public List<ModuleDependencie> getAllDependenciesForModule(String module) {
        if (this.moduleDependencieMap.containsKey(module)) {
            return Lists.newArrayList(this.moduleDependencieMap.get(module));
        } else {
            List<ModuleDependencie> moduleDependencieList = new ArrayList<ModuleDependencie>();
            return moduleDependencieList;
        }
    }

    @Override
    public void insert(IModule module) throws InvalidModuleException {
        ModuleInfo moduleInfo = ModuleUtils.getModuleInfo(module);

        String[] dependencies = moduleInfo.dependencies();

        for (String dependencieStr : dependencies) {
            String[] strArray = dependencieStr.split(":");
            ModuleDependencie moduleDependencie = null;

            if (strArray.length > 1) {
                moduleDependencie = new ModuleDependencie(strArray[0], strArray[1]);
            } else {
                moduleDependencie = new ModuleDependencie(strArray[0]);
            }

            this.addDependencie(moduleInfo.moduleName(), moduleDependencie);
        }

        this.moduleInfoMap.put(moduleInfo.moduleName(), moduleInfo);
    }

    @Override
    public void setBackendEngineVersion(String version) {
        this.backendEngineVersion = version;
    }

    @Override
    public boolean checkDependencies(IModule module) throws InvalidModuleException {
        ModuleInfo moduleInfo = ModuleUtils.getModuleInfo(module);
        String[] dependenciesStr = moduleInfo.dependencies();

        for (String dependencieStr : dependenciesStr) {
            String[] strArray = dependencieStr.split(":");
            String dependencieModule = "";
            String dependencieVersion = "";

            if (strArray.length > 1) {
                dependencieModule = strArray[0];
                dependencieVersion = strArray[1];
            } else {
                dependencieModule = strArray[0];
                dependencieVersion = "*.*.*";
            }

            //check if module is available
            if (this.moduleInfoMap.containsKey(dependencieModule)) {
                //check version
                if (!this.checkVersion(this.moduleInfoMap.get(dependencieModule).version(), dependencieVersion)) {
                    //dependencie module version isnt supported by module
                    return false;
                }
            } else if (dependencieModule.equals("backendengine")) {
                if (!this.checkVersion(this.backendEngineVersion, dependencieVersion)) {
                    //dependencie module version isnt supported by module
                    return false;
                }
            } else {
                //dependencie module is not available
                return false;
            }
        }

        return true;
    }

    private boolean checkVersion (String neededVersion, String isVersion) {
        String[] neededVersionArray = this.splitVersionIntoStringArray(neededVersion);
        int[] isVersionArray = this.splitVersionIntoIntArray(isVersion);
        int majorVersion1 = isVersionArray[0];
        int minorVersion1 = isVersionArray[1];
        int patchVersion1 = isVersionArray[2];

        if (neededVersionArray[0].equals("*") || Integer.parseInt(neededVersionArray[0]) == majorVersion1) {
            //major version is supported by module
            if (neededVersionArray[1].equals("*") || Integer.parseInt(neededVersionArray[1]) == minorVersion1) {
                //minor version is supported by module
                if (neededVersionArray[2].equals("*") || Integer.parseInt(neededVersionArray[2]) == patchVersion1) {
                    //patch version is supported by module
                    return true;
                } else {
                    return false;
                }
            }
        }

        return false;
    }

    private int[] splitVersionIntoIntArray (String version) {
        int[] versionInt = new int[3];

        String[] strArray = version.split(".");

        if (strArray.length > 2) {
            versionInt[0] = Integer.parseInt(strArray[0]);
            versionInt[1] = Integer.parseInt(strArray[1]);
            versionInt[2] = Integer.parseInt(strArray[2]);
        } else if (strArray.length > 1) {
            versionInt[0] = Integer.parseInt(strArray[0]);
            versionInt[1] = Integer.parseInt(strArray[1]);
            versionInt[2] = 0;
        } else {
            versionInt[0] = Integer.parseInt(strArray[0]);
            versionInt[1] = 0;
            versionInt[2] = 0;
        }

        return versionInt;
    }

    private String[] splitVersionIntoStringArray (String version) {
        String[] versionStr = new String[3];

        String[] strArray = version.split(".");

        if (strArray.length > 2) {
            versionStr[0] = strArray[0];
            versionStr[1] = strArray[1];
            versionStr[2] = strArray[2];
        } else if (strArray.length > 1) {
            versionStr[0] = strArray[0];
            versionStr[1] = strArray[1];
            versionStr[2] = "*";
        } else {
            versionStr[0] = strArray[0];
            versionStr[1] = "*";
            versionStr[2] = "*";
        }

        return versionStr;
    }

    @Override
    public Map<IModule,Boolean> process(List<IModule> moduleList) {
        Map<IModule,Boolean> moduleSupportedMap = new HashMap<IModule,Boolean>();

        for (IModule module : moduleList) {
            try {
                if (this.process(module)) {
                    moduleSupportedMap.put(module, true);
                } else {
                    moduleSupportedMap.put(module, false);
                }
            } catch (InvalidModuleException e) {
                moduleSupportedMap.put(module, false);
                e.printStackTrace();
            }
        }

        return moduleSupportedMap;
    }

    @Override
    public boolean process(IModule module) throws InvalidModuleException {
        //check if module can started
        if (this.checkDependencies(module)) {
            this.insert(module);
            return true;
        } else {
            return false;
        }
    }

}
