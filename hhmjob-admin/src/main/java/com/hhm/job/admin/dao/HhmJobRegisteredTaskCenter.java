package com.hhm.job.admin.dao;

import com.alibaba.fastjson.JSON;
import com.hhm.job.admin.dto.TaskHealthCheckDto;
import com.hhm.job.admin.dto.TaskRegisterMessageDto;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 已经注册的任务-的任务中心
 * @Author huanghm
 * @Date 2023/1/11
 */
@Slf4j
public class HhmJobRegisteredTaskCenter {
    private final static Map<String, List<TaskRegisterMessageDto>> registeredTaskMap = new HashMap<>();
    public static Map<String, List<TaskRegisterMessageDto>> getRegisteredTaskMap() {
        return registeredTaskMap;
    }

    public static List<TaskRegisterMessageDto> getTaskList(String taskClass){
        return registeredTaskMap.get(taskClass);
    }

    public static void putRegisterTask(String taskClass, TaskRegisterMessageDto r){
        registeredTaskMap.putIfAbsent(taskClass, new ArrayList<>());
        registeredTaskMap.get(taskClass).removeIf(t ->  t.getIp().equals(r.getIp()) && t.getPort() == r.getPort() && t.getTaskClass().equals(r.getTaskClass()));
        r.setId(r.hashCode());
        registeredTaskMap.get(taskClass).add(r);
    }

    /**
     * 更新上次健康监测时间
     * @param healthCheckDto
     */
    public static void updateLastHealCheckTime(TaskHealthCheckDto healthCheckDto){
        if (healthCheckDto == null || healthCheckDto.getTaskClassList() == null || healthCheckDto.getTaskClassList().isEmpty()){
            return;
        }
        healthCheckDto.getTaskClassList().forEach(taskClass -> {
            final List<TaskRegisterMessageDto> taskRegisterMessageDtos = registeredTaskMap.get(taskClass);
            if(taskRegisterMessageDtos != null && !taskRegisterMessageDtos.isEmpty()){
                taskRegisterMessageDtos.stream()
                        .filter(t -> t.getIp().equals(healthCheckDto.getIp()) && t.getPort() == healthCheckDto.getPort())
                        .findFirst()
                        .ifPresent(target -> target.setLastHealCheckTime(System.currentTimeMillis()));
            }
        });
    }
}
