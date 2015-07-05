package com.jukusoft.jbackendengine.task.impl;

import com.jukusoft.jbackendengine.task.ITask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Justin on 02.07.2015.
 */
public abstract class AsyncTask implements ITask {

    private Object[] objects = null;
    private Map<String,Object> result = new HashMap<String,Object>();

    public AsyncTask (Object... objects) {
        this.objects = objects;
    }

    @Override
    public abstract void doInBackground(Object... objects);

    @Override
    public abstract void onComplete(Map<String,Object> result);

    @Override
    public void run() {
        this.doInBackground(this.objects);
        this.onComplete(this.result);
    }

}
