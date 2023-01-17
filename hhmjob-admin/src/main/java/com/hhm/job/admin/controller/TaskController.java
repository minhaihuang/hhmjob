package com.hhm.job.admin.controller;

import com.hhm.job.admin.dao.HhmJobAdminTaskCenter;
import com.hhm.job.admin.dao.HhmJobRegisteredTaskCenter;
import com.hhm.job.admin.dto.Response;
import com.hhm.job.admin.dto.StartOrStopLogDto;
import com.hhm.job.admin.dto.TaskAdminDto;
import com.hhm.job.admin.dto.TaskAdminTreeVo;
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
import java.util.Optional;

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

    @PostMapping("/startGetLog")
    public Response<Boolean> startGetLog(@RequestBody StartOrStopLogDto startOrStopLogDto){
        try {
            String url = "http://" + startOrStopLogDto.getIp() + ":" + startOrStopLogDto.getPort() + "/hhmjob/startGetLog" + "?taskClass=" + startOrStopLogDto.getTaskClass();
            OkHttpUtil.get(url);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return ResponseUtil.success(true);
    }

    @GetMapping("/list")
    public Response<List<TaskAdminTreeVo>> taskList(){

        List<TaskAdminTreeVo> taskList = new ArrayList<>();
        HhmJobAdminTaskCenter.getTaskDtoMap().values().forEach(e -> {
            TaskAdminTreeVo taskAdminTreeVo = new TaskAdminTreeVo();
            taskAdminTreeVo.setTaskName(e.getTaskName());
            taskAdminTreeVo.setTaskClass(e.getTaskClass());
            taskAdminTreeVo.setCron(e.getCron());
            taskAdminTreeVo.setStatus(e.getStatus());
            taskAdminTreeVo.setTaskNum(e.getTaskNum());
            taskAdminTreeVo.setId(e.getId());

            final List<TaskRegisterMessageDto> rList = Optional.ofNullable(HhmJobRegisteredTaskCenter.getTaskList(e.getTaskClass())).orElse(new ArrayList<>());
            List<TaskAdminTreeVo> children = new ArrayList<>();
            rList.forEach( r -> {
                TaskAdminTreeVo t = new TaskAdminTreeVo();
                t.setTaskName(r.getIp()+":" + r.getPort());
                t.setTaskClass(r.getTaskClass());
                t.setCron(taskAdminTreeVo.getCron());
                t.setStatus(r.getStatus());
                t.setIsChildren(true);
                t.setId(r.getId());
                children.add(t);
            });
            taskAdminTreeVo.setChildren(children);

            taskList.add(taskAdminTreeVo);
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

        return ResponseUtil.success("success");
    }

}
