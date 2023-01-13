package com.hhm.job.core.dto;

import com.hhm.job.core.scheduler.HhmJobCustomTaskScheduler;
import lombok.Data;

/**
 * @Author huanghm
 * @Date 2022/5/25
 */
@Data
public class TargetAndSchedulerDto {
    // 基本信息
    private String taskClass;

    private int status = 0;

    // 对象信息
    private Object target;

    // 任务调度器
    private HhmJobCustomTaskScheduler hhmJobCustomTaskScheduler;

    private String method = "doJob";
}
