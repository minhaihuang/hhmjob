package com.hhm.job.core.listener;

import com.alibaba.fastjson.JSON;
import com.hhm.job.core.dto.TargetAndSchedulerDto;
import com.hhm.job.core.health.HhmJobTaskClassFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;

import java.util.Map;

/**
 * 注销所有任务
 * @Author huanghm
 * @Date 2023/1/11
 */
@Slf4j
@Configuration
public class HhmJobApplicationStoppedListener implements ApplicationListener<ContextClosedEvent> {

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // 注销所有任务
        final Map<String, TargetAndSchedulerDto> taskMap = HhmJobTaskClassFactory.getTaskMap();
        log.info(JSON.toJSONString(taskMap));
    }
}
