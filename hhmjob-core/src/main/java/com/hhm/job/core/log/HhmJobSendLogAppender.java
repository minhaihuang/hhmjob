package com.hhm.job.core.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;

/**
 * @Author huanghm
 * @Date 2023/1/13
 */
@Slf4j
public class HhmJobSendLogAppender extends ConsoleAppender<ILoggingEvent> {
    private WebSocketClient webSocketClient;
    @Override
    protected void append(ILoggingEvent event) {
        log.info(event.getThreadName());
        byte[] byteArray = this.encoder.encode(event);
        final String message = new String(byteArray);
//        System.out.println("----------start-------------");
//        System.out.println(new String(byteArray));
//        System.out.println("-----------end-------------");
        // 这里指定发送的规则由服务端决定参数格式
        webSocketClient.send(message);
    }

    public void setWebSocketClient(WebSocketClient webSocketClient) {
        this.webSocketClient = webSocketClient;
    }
}
