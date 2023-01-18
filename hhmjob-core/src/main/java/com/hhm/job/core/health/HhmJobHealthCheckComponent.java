package com.hhm.job.core.health;

import com.alibaba.fastjson.JSON;
import com.hhm.job.core.dto.HhmJobTaskHealthCheckDto;
import com.hhm.job.core.dto.TargetAndSchedulerDto;
import com.hhm.job.core.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * @Author huanghm
 * @Date 2023/1/10
 */
@Slf4j
@Component
public class HhmJobHealthCheckComponent {
    @Value("${hhm.job.registerUrl:}")
    private String registerUrl;
    @Value("${server.port:8080}")
    private int port;

    private HhmJobRegisterComponent hhmJobRegisterComponent;

    private boolean isChecking;

    /**
     * 设置hhmJobRegisterComponent，避免相互依赖
     * @param hhmJobRegisterComponent
     */
    public void  setHhmJobRegisterComponent(HhmJobRegisterComponent hhmJobRegisterComponent){
        this.hhmJobRegisterComponent = hhmJobRegisterComponent;
    }

    public synchronized void startHealthCheck(){
        // 开始健康监测
        if(!isChecking) {
            isChecking = true;
            new Thread(this::healCheck).start();
        }
    }

    public void healCheck() {
        long now = System.currentTimeMillis();
        try {
            while (isChecking) {
                final Map<String, TargetAndSchedulerDto> taskMap = HhmJobTaskClassFactory.getTaskMap();
                try {
                    String IP = InetAddress.getLocalHost().getHostAddress();

                    HhmJobTaskHealthCheckDto healthCheckDto = new HhmJobTaskHealthCheckDto();
                    healthCheckDto.setIp(IP);
                    healthCheckDto.setPort(port);
                    healthCheckDto.setTaskClassList(new ArrayList<>(taskMap.keySet()));

                    String url = registerUrl + "/health/check";
                    final String s = OkHttpUtil.post(url, JSON.toJSONString(healthCheckDto));
                    if ("pong".equals(s)) {
                        // log.info("健康检测成功");
                        now = System.currentTimeMillis();
                    }
                } catch (Exception e) {
                    log.error("健康检测失败");
                    log.error(e.getMessage(), e);
                }
                // 当1分钟没有接收到任务中心的相应时，终止所有任务。重新注册
                if (System.currentTimeMillis() - now > (60 * 1000)) {
                    taskMap.values().forEach(t -> {
                        if (t.getStatus() == 1) {
                            Map<Object, ScheduledFuture<?>> scheduledTasksMap = t.getHhmJobCustomTaskScheduler().getScheduledTasks();
                            ScheduledFuture<?> scheduledFuture = scheduledTasksMap.get(t.getTarget());
                            while (!scheduledFuture.isCancelled()) {
                                scheduledFuture.cancel(true);
                            }
                            t.setTarget(null);
                            t.setHhmJobCustomTaskScheduler(null);
                            t.setStatus(0);
                            log.info("任务中心无响应，终止任务：" + t.getTaskClass());
                        }
                    });
                    // 重新注册
                    log.info("重新注册任务");
                    hhmJobRegisterComponent.startRegisterTasks();
                    isChecking = false;
                }
                // 休眠10秒
                Thread.sleep(10000L);
            }
        }catch (Throwable e){
            log.error(e.getMessage(),e);
        }
    }
}
