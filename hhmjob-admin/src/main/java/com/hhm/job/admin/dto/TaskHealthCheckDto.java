package com.hhm.job.admin.dto;

import lombok.Data;

import java.util.List;

/**
 * 任务健康监测dto
 * @Author huanghm
 * @Date 2023/1/3
 */
@Data
public class TaskHealthCheckDto {
    private String ip;
    private int port;
    private List<String> taskClassList;
}
