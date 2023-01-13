package com.hhm.job.admin.controller;

import com.hhm.job.admin.dao.HhmJobRegisteredTaskCenter;
import com.hhm.job.admin.dto.TaskHealthCheckDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康监测controller
 * @Author huanghm
 * @Date 2023/1/4
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @PostMapping("/check")
    public String healthCheck(@RequestBody TaskHealthCheckDto taskHealthCheckDto) {
        // 更新上次健康监测时间
        HhmJobRegisteredTaskCenter.updateLastHealCheckTime(taskHealthCheckDto);
        return "pong";
    }
}
