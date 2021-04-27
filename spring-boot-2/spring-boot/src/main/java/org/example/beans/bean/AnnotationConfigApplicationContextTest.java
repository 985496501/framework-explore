package org.example.beans.bean;

import org.example.EntryApplication;
import org.example.beans.bean.cycle.A;
import org.example.beans.bean.cycle.B;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.lang.reflect.Modifier;

/**
 * 1. 首先探索完成 BeanDefinition 的创建 然后注册到 BeanDefinitionRegistry的过程;
 * 1.1 注册的源头是什么, 什么样的class才会被封装成BeanDefinition?
 * 1.2 @Component 的作用, @Configuration 的作用, 与@Component的区别 ?
 * 1.3 @Condition 注解在BeanDefinition的创建之前完成的拦截作用
 * <p>
 * 2. 在BeanFactory中有了BeanDefinition之后, 下一步的处理应该是什么? 答案是 BeanFactoryPostProcessor
 * 2.1 BeanFactoryPostProcessor 对BeanFactory完成后置处理 的大体逻辑是什么?
 * <p>
 * 3. BeanDefinition已经完全准备完毕, 剩下就开始正在的拿这些模板来创建Bean
 * 3.1 Bean 和 实例对象的区别?
 * 3.2 Bean的大体创建过程, 或者说 Spring Bean的声明周期
 * 3.3 BeanPostProcessor的扩展, 对创建的Spring对象完成的扩展
 * <p>
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
     * <p>
     * <p>
     * see {@link AnnotatedGenericBeanDefinition}
     * see {@link AnnotatedTypeMetadata}
     * <p>
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
     * <p>
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
     * <p>
     * 而是首先通过路径扫描的情况  ClassPathScanner
     * 下面的这个方法就是这样走的;
     * 主要看 这个真正干活的 ClassPathScanner 过滤扫描那些类?
     * see {@link ClassPathScanningCandidateComponentProvider}
     * see {@link ClassPathBeanDefinitionScanner} 这个是对外暴露 classPath bean定义的扫描器; 它继承了上面这个类;
     * 创建这个 class path BeanDefinition 扫描器 会注入一个默认的 AnnotationTypeFilter(Component.class)
     * <p>
     * isCandidateComponent(metadataReader): 这个就是核心过滤方法;
     * <p>
     * SimpleAnnotationMetadata: 扫描之后的注解封装成这个对象;
     * <p>
     * 这块先不看了主要就是扫描 @Component 注解的类完成 BeanDefinition的注入;
     * 在这个filter中同时走了 @Condition() 的逻辑判断;
     * Set<BeanDefinition> scanCandidateComponents(String basePackage): 这个就是扫描候选组件
     * <p>
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
     * <p>
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


    // ------- ConfigurableApplicationContext refresh() debug 看创建对象如何解决循环依赖 -----------------------------------

    /**
     * refresh(): {@link AbstractApplicationContext#refresh()}
     * <p>
     * 1. prepareRefresh(): 修改上下文状态量; 等等, 这块的代码主要就是看 GenericWebApplicationContext
     * 这里涉及到属性填充的问题, 初始化servlet等属性, web 这servlet是必须的;
     * 2. 创建一个BeanFactory 默认就是 DefaultListableBeanFacoty, 给这个BeanFactory设置一个ID
     * 这个工厂默认在 GenericApplicationContext 创建这个对象就会默认创建一个bean工厂： new DefaultListableBeanFactory()
     * 3. prepareBeanFactory(ConfigurableListableBeanFactory): 为这个上下文准备Bean工厂
     * >设置类加载器
     * ...
     * >设置 beanPostProcessor(ApplicationContextAwareProcessor) 通过ApplicationContext设置回调接口Aware
     * - EnvironmentAware
     * - EmbeddedValueResolverAware {@link EmbeddedValueResolver}
     * - ResourceLoaderAware    [applicationContext]
     * - ApplicationEventPublisherAware  [applicationContext]
     * - MessageSourceAware     [applicationContext]
     * - ApplicationContextAware    [applicationContext]
     * <p>
     * >设置忽略DI, {@link ConfigurableListableBeanFactory#ignoreDependencyInterface(java.lang.Class)}
     * 就是设置的上面的这几个接口的值 6 种类型, 就是这些类型忽略 DI autowiring;  Doc: 默认就是忽略BeanFactoryAware
     * <p>
     * >注册解析的依赖, {@link ConfigurableListableBeanFactory#registerResolvableDependency(java.lang.Class, java.lang.Object)}
     * 如何实现的呢, 给一个默认的类型和值; 要求这个值必须是给的执行的类型 {@link Class#isInstance(java.lang.Object)}
     * 或者是对象工厂类型, 是 {@link ObjectFactory} 才会填入到 Map from dependency type to corresponding autowired value;
     * T, ObjectFactory<T> 就通过这个工厂获取对应的被 注入 对象;
     * <p>
     * >直接注入环境的单例bean; Spring相关环境相关变量维护{@link StandardEnvironment}
     * - environment, StandardEnvironment
     * - systemProperties, map, 这个是JVM的相关信息, JVM同时也会从OS获取一部分环境变量 System.getProperties()
     * - systemEnvironment, map  这是系统环境相关的变量 比如Window path, OS的相关信息  (Map) System.getenv()
     * <p>
     * <p>
     * <p>
     * 4. 执行 已经注册到这个上下文的 工厂处理器
     * 定义: {@link ConfigurableApplicationContext#addBeanFactoryPostProcessor(org.springframework.beans.factory.config.BeanFactoryPostProcessor)}
     * invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory)
     * PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());
     * <p>
     * AbstractApplicationContext 中维护了所有的 BeanFactoryProcessor;  这里进行了维护;
     * 这个就是核心方法  执行工厂的处理器, 最主要的就是 ConfigurationClassPostProcessor;
     * {@link ConfigurationClassPostProcessor} 它不仅可以处理BeanDefinition 还可以处理BeanDefinitionRegistry
     * 上面这是个很大的一块功能点, 具体看 {@link Configuration} 的解析过程; 这整个第四步耗时很长, 执行的逻辑很多
     * <p>
     * 5. 注册bean的处理器, 拦截bean的创建;
     * 定义: {@link ConfigurableBeanFactory#addBeanPostProcessor(org.springframework.beans.factory.config.BeanPostProcessor)}
     * 我们直接看 {@link AbstractBeanFactory#addBeanPostProcessor(org.springframework.beans.factory.config.BeanPostProcessor)} 的具体方法：
     * <p>
     * 6. ...
     * <p>
     * <p>
     * 7. 直接看创建bean的过程:
     * finishBeanFactoryInitialization(ConfigurableListableBeanFactory)
     * 是由可配置的列表化的bean工厂来创建单例对象;
     * 定义: {@link ConfigurableListableBeanFactory}
     * {@link ConfigurableListableBeanFactory#preInstantiateSingletons()} 配置的列表的bean工厂
     * 进行发起所有的单例对象的创建, 要去在bean启动的最后阶段创建, 如果有一个bean创建失败, 应该调用
     * {@link ConfigurableBeanFactory#destroySingletons()} 把bean全部销毁, 释放所有的资源;
     * DefaultListableBeanFactory的默认实现:
     * 首先获取beanDefinitionNames, 是通过copy一个副本进行操作的; 目的在于可能会有新的bean定义会加入到集合中;
     * 这里保存<name, object> 同时把name都拿出来标示, HttpRequest 也这样设计；
     * 循环这个names,  通过 Map<String, RootBeanDefinition> mergedBeanDefinitions 获取rootBeanDefiniton;
     * <p>
     * 条件：不是抽象的, 单例的, 非懒的
     * 两个分支：
     * FactoryBean, 是一种特殊的bean, 只不过它 通过它是获取的产生的对象;
     * 普通Bean, getBean(beanName) 就正式开始了bean的创建过程; {@link BeanFactory#getBean(java.lang.String)}
     * <p>
     * 模板实现是 AbstractBeanFactory的 doGetBean(name, requiredType, args, typeCheckOnly)
     * 主要看这个方法：
     * 首先检查手动注册的bean, 这个先不看
     * prototypesCurrentlyInCreation 如果当前线程如果保存这个 beanName 直接抛出异常;
     * typeCheckOnly: false 一开始创建这个bean就会把beanName放到 Set<String> alreadyCreated
     * 取出RootBeanDefinition 检查定义
     * 1. 看是否有 DependsOn String[] 如果有就先创建这个bean;
     * 2. 如果是单例的, 就调用 getSingleton(String beanName, ObjectFactory<?> singletonFactory)
     * singletonsCurrentlyInCreattion.remove(beanName) 查看正在创建的对象;
     * <p>
     * {@link DefaultSingletonBeanRegistry#getSingleton(java.lang.String,
     * org.springframework.beans.factory.ObjectFactory)}
     * 这个 singletonFactory.createBean(beanName, RootBeanDefinition, args):
     * resolveBeanClass(mdb, beanName): 看看这个bean有什么源class对象;
     * <p>
     * 准备定义中的 overrides的 方法; todo:
     * <p>
     *
     *
     * <p>
     * 真正创建bean; {@link AbstractAutowireCapableBeanFactory}
     * doCreateBean(beanName, RootBeanDefinition, args): 这个方法 解决了 循环依赖 的麻烦;
     *
     * 这个方式就是实际创建bean的过程, Pre-creation 在这已经发生了
     * 然后这个方法会选择 是使用默认bean实例化，还是使用工厂方法, 还是使用构造方法自动注入3中方式;
     * <p>
     * 创建实例对象:
     * createBeanInstance():
     * 必须有class对象, 这个类必须是public, 这个beanDefinition 必须允许获取获取非public权限
     * 创建对象：
     *  1. instanceSupplier
     *  2. factoryMethod
     *  3. @Autowired
     *  4. default constructor, autowire Constructor
     *  5. instantiateBean(beanName, mbd)
     *
     *
     *  今天必须把 cycle dependency;
     *
     *
     *  具体看这个方法:
     *  {@link InstantiationStrategy#instantiate(org.springframework.beans.factory.support.RootBeanDefinition,
     *                              java.lang.String, org.springframework.beans.factory.BeanFactory)}
     *
     *  处理循环依赖的方法 {@link AbstractAutowireCapableBeanFactory}  doCreateBean(x, x, x)
     *  实例化对象就仅仅是使用合适的方法 把 这个 beanName 的对象创建出来, 返回这个  BeanWrapper<类型, 这个单例对象>
     *
     *      1. 开始处理循环依赖: 这里处理循环依赖必须是单例对象, 而且当前这个抽象注入能力的bean工厂 允许循环依赖  allowCircularReferences,
     *  而且上面这个这个beanName 已经预存在 singletonsCurrentlyInCreation. 那么这个beanName是什么时候塞进这个单例正在创建中呢?
     *  beforeSingletonCreation(String beanName) 将beanName放入到 singletonsCurrentlyInCreation;
     *  这里有一个变量是 earlySingletonExposure, 这个就是是否提前暴露单例引用： 也是处理循环依赖的开关;
     *
     *  在上面这个方法中, 如果必须 true && true, 后面就是在  单例对象正在创建中 添加一定成功, flase;
     *  前面还有一段判断， inCreationCheckExclusion
     *  什么情况才会报正在创建中的错误? 添加正在创建单例对象失败, 并且这个对象名还没有包括在创建检查中 就会报错;
     *
     *  在这里方法中调用 {@link DefaultSingletonBeanRegistry#getSingleton(java.lang.String, org.springframework.beans.factory.ObjectFactory)}
     *      2. 如果满足上面的条件, 就添加了一层缓存, addSingletonFactory(beanName, singletonFactory) DefaultSingletonBeanRegistry中的方法;
     *      可以看出这个 默认的单例bean注册中心; 维护了单例池; 单例对象工厂;
     *  加锁, 同步所有线程的方法, singletonObjects 单例池:
     *  singletonFactories.put(beanName, singletonFactory);
     *  earlySingletonObjects.remove(beanName); 但是这个好像是没有值的? 那么问题来了这个 earlySingletonObjects 是淦什么用的?
     *  registeredSingletons.set(beanName) 把这个单例bean放入已经注册池中;
     *
     *
     * 后面就开始填充bean属性, 然后 initializeBean()
     *
     * afterSingletonCreation(beanName): 这就是把正在创建的单例直接移除;
     *
     * addSingleton(bean, singleton):
     * 单例池put;
     * 单例工厂remove;
     * 提前引用单例池remove;
     * 注册的单例add;
     */
    @Test
    public void getBeanTest() {
        // context.register(EntryApplication.class);
        // getBean之前必须先注册BeanDefinition;
        // 直接getBean() 首先就是校验BeanFactory是否已经激活, 然后调用BeanFactory.getBean(Class) 激活必须refresh()
        context.register(A.class);
        context.register(B.class);
//        context.refresh();
        DefaultListableBeanFactory defaultListableBeanFactory = context.getDefaultListableBeanFactory();
        A bean = defaultListableBeanFactory.getBean(A.class);
        // 通过上下文getBean() 会初始化整个项目中的bean对象, 还必须refresh();
//        A bean = context.getBean(A.class);
        System.out.println(bean);
    }


    @Test
    public void classTest() {
        // public, protected, private, final, static, abstract and interface;
        int modifiers = AnnotationConfigApplicationContextTest.class.getModifiers();
        System.out.println(modifiers);

        System.out.println("is public: " + Modifier.isPublic(modifiers));
    }

}
