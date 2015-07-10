package com.jukusoft.jbackendengine.example.taskmanager;

import com.jukusoft.jbackendengine.backendengine.IEditableBackendEngine;
import com.jukusoft.jbackendengine.backendengine.factory.BackendEngineFactory;
import com.jukusoft.jbackendengine.backendengine.task.ITaskManager;
import com.jukusoft.jbackendengine.backendengine.task.impl.AsyncTask;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

/**
 * Created by Justin on 10.07.2015.
 */
public class ExampleMain {

    public static void main (String[] args) {
        //create new instance of backend engine
        final IEditableBackendEngine backendEngine = BackendEngineFactory.createNewDefaultBackendEngine();

        //get task manager
        ITaskManager taskManager = backendEngine.getTaskManager();

        Runnable runnable = new Runnable() {
            public void run() {
                backendEngine.getLoggerManager().debug("Runnable runs asynchronous");
            }
        };

        /*
        * JBackendEngine TaskManager is using ExecutorService and so on for better performance
        */

        //run runnable asynchronous in background
        taskManager.executeAsyncTask(runnable);
        taskManager.executeAsyncTask(runnable);
        taskManager.executeAsyncTask(runnable);



        /*
        * a simple performance benchmark with AsyncTask and a normal Thread, which is executed without TaskManager
        * AsyncTask is like a Runnable,
        * but AsyncTask supports methods like onComplete for results
        */

        final Calendar calendar = new GregorianCalendar();
        final Long startTime = calendar.getTimeInMillis();

        AsyncTask asyncTask = new AsyncTask() {
            @Override
            public void doInBackground(Object... objects) {
                for (int i = 0; i < 10000000; i++) {
                    //Do something
                }
            }

            @Override
            public void onComplete(Map<String, Object> map) {
                Long endTime = calendar.getTimeInMillis();
                backendEngine.getLoggerManager().debug("AsyncTask needs " + (endTime - startTime) + ".");
            }
        };

        final Long startTime2 = calendar.getTimeInMillis();
        Runnable runnable1 = new Runnable() {
            public void run() {
                for (int i = 0; i < 10000000; i++) {
                    //Do something
                }

                Long endTime = calendar.getTimeInMillis();
                backendEngine.getLoggerManager().debug("Runnable needs " + (endTime - startTime2) + ".");
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

        //if you dont use backend engine anymore, you can shutdown backend engine
        backendEngine.shutdown();
    }

}
