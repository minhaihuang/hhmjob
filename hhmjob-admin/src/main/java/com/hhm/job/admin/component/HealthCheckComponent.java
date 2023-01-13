package com.hhm.job.admin.component;

import com.alibaba.fastjson.JSON;
import com.hhm.job.admin.dao.HhmJobAdminTaskCenter;
import com.hhm.job.admin.dao.HhmJobRegisteredTaskCenter;
import com.hhm.job.admin.dto.OperateRemoteTaskDto;
import com.hhm.job.admin.dto.TaskAdminDto;
import com.hhm.job.admin.dto.TaskRegisterMessageDto;
import com.hhm.job.admin.util.OkHttpUtil;
import javafx.concurrent.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 健康监测组件
 *
 * @Author huanghm
 * @Date 2023/1/11
 */
@Slf4j
@Component
public class HealthCheckComponent {

    @PostConstruct
    public void init() {
        new Thread(this::healthCheck).start();
    }

    /**
     * 当连续3分钟没有收到心跳时，认为该任务已下线，从任务中心移除
     */
    public void healthCheck(){
        while (true) {
            final Map<String, List<TaskRegisterMessageDto>> registeredTaskMap = HhmJobRegisteredTaskCenter.getRegisteredTaskMap();
            long now = System.currentTimeMillis();
            // 删除已失活任务
            Map<String,Integer> runningTaskNumMap = new HashMap<>();
            try {
                if (!registeredTaskMap.isEmpty()) {
                    for (Map.Entry<String, List<TaskRegisterMessageDto>> entry : registeredTaskMap.entrySet()) {
                        final List<TaskRegisterMessageDto> tList = entry.getValue();
                        if (tList == null || tList.isEmpty()) {
                            continue;
                        }
                        // 把不健康的任务移除注册。若移除的是正在运行的任务，开启另外一个节点去执行任务
                        Iterator<TaskRegisterMessageDto> it = tList.iterator();
                        while (it.hasNext()) {
                            final TaskRegisterMessageDto next = it.next();
                            if (now - next.getLastHealCheckTime() > 1 * 30 * 1000){
                                log.info("任务失活，移除 ==>" + JSON.toJSONString(next));
                                it.remove();
                            }else if(next.getStatus() == 1){
                                runningTaskNumMap.put(entry.getKey(), runningTaskNumMap.get(entry.getKey()) == null? 1 : runningTaskNumMap.get(entry.getKey()) + 1);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("健康监测失败");
                log.error(e.getMessage(), e);
            }

            // 重新开启任务。经过上面的检查，剩下的都是健康的节点
            try {
                final Map<String, TaskAdminDto> taskDtoMap = HhmJobAdminTaskCenter.getTaskDtoMap();
                for (Map.Entry<String, TaskAdminDto> entry : taskDtoMap.entrySet()) {
                    if (registeredTaskMap.get(entry.getKey()) == null || registeredTaskMap.get(entry.getKey()).isEmpty()){
                        continue;
                    }

                    String taskClass = entry.getKey();
                    TaskAdminDto taskAdminDto = entry.getValue();

                    if(taskAdminDto.getStatus() == 0) {
                        // 关闭所有节点
                        stopAllNode(taskClass);
                    }else{
                        // 调整运行中的节点数，使之与页面配置的相同
                        handleRunningNode(runningTaskNumMap, taskClass, taskAdminDto);
                    }

                }
            }catch (Throwable e) {
                log.error(e.getMessage(), e);
            }

            // 休眠10秒
            try {
                Thread.sleep(10 * 1000L);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private void handleRunningNode(Map<String,Integer> runningTaskNumMap, String taskClass, TaskAdminDto taskAdminDto) {
        final Map<String, List<TaskRegisterMessageDto>> registeredTaskMap = HhmJobRegisteredTaskCenter.getRegisteredTaskMap();
        // 某个class正在运行是任务数
        int num = runningTaskNumMap.get(taskClass) == null? 0: runningTaskNumMap.get(taskClass);
        for (TaskRegisterMessageDto taskRegisterMessageDto : registeredTaskMap.get(taskClass)) {
            if(taskAdminDto.getTaskNum() == num){
                continue;
            }
            // 正在运行的节点比设置的节点少，开启新节点执行任务
            if (taskAdminDto.getTaskNum() > num && taskRegisterMessageDto.getStatus() == 0) {
                startOrStopNode(taskRegisterMessageDto, 1);
                num++;
            }
            // 正在运行的节点比设置的节点多，关闭正在运行的节点
            if (taskAdminDto.getTaskNum() < num && taskRegisterMessageDto.getStatus() == 1) {
                startOrStopNode(taskRegisterMessageDto, 0);
                num--;
            }
        }
    }

    private void stopAllNode(String taskClass) {
        final Map<String, List<TaskRegisterMessageDto>> registeredTaskMap = HhmJobRegisteredTaskCenter.getRegisteredTaskMap();
        for (TaskRegisterMessageDto taskRegisterMessageDto : registeredTaskMap.get(taskClass)) {
            if (taskRegisterMessageDto.getStatus() == 1) {
                startOrStopNode(taskRegisterMessageDto, 0);
            }
        }
    }


    public void startOrStopNode(TaskRegisterMessageDto taskRegisterMessageDto, int status){

        TaskAdminDto taskAdminDto = HhmJobAdminTaskCenter.getTaskDtoMap().get(taskRegisterMessageDto.getTaskClass());
        OperateRemoteTaskDto operateRemoteTaskDto = new OperateRemoteTaskDto();
        operateRemoteTaskDto.setTaskClass(taskAdminDto.getTaskClass());
        operateRemoteTaskDto.setCron(taskAdminDto.getCron());
        operateRemoteTaskDto.setStatus(status);

        try {
            String url = "http://" + taskRegisterMessageDto.getIp() + ":" + taskRegisterMessageDto.getPort() + "/hhmjob/operate";
            final String post = OkHttpUtil.post(url, JSON.toJSONString(operateRemoteTaskDto));
            log.info(post);
            taskRegisterMessageDto.setStatus(status);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }
}
