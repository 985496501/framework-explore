package org.example.websocket.config;

import org.example.websocket.handler.WebSocketServer;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Websocket emulation through sock.js, pub-sub stomp protocol
 *
 *
 * 1. 从这探究一下@Enable* 这样注解的流程
 * {@link AbstractApplicationContext#refresh()}
 * AbstractApplicationContext 这个抽象的应用上下文定义了一些模板方法, 比如著名的 refresh()
 * 它是 ConfigurableApplicationContext 定义的接口, 这个应用上下文是可以配置的, refresh()刷新整个
 * 应用的上下文;
 * 这个抽象的模板方法是这样定义这个执行序列的：
 * 核心关键步骤：
 * 1. 在这里要求有一个统一的 beanFactory, 实现： DefaultListableBeanFactory.
 * 但是真正使用 bean工厂  我们实际使用的接口是 ConfigurableListableBeanFactory.
 * see {@link ConfigurableListableBeanFactory} Facade 聚合了很多能力的接口.
 * 拆分功能, 然后有机的聚合功能. 展现统一的门面系统. 主要就是让调用方非常舒服了.
 * 2. 准备 BeanFactory
 *
 * 3. 后置处理 beanFactory
 * 4. 执行beanFactory的后置处理器
 *
 * 5. 注册bean的后置处理器
 *
 *
 * 0. 完成beanFactory的初始化。
 * finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory)
 * 我们debug发现就是在这个类中进行我们bean的创建的。 track it.
 * // todo: 妈的我这个idea老是提示过期所以我得先提交代码 等明天上班再看
 *
 *
 *
 *
 *
 * @author: jinyun
 * @date: 2021/2/23
 */
@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {
    final WebSocketServer socketSessionHandler;

    public WebsocketConfig(WebSocketServer webSocketServer) {
        this.socketSessionHandler = webSocketServer;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketSessionHandler, "ws/register");
    }
}
