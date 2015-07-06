package com.jukusoft.jbackendengine.backendengine.module;

import com.jukusoft.jbackendengine.backendengine.manager.IManager;
import com.jukusoft.jbackendengine.backendengine.module.exception.ModuleNotFoundException;
import com.jukusoft.jbackendengine.backendengine.module.exception.InvalidModuleException;
import com.jukusoft.jbackendengine.backendengine.module.exception.NotSupportedModuleException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Justin on 03.07.2015.
 */
public interface IModuleManager extends IManager {

    public void registerModule (String name, IModule module) throws NotSupportedModuleException, InvalidModuleException;
    public void removeModule (String name);
    public boolean contains (String name);
    public boolean isSupportedModule (IModule module) throws InvalidModuleException;
    public boolean hasExclusiveEngineRights (IModule module);
    public void loadModulesFromFile (File moduleFile) throws IOException;
    public void loadModulesFromDir (File moduleDir) throws IOException;
    public void loadAndStart (File moduleDir);
    public void shutdownAllModules ();
    public void startModule (String name);
    public void startAllModules ();
    public List<ModuleInfo> listAllModuleInfo ();
    public IModule getModule (String name) throws ModuleNotFoundException;

}
