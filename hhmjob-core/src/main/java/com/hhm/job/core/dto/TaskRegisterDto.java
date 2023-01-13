package com.hhm.job.core.dto;

import lombok.Data;

import java.util.List;

/**
 * 注册任务dto
 * @Author huanghm
 * @Date 2023/1/3
 */
@Data
public class TaskRegisterDto {
    private String ip;
    private int port;
    private List<String> taskClassList;
}
