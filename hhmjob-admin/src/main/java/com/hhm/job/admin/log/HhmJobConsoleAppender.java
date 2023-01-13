package com.hhm.job.admin.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import com.hhm.job.admin.controller.OneWebSocketController;

/**
 * @Author huanghm
 * @Date 2023/1/13
 */

public class HhmJobConsoleAppender extends ConsoleAppender<ILoggingEvent> {
    private OneWebSocketController oneWebSocketController;
    @Override
    protected void append(ILoggingEvent event) {
        byte[] byteArray = this.encoder.encode(event);
//        String content = event.getMessage();
//        System.out.println(content);
//
//        String content2 = event.getFormattedMessage();
        System.out.println("----------start-------------");
        System.out.println(new String(byteArray));
        oneWebSocketController.sendMessage2("com.hhm.job.service.task.AutoStopTask", new String(byteArray));
        System.out.println("-----------end-------------");

    }

    public void setOneWebSocketController(OneWebSocketController oneWebSocketController) {
        this.oneWebSocketController = oneWebSocketController;
    }
}
