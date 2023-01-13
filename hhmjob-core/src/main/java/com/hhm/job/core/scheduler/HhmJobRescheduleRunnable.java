package com.hhm.job.core.scheduler;

import org.springframework.lang.Nullable;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.support.DelegatingErrorHandlingRunnable;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.util.Assert;
import org.springframework.util.ErrorHandler;

import java.time.Clock;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @Author huanghm
 * @Date 2022/5/24
 */
public class HhmJobRescheduleRunnable extends DelegatingErrorHandlingRunnable implements ScheduledFuture<Object>{
    private Trigger trigger;
    private SimpleTriggerContext triggerContext;
    private ScheduledExecutorService executor;
    @Nullable
    private ScheduledFuture<?> currentFuture;
    @Nullable
    private Date scheduledExecutionTime;
    //private final Object triggerContextMonitor = new Object();

    public HhmJobRescheduleRunnable(Runnable delegate, Trigger trigger, Clock clock, ScheduledExecutorService executor, ErrorHandler errorHandler) {
        super(delegate, errorHandler);
        this.trigger = trigger;
        this.triggerContext = new SimpleTriggerContext(clock);
        this.executor = executor;
    }

    @Nullable
    public ScheduledFuture<?> schedule() {
//        synchronized(this.triggerContextMonitor) {
//            this.scheduledExecutionTime = this.trigger.nextExecutionTime(this.triggerContext);
//            if (this.scheduledExecutionTime == null) {
//                return null;
//            } else {
//                long initialDelay = this.scheduledExecutionTime.getTime() - this.triggerContext.getClock().millis();
//                this.currentFuture = this.executor.schedule(this, initialDelay, TimeUnit.MILLISECONDS);
//                return this;
//            }
//        }
        this.scheduledExecutionTime = this.trigger.nextExecutionTime(this.triggerContext);
        if (this.scheduledExecutionTime == null) {
            return null;
        } else {
            long initialDelay = this.scheduledExecutionTime.getTime() - this.triggerContext.getClock().millis();
            this.currentFuture = this.executor.schedule(this, initialDelay, TimeUnit.MILLISECONDS);
            return this;
        }
    }

    private ScheduledFuture<?> obtainCurrentFuture() {
        Assert.state(this.currentFuture != null, "No scheduled future");
        return this.currentFuture;
    }

    public void run() {
        Date actualExecutionTime = new Date(this.triggerContext.getClock().millis());
        super.run();
        Date completionTime = new Date(this.triggerContext.getClock().millis());
//        synchronized(this.triggerContextMonitor) {
//            Assert.state(this.scheduledExecutionTime != null, "No scheduled execution");
//            this.triggerContext.update(this.scheduledExecutionTime, actualExecutionTime, completionTime);
//            if (!this.obtainCurrentFuture().isCancelled()) {
//                this.schedule();
//            }
//        }
        Assert.state(this.scheduledExecutionTime != null, "No scheduled execution");
        this.triggerContext.update(this.scheduledExecutionTime, actualExecutionTime, completionTime);
        if (!this.obtainCurrentFuture().isCancelled()) {
            this.schedule();
        }
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
//        synchronized(this.triggerContextMonitor) {
//            return this.obtainCurrentFuture().cancel(mayInterruptIfRunning);
//        }
        return this.obtainCurrentFuture().cancel(mayInterruptIfRunning);
    }

    public boolean isCancelled() {
//        synchronized(this.triggerContextMonitor) {
//            return this.obtainCurrentFuture().isCancelled();
//        }
        return this.obtainCurrentFuture().isCancelled();
    }

    public boolean isDone() {
//        synchronized(this.triggerContextMonitor) {
//            return this.obtainCurrentFuture().isDone();
//        }
        return this.obtainCurrentFuture().isDone();
    }

    public Object get() throws InterruptedException, ExecutionException {
        ScheduledFuture curr;
//        synchronized(this.triggerContextMonitor) {
//            curr = this.obtainCurrentFuture();
//        }
        curr = this.obtainCurrentFuture();
        return curr.get();
    }

    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        ScheduledFuture curr;
//        synchronized(this.triggerContextMonitor) {
//            curr = this.obtainCurrentFuture();
//        }
        curr = this.obtainCurrentFuture();
        return curr.get(timeout, unit);
    }

    public long getDelay(TimeUnit unit) {
        ScheduledFuture curr;
//        synchronized(this.triggerContextMonitor) {
//            curr = this.obtainCurrentFuture();
//        }
        curr = this.obtainCurrentFuture();
        return curr.getDelay(unit);
    }

    public int compareTo(Delayed other) {
        if (this == other) {
            return 0;
        } else {
            long diff = this.getDelay(TimeUnit.MILLISECONDS) - other.getDelay(TimeUnit.MILLISECONDS);
            return diff == 0L ? 0 : (diff < 0L ? -1 : 1);
        }
    }
}
