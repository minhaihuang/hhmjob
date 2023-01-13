package com.hhm.job.core.controller;

import com.hhm.job.core.dto.OperateTaskDto;
import com.hhm.job.core.dto.TargetAndSchedulerDto;
import com.hhm.job.core.health.HhmJobHealthCheckComponent;
import com.hhm.job.core.health.HhmJobTaskClassFactory;
import com.hhm.job.core.scheduler.HhmJobCustomTaskScheduler;
import com.hhm.job.core.util.SpringBeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;

/**
 * @Author huanghm
 * @Date 2023/1/4
 */
@Slf4j
@RestController
@RequestMapping("/hhmjob")
public class HhmJobOperateJobController {
    @Resource
    private HhmJobHealthCheckComponent hhmJobHealthCheckComponent;
    @PostMapping("/operate")
    public String operate(@RequestBody OperateTaskDto operateTaskDto){
        final TargetAndSchedulerDto targetAndSchedulerDto = HhmJobTaskClassFactory.getTask(operateTaskDto.getTaskClass());
        if (Objects.isNull(targetAndSchedulerDto)) {
            throw new RuntimeException("不存在该任务");
        }
        if (1 == operateTaskDto.getStatus()) { // 开启任务
            if(targetAndSchedulerDto.getHhmJobCustomTaskScheduler() == null && targetAndSchedulerDto.getTarget() == null){
                Object o;
                try {
                    o = SpringBeanUtil.getBean(Class.forName(targetAndSchedulerDto.getTaskClass()));
                } catch (Exception e) {
                    log.error(e.getMessage(),e);
                    throw new RuntimeException("不存在该class");
                }
                HhmJobCustomTaskScheduler hhmJobCustomTaskScheduler = new HhmJobCustomTaskScheduler();
                targetAndSchedulerDto.setTarget(o);
                targetAndSchedulerDto.setHhmJobCustomTaskScheduler(hhmJobCustomTaskScheduler);
            }

            try {
                ScheduledMethodRunnable runnable = new ScheduledMethodRunnable(targetAndSchedulerDto.getTarget(),
                        Objects.requireNonNull(ReflectionUtils.findMethod(targetAndSchedulerDto.getTarget().getClass(), targetAndSchedulerDto.getMethod())));
                targetAndSchedulerDto.getHhmJobCustomTaskScheduler().schedule(runnable, new CronTrigger(operateTaskDto.getCron()));
                targetAndSchedulerDto.setStatus(1);
                log.info("开启任务成功：class=" + targetAndSchedulerDto.getTaskClass());
            }catch (Exception e){
                log.error(e.getMessage(),e);
                throw new RuntimeException("开启任务失败，" + e.getMessage());
            }
        }else { // 停止任务
            try {
                Map<Object, ScheduledFuture<?>> scheduledTasksMap = targetAndSchedulerDto.getHhmJobCustomTaskScheduler().getScheduledTasks();
                ScheduledFuture<?> scheduledFuture = scheduledTasksMap.get(targetAndSchedulerDto.getTarget());
                while (!scheduledFuture.isCancelled()){
                    scheduledFuture.cancel(true);
                }
                targetAndSchedulerDto.setTarget(null);
                targetAndSchedulerDto.setHhmJobCustomTaskScheduler(null);
                targetAndSchedulerDto.setStatus(0);
                log.info("停止任务成功：class=" + targetAndSchedulerDto.getTaskClass());
            }catch (Exception e) {
                log.error(e.getMessage(),e);
                throw new RuntimeException("停止任务失败，" + e.getMessage());
            }
        }
        return "success";
    }
}
