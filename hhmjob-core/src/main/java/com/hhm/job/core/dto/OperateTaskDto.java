package com.hhm.job.core.dto;

import lombok.Data;

/**
 * 操作任务的dto
 * @Author huanghm
 * @Date 2023/1/4
 */
@Data
public class OperateTaskDto {
    private String taskClass;
    private int status;
    private String cron;
}
