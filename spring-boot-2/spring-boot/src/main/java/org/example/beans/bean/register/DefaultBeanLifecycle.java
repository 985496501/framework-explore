package org.example.beans.bean.register;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.*;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 探索bean的生命周期 完成特定功能的定制
 *
 *
 *
 *
 *
 *
 * {@link SmartInitializingSingleton} triggered at the end of the singleton pre-instantiation phase. todo: 预实例化 的结尾 ?;
 * 什么是预实例化？ 应该就是创建对象, earlySingletons 之前, 如果是提前引用了 所必须要求初始化的属性;
 * 查看了网上的讲解说是 所有的单例bean都创建好了之后调用一次,
 * Compare to {@link InitializingBean} 这个是创建指定的bean的时候完成属性填充之后 调用的方法;
 *
 *
 * 如果你 想 开启/管理 异步任务, 最好实现 声明周期接口, instead 它提供了丰富的 运行状态管理的模型 并且 允许阶段话的启动或者关闭;
 * If you intend to start/manage asynchronous tasks, perferably implement {@link Lifecycle} instead which offers a richer
 * model for runtime management and allows for phased startup/shutdown.
 *
 * {@link Lifecycle} A common interface defining methods for start/stop lifecycle control.
 * The typical use case for this is to control asynchronous processing. 典型的使用案例就是控制 异步处理;
 *
 * Note: This interface does not imply specific auto-startup semantics.
 * Consider implementing {@link SmartLifecycle} for that purpose.
 *
 * 一般就是一个bean定义在Spring Context 容器或者就是 ApplicationContext itself.
 * Container wull propagate start/stop signals to all components.
 *
 * 推荐实现： smartLifecyle interface provides sophisticated integration with the application context's
 * startup and shutdown phases.
 *
 *
 *
 *
 * {@link DisposableBean} disposableBean 表示用完就会丢弃, 表示destroy() 方法处理 一些必要的 release resource.
 *
 * @author: jinyun
 * @date: 2021/4/12
 */
@Slf4j
//@Profile("dev")
@Component
public class DefaultBeanLifecycle implements Ordered, InitializingBean, ApplicationEventPublisherAware,
        ApplicationContextAware, EnvironmentAware, CommandLineRunner, BeanNameAware,
        SmartInitializingSingleton, DisposableBean, SmartLifecycle {

    private final AtomicInteger order = new AtomicInteger(0);

    public DefaultBeanLifecycle() {
        // 实例化这个对象的时候调用
        log.error("====> 回调的方法: {}, 次序: {}", "DefaultBeanLifecycle()", order.incrementAndGet());
    }

    @Override
    public void setBeanName(String name) {
        log.error("====> 回调的方法: {}, 次序: {} beanName: {} ----> ", "BeanNameAware", order.incrementAndGet(), name);
    }

    // ------ApplicationContextAwareProcessor.postProcessBeforeInitialization()-----------------------------------------
    // ------invokeAwareInterfaces()---------
    // SpringApplication.prepareBeanFactory() 准备Bean工厂就是把 BeanPostProcessor 加入到BeanPostProcessor
    // AbstractBeanFactory.List<BeanPostProcessor> beanPostProcessors;

    @Override
    public void setEnvironment(Environment environment) {
        // 实例化完这个对象之后就回调这个环境
        log.error("====> 回调的方法: {}, 次序: {}", "EnvironmentAware", order.incrementAndGet());
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        // 注入ApplicationEventPublisher
        log.error("====> 回调的方法: {}, 次序: {}", "ApplicationEventPublisherAware", order.incrementAndGet());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        // 实例化完这个对象之后就回调这个方法
        log.error("====> 回调的方法: {}, 次序: {}", "ApplicationContextAware", order.incrementAndGet());
    }

    // private final Set<Class<?>> ignoredDependencyInterfaces = new HashSet<>();


    // --------------------------------------------------------------------------------------------------------

    @Override
    public void afterPropertiesSet() {
        // 在所有其他属性填充完毕调用这个方法
        log.error("====> 回调的方法: {}, 次序: {}", "InitializingBean ----> ", order.incrementAndGet());
    }

    @Override
    public void afterSingletonsInstantiated() {
        // 在整个bean的配置的最后阶段完成这个方法的回调
        log.error("====> 回调的方法: {}, 次序: {}", "SmartInitializingSingleton ----> ", order.incrementAndGet());
    }

    @Override
    public void destroy() {
        // 关闭应用程序的时候 会销毁所有的bean调用
        log.error("====> 回调的方法: {}, 次序: {}", "DisposableBean ----> Interface to be implemented by beans that want to release resources on destruction: ", order.incrementAndGet());
    }

    @Override
    public void run(String... args) {
        // 整个应用创建成功之后 发布这个事件
        log.error("====> 回调的方法: {}, 次序: {}", "CommandLineRunner ----> ", order.incrementAndGet());
    }

    @Override
    public int getOrder() {
        // bean的实例化的顺序
        log.error("====> 回调的方法: {}, 次序: {}", "CommandLineRunner ----> ", order.incrementAndGet());
        return Ordered.LOWEST_PRECEDENCE;
    }


    @Override
    public void start() {
        log.error("====> 回调的方法: {}, 次序: {}", "start ----> ", order.incrementAndGet());
    }

    @Override
    public void stop() {
        log.error("====> 回调的方法: {}, 次序: {}", "stop ----> ", order.incrementAndGet());
    }

    @Override
    public boolean isRunning() {
        return false;
    }


}
