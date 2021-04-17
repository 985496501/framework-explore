package org.example.beans.bean;

import org.example.EntryApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.AnnotationConfigRegistry;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author: jinyun
 * @date: 2021/4/17
 */
public class AnnotationConfigApplicationContextTest {
    /**
     * 直接实现了   AnnotationConfigRegistry
     * 同时也 GenericApplicationContext 这个类就是 IOC的持久对象
     *
     */
    public final static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();


    // -----------AnnotationConfigRegistry-----------------------------------------------------------------------
    /**
     * see {@link AnnotationConfigRegistry} context的直接实现
     * see {@link AnnotatedBeanDefinitionReader} {@link BeanDefinitionRegistry}
     *
     *
     * see {@link AnnotatedGenericBeanDefinition}
     * see {@link AnnotatedTypeMetadata}
     *
     *
     */
    @Test
    public void registerTest() {
        // AnnotationConfigRegsitry的接口, 所以这个方法的目的就是扫描注解
        context.register(EntryApplication.class);

        // 等效于 都会注册这个bean
        AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(context);
        reader.registerBean(EntryApplication.class);
    }

    /**
     *
     */
    @Test
    public void scanTest() {
        context.scan("");
    }

    // -----------AnnotationConfigRegistry-----------------------------------------------------------------------



    // -----------ConfigurableApplicationContext-----------------------------------------------------------------------

    /**
     * see {@link ConfigurableApplicationContext}
     * 可配置的应用上下文, 只有它才能刷新应用上下文, 填充整个IOC
     * As this is a startup method, it should destroy already created singletons if it fails,
     * to avoid dangling resources. In other words, after invocation of this method,
     * either all or no singletons at all should be instantiated.
     */
    @Test
    public void refreshTest() {
        context.refresh();
    }




    // -------------------------------------------------------------------------------------------------

    /**
     *
     */
    @Test
    public void getBeanTest() {
        // 直接getBean
        context.getBean(EntryApplication.class);
    }

}
