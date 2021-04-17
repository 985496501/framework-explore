package org.example.beans.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Spring 定义了一个接口, Import bean定义的注册员
 * 显然, 这个接口的作用 就是 bean 定义相关的, 它负责把 Import(BeanClass) 这个bean定义 写入到 beanDefinitionRegistry中吧.
 * 在bean定义层面, 这个是最优的选择。
 * 并且Spring 建议在bean定义层面, 使用4个回调方法, 或者提供这个4个类型的构造方法, [optional]
 *
 * 这个接口主要看一个方法：
 * registerBeanDefinitions(AnnotationMetadata, BeanDefinitionRegistry)
 * AnnotationMetadata: 注解元数据 ？
 * BeanDefinitonRegistry: bean定义中心
 *
 * 这个方法和@Configuration相关。
 * 还有一个类需要注意就是  BeanDefinitionRegistryPostProcessor.
 *
 * @author: jinyun
 * @date: 2021/3/26
 */
public class ImportMyBean implements ImportBeanDefinitionRegistrar,
        EnvironmentAware, BeanFactoryAware, BeanClassLoaderAware, ResourceLoaderAware {
    private ClassLoader classLoader;
    private BeanFactory beanFactory;
    private Environment environment;
    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
