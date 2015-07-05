package com.jukusoft.jbackendengine.module;

import com.jukusoft.jbackendengine.module.exception.InvalidModuleException;

import java.lang.annotation.Annotation;

/**
 * Created by Justin on 03.07.2015.
 */
public class ModuleUtils {

    public static boolean containsModuleInfo (IModule module) {
        return module.getClass().isAnnotationPresent(ModuleInfo.class);
    }

    public static boolean containsModuleInfo (Class<?> cls) {
        return cls.isAnnotationPresent(ModuleInfo.class);
    }

    public static ModuleInfo getModuleInfo (IModule module) throws InvalidModuleException {
        if (module.getClass().isAnnotationPresent(ModuleInfo.class)) {
            Annotation annotation = module.getClass().getAnnotation(ModuleInfo.class);
            ModuleInfo moduleInfo = (ModuleInfo) annotation;

            return moduleInfo;
        } else {
            throw new InvalidModuleException("module " + module.getClass().getName() + " isnt valide.");
        }
    }

}
