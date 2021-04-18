package org.example.beans.bean;

import org.example.beans.bean.register.DefaultBeanLifecycle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.function.Supplier;

/**
 * <p>
 * see {@link AbstractAutowireCapableBeanFactory} extends AbstractBeanFactory
 * implements AutowireCapableBeanFactory
 * 这个类 用于真正创建真正的实例bean.  see {@link RootBeanDefinition} a unified bean definition view at runtime.
 * </p>
 * <p>
 * 这个抽象默认实现了 {@link AbstractAutowireCapableBeanFactory#createBean(java.lang.Class)} 默认实现了这个接口
 * {@link AutowireCapableBeanFactory#createBean(java.lang.Class)}
 * <ul>
 *     <li>
 *         1. 通过一个类的字节码文件, 创建一个 RootBeanDefinition, set prototype, allowCaching.
 *     </li>
 *     <li>
 *         2. 它调用了 {@link AbstractBeanFactory} 的  createBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args)
 *         这个实现方法, 中间有一个bean的拦截方法, 可以通过postProcessor提前返回bean的机会
 *         正常的是调用 doCreateBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args);
 *     </li>
 * </ul>
 * <p>
 * 其中 调用 有一个创建实例(仅仅是对象实例, 刚刚分配了内存, 进行简单的初始化, 并没有走完整个生命周期)的方法： createBeanInstance(,,)
 * 这个方法会根据如下优先权创建对象实例：
 * <ul>
 *     <li>
 *         1. instanceSupplier 这个对象生产者 如果有直接生产对象返回 see {@link Supplier}
 *     </li>
 *     <li>
 *         2. factoryMethodName 通过工厂方法, string 如果有 就是使用工厂方法创建 对象实例
 *
 *         <ul>
 *             <li>ConfigurationClassBeanDefinition 发现了这个内部类, 那么这个类的初始化过程呢？
 *              这是一个私有的内部类, 显然是给spring内部自己用的, 但是我们创建自己需要的对象的时候
 *              使用了factoryMethod进行创建  用到了这个类：
 *              通过debug发现这又是另一个流程了  AbstractApplicationContext.invokeBeanFactoryPostProcessors(beanFactory)
 *             </li>
 *         </ul>
 *     </li>
 *     <li>
 *         3. 使用有参构造方法进行创建对象实例
 *     </li>
 *     <li>
 *         4. 使用默认的无参构造方法创建对象实例, 这个是最基本的底线了
 *     </li>
 * </ul>
 * 走完这个方法实际上就已经创建出这个对象实例了.
 * <p>
 * ... 待探索...
 * <p>
 * 然后就是需要对这个对象实例 和 这个对象的类型
 * 填充属性,
 * 初始化bean
 * <p>
 * 最后一步： register bean as disposable.
 * 返回 exposedObject.
 * </p>
 *
 * @author: jinyun
 * @date: 2021/4/6
 */
public class CreateBeanTest {


    /**
     * <p>
     * 这个方法就是执行 bean工厂的后置处理器， 这些后置处理器已经注册成了bean在这个上下文中
     * 这个方法创建 并  执行所有的  bean工厂的后置处理器, 这个方法在进行 单例实例化之前一定要调用
     * </p>
     * <p>
     * <h2>Bean工厂的后置处理器 对 Bean 工厂实例化之后进行增强 </h2>
     * see {@link AbstractApplicationContext#getBeanFactoryPostProcessors()}
     * 抽象的应用上下文维护了一个初始化的 BeanFactoryProcessors 的空列表, ?
     * 什么时候解析填充了这个 bean工厂处理器呢？
     * <p>see {@link AbstractApplicationContext#addBeanFactoryPostProcessor(org.springframework.beans.factory.config.BeanFactoryPostProcessor)}</p>
     *
     * <pre>
     * ==> SpringApplication#prepareContext(DefaultBootstrapContext, ConfigurableApplicationContext,
     *                                      ConfigurableEnvironment, SpringApplicationRunListeners, ApplicationArguments, Banner)
     * ==> void applyInitializers(ConfigurableApplicationContext context)
     * ==> ApplicationContextInitializer.initialize(context)
     *      ==> ApplicationContextInitializer 是怎么初始化的呢?
     *      ==> 这他妈又是一个子问题?
     *      ==> 这个就是 AbstractApplicationContext 的 beanFactoryPostProcessors 填充内置的处理器
     * </pre>
     * <p>
     *
     *
     * <h2>SpringApplication的实例化做了那些事情? </h2>
     * 目前先看比较重要的一点, ApplicationContextInitializer实现实例保存到SpringApplication里面先保存着
     * 这个通过 SpringFactoriesLoader.loadFactories() 加载 META-INF/spring.factories 下面配置的 应用上下文初始化器加载进来
     * 默认有8个.
     * 终于发现其中的一个: SharedMetadataReaderFactoryContextInitializer 这个是 BeanDefinitionRegistryPostProcessor
     * <p>
     * 而且这个 invokeBeanFactoryPostProcessors(beanFactory) 这个beanFactory 我们用的是 ConfigurableListableBeanFactory
     * 这个方法委派一个类完成具体的 invokeBeanFactoryPostProcessors
     *
     * <p>see {@link PostProcessorRegistrationDelegate#invokeBeanFactoryPostProcessors(org.springframework.beans.factory.config.ConfigurableListableBeanFactory, java.util.List)}</p>
     * <ul>
     *  <li>
     *      <p>执行所有的beanFactoryPostProcessor, 直接调用了一个静态方法，</p>
     *      <p>list_size = 3; 两种类型： BeanFactoryPostProcessor, BeanDefinitionRegistryPostProcessor</p>
     *      <p>通过类图可以看到 有 bean定义注册中心的后置处理器 是 bean 工厂的子类。  这么设计, 说明 bean定义的注册中心同样有 bean工厂的处理方法</p>
     *      <p>通过分类：首先选出 Registry的后置处理器 2个， 然后上层的 bean工厂的后置处理器 1个,
     *              这里就是把internal的类过滤掉, 然后紧紧在后面实例过滤解析我们定义的Bean (@Configuration or @Component),
     *              先不看具体的实现</p>
     *      <p>然后通过IOC {@link ListableBeanFactory#getBeanNamesForType(java.lang.Class, boolean, boolean)}
     *              getBeanNamesForType(Registry.class, true, false) 说明获取已经实例化的 Registry的beanNames
     *              看看已经被IOC管理起来的bean 只有 {@link AnnotationConfigUtils#CONFIGURATION_ANNOTATION_PROCESSOR_BEAN_NAME}
     *              internalConfigurationAnnotationProcessor.
     *              </p>
     *      <p>{@link BeanFactory#isTypeMatch(java.lang.String, java.lang.Class)} 这里写死 PriorityOrdered 必须是这个优先权的子类,
     *              也就是实现了这个接口的类，说明一旦你实现了好多的接口交给IOC创建它就能找到你这个类是不是一个特定接口的子类。
     *              具体需要测试，然后探究这块是如何解析 和 设计的 ???
     *              internalConfigurationAnnotationProcessor 这个类的 bean 是 ConfigurationClassPostProcessor
     *              {@link ConfigurationClassPostProcessor} 这就是一个核心类
     *              </p>
     *      <p>invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry, beanFactory.getApplicationStartup());
     *              然后就是看 ConfigurationClassPostProcessor的 postBeanDefinitonRegistry(registry) 这个方法；
     *              processConfigBeanDefinitions(Registry): 就是处理配置的 beanDefinitions, 下面细节探究
     *      </p>
     *      <p>所以 对 {@link Configuration} 核心处理来了,
     *              BeanDefinitionRegistry.getBeanDefinitionNames();
     *              获取所有的beanDefinitionNames. 目前bean相关定义的类有6个internal, 一个Main Class.
     *              ConfigurationClassParser: 这就是解析(parse) 每一个@Configuration 的类：
     *      </p>
     *  </li>
     *
     *
     * <p>
     * <ul/>
     *
     * <h3>我们具体看一下这个类 ConfigurationClassParser </h3>
     * <ul>
     *     <li>MetadataReaderFactory: 元数据读取器工厂, 肯定就是用来创建 元数据读取器的，但是它允许为每个original resource缓存一个metadataReader</li>
     *     <li>CachingMetadataReaderFactory: 实际上是使用的这个</li>
     *     <li>ProblemReporter: 这个类忽略</li>
     *     <li>ResourceLoader: 这个就是ApplicationContext, AbstractApplicationContext</li>
     *     <li>BeanNameGenerator: 这个类就是专门为bean创建name的， 比如我们@Bean 就不问了这种, 这个用的是 {@link AnnotationBeanNameGenerator}
     *              这个类很简单，也是根据BeanDefinition. 如果是AnnotatedBeanDefinition.
     *     </li>
     *
     *     <li>
     *         需要用到 parse(Set<BeanDefinitionHolder>): Holder 就是一个简单的封装，one Name some aliases ==> BeanDefinition.
     *         需要知道这几个BeanDefinition的子类：
     *         AnnotatedBeanDefinition： 可以获取这个类的 注解元数据 and 方法元数据 这是一个接口
     *         AbstractBeanDefinition： 这是一个抽象类： 模板实现了 Bean定义的很多属性
     *     </li>
     *     <li>
     *         processConfigurationClass(ConfigurationClass,Predicate)
     *         doProcessConfigurationClass() 这个方法了不得了 是我们经常看到的注解的核心处理方法
     *     </li>
     *
     *     <li>
     *         从这个类的 sourceClass 上面扫描一个遍 类注解, 成员上的注解，方法上的注解  这个方法可以被调用很多次
     *         1. 第一个是这个类上面有没有加@Component, 好像是处理 内部成员类？
     *         2. 处理这个类：@PropertySource, 暂时先不看
     *         3. 处理 @ComponentScan
     *         4. 处理  @Import
     *         5. 处理  @ImportResource
     *         6. 处理  @Bean
     *         7. 处理接口
     *         8. 处理父类
     *
     *         上面这些情况我们后面一点一点的测试：
     *     </li>
     *
     *     <li>
     *
     *     </li>
     * </ul>
     *
     *
     *
     * <p>see {@link BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry(org.springframework.beans.factory.support.BeanDefinitionRegistry)}</p>
     *
     * <pre>
     * ==> {@link ConfigurationClassPostProcessor#postProcessBeanDefinitionRegistry(org.springframework.beans.factory.support.BeanDefinitionRegistry)}
     * ==> this.reader = new ConfigurationClassBeanDefinitionReader
     * ==> invokeBeanDefinitionRegistryPostProcessors()
     * </pre>
     *
     * <h3>这个接口也是 beanDefinitionRegistry. 所以这个对象会后置 处理 这个beanFactory. 干了啥事呢？<h3/>
     * <ul>
     *     <li>
     * <p>
     * <p>
     * <p>
     * 1> org.springframework.boot.autoconfigure.internalCachingMetadataReaderFactory, SharedMetadataReaderFactoryBean (beanDefiniton)
     *    把这个beanDefiniton 注册到 bean定义中心
     *    </li> <li>
     * 2> configureConfigurationClassPostProcessor(registry); 这个方法就尤为重要了
     *    配置 配置类的后置处理器
     *    beanDefinitonRegistry.getBeanDefinition(org.springframework.context.annotation.internalConfigurationAnnotationProcessor)
     *    这个bean定义是 BeanDefinitionRegistryPostProcessor 处理bean定义注册的后置处理器
     * <p>
     *    获取的这个BeanDefinition: ConfigurationClassPostProcessor.
     *    啥事没干, 就是在这个BeanDefinition.getPropertyValues().add("metadataReaderFactory", new RuntimeBeanReference(BEAN_NAME));
     * </li></ul>
     * <p>
     * <h2>ConfigurationClassPostProcessor 重点需要看这个 配置类的后置处理器</h2>
     * 这个类就是用于解析 @Configuration 的。
     */
    public void invokeBeanFactoryPostProcessors() {
    }

    /**
     * 如何测试呢？
     *
     * 首先看了这个类： {@link AnnotationConfigWebApplicationContext}
     *
     *
     * 但是这个类中简单了说明了下，这个类提供了web环境， 现在我们仅仅是测试 bean 对象相关的代码, 而且是 根据注解系统相关的bean 而已
     * 因为我们目前基于spring开发 都是通过 <b>注解</b> ==> BeanDefinition ==> BeanDefinitonRegistry ==> BeanFactory ==> Bean
     *
     * 所以我们使用 {@link AnnotationConfigApplicationContext} 这个最基建 的类：
     * 注解配置的应用 上下文
     */
    @Test
    public void AnnotationConfigApplicationContextTest() {
        // 我们创建这个 注解配置的应用上下文, 实际上它delegate 两个功能类： 这俩个功能类绑定到这个上下文
        // 我们无法修改 这两个核心 worker
        // 我们目前就看 AnnotatedBeanDefinitonReader 用它来实例化我们的注解Bean对象
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
        // 这个类可以 @Component 或者 @Configuration 相关的注解bean

        // 我们先测试一个简单的 @Component 不好搞先直接注册一个简单的 Class 字节码形式的bean吧
        // 这是我们定义的简单的bean, 其中还扩展了很多个接口
        ac.register(DefaultBeanLifecycle.class);
        ac.refresh();
        DefaultBeanLifecycle defaultBeanLifecycle = ac.getBean("defaultBeanLifecycle", DefaultBeanLifecycle.class);
        assert defaultBeanLifecycle != null;

        // 回调关闭资源
        ac.close();
    }
}
