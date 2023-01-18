package com.hhm.job.service.task;

import com.hhm.job.core.task.HhmJobAbstractTask;
import com.hhm.job.service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author huanghm
 * @Date 2022/5/12
 */
@Component
@Slf4j
public class AutoStopTask2 extends HhmJobAbstractTask {
    @Resource
    private UserService userService;
    private int count;

    @Override
    public void doJob() {
        userService.test();
        log.info("hhm任务2, currentThread {}任务执行次数：{}",  Thread.currentThread(), count + 1);
        count++;
    }
}
