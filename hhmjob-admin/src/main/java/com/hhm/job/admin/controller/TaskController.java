package com.hhm.job.admin.controller;

import com.alibaba.fastjson.JSON;
import com.hhm.job.admin.dao.HhmJobAdminTaskCenter;
import com.hhm.job.admin.dao.HhmJobRegisteredTaskCenter;
import com.hhm.job.admin.dto.OperateRemoteTaskDto;
import com.hhm.job.admin.dto.Response;
import com.hhm.job.admin.dto.TaskAdminDto;
import com.hhm.job.admin.dto.TaskRegisterDto;
import com.hhm.job.admin.dto.TaskRegisterMessageDto;
import com.hhm.job.admin.util.OkHttpUtil;
import com.hhm.job.admin.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务controller
 * @Author: huanghm
 * @Date: 2022/05/14
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/task")
public class TaskController {
    // private final Map<String, TaskAdminDto> taskDtoMap = new HashMap<>();

    @GetMapping("/list")
    public Response<List<TaskAdminDto>> taskList(){
        List<TaskAdminDto> taskList = new ArrayList<>();
        HhmJobAdminTaskCenter.getTaskDtoMap().values().forEach(e -> {
            TaskAdminDto taskAdminDto = new TaskAdminDto();
            taskAdminDto.setTaskName(e.getTaskName());
            taskAdminDto.setTaskClass(e.getTaskClass());
            taskAdminDto.setCron(e.getCron());
            taskAdminDto.setStatus(e.getStatus());
            taskAdminDto.setTaskNum(e.getTaskNum());
            taskList.add(taskAdminDto);
        });
        return ResponseUtil.success(taskList);
    }

    /**
     * 注册任务
     * @param taskRegisterDto
     * @return
     */
    @PostMapping("/register")
    public Response<String> registerJob(@RequestBody TaskRegisterDto taskRegisterDto){
        String ip = taskRegisterDto.getIp();
        int port = taskRegisterDto.getPort();
        final List<String> taskClassList = taskRegisterDto.getTaskClassList();
        for(String taskClass: taskClassList) {
            TaskRegisterMessageDto taskRegisterMessageDto = new TaskRegisterMessageDto();
            taskRegisterMessageDto.setIp(ip);
            taskRegisterMessageDto.setPort(port);
            taskRegisterMessageDto.setTaskClass(taskClass);
            taskRegisterMessageDto.setLastHealCheckTime(System.currentTimeMillis());
            HhmJobRegisteredTaskCenter.putRegisterTask(taskClass, taskRegisterMessageDto);
        }
        // 测试方法开始
//        TaskDto taskDto = new TaskDto();
//        taskDto.setTaskClass("com.hhm.job.service.task.AutoStopTask");
//        taskDto.setCron("*/3 * * * * ?");
//        taskDto.setTaskName("test1");
//
//        addJob(taskDto);
        // 测试方法结束
        return ResponseUtil.success("success");
    }

    @PostMapping("/add")
    public Response<String> addJob(@RequestBody TaskAdminDto taskAdminDto){
        HhmJobAdminTaskCenter.addTaskAdmin(taskAdminDto);
        return ResponseUtil.success("success");
    }

    @PostMapping("/edit")
    public Response<String> edit(@RequestBody TaskAdminDto taskAdminDto){
        HhmJobAdminTaskCenter.editTaskAdmin(taskAdminDto);
        return ResponseUtil.success("success");
    }

    @PostMapping("/delete")
    public Response<String> delete(@RequestBody TaskAdminDto taskAdminDto){
        HhmJobAdminTaskCenter.deleteTaskAdmin(taskAdminDto);
        return ResponseUtil.success("success");
    }

    @ResponseBody
    @PostMapping("/operate")
    public Response<String> operateJob(@RequestBody TaskAdminDto taskAdminDto){
        final List<TaskRegisterMessageDto> taskList = HhmJobRegisteredTaskCenter.getTaskList(taskAdminDto.getTaskClass());
        if(taskList == null || taskList.isEmpty()){
            return ResponseUtil.fail("不存在该任务");
        }
        final TaskAdminDto taskAdminDtoInMap = HhmJobAdminTaskCenter.getTaskDtoMap().get(taskAdminDto.getTaskClass());
        taskAdminDtoInMap.setStatus(taskAdminDtoInMap.getStatus() == 0? 1: 0);

//        OperateRemoteTaskDto operateRemoteTaskDto = new OperateRemoteTaskDto();
//        operateRemoteTaskDto.setTaskClass(taskAdminDtoInMap.getTaskClass());
//        operateRemoteTaskDto.setCron(taskAdminDtoInMap.getCron());
//        operateRemoteTaskDto.setStatus(taskAdminDtoInMap.getStatus());
//
//        try {
//            TaskRegisterMessageDto taskRegisterMessageDto = taskList.get(0);
//            String url = "http://" + taskRegisterMessageDto.getIp() + ":" + taskRegisterMessageDto.getPort() + "/hhmjob/operate";
//            final String post = OkHttpUtil.post(url, JSON.toJSONString(operateRemoteTaskDto));
//            taskRegisterMessageDto.setStatus(taskAdminDtoInMap.getStatus());
//            log.info(post);
//        }catch (Exception e){
//            log.error(e.getMessage(),e);
//            return ResponseUtil.fail(e.getMessage());
//        }
        return ResponseUtil.success("success");
    }

}
