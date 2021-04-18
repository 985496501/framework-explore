package org.example.beans.bean.scan;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.*;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <a ref="https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans">link</a>
 *
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
 * <p>BeanFactory implementations should support the standard bean lifecycle interfaces as far as possible:</p>
 * <ol>
 *     <li>BeanNameAware</li>
 *     <li>BeanClassLoaderAware</li>
 *     <li>BeanFactoryAware</li>
 *     <li>EnvironmentAware</li>
 *     <li>ResourceLoaderAware</li>
 * </ol>
 *
 * @author: jinyun
 * @date: 2021/3/16
 */
@Component
public class SimpleBean implements Ordered, InitializingBean, DisposableBean,
        ApplicationContextAware, BeanNameAware, BeanClassLoaderAware, BeanFactoryAware, EnvironmentAware, ResourceLoaderAware {

    private final AtomicInteger i = new AtomicInteger(1);

    /**
     * manipulate the context.
     */
    private ConfigurableApplicationContext configurableApplicationContext;

    private ClassLoader classLoader;

    private Environment environment;

    private BeanFactory beanFactory;

    private ResourceLoader resourceLoader;

    @PostConstruct
    public void init() {
        System.out.println("SimpleBean.... 创建bean的回调...通过@PostConstruct." + i.incrementAndGet());
    }

    @PreDestroy
    public void destroy2() {
        System.out.println("SimpleBean.... 销毁bean的回调....通过@PreDestroy" + i.incrementAndGet());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("SimpleBean.... 创建bean的回调....通过afterPropertiesSet()" + i.incrementAndGet());
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("SimpleBean.... 销毁bean的回调....通过destroy()" + i.incrementAndGet());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // an alternative using @Autowired
        System.out.println("我来赋值applicationContext了， 我在哪一步进行回调的呢 == " + i.incrementAndGet());
        // When application creates this object
        System.out.println(applicationContext);
        // Casting this reference to a known subclass of this interface for exposing additional functionality.
        // [AnnotationConfigServletWebServerApplicationContext]
        this.configurableApplicationContext = (ConfigurableApplicationContext)applicationContext;
        ConfigurableEnvironment environment = configurableApplicationContext.getEnvironment();
        String[] activeProfiles = environment.getActiveProfiles();
        String[] defaultProfiles = environment.getDefaultProfiles();
        System.out.println(Arrays.toString(activeProfiles) + "  ==========================  " + Arrays.toString(defaultProfiles));
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("after population of a normal bean properties but before an initializing callback" + i.incrementAndGet());
        System.out.println(name);
    }

    public static void main(String[] args) {
        // original beanName
        System.out.println(BeanFactoryUtils.originalBeanName("org.example.beans.bean.scan.SimpleBean"));
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("BeanClassLoaderAware==" + classLoader + i.incrementAndGet());
        this.classLoader = classLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        System.out.println("EnvironmentAware==" + environment + i.incrementAndGet());
        this.environment = environment;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("BeanFactoryAware==" + beanFactory + i.incrementAndGet());
        this.beanFactory = beanFactory;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        System.out.println("ResourceLoaderAware==" + resourceLoader + i.incrementAndGet());
        this.resourceLoader = resourceLoader;
    }

    @Override
    public int getOrder() {
        System.out.println("Ordered==" + resourceLoader + i.incrementAndGet());
        return Ordered.LOWEST_PRECEDENCE;
    }
}
