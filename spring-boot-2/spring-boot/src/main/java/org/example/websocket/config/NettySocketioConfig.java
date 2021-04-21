package org.example.websocket.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

/**
 * @author: jinyun
 * @date: 2021/4/7
 */
@Slf4j
//@Configuration(proxyBeanMethods = false)
public class NettySocketioConfig {

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setPort(8081);
//        config.setHostname(hostName);
        config.setOrigin(null); // 开启跨域
        config.setPingInterval(1000);
        config.setPingTimeout(5000);
        config.setBossThreads(1);
        config.setWorkerThreads(10);
        SocketIOServer socketIOServer = new SocketIOServer(config);
        socketIOServer.start();
        log.info("netty-socketio server starts");
        return socketIOServer;
    }

    /**
     * 用于扫描netty-socketio的注解，比如 @OnConnect、@OnEvent
     */
    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketIOServer) {
        return new SpringAnnotationScanner(socketIOServer);
    }
}
