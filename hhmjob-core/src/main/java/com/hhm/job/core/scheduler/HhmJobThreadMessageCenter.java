package com.hhm.job.core.scheduler;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author huanghm
 * @Date 2023/1/17
 */
public class HhmJobThreadMessageCenter {
    // 记录任务当前执行的线程信息。用于打印日志
    private static final Map<String, String> taskAndThreadNameMap = new HashMap<>();

    public static Map<String, String> getTaskAndThreadNameMap(){
        return taskAndThreadNameMap;
    }
}
