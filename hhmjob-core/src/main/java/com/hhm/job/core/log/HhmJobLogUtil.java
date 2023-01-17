package com.hhm.job.core.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import com.hhm.job.core.scheduler.HhmJobThreadMessageCenter;
import com.hhm.job.core.websocket.HhmJobWebSocketClientFactory;
import org.java_websocket.client.WebSocketClient;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * @Author huanghm
 * @Date 2023/1/16
 */
public class HhmJobLogUtil {

    /**
     * 开始监听日志并通过websocket推送到任务中心
     * @param taskClass
     */
    public static void listenLogAndSendToAdmin(String taskClass) {
        // 获取websocket连接
        WebSocketClient webSocketClient = HhmJobWebSocketClientFactory.getClient(taskClass);

        Logger log = (Logger) LoggerFactory.getLogger("ROOT");
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

        /*这里配置encoder*/
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setCharset(Charset.forName("UTF-8"));
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n");
        encoder.setImmediateFlush(true);
        encoder.setContext(lc);

        // 设置appender
        final HhmJobSendLogAppender hhmJobSendLogAppender = new HhmJobSendLogAppender();
        hhmJobSendLogAppender.setEncoder(encoder);
        hhmJobSendLogAppender.setContext(lc);
        hhmJobSendLogAppender.setWebSocketClient(webSocketClient);
        hhmJobSendLogAppender.addFilter(new HhmJobThreadLogFilter(HhmJobThreadMessageCenter.getTaskAndThreadNameMap().get(taskClass)));

        log.addAppender(hhmJobSendLogAppender);
        log.setLevel(Level.INFO);

        encoder.start();
        hhmJobSendLogAppender.start();
    }
}
