package org.example.websocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * 数据发送的handler
 *
 * @author: jinyun
 * @date: 2021/2/23
 */
@Slf4j
@Component
public class EnhancedTextWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("someone connecting websocket server...");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {

    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("someone disconnected websocket server...");
        SocketSessionHandler.getInstance().removeSession(session);
    }
}
