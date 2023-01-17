package com.hhm.job.core.scheduler;

import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.scheduling.support.TaskUtils;
import org.springframework.util.ErrorHandler;
import org.springframework.util.concurrent.ListenableFuture;

import java.time.Clock;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * @Author huanghm
 * @Date 2022/5/12
 */
public class HhmJobCustomTaskScheduler extends ThreadPoolTaskScheduler {
    private Map<Object, ScheduledFuture<?>> scheduledTasks = new IdentityHashMap<>();
    public HhmJobCustomTaskScheduler() {
        super();
        super.initialize();
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable task, Trigger trigger) {
//        ScheduledFuture<?> future = super.schedule(task, trigger);
        ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(1,new HhmJobThreadFactory());
        ScheduledFuture<?> future;
        try {
            Clock clock = Clock.systemDefaultZone();
            ErrorHandler errorHandler = TaskUtils.getDefaultErrorHandler(true);
            future = new HhmJobRescheduleRunnable(task, trigger, clock, executor, errorHandler).schedule();
        }
        catch (RejectedExecutionException ex) {
            throw new TaskRejectedException("Executor [" + executor + "] did not accept task: " + task, ex);
        }
        this.putScheduledTasks(task, future);
        return future;
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
        ScheduledFuture<?> future = super.scheduleAtFixedRate(task, period);
        this.putScheduledTasks(task, future);
        return future;
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
        ScheduledFuture<?> future = super.scheduleAtFixedRate(task, startTime, period);
        this.putScheduledTasks(task, future);
        return future;
    }

    private void putScheduledTasks(Runnable task, ScheduledFuture<?> future) {
        ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) task;
        scheduledTasks.put(runnable.getTarget(), future);
    }


    @Override
    public void setPoolSize(int poolSize) {
        super.setPoolSize(poolSize);
    }

    @Override
    public void setRemoveOnCancelPolicy(boolean flag) {
        super.setRemoveOnCancelPolicy(flag);
    }

    @Override
    public void setContinueExistingPeriodicTasksAfterShutdownPolicy(boolean flag) {
        super.setContinueExistingPeriodicTasksAfterShutdownPolicy(flag);
    }

    @Override
    public void setExecuteExistingDelayedTasksAfterShutdownPolicy(boolean flag) {
        super.setExecuteExistingDelayedTasksAfterShutdownPolicy(flag);
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {
        super.setErrorHandler(errorHandler);
    }

    @Override
    public void setClock(Clock clock) {
        super.setClock(clock);
    }

    @Override
    public Clock getClock() {
        return super.getClock();
    }

    @Override
    protected ExecutorService initializeExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        return super.initializeExecutor(threadFactory, rejectedExecutionHandler);
    }

    @Override
    protected ScheduledExecutorService createExecutor(int poolSize, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {
        return super.createExecutor(poolSize, threadFactory, rejectedExecutionHandler);
    }

    @Override
    public ScheduledExecutorService getScheduledExecutor() throws IllegalStateException {
        return super.getScheduledExecutor();
    }

    @Override
    public ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor() throws IllegalStateException {
        return super.getScheduledThreadPoolExecutor();
    }

    @Override
    public int getPoolSize() {
        return super.getPoolSize();
    }

    @Override
    public int getActiveCount() {
        return super.getActiveCount();
    }

    @Override
    public boolean isRemoveOnCancelPolicy() {
        return super.isRemoveOnCancelPolicy();
    }

    @Override
    public void execute(Runnable task) {
        super.execute(task);
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        super.execute(task, startTimeout);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(task);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(task);
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task) {
        return super.submitListenable(task);
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task) {
        return super.submitListenable(task);
    }

    @Override
    protected void cancelRemainingTask(Runnable task) {
        super.cancelRemainingTask(task);
    }

    @Override
    public ScheduledFuture<?> schedule(Runnable task, Date startTime) {
        return super.schedule(task, startTime);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Date startTime, long delay) {
        return super.scheduleWithFixedDelay(task, startTime, delay);
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long delay) {
        return super.scheduleWithFixedDelay(task, delay);
    }

    public Map<Object, ScheduledFuture<?>> getScheduledTasks() {
        return scheduledTasks;
    }
}
