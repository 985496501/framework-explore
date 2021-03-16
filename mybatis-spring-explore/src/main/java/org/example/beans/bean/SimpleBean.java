package org.example.beans.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * <p>Lifecycle:</p>
 * w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 1030 ms
 * 打印bean的生命周期  3种情况
 * <ol>
 *     <li>实现 InitializingBean, DisposableBean 接口</li>
 *     <li>使用 @PostConstruct, @PreDestroy 注解</li>
 *     <li>使用 init(), destroy() 默认方法</li>
 * </ol>
 *
 * <p>ApplicationContext:</p>
 *
 * @author: jinyun
 * @date: 2021/3/16
 */
@Component
public class SimpleBean implements InitializingBean, DisposableBean, ApplicationContextAware {
    private ConfigurableApplicationContext configurableApplicationContext;

    @PostConstruct
    public void init() {
        System.out.println("SimpleBean.... 创建bean的回调1....");
    }

    @PreDestroy
    public void destroy2() {
        System.out.println("SimpleBean.... 销毁bean的回调....2");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("SimpleBean.... 创建bean的回调2....");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("SimpleBean.... 销毁bean的回调....");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("我来赋值applicationContext了， 我在哪一步进行回调的呢");
        // When application creates this object
        System.out.println(applicationContext);
        // Casting this reference to a known subclass of this interface for exposing additional functionality.
        // [AnnotationConfigServletWebServerApplicationContext]
        this.configurableApplicationContext = (ConfigurableApplicationContext)applicationContext;
    }
}
