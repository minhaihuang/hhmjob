package com.hhm.job.core.health;

import com.alibaba.fastjson.JSON;
import com.hhm.job.core.dto.TaskRegisterDto;
import com.hhm.job.core.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * register task
 * @Author huanghm
 * @Date 2023/1/11
 */
@Slf4j
@Component
public class HhmJobRegisterComponent {
    @Value("${server.port:8080}")
    private int port;
    @Value("${hhm.job.registerUrl:}")
    private String registerUrl;

    private HhmJobHealthCheckComponent hhmJobHealthCheckComponent;

    /**
     * 是否正在注册
     */
    private boolean isRegistering;

    /**
     * 设置hhmJobHealthCheckComponent，避免相互依赖
     * @param hhmJobHealthCheckComponent
     */
    public void setHhmJobHealthCheckComponent(HhmJobHealthCheckComponent hhmJobHealthCheckComponent){
        this.hhmJobHealthCheckComponent = hhmJobHealthCheckComponent;
    }

    /**
     * 开始注册任务
     */
    public synchronized void startRegisterTasks(){
        if(!isRegistering){
            isRegistering = true;
            new Thread(this::doRegister).start();
        }
    }

    /**
     * 执行注册操作的核心
     */
    private void doRegister(){
        while (isRegistering) {
            log.info("开始注册任务");
            List<String> taskClassList = new ArrayList<>(HhmJobTaskClassFactory.getTaskMap().keySet());
            try {
                String IP = InetAddress.getLocalHost().getHostAddress();
                TaskRegisterDto taskRegisterDto = new TaskRegisterDto();
                taskRegisterDto.setIp(IP);
                taskRegisterDto.setPort(port);
                taskRegisterDto.setTaskClassList(taskClassList);

                // 获取注册路径
                String url = registerUrl + "/task/register";

                final String post = OkHttpUtil.post(url, JSON.toJSONString(taskRegisterDto));
                log.info("register result:" + post);

                isRegistering = false;
                log.info("注册任务成功");

                // 开始健康监测
                hhmJobHealthCheckComponent.startHealthCheck();

            } catch (Exception e) {
                log.error("注册任务失败");
                log.error(e.getMessage(), e);
                // 3秒后再注册
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException interruptedException) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}
