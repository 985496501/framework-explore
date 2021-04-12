package org.example.beans.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 探索bean的生命周期 完成特定功能的定制
 *
 * @author: jinyun
 * @date: 2021/4/12
 */
@Slf4j
@Profile("dev")
@Component
public class DefaultBeanLifecycle implements SmartInitializingSingleton, InitializingBean, DisposableBean,
        ApplicationContextAware, EnvironmentAware, CommandLineRunner {

    private final AtomicInteger order = new AtomicInteger(0);

    public DefaultBeanLifecycle() {
        // 实例化这个对象的时候调用
        log.error("====> 回调的方法: {}, 次序: {}", "DefaultBeanLifecycle()", order.incrementAndGet());
    }

    @Override
    public void setEnvironment(Environment environment) {
        // 实例化完这个对象之后就回调这个环境
        log.error("====> 回调的方法: {}, 次序: {}", "EnvironmentAware", order.incrementAndGet());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        // 实例化完这个对象之后就回调这个方法
        log.error("====> 回调的方法: {}, 次序: {}", "ApplicationContextAware", order.incrementAndGet());
    }


    @Override
    public void afterPropertiesSet() {
        // 在所有其他属性填充完毕调用这个方法
        log.error("====> 回调的方法: {}, 次序: {}", "InitializingBean", order.incrementAndGet());
    }

    @Override
    public void afterSingletonsInstantiated() {
        // 在整个bean的配置的最后阶段完成这个方法的回调
        log.error("====> 回调的方法: {}, 次序: {}", "SmartInitializingSingleton", order.incrementAndGet());
    }

    @Override
    public void destroy() {
        // 关闭应用程序的时候 会销毁所有的bean调用
        log.error("====> 回调的方法: {}, 次序: {}", "DisposableBean", order.incrementAndGet());
    }

    @Override
    public void run(String... args) {
        // 整个应用创建成功之后 发布这个事件
        log.error("====> 回调的方法: {}, 次序: {}", "CommandLineRunner", order.incrementAndGet());
    }
}
