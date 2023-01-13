package com.hhm.job.core.log;

import ch.qos.logback.classic.net.SocketAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;

/**
 * @Author huanghm
 * @Date 2023/1/13
 */
public class HhmJobConsoleAppender extends ConsoleAppender<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent event) {
        byte[] byteArray = this.encoder.encode(event);
//        String content = event.getMessage();
//        System.out.println(content);
//
//        String content2 = event.getFormattedMessage();
        System.out.println("----------start-------------");
        System.out.println(new String(byteArray));
        System.out.println("-----------end-------------");


    }
}
