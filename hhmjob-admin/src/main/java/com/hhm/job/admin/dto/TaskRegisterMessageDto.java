package com.hhm.job.admin.dto;

import lombok.Data;

/**
 * @Author huanghm
 * @Date 2022/5/25
 */
@Data
public class TaskRegisterMessageDto {
    private String taskClass;
    private int status = 0;
    private String ip;
    private int port;
    private long lastHealCheckTime;

    private String nameSpace;

    private int id = 0;
}
