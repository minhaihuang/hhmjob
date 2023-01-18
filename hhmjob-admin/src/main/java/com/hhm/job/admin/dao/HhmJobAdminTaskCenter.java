package com.hhm.job.admin.dao;

import com.hhm.job.admin.dto.TaskAdminDto;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author huanghm
 * @Date 2023/1/11
 */
public class HhmJobAdminTaskCenter {
    private final static Map<String, TaskAdminDto> taskDtoMap = new HashMap<>();
    public static void addTaskAdmin(TaskAdminDto taskAdminDto){
        String key = taskAdminDto.getTaskClass() ;

        if (taskDtoMap.containsKey(key)) {
             throw new RuntimeException("已存在该任务");
        }
        taskAdminDto.setId(System.currentTimeMillis());
        taskAdminDto.setStatus(0);
        taskDtoMap.put(key, taskAdminDto);
    }

    public static void editTaskAdmin(TaskAdminDto taskAdminDto){
        String key = taskAdminDto.getTaskClass() ;

        final TaskAdminDto dto = taskDtoMap.get(key);
        dto.setCron(taskAdminDto.getCron());
        dto.setTaskName(taskAdminDto.getTaskName());
        dto.setTaskNum(taskAdminDto.getTaskNum());

        taskDtoMap.put(key, dto);
    }

    public static void deleteTaskAdmin(TaskAdminDto taskAdminDto){
        String key = taskAdminDto.getTaskClass();
        final TaskAdminDto t = taskDtoMap.get(key);
        if(t.getStatus() == 1){
            throw new RuntimeException("任务运行中，不允许删除");
        }
        taskDtoMap.remove(key);
    }

    public static Map<String, TaskAdminDto> getTaskDtoMap() {
        return taskDtoMap;
    }
}
