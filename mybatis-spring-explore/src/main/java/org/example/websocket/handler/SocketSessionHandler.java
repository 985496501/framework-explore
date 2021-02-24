package org.example.websocket.handler;

import org.example.websocket.pojo.SocketSession;
import org.springframework.web.socket.WebSocketSession;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 担任缓存用户信息的作用
 *
 * @author: jinyun
 * @date: 2021/2/23
 */
public final class SocketSessionHandler {
    /**
     * 可由类直接获取static 登录的人
     * unique key
     */
    public static final Map<String, SocketSession> ONLINES = new ConcurrentHashMap<>();

    private SocketSessionHandler() {}

    private static final SocketSessionHandler HANDLER = new SocketSessionHandler();


    public static SocketSessionHandler getInstance() {
        return HANDLER;
    }

    public void addSession(WebSocketSession webSocketSession, String sessionId) {
        ONLINES.put(sessionId, new SocketSession(sessionId, webSocketSession, null, new Date()));
    }

    public SocketSession findOne(String sessionId) {
        return ONLINES.get(sessionId);
    }

    public void removeSession(String sessionId) {
        ONLINES.remove(sessionId);
    }

    public void removeSession(WebSocketSession webSocketSession) {
        ONLINES.forEach((k, v) -> {
            if (v.getWebSocketSession().equals(webSocketSession)) {
                removeSession(k);
            }
        });
    }
}
