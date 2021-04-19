package org.example.beans.bean;

import org.example.EntryApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 1. 首先探索完成 BeanDefinition 的创建 然后注册到 BeanDefinitionRegistry的过程;
 * 1.1 注册的源头是什么, 什么样的class才会被封装成BeanDefinition?
 * 1.2 @Component 的作用, @Configuration 的作用, 与@Component的区别 ?
 * 1.3 @Condition 注解在BeanDefinition的创建之前完成的拦截作用
 *
 * 2. 在BeanFactory中有了BeanDefinition之后, 下一步的处理应该是什么? 答案是 BeanFactoryPostProcessor
 * 2.1 BeanFactoryPostProcessor 对BeanFactory完成后置处理 的大体逻辑是什么?
 *
 * 3. BeanDefinition已经完全准备完毕, 剩下就开始正在的拿这些模板来创建Bean
 * 3.1 Bean 和 实例对象的区别?
 * 3.2 Bean的大体创建过程, 或者说 Spring Bean的声明周期
 * 3.3 BeanPostProcessor的扩展, 对创建的Spring对象完成的扩展
 *
 * 4. Spring IOC 的整个流程基本完毕, 还有其他补充吗?
 *
 * @author: jinyun
 * @date: 2021/4/17
 */
public class AnnotationConfigApplicationContextTest {
    /**
     * 直接实现了   AnnotationConfigRegistry
     * 同时也 {@link GenericApplicationContext} 这个类就是 IOC的持久对象
     * 它仅仅持久了BeanFactory的核心对象, 但是仅仅是 GenericApplicationContext 对象有了它确实可以访问IOC
     * 但是它 没有写入能力, 没有初始化的能力.
     * 所以 需要 下面这个 AnnotationConfigApplicationContext
     * 然后这也是Spring 包装设计模式的典型应用啊、
     */
    public final static AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();


    // -----------AnnotationConfigRegistry: 基于注解的BeanDefinition 注册到 BeanDefinitionRegistry------------------------
    /**
     * see {@link AnnotationConfigRegistry} context的直接实现
     * see {@link AnnotatedBeanDefinitionReader} {@link BeanDefinitionRegistry}
     *
     *
     * see {@link AnnotatedGenericBeanDefinition}
     * see {@link AnnotatedTypeMetadata}
     *
     * 主要看 AnnotationConfigRegistry 这个的实现 register()
     * 通过这个方法我们可以看到 由一个Class对象交给Context, 通过Context的注解解析系统, 这中间最重要的就是@Condition 这个条件注解的
     * 整体解析过程了  这一块也是 Spring 的核心子模块, todo: 探索整个 @Condition
     * 解析成 注解配置的bean定义
     * 然后最终注册到 DefaultListableBeanFactory 的 BeanDefinitionMap 中, 准备好IOC的原材料;
     */
    @Test
    public void registerTest() {
        // AnnotationConfigRegistry的接口, 所以这个方法的目的就是扫描注解
        context.register(EntryApplication.class);

        // 等效于 都会注册这个bean
        AnnotatedBeanDefinitionReader reader = new AnnotatedBeanDefinitionReader(context);
        reader.registerBean(EntryApplication.class);

        // 通过一些的解析 构造beanDefinition对象 最终通过 BeanDefinitionHolder 注册到 BeanDefinitionRegistry
        // 我们的 context 就是 BeanDefinitionRegistry的注解实现类；
        // registerBeanDefinition(primaryName, BeanDefinition)
        // 而我们看到源码可以发现 它就是个委托类。实际上是 包装设计模式. 就是delegateBeanFactory来最终存储BeanDefinition

        // todo: 明天简单的画下这个BeanDefinition的 设计模式 包装设计模式？
        // BeanDefinitionRegistry的实现类： GenericApplicationContext , DefaultListableBeanFactory
        // GenericApplicationContext 委派 DefaultListableBeanFactory
        // AnnotationConfigApplicationContext 是 GenericApplicationContext 的扩展子类


        // DefaultListableBeanFactory 完成 registryBeanDefinition(primaryBeanName, BeanDefinition)
        // 如果已经有了判断一下 进行覆盖, exist condition: map.get(primaryBeanName)  bean的名字是唯一标识
        // 如果IOC还没有进行bean的创建
        // beanDefinitionMap.put(name, def) & beanDefinitionNameSet.add(name)

        // 如果已经开始创建bean了, 就加锁然后重新 赋值 这块比较简单 先不看了.
    }

    /**
     * 测试这个特殊的注解 {@link EntryApplication} 的bean的具体细节
     *
     * 明确注册bean
     */
    @Test
    public void springBootApplicationTest() {
        // 先不加注解 可以直接扫描进入 当成一个普通的bean
        // 加注解：变化仅仅是 AnnotatedBeanGenericDefinition 中的 metadata (StandardAnnotationMetata) 中的 mergedAnnotations (TypeMappedAnnotations)
        // TypeMappedAnnotations: 可以解析注解完成 在这个对象中存储;
        // 但是这个方法仅仅是new, 把当前的class存在这个对象里面了, Annotation[] 里面啥都没有;
        context.register(EntryApplication.class);
    }

    /**
     * 正常情况下 我们不会 enumerate register bean
     *
     * 而是首先通过路径扫描的情况  ClassPathScanner
     * 下面的这个方法就是这样走的;
     * 主要看 这个真正干活的 ClassPathScanner 过滤扫描那些类?
     * see {@link ClassPathScanningCandidateComponentProvider}
     * see {@link ClassPathBeanDefinitionScanner} 这个是对外暴露 classPath bean定义的扫描器; 它继承了上面这个类;
     * 创建这个 class path BeanDefinition 扫描器 会注入一个默认的 AnnotationTypeFilter(Component.class)
     *
     * isCandidateComponent(metadataReader): 这个就是核心过滤方法;
     *
     * SimpleAnnotationMetadata: 扫描之后的注解封装成这个对象;
     *
     * 这块先不看了主要就是扫描 @Component 注解的类完成 BeanDefinition的注入;
     * 在这个filter中同时走了 @Condition() 的逻辑判断;
     * Set<BeanDefinition> scanCandidateComponents(String basePackage): 这个就是扫描候选组件
     *
     * 大体流程就是通过 resourceLoader 把 basePackage 下的class都读取进来 然后逐个过滤 然后 解析成BeanDefinition
     * 过滤完之后的 class 对象list 走的就是上面 register() 一样的逻辑了;
     */
    @Test
    public void scanTest() {
        context.scan("org.example.beans.bean.scan", "org.app.config");
    }


    /**
     * 这个工具类啊  有点东西的
     * 配置类 工具
     *
     * Set<String> candidateIndicator 这个候选指示器
     * 主要显式声明了 @Component @ComponentScan @Import @ImportResource
     * 这4个注解：
     */
    @Test
    public void configurationClassUtilTest() {
        // AnnotationMetadata 继承了 ClassMetadata 和 AnnotatedTypeMetadata.
        // ClassMetadata: 定义了类相关的信息
        // AnnotatedTypeMetadata: 定义了注解相关的信息

        // AnnotationMetadata: 实际上就是聚合了两个功能, 可以让我们对一个Class的注解相关信息清晰明了啊

        // 通过BeanDefinition,  --> order  通过 attribute 来获取 好像有一个 ConfigurationClassPostProcessor.
        // 通过AnnotationMetadata, --> order 通过 @Order 注解的 value

        // isConfigurationCandidate(AnnotationMetadata)
        // 如果有上面4中注解就 true, 如果没有再看是否有加了 @Bean 的方法
        // AnnotationMetaData.isAnnotated(String annotationName);
        // ==> 这个方法实际上 AnnotatedTypeMetadata 的方法,  这个注解相关的接口抽象还有一个核心方法：
        // ==> getAllAnnotationAttributes(String annotationName, boolean classValuesToString)
        // 看样子是获取所有的注解的属性： 通过注解的全限定性名称

        // AnnotationMetaData.hasAnnotatedMethod(String annotationName)
    }

    // -----------AnnotationConfigRegistry: 基于注解的BeanDefinition 注册到 BeanDefinitionRegistry -----------------




    // -----------SingletonBeanRegistry: 可以直接把 beanName, Singleton 直接注册到IOC -------------------------------------










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
