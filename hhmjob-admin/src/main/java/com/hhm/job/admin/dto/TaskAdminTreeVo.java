package com.hhm.job.admin.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: huanghm
 * @Date: 2022/05/14
 * @Description:
 */
@Data
public class TaskAdminTreeVo {
    private int id;
    private String taskName;
    private String taskClass;
    private String cron;
    private int status = 0;
    private int taskNum;
    private Boolean isChildren = false;

    private List<TaskAdminTreeVo> children;
}
