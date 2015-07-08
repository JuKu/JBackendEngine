package com.jukusoft.jbackendengine.example.asynctask;

import com.jukusoft.jbackendengine.backendengine.IEditableBackendEngine;
import com.jukusoft.jbackendengine.backendengine.factory.BackendEngineFactory;
import com.jukusoft.jbackendengine.backendengine.task.impl.AsyncTask;

import java.util.Map;

/**
 * Created by Justin on 08.07.2015.
 */
public class ExampleMain {

    public static void main (String[] args) {
        //create new backend engine with BackendEngineFactory
        final IEditableBackendEngine backendEngine = BackendEngineFactory.createNewDefaultBackendEngine();

        /*
        * IEditableBackendEngine extends IBackendEngine
        * the interface IEditableBackendEngine allows you to replace the main components and modules from the engine,
        * with IBackendEngine you can only add modules or use them.
        */

        //create new AsyncTask
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            public void doInBackground(Object... objects) {
                System.out.println("Task scheduled.");
            }

            @Override
            public void onComplete(Map<String, Object> map) {
                System.out.println("Task completed.");

                //after task complete you can shutdown the engine
                backendEngine.shutdown();
            }
        };

        //add asynctask to task manager queue
        backendEngine.getTaskManager().executeAsyncTask(asyncTask);
    }

}
