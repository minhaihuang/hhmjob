package com.hhm.job.core.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import org.java_websocket.client.WebSocketClient;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.Charset;

/**
 * @Author huanghm
 * @Date 2023/1/13
 */
// @Component
public class LoggerAppender {
    @Resource
    private WebSocketClient webSocketClient;

    @PostConstruct
    public void init(){
        addAppender();
    }

    public void addAppender() {
        /**
         * 日志记录Logger
         */
        Logger log = (Logger) LoggerFactory.getLogger("com.hhm.job.service.task.AutoStopTask");
//        Logger LOG = (Logger) LoggerFactory.getLogger("ROOT");
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

        /*这里配置encodeer*/
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setCharset(Charset.forName("UTF-8"));
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n");
        encoder.setImmediateFlush(true);
        encoder.setContext(lc);

        final HhmJobSendLogAppender hhmJobSendLogAppender = new HhmJobSendLogAppender();
        hhmJobSendLogAppender.setEncoder(encoder);
        hhmJobSendLogAppender.setContext(lc);
        hhmJobSendLogAppender.setWebSocketClient(webSocketClient);

        log.addAppender(hhmJobSendLogAppender);
        log.setLevel(Level.INFO);

        encoder.start();
        hhmJobSendLogAppender.start();


//        SocketAppender socketAppender = new SocketAppender();
//        socketAppender.setRemoteHost("127.0.0.1");
//        socketAppender.setPort(9999);
//        socketAppender.setContext(lc);
//        socketAppender.addFilter(new HhmJobLogFilter());
//        LOG.setLevel(Level.INFO);
//        LOG.addAppender(socketAppender);
//
//        socketAppender.start();
//        encoder.start();

//        Logger LOG = (Logger) LoggerFactory.getLogger("com.hhm.job.core.health.HhmJobRegisterComponent");
//        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
//                /*这里配置encodeer。*/
//        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
//        encoder.setCharset(Charset.forName("UTF-8"));
//        encoder.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
//        encoder.setImmediateFlush(true);
//        encoder.setContext(lc);
//
//        /*这里启配置appender*/
//        FileAppender appender = new FileAppender();
//        appender.setEncoder(encoder);
//        appender.setFile("F:\\hhmjob-open\\test.log");
//        appender.setName("TestFile");
//        appender.setAppend(false);
//        appender.setContext(lc);
//
//        /*这里启动encoder和appender。*/
//        encoder.start();
//        appender.start();
//
//        LOG.addAppender(appender);
//        LOG.info("这里时一条所有appender都可以输出的数据。");
//
//        /*修改Logger级别*/
//        LOG.setLevel(Level.ERROR);
//        LOG.info("这里的数据所有appender都不会输出。");
//        LOG.error("这里时一条所有appender都可以输出的数据。");
        /*删除appender*/
//        LOG.detachAppender("TestFile");
//        LOG.error("这条数据不会在FileAppender中输出。");

    }

//    public void addAppender() throws IOException {
//        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

//        /*这里配置encodeer。*/
//        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
//        encoder.setCharset(Charset.forName("UTF-8"));
//        encoder.setPattern("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n");
//        encoder.setImmediateFlush(true);
//        encoder.setContext(lc);
//
//        /*这里启配置appender*/
//        FileAppender appender = new FileAppender();
//        appender.setEncoder(encoder);
//        appender.setFile("/home/bash/workspace/temp/test.log");
//        appender.setName("TestFile");
//        appender.setAppend(false);
//        appender.setContext(lc);
//
//        /*这里启动encoder和appender。*/
//        encoder.start();
//        appender.start();
//
//        LOG.addAppender(appender);
//        LOG.info("这里时一条所有appender都可以输出的数据。");
//
//        /*修改Logger级别*/
//        LOG.setLevel(Level.ERROR);
//        LOG.info("这里的数据所有appender都不会输出。");
//        LOG.error("这里时一条所有appender都可以输出的数据。");
//        /*删除appender*/
//        LOG.detachAppender("TestFile");
//        LOG.error("这条数据不会在FileAppender中输出。");
//    }
}
