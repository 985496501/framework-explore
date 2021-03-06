package org.example.websocket.config;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;


/**
 * <p>STOMP over WebSocket support is available in the spring-messaging and spring-websocket modules.
 * Once you have those dependencies, you can expose a stomp endpoint. </p>
 *
 * &#064;EnableWebSocketMessageBroker enables broker-backed messaging over websocket
 * using a higher-level messaging sub-protocol.
 *
 *
 * 使用 @Configuration @xxx 的经典注解, 配置 常常这个@xxx 会@Import(Delegating**Configuration.class)
 * 这套spring常用的经典配置组合.   @Import(DelegatingWebSocketMessageBrokerConfiguration.class)
 * todo: 探索spring是如何完成这套经典组合的实现的. 是如何进行扩展配置的.
 *
 *
 * 主要在 webSocket message broker 配置器中配置 StompEndpointRegistry  MessageBrokerRegistry.
 * restrictive proxies: 限制的, re + strict + ive: 再三严格，就是限制性的.
 * @author: jinyun
 * @date: 2021/4/6
 */
//@Configuration
//@EnableWebSocketMessageBroker
public class EnableStompConfig /*implements WebSocketMessageBrokerConfigurer*/ {
   /* *//**
     * interceptors:
     * see {@link OriginHandshakeInterceptor}
     * and {@link HttpSessionHandshakeInterceptor}
     *
     * @param registry
     *//*
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register a STOMP over WebSocket endpoint at the given mapping path.
        // Enable SockJS fallback options.
        // /* is the HTTP URL for the endpoint to which a WebSocket(or SockJS) client needs to
        // connect for the WebSocket handshake.

        // applicaitonName/handshake  ws://localhost:8080/im/handshake
        // gateway nginx 不要匹配去除前缀
        registry.addEndpoint("/stomp/endpoint")
                // todo: 自定义解析用户信息 从头中解析用户信息 然后registerUserDetail       version1: 单体架构
                .addInterceptors(new HandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                        return false;
                    }

                    @Override
                    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

                    }
                })
                // 支持自定义 handler WebSocketTransportHandler
                .setAllowedOrigins("*")
                .withSockJS();
    }

    *//**
     * MessageBrokerRegistry: 消息代理中心  这个对象就是一个存储对象
     * debug: 看看这个对象结构
     * config message broker options.
     *
     * @param registry
     *//*
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Configure message broker options.
        // Use the built-in message broker for subsriptions and broadcasting and route messages whose destionation
        // header begins with 'queue or topic' to the broker.
//        registry.enableStompBrokerRelay("/queue/", "/topic/");
        // stomp messages whose destination header begins with /app are routed to @MessageMapping methods in @Controller classes.

        // client.send("/im/xx") routo to @MessageMapping("xx")
        registry.setApplicationDestinationPrefixes("/im");

        registry.enableSimpleBroker("/queue")
                // 当使用stomp 如果服务端和客户端协商交换心跳, SockJS心跳就不会生效
                .setHeartbeatValue(new long[] {10000, 10000})
                .setTaskScheduler(heartbeatTaskScheduler());

        registry.setUserDestinationPrefix("/user");

    }*/

    private TaskScheduler heartbeatTaskScheduler() {
        ThreadPoolTaskScheduler poolTaskScheduler = new ThreadPoolTaskScheduler();
        poolTaskScheduler.setPoolSize(1);
        poolTaskScheduler.setThreadNamePrefix("message-broker-heartbeat-");
        poolTaskScheduler.initialize();
        return poolTaskScheduler;
    }
}
