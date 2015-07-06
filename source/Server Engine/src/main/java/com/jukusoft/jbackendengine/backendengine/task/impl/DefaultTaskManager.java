package com.jukusoft.jbackendengine.backendengine.task.impl;

import com.jukusoft.jbackendengine.backendengine.task.ITaskManager;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Justin on 02.07.2015.
 */
public class DefaultTaskManager implements ITaskManager {

    private ThreadPoolExecutor executorService = null;
    private ScheduledExecutorService scheduledExecutorService = null;
    private int corePoolSize = 0;
    private int taskCounter = 0;

    public DefaultTaskManager (int corePoolSize, int maxPoolSize, int scheduledExecutorServiceThreads) {
        this.corePoolSize = corePoolSize;
        this.executorService = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 1000 * 60, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        this.scheduledExecutorService = Executors.newScheduledThreadPool(scheduledExecutorServiceThreads);
    }

    @Override
    public void executeAsyncTask(AsyncTask task) {
        Future future = this.executorService.submit(task);
        this.taskCounter++;
    }

    @Override
    public <T> Future<T> submitAndWaitWhileWorking(Callable<T> callable) {
        return this.executorService.submit(callable);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> collection) throws InterruptedException {
        return this.executorService.invokeAll(collection);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> collection) throws ExecutionException, InterruptedException {
        return this.executorService.invokeAny(collection);
    }

    @Override
    public <T> ScheduledFuture<T> scheduleWithDelay(Callable<T> callable, Long delay, TimeUnit timeUnit) {
        return this.scheduledExecutorService.schedule(callable, delay, timeUnit);
    }

    @Override
    public ScheduledFuture schedulePeriodically(Runnable runnable, Long initialDelay, Long period, TimeUnit timeUnit) {
        return this.scheduledExecutorService.scheduleAtFixedRate(runnable, initialDelay, period, timeUnit);
    }

    @Override
    public ScheduledFuture schedulePeriodically(Runnable runnable, Long delay, TimeUnit timeUnit) {
        return this.scheduledExecutorService.scheduleAtFixedRate(runnable, 0l, delay, timeUnit);
    }

    @Override
    public int threadPoolSize() {
        return this.executorService.getActiveCount();
    }

    @Override
    public void shutdown() {
        // http://www.vogella.com/tutorials/JavaConcurrency/article.html
        // This will make the executor accept no new threads
        // and finish all existing threads in the queue
        this.executorService.shutdown();
    }

    @Override
    public void terminate() {
        try {
            // Wait until all threads are finish
            this.executorService.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
