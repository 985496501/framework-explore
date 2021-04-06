package org.example.websocket.config;

import org.example.websocket.handler.EnhancedTextWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

/**
 * @author: jinyun
 * @date: 2021/4/6
 */
@Configuration
@EnableWebSocket
public class WebSocketServerConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Configure a WebSocketHandler at the specified URL paths.
        registry.addHandler(new EnhancedTextWebSocketHandler(), "/enhanced").setAllowedOrigins("*");
    }


    /**
     * config websocket
     *
     * @return
     */
    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean factoryBean = new ServletServerContainerFactoryBean();
        factoryBean.setMaxTextMessageBufferSize(8192);
        factoryBean.setMaxBinaryMessageBufferSize(8192);
        return factoryBean;
    }
}
