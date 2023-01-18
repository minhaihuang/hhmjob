package com.hhm.job.admin.dto;

import lombok.Data;

/**
 * @Author: huanghm
 * @Date: 2022/05/14
 * @Description:
 */
@Data
public class TaskAdminDto {
    private String taskName;
    private String taskClass;
    private String cron;
    private int status = 0;
    private int taskNum;
    private long id = 0;
//    private String namespace;
}
