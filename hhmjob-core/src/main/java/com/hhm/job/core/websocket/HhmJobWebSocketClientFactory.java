package com.hhm.job.core.websocket;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * websocket配置类
 *
 * @Author huanghm
 * @Date 2023/1/16
 */
@Slf4j
public class HhmJobWebSocketClientFactory {

    public static WebSocketClient getClient(String taskClass) {
        WebSocketClient webSocketClient = null;
        try {
            taskClass = taskClass + "-" + "node";
            webSocketClient = new WebSocketClient(new URI("ws://localhost:9090/test/one/" + taskClass), new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    log.info("[websocket] 连接成功");
                }

                @Override
                public void onMessage(String message) {
                    log.info("[websocket] 收到消息={}", message);

                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.info("[websocket] 退出连接");
                }

                @Override
                public void onError(Exception ex) {
                    log.info("[websocket] 连接错误={}", ex.getMessage());
                }
            };
            webSocketClient.connect();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return webSocketClient;
    }
}
