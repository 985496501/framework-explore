package org.example.websocket.config;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import lombok.extern.slf4j.Slf4j;
import org.example.websocket.EndPoint;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: jinyun
 * @date: 2021/4/7
 */
@Slf4j
@Service
public class ConnectEventHandler implements EndPoint {
    public static final ConcurrentHashMap<Long, SocketIOClient> sessionMap = new ConcurrentHashMap<>(16);

    /**
     * 客户端连接的时候触发
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        Long userId = Long.valueOf(client.getHandshakeData().getSingleUrlParam("userId"));
        log.info("客户端: {}, 用户id: {} 已连接", client.getSessionId(), userId);
        sessionMap.put(userId, client);
    }


    /**
     * 客户端关闭连接时触发
     */
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        sessionMap.remove(getSessionId(client.getSessionId().toString()));
        log.info("客户端:" + client.getSessionId() + "断开连接");
    }


    @OnEvent(value = "noEvent")
    public void onEvent(SocketIOClient client, Integer data, AckRequest request) {
        log.info("工号no = {}的用户推送消息", data    );
        if (data != null && data > 0) {

        }
    }

    @Override
    public void pushToOne(Long userId, String eventName, Object data) {
        SocketIOClient socketIOClient = sessionMap.get(userId);
        if (socketIOClient != null) {
            try {
                // 推送消息即为调用SocketIOClient的sendEvent方法
                socketIOClient.sendEvent(eventName, data);
            } catch (Exception e) {
                log.info("userId = {} 的用户异常 {}", userId, e.getMessage());
            }
        }

    }

    private Long getSessionId(String sessionId) {
        for (Map.Entry<Long, SocketIOClient> entry : sessionMap.entrySet()) {
            if (entry.getValue().getSessionId().toString().equals(sessionId)) {
                return entry.getKey();
            }
        }

        return null;
    }
}
