package com.hhm.job.admin.dto;

import lombok.Data;

/**
 * @Author huanghm
 * @Date 2023/1/16
 */
@Data
public class StartOrStopLogDto {
    private String taskClass;
    private String ip;
    private int port = 8080;
    // 1-获取日志，0-关闭日志
    private int opType;
}
