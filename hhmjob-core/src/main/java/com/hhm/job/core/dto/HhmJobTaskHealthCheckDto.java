package com.hhm.job.core.dto;

import lombok.Data;

import java.util.List;

/**
 * 任务健康监测dto
 * @Author huanghm
 * @Date 2023/1/3
 */
@Data
public class HhmJobTaskHealthCheckDto {
    private String ip;
    private int port;
    private List<String> taskClassList;
}
