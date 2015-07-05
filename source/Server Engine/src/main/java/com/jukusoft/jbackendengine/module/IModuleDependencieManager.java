package com.jukusoft.jbackendengine.module;

import com.jukusoft.jbackendengine.manager.IManager;
import com.jukusoft.jbackendengine.module.exception.InvalidModuleException;

import java.util.List;
import java.util.Map;

/**
 * Created by Justin on 03.07.2015.
 */
public interface IModuleDependencieManager extends IManager {

    public void addDependencie (String module, ModuleDependencie moduleDependencie);
    public void removeDependencie (String module, ModuleDependencie moduleDependencie);
    public List<ModuleDependencie> getAllDependenciesForModule (String module);
    public void insert (IModule module) throws InvalidModuleException;
    public void setBackendEngineVersion (String version);
    public boolean checkDependencies (IModule module) throws InvalidModuleException;
    public Map<IModule,Boolean> process (List<IModule> moduleList);
    public boolean process (IModule module) throws InvalidModuleException;

}
