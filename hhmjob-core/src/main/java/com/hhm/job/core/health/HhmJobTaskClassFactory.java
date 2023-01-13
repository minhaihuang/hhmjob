package com.hhm.job.core.health;


import com.hhm.job.core.dto.TargetAndSchedulerDto;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author huanghm
 * @Date 2023/1/4
 */
public class HhmJobTaskClassFactory {
    private static Map<String, TargetAndSchedulerDto> taskMap = new HashMap<>();

    public static void putTask(String taskClass) {
        TargetAndSchedulerDto targetAndSchedulerDto = new TargetAndSchedulerDto();
        targetAndSchedulerDto.setTaskClass(taskClass);
        taskMap.put(taskClass, targetAndSchedulerDto);
    }

    public static TargetAndSchedulerDto getTask(String taskClass) {
        if(!taskMap.containsKey(taskClass)) {
            throw new RuntimeException("不存在该任务");
        }
        return taskMap.get(taskClass);
    }

    public static Map<String, TargetAndSchedulerDto> getTaskMap() {
        return taskMap;
    }
}
