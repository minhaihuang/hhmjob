package com.hhm.job.admin;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.net.SimpleSocketServer;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author huanghm
 * @Date 2023/1/4
 */
@SpringBootApplication
public class HhmJobAdminApplication {
    public static void main(String[] args) {
//        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//        SimpleSocketServer sss = new SimpleSocketServer(lc, 9999);  //该端口无法写入spring配置文件，因为启动加载顺序问题，可以添加到环境变量；
        SpringApplication.run(HhmJobAdminApplication.class, args);
//        sss.start();
    }
}
