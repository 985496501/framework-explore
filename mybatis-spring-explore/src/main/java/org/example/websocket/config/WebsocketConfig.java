package org.example.websocket.config;

import org.example.websocket.handler.EnhancedTextWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author: jinyun
 * @date: 2021/2/23
 */
@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {
    final EnhancedTextWebSocketHandler socketSessionHandler;

    public WebsocketConfig(EnhancedTextWebSocketHandler enhancedTextWebSocketHandler) {
        this.socketSessionHandler = enhancedTextWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketSessionHandler, "ws/register");
    }
}
