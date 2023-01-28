package com.hhm.job.admin.websocket;

import com.hhm.job.admin.dao.HhmJobRegisteredTaskCenter;
import com.hhm.job.admin.dto.TaskRegisterMessageDto;
import com.hhm.job.admin.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 前后端交互的类实现消息的接收推送(自己发送给自己)
 *
 * @Author huanghm
 * @Date 2023/1/13
 * @ServerEndpoint(value = "/test/one") 前端通过此URI和后端交互，建立连接
 */
@Slf4j
@ServerEndpoint(value = "/test/one/{taskClassMessage}")
@Component
public class WebSocketComponent {
    // 已经建立连接的客户端。前端与admin的socket连接key格式 ${taskClass}-client_id, 任务节点与admin的socket连接key格式 ${taskClass}-node,
    private static Map<String, Session> sessionMap = new HashMap<>();
    private static Map<String, String> sessionAndTaskClassKeyMap = new HashMap<>();
    /**
     * 记录当前在线连接数
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("taskClassMessage") String taskClassMessage, Session session) {
        final String[] taskClassMessageSplit = taskClassMessage.split("_");
        String clientKey = taskClassMessageSplit[0]; // ${taskClass}-client

        String[] taskClassAndType = clientKey.split("-");
        String taskClass = taskClassAndType[0];

        sessionMap.put(clientKey, session);
        sessionAndTaskClassKeyMap.put(session.getId(), clientKey);

        onlineCount.incrementAndGet(); // 在线数加1
        log.info("有新连接加入：{}，当前在线连接数为：{}", session.getId(), onlineCount.get());

        // 建立任务中心和任务节点的链接，开始获取通过webSocket获取日志
        if(taskClassMessageSplit.length > 1) {
            try {
                long id = Integer.parseInt(taskClassMessageSplit[1]);
                final List<TaskRegisterMessageDto> taskList = HhmJobRegisteredTaskCenter.getTaskList(taskClass);
                final TaskRegisterMessageDto registerMessageDto = taskList.stream().filter(r -> r.getId() == id).findFirst().orElse(null);
                if(registerMessageDto == null || registerMessageDto.getStatus() == 0){
                    return;
                }
                String url = "http://" + registerMessageDto.getIp() + ":" + registerMessageDto.getPort() + "/hhmjob/startGetLog" + "?taskClass=" + registerMessageDto.getTaskClass();
                OkHttpUtil.get(url);
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        onlineCount.decrementAndGet(); // 在线数减1

        try {
            // 获取对应的另外一个对应的session，将其关闭
            String taskClassKey = sessionAndTaskClassKeyMap.get(session.getId());
            sessionAndTaskClassKeyMap.remove(session.getId());

            String[] taskClassKeySplit = taskClassKey.split("-");
            String taskClass = taskClassKeySplit[0];
            String targetKey = taskClassKeySplit[1].equals("client") ? taskClass + "-node": taskClass + "-client";
            String targetKey2 = !taskClassKeySplit[1].equals("client") ? taskClass + "-node": taskClass + "-client";
            Session sessionOther = sessionMap.get(targetKey);
            if (sessionOther !=  null) sessionOther.close();
            sessionMap.remove(targetKey);
            sessionMap.remove(targetKey2);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }

        log.info("有一连接关闭：{}，当前在线连接数为：{}", session.getId(), onlineCount.get());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        // log.info("服务端收到任务节点[{}]的消息:{}", session.getId(), message);

        // 收到任务节点消息，将其发送到页面进行展示
        final String nodeKey = sessionAndTaskClassKeyMap.get(session.getId());
        String clientKey = nodeKey.split("-")[0] + "-" + "client";
        Session clientSession = sessionMap.get(clientKey);

        this.sendMessage(message, clientSession);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        log.error(error.getMessage(),error);
    }

    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(String message, Session toSession) {
        try {
            // log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败：{}", e);
        }
    }
}
