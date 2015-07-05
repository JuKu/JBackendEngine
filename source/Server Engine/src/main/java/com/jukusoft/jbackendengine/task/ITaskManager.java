package com.jukusoft.jbackendengine.task;

import com.jukusoft.jbackendengine.task.impl.AsyncTask;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Justin on 02.07.2015.
 */
public interface ITaskManager {

    public void executeAsyncTask (AsyncTask task);
    public <T> Future<T> submitAndWaitWhileWorking (Callable<T> callable);
    public <T> List<Future<T>> invokeAll (Collection<? extends Callable<T>> collection) throws InterruptedException;
    public <T> T invokeAny (Collection<? extends Callable<T>> collection) throws ExecutionException, InterruptedException;
    public <T> ScheduledFuture<T> scheduleWithDelay (Callable<T> callable, Long delay, TimeUnit timeUnit);
    public ScheduledFuture schedulePeriodically (Runnable runnable, Long initialDelay, Long period, TimeUnit timeUnit);
    public ScheduledFuture schedulePeriodically (Runnable runnable, Long delay, TimeUnit timeUnit);
    public int threadPoolSize ();
    public void shutdown ();
    public void terminate ();

}
