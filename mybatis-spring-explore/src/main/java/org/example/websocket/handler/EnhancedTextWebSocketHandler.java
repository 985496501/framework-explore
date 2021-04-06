package org.example.websocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * 数据发送的handler
 *
 * @author: jinyun
 * @date: 2021/2/23
 */
@Slf4j
public class EnhancedTextWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("someone connecting websocket server...");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 只需要在这处理相应的文本业务逻辑即可
        // 顶层接口 WebSocketHandler.handleMessage 已经通过抽象类完成选择

        // text <String>
        String payload = message.getPayload();
        System.out.println("连接为： " + session.getId() + "  发送的消息：" + payload);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("someone disconnected websocket server...");
        SocketSessionHandler.getInstance().removeSession(session);
    }
}
