package com.hhm.job.admin.dto;

import lombok.Data;

/**
 * 操作任务的dto
 * @Author huanghm
 * @Date 2023/1/4
 */
@Data
public class OperateRemoteTaskDto {
    private String taskClass;
    private int status;
    private String cron;
}
