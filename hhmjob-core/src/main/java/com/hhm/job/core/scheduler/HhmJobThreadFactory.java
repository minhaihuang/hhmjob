package com.hhm.job.core.scheduler;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author huanghm
 * @Date 2023/1/17
 */
public class HhmJobThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNum = new AtomicInteger(1);
    private final AtomicInteger threadNum = new AtomicInteger(1);
    private final boolean daemoThread;
    private final ThreadGroup threadGroup;
    private final String prefix;

    public HhmJobThreadFactory() {
        this("hhmjob-threadpool-" + poolNum.getAndIncrement(), false);
    }

    public HhmJobThreadFactory(String prefix, boolean daemo) {
        this.prefix = prefix != null ? prefix + "-thread-" : "";
        daemoThread = daemo;
        SecurityManager s = System.getSecurityManager();
        threadGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }


    @Override
    public Thread newThread(Runnable r) {
        String name = prefix + threadNum.getAndIncrement();
        Thread ret = new Thread(threadGroup, r, name);
        ret.setDaemon(daemoThread);
        return ret;
    }
}
