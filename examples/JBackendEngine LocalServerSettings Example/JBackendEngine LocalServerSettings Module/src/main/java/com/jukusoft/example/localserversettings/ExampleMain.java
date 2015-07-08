package com.jukusoft.example.localserversettings;

import com.jukusoft.jbackendengine.backendengine.IEditableBackendEngine;
import com.jukusoft.jbackendengine.backendengine.factory.BackendEngineFactory;
import com.jukusoft.jbackendengine.backendengine.impl.DefaultBackendEngine;

import java.io.File;

/**
 * Created by Justin on 08.07.2015.
 */
public class ExampleMain {

    public static void main (String[] args) {
        //create new backend engine with BackendEngineFactory
        IEditableBackendEngine backendEngine = BackendEngineFactory.createNewDefaultBackendEngine();

        //The BackendEngineFactory creates a default backend engine like this code
        //IEditableBackendEngine backendEngine2 = new DefaultBackendEngine(new File("./config"));

        //if you use sections, you can get the key with section.key
        String value1 = backendEngine.getLocalSettings().getSetting("Section1.key1");
        System.out.println("Section: Section1, key: key1, value: " + value1 + ".");

        String value2 = backendEngine.getLocalSettings().getSetting("Section2.key2");
        System.out.println("Section: Section2, key: key2, value: " + value2 + ".");

        //you can also use the getSettingOrDefaultValue method to set a default value
        String value3 = backendEngine.getLocalSettings().getSettingOrDefaultValue("Section3.key3", "default value");

        //Output: Section: Section3, key: key3, value: default value
        //this method returns "default value", because Section3.key3 doesnt exists in a configuration file in the ./config directory
        System.out.println("Section: Section3, key: key3, value: " + value3 + ".");

        //you can shutdown the backend engine, if you dont use them anymore
        backendEngine.shutdown();
    }

}
