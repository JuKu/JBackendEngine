package com.jukusoft.jbackendengine.backendengine.module.impl;

import com.jukusoft.jbackendengine.backendengine.IBackendEngine;
import com.jukusoft.jbackendengine.backendengine.module.*;
import com.jukusoft.jbackendengine.backendengine.module.exception.ModuleNotFoundException;
import com.jukusoft.jbackendengine.backendengine.module.exception.NotSupportedModuleException;
import com.jukusoft.jbackendengine.backendengine.module.exception.InvalidModuleException;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Justin on 03.07.2015.
 */
public class DefaultModuleManager implements IModuleManager {

    private Map<String,IModule> moduleMap = new HashMap<String,IModule>();
    private IBackendEngine backendEngine = null;
    private IModuleDependencieManager moduleDependencieManager = null;

    public DefaultModuleManager (IBackendEngine backendEngine) {
        this.backendEngine = backendEngine;
        this.moduleDependencieManager = new DefaultModuleDependencieManager();

        //register module dependencie manager
        this.backendEngine.registerManager("moduleDependencieManager", this.moduleDependencieManager);
    }

    @Override
    public void registerModule(String name, IModule module) throws NotSupportedModuleException, InvalidModuleException {
        if (this.isSupportedModule(module) && this.moduleDependencieManager.checkDependencies(module)) {
            module.initialize(this.backendEngine);
            this.moduleMap.put(name, module);
        } else {
            throw new NotSupportedModuleException("module " + module.getClass().getName() + " isnt supported.");
        }
    }

    @Override
    public void removeModule(String name) {
        this.moduleMap.remove(name);
    }

    @Override
    public boolean contains(String name) {
        return this.moduleMap.containsKey(name);
    }

    @Override
    public boolean isSupportedModule(IModule module) throws InvalidModuleException {
        if (module.getClass().isAnnotationPresent(ModuleInfo.class)) {
            Annotation annotation = module.getClass().getAnnotation(ModuleInfo.class);
            ModuleInfo moduleInfo = (ModuleInfo) annotation;

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean hasExclusiveEngineRights(IModule module) {
        try {
            //module has to set exclusive engine rights in ModuleInfo annotation
            ModuleInfo moduleInfo = ModuleUtils.getModuleInfo(module);
            return moduleInfo.permissionToOverrideEngineModules();
        } catch (InvalidModuleException e) {
            this.backendEngine.getLoggerManager().warn("InvalidModuleException on module " + module.getClass().getName() + " " + e.getStackTrace() + ".");
            e.printStackTrace();

            return false;
        }
    }

    @Override
    public void loadModulesFromFile(File moduleFile) throws IOException {
        IModuleLoader moduleLoader = new DefaultModuleLoader();
        List<IModule> moduleList = moduleLoader.loadModulesFromFile(moduleFile);

        this.loadModules(moduleList);
    }

    @Override
    public void loadModulesFromDir(File moduleDir) throws IOException {
        IModuleLoader moduleLoader = new DefaultModuleLoader();
        List<IModule> moduleList = moduleLoader.loadModulesFromDir(moduleDir);

        this.loadModules(moduleList);
    }

    @Override
    public void loadAndStart(File moduleDir) {
        try {
            this.loadModulesFromDir(moduleDir);
            this.startAllModules();
        } catch (IOException e) {
            backendEngine.getLoggerManager().warn("Couldnt load modules " + e.getStackTrace() + ".");
            e.printStackTrace();
        }
    }

    @Override
    public void shutdownAllModules() {
        for (Map.Entry<String,IModule> entry : this.moduleMap.entrySet()) {
            entry.getValue().stop();
        }
    }

    private void loadModules (List<IModule> moduleList) {
        //check dependencies
        Map<IModule,Boolean> result = this.moduleDependencieManager.process(moduleList);

        for (Map.Entry<IModule,Boolean> entry : result.entrySet()) {
            IModule module = entry.getKey();
            boolean isSupported = entry.getValue();

            if (isSupported) {
                try {
                    ModuleInfo moduleInfo = ModuleUtils.getModuleInfo(module);
                    this.registerModule(moduleInfo.moduleName(), module);
                } catch (InvalidModuleException e) {
                    this.backendEngine.getLoggerManager().warn("Coulnt load module " + module.getClass().getName() + ", because module hasnt a ModuleInfo annotation " + e.getStackTrace() + ".");
                    e.printStackTrace();
                } catch (NotSupportedModuleException e) {
                    this.backendEngine.getLoggerManager().warn("Coulnt load module " + module.getClass().getName() + ", because module isnt supported.");
                    e.printStackTrace();
                }
            } else {
                this.backendEngine.getLoggerManager().warn("Couldnt load module " + module.getClass().getName() + ", because module isnt supported.");
            }
        }
    }

    private Map<ModuleInfo.Priority,List<IModule>> getPriorityLists (List<IModule> moduleList) {
        Map<ModuleInfo.Priority,List<IModule>> priorityListMap = new HashMap<ModuleInfo.Priority,List<IModule>>();

        priorityListMap.put(ModuleInfo.Priority.LOW_LEVEL, new ArrayList<IModule>());
        priorityListMap.put(ModuleInfo.Priority.DB, new ArrayList<IModule>());
        priorityListMap.put(ModuleInfo.Priority.HIGH, new ArrayList<IModule>());
        priorityListMap.put(ModuleInfo.Priority.MEDIUM, new ArrayList<IModule>());
        priorityListMap.put(ModuleInfo.Priority.LOW, new ArrayList<IModule>());

        ModuleInfo moduleInfo = null;

        for (IModule module : moduleList) {
            try {
                //get ModuleInfo
                moduleInfo = ModuleUtils.getModuleInfo(module);
                ModuleInfo.Priority priority = moduleInfo.priority();

                //add module to list
                priorityListMap.get(priority).add(module);
            } catch (InvalidModuleException e) {
                this.backendEngine.getLoggerManager().warn("InvalidModuleException in module " + module.getClass().getName() + " " + e.getStackTrace() + ".");
                e.printStackTrace();
            }
        }

        return priorityListMap;
    }

    private void startModulesByPriority (List<IModule> moduleList) {
        Map<ModuleInfo.Priority,List<IModule>> priorityIModuleMap = this.getPriorityLists(moduleList);

        //start all modules
        this.startModules(priorityIModuleMap.get(ModuleInfo.Priority.LOW_LEVEL));
        this.startModules(priorityIModuleMap.get(ModuleInfo.Priority.DB));
        this.startModules(priorityIModuleMap.get(ModuleInfo.Priority.HIGH));
        this.startModules(priorityIModuleMap.get(ModuleInfo.Priority.MEDIUM));
        this.startModules(priorityIModuleMap.get(ModuleInfo.Priority.LOW));
    }

    private void startModules (List<IModule> moduleList) {
        for (IModule module : moduleList) {
            this.startModule(module);
        }
    }

    private void startModule (IModule module) {
        if (!module.isInitialized()) {
            module.initialize(this.backendEngine);
        }

        module.start(this.backendEngine);
    }

    @Override
    public void startModule(String name) {
        if (this.moduleMap.containsKey(name)) {
            IModule module = this.moduleMap.get(name);
            module.start(this.backendEngine);
        }
    }

    @Override
    public void startAllModules() {
        List<IModule> moduleList = new ArrayList<IModule>();

        for (Map.Entry<String,IModule> entry : this.moduleMap.entrySet()) {
            IModule module = entry.getValue();
            moduleList.add(module);
        }

        //start modules by priority
        this.startModulesByPriority(moduleList);
    }

    @Override
    public List<ModuleInfo> listAllModuleInfo() {
        List<ModuleInfo> moduleInfoList = new ArrayList<ModuleInfo>();

        for (Map.Entry<String,IModule> entry : this.moduleMap.entrySet()) {
            IModule module = entry.getValue();

            try {
                ModuleInfo moduleInfo = ModuleUtils.getModuleInfo(module);
                moduleInfoList.add(moduleInfo);
            } catch (InvalidModuleException e) {
                e.printStackTrace();
            }
        }

        return moduleInfoList;
    }

    @Override
    public IModule getModule(String name) throws ModuleNotFoundException {
        if (this.contains(name)) {
            return this.moduleMap.get(name);
        } else {
            throw new ModuleNotFoundException("module " + name + " not found.");
        }
    }

}
