package com.jukusoft.jbackendengine.module;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Justin on 03.07.2015.
 */
public interface IModuleLoader {

    public List<IModule> loadModulesFromDir (File modulesDir) throws IOException;
    public List<IModule> loadModulesFromFile (File moduleFile) throws IOException;

}
