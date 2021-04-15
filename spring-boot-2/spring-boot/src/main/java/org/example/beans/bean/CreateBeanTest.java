package org.example.beans.bean;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.AbstractApplicationContext;

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
     * see {@link AbstractApplicationContext#addBeanFactoryPostProcessor(org.springframework.beans.factory.config.BeanFactoryPostProcessor)}
     *
     * ==> SpringApplication#prepareContext(DefaultBootstrapContext, ConfigurableApplicationContext,
     *                                      ConfigurableEnvironment, SpringApplicationRunListeners, ApplicationArguments, Banner)
     * ==> void applyInitializers(ConfigurableApplicationContext context)
     * ==> ApplicationContextInitializer.initialize(context)
     *      ==> ApplicationContextInitializer 是怎么初始化的呢?
     *      ==> 这他妈又是一个子问题?
     *      ==> 这个就是 AbstractApplicationContext 的 beanFactoryPostProcessors 填充内置的处理器
     * <p>
     *
     *
     * <h2>SpringApplication的实例化做了那些事情? </h2>
     * 目前先看比较重要的一点, ApplicationContextInitializer实现实例保存到SpringApplication里面先保存着
     * 这个通过 SpringFactoriesLoader.loadFactories() 加载 META-INF/spring.factories 下面配置的 应用上下文初始化器加载进来
     * 默认有8个.
     * 终于发现其中的一个: SharedMetadataReaderFactoryContextInitializer 这个是 BeanDefinitionRegistryPostProcessor
     *
     * 而且这个 invokeBeanFactoryPostProcessors(beanFactory) 这个beanFactory 我们用的是 ConfigurableListableBeanFactory
     * 这个方法委派一个类完成具体的 invokeBeanFactoryPostProcessors
     * see {@link PostProcessorRegistrationDelegate#invokeBeanFactoryPostProcessors(org.springframework.beans.factory.config.ConfigurableListableBeanFactory,
     *                                                                              java.util.List)}
     * see {@link BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry(org.springframework.beans.factory.support.BeanDefinitionRegistry)}
     * ==> {@link ConfigurationClassPostProcessor#postProcessBeanDefinitionRegistry(org.springframework.beans.factory.support.BeanDefinitionRegistry)}
     * ==> this.reader = new ConfigurationClassBeanDefinitionReader
     *
     *
     *
     * ==> invokeBeanDefinitionRegistryPostProcessors()
     * 这个接口也是 beanDefinitionRegistry. 所以这个对象会后置 处理 这个beanFactory.
     * 干了啥事呢？
     *
     *
     * 1> org.springframework.boot.autoconfigure.internalCachingMetadataReaderFactory, SharedMetadataReaderFactoryBean (beanDefiniton)
     *    把这个beanDefiniton 注册到 bean定义中心
     * 2> configureConfigurationClassPostProcessor(registry); 这个方法就尤为重要了
     *    配置 配置类的后置处理器
     *    beanDefinitonRegistry.getBeanDefinition(org.springframework.context.annotation.internalConfigurationAnnotationProcessor)
     *    这个bean定义是 BeanDefinitionRegistryPostProcessor 处理bean定义注册的后置处理器
     *
     *    获取的这个BeanDefinition: ConfigurationClassPostProcessor.
     *    啥事没干, 就是在这个BeanDefinition.getPropertyValues().add("metadataReaderFactory", new RuntimeBeanReference(BEAN_NAME));
     *
     * <p>
     * <h2>ConfigurationClassPostProcessor 重点需要看这个 配置类的后置处理器</h2>
     * 这个类就是用于解析 @Configuration 的。
     *
     *
     */
    public void invokeBeanFactoryPostProcessors() {
    }
}
