package com.jukusoft.jbackendengine.backendengine.task;

import java.util.Map;

/**
 * Created by Justin on 02.07.2015.
 */
public interface ITask extends Runnable {

    public void doInBackground (Object... objects);
    public void onComplete (Map<String,Object> result);

}
