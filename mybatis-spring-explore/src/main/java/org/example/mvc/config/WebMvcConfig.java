package org.example.mvc.config;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.web.servlet.ServletContextInitializerBeans;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.ServletContext;

/**
 * <p>
 * 1. 在这里我们细节的学习 webMVC 相关的配置
 * 首先是Servlet3.0规范, 一个接口 {@link WebApplicationInitializer} spring.framework.web MVC的重要接口
 * 但是我们自定义实现这个接口 然后声明成 spring bean 但是回调方法没有触发 springboot.
 *
 *
 * 这个就是配置自己的servlet {@link DispatcherServlet}, listener, filter
 * 加入到一个 web全局的上下文中去 {@link ServletContext} javax.servlet jdk的规范已经写入了
 *
 * <p>
 * 2. DispatcherServlet 这个servlet对检查 webApplicationContext 中的 special bean,
 * 这个类是 Servlet的封装 这个类图 很 全面, 对这个类已经要看这个类图 然后逐步分析它的继承体系。
 * 它是servlet 和 servletConfig 的聚合子类. 它具备 environment 的读写权限, 并且能获取 applicationContext.
 * 就是固定类型的bean, 比如定义的接口类型：
 * {@link HandlerMapping}
 * {@link HandlerAdapter}
 * 这个servlet依赖于其他bean为它做 diverse functionality[u, 泛指 功能，计算机功能]
 * 如果没有找到特殊的bean, 它就会再 DispatcherServlet.properties 里进行初始化默认的特殊bean
 *
 * <p>
 * 3. DispatcherServlet的实例化：
 * AbstractApplicationContext # refresh()
 * onRefresh();
 * 我们使用的 {@link AnnotationConfigServletWebServerApplicationContext} 基于注解配置的servlet web服务器应用的上下文
 * 它继承自 {@link ServletWebServerApplicationContext} 它是 servlet web server应用上下文.
 *
 * ServletWebServletApplicationContext.onRefresh()
 * createServer() [private];
 * -- webServer and servletContext == null, 检索查询webServerFactory
 * 这里知道为什么要使用工厂模式了吗？ 定义创建指定功能的接口进行标记 可以查询出来
 * 具体的子类实现哦，可以忽略具体实现。 定义一个顶级的工厂方法以及生产的这个对象的顶级功能接口。
 * interface ServletWebServerFactory {
 *     WebServer getWebServer(ServletContextInitializer...);
 * }
 * 通过3个接口规范了这整个 spring 嵌入式容器下的 ServletWebServer.
 * 抽象出这顶级接口之后 spring 实现了3种ServletWebServer
 * 1. tomcat
 * 2. jetty
 * 3. undertow
 *
 * <p>
 * -- DefaultListableBeanFactory.getBeanNamesByType(ServletWebServerFactory.class);
 * 来获取 DefaultListableBeanFactory里面有一个缓存 Map<Class, String[]> allBeanNamesByType;
 * getBeanFactory就是获取上面默认的可列表工厂 getBean(beanName, class) 获取指定名指定类型的bean
 * abstractBeanFactory.doGetBean();
 * DefaultSingletonBeanRegistry.getSingleton(beanName)
 *  单例池里面获取, 如果有直接返回 如果没有就从 创建中返回该对象的提前引用
 *  如果创建中也没有 就从 singletonFactory 中获取工厂方法， 工厂方法获取该对象
 *  然后工厂中remove掉，然后把这个对象添加到正在创建中，并返回。
 *
 *  这里有一个原子操作, 就是map的add remove读写操作多线程会产生并发问题
 *  就在这一系列操作加入了锁.
 *
 * 获取了Undertow的工厂然后创建了ServletWebServer
 * 但是需要 ServletContextInitializer
 *
 * <p>
 * 我们看下 这个ServletWebServer的启动过程：
 * 1. 创建外部配置的端口是这么传递进去的？
 * 2. spring定义的 javax.servlet.dispatcherServlet是怎么伪装进入ServletContext的？
 *
 * log:
 * 初始化 WebApplicationContext
 * Root WebApplicationContext:  创建完webServer实例, 仅仅是创建了对象
 * 还没有系统调用申请 net相关的资源, 比如端口号 和 线程等.
 *
 * <p>
 * see {@link ServletWebServerApplicationContext} 中 getServletContextInitializerBeans()
 * see {@link ServletContextInitializerBeans}  new ServletContextInitializerBeans(listableBeanFactory);
 * 这个创建的过程 是通过 beanFactory 获取 所有的 ServletContextInitializer
 * {@link ListableBeanFactory#getBeanNamesForType(java.lang.Class, boolean, boolean) 这个方法返回了
 * dispatcherServletRegistration;
 *
 * 所有我们要看下这个beanNames是如何初始化的?
 * 原来是自动装配进入的
 * see {@link DispatcherServletAutoConfiguration}
 * see {@link DispatcherServletRegistrationBean} 这个bean的   name=dispatcherServletRegistration
 * 并且它就是 ServletContextInitializer 的具体实现类; 通过自动装配的自动配置 创建了DispatcherServlet并把它封装
 * 到DispatcherServletRegistrationBean里面,
 *
 * <p>
 * 我们发现配置 ServletWebServer的配置类: {@link ServerProperties}
 * 我们一般就是配置server.port=8800
 * 还可以配置最大头部：
 * 还可以配置undertow的io线程 和 工作线程; 这些线程都是RUNNING状态;
 * 也就是无论有没有请求都在工作.
 *
 * 通过{@link WebMvcProperties} 配置 DispatcherServlet
 * 1. dispatchOptionsRequest = true 是否转发Options请求 到 frame.doService(), 默认是true
 * 2. dispatchTraceRequest = false 是否转发trace请求 到 doService(), 默认是false
 * 3. throwExceptionIfNoHandlerFound = false 如果找不到对应的handler是不是需要抛出异常, 默认false
 * 4. publishRequestHandledEvents = true 请求已经处理完是否发布时间, 默认是true
 * 5. logRequestDetails 是否打印 potentially sensitive request detail, debug, trace is allowed. 默认是false
 *
 *
 * <p>
 *
 *
 * {@link }
 *
 *
 *
 *
 *
 * @author: Liu Jinyun
 * @date: 2021/4/4/0:10
 */
public class WebMvcConfig {
    public static void main(String[] args) {

    }
}
