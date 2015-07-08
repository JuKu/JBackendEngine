package com.jukusoft.jbackendengine.backendengine.module.impl;

import com.jukusoft.jbackendengine.backendengine.module.IModule;
import com.jukusoft.jbackendengine.backendengine.module.IModuleLoader;
import com.jukusoft.jbackendengine.backendengine.module.ModuleUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Created by Justin on 03.07.2015.
 */
public class DefaultModuleLoader implements IModuleLoader {

    @Override
    public List<IModule> loadModulesFromDir(File modulesDir) throws IOException {
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar") || name.endsWith(".jam");
            }
        };

        if (!modulesDir.exists()) {
            System.out.println("directory modules doesnt exists, creating directory modules.");
            modulesDir.mkdirs();
        }

        File[] files = modulesDir.listFiles(filenameFilter);

        System.out.println("" + files.length + " .jar files in directory " + modulesDir.getAbsolutePath() + " found.");

        ClassLoader classLoader = new URLClassLoader(fileArrayToURLs(files));
        List<Class<IModule>> moduleClasses = extractClassesFromJARs(files, classLoader);
        return createModuleObjects(moduleClasses);
    }

    @Override
    public List<IModule> loadModulesFromFile(File moduleFile) throws IOException {
        ClassLoader classLoader = new URLClassLoader(fileToURL(moduleFile));
        List<Class<IModule>> moduleClasses = extractClassesFromJAR(moduleFile, classLoader);
        return createModuleObjects(moduleClasses);
    }

    private URL[] fileArrayToURLs (File[] files) throws MalformedURLException {

        URL[] urlsArray = new URL[files.length];

        for (int i = 0; i < files.length; i++) {
            urlsArray[i] = files[i].toURI().toURL();
        }

        return urlsArray;
    }

    private URL[] fileToURL (File file) throws MalformedURLException {
        URL[] urlsArray = new URL[1];
        urlsArray[0] = file.toURI().toURL();
        return urlsArray;
    }

    public boolean isModuleClass (Class<?> cls) {
        for (Class<?> cls1 : cls.getInterfaces()) {
            if ((cls1.equals(IModule.class) || cls1.equals(Module.class)) && ModuleUtils.containsModuleInfo(cls1)) {
                return true;
            }
        }

        return false;
    }

    public IModule createModuleObject (Class<IModule> moduleClass) throws IllegalAccessException, InstantiationException {
        return moduleClass.newInstance();
    }

    private List<IModule> createModuleObjects(List<Class<IModule>> classList) {

        List<IModule> modules = new ArrayList<IModule>(classList.size());

        for (Class<IModule> module : classList) {
            try {
                modules.add(module.newInstance());
            } catch (InstantiationException e) {
                System.err.println("Cannot create new instance of class " + module.getName() + ".");
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                System.err.println("IllegalAccess for module: " + module.getName() + ".");
                e.printStackTrace();
            }
        }

        return modules;
    }

    private List<Class<IModule>> extractClassesFromJARs(File[] jars, ClassLoader cl) throws IOException {

        List<Class<IModule>> classList = new ArrayList<Class<IModule>>();

        for (File jar : jars) {
            classList.addAll(extractClassesFromJAR(jar, cl));
        }

        return classList;
    }

    private List<Class<IModule>> extractClassesFromJAR(File jarFile, ClassLoader classLoader) throws IOException {

        List<Class<IModule>> classList = new ArrayList<Class<IModule>>();

        JarInputStream jarInputStream = new JarInputStream(new FileInputStream(jarFile));
        JarEntry ent = null;

        while ((ent = jarInputStream.getNextJarEntry()) != null) {
            if (ent.getName().toLowerCase().endsWith(".class")) {
                try {
                    Class<?> cls = classLoader.loadClass(ent.getName().substring(0, ent.getName().length() - 6).replace('/', '.'));
                    if (isModuleClass(cls)) {
                        classList.add((Class<IModule>) cls);
                    }
                } catch (ClassNotFoundException e) {
                    System.err.println("Cannot load class " + ent.getName() + ".");
                    e.printStackTrace();
                }
            }
        }

        jarInputStream.close();
        return classList;
    }

}
