package org.example.mvc.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.ServletContext;

/**
 * 1. 在这里我们细节的学习 webMVC 相关的配置
 * 首先是Servlet3.0规范, 一个接口 {@link WebApplicationInitializer} spring.framework.web MVC的重要接口
 * 但是我们自定义实现这个接口 然后声明成 spring bean 但是回调方法没有触发 springboot.
 *
 *
 * 这个就是配置自己的servlet {@link DispatcherServlet}, listener, filter
 * 加入到一个 web全局的上下文中去 {@link ServletContext} javax.servlet jdk的规范已经写入了
 *
 * 2. DispatcherServlet 这个servlet对检查 webApplicationContext 中的 special bean,
 * 这个类是 Servlet的封装 这个类图 很 全面, 对这个类已经要看这个类图 然后逐步分析它的继承体系。
 * 它是servlet 和 servletConfig 的聚合子类. 它具备 environment 的读写权限, 并且能获取 applicationContext.
 * 就是固定类型的bean, 比如定义的接口类型：
 * {@link HandlerMapping}
 * {@link HandlerAdapter}
 * 这个servlet依赖于其他bean为它做 diverse functionality[u, 泛指 功能，计算机功能]
 * 如果没有找到特殊的bean, 它就会再 DispatcherServlet.properties 里进行初始化默认的特殊bean
 *
 * 3. DispatcherServlet的实例化：
 * AbstractApplicationContext # refresh()
 * onRefresh();
 * 我们使用的AnnotationConfigServletWebServerApplicationContext
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
 *
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
 *
 * @author: Liu Jinyun
 * @date: 2021/4/4/0:10
 */
public class WebMvcConfig {
    public static void main(String[] args) {

    }
}
