package org.example.websocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;

/**
 * 数据发送的handler
 *
 * 这个就是 webSocket server的最大类
 *
 *
 * @author: jinyun
 * @date: 2021/2/23
 */
@Slf4j
//@Component
public class WebSocketServer extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("someone connecting websocket server...");
        String sessionId = session.getId();
        String acceptedProtocol = session.getAcceptedProtocol();
        Map<String, Object> attributes = session.getAttributes();
        HttpHeaders httpHeaders = session.getHandshakeHeaders();

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        // 不用任何处理 AbstractWebSocketHandler已经 根据 message 做好了策略选择
        try {
            super.handleMessage(session, message);
        } catch (Exception e) {
            log.error("not support");
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("someone disconnected websocket server...");
    }
}
