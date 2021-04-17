package org.example.spring.loader;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * Class URL represents a Uniform Resource Locator [统一资源定位符]
 * 在万维网中一个 指向资源的 指针, 那么它 就是万维网中标识一个资源的唯一定位坐标
 * <p>
 * http://www.baidu.com/doc/1.txt
 * protocol: http(HyperText Transfer Protocol)
 * host: dns域名, If the port is not specified, the default port for the protocol
 * is used instead. For example, http default port is 80. http://www.baidu.com:80/doc/1.txt
 * /doc/1.txt  /doc= path  1.txt URN
 * URI 统一资源标识符 它标识了唯一的资源
 * 一个最小的资源：必须需要URL先找到定位 然后通过其他标识找到URI
 *
 * @author: jinyun
 * @date: 2021/2/9
 */
public class MainLoaderTest {

    @Test
    public void getResourcesTest() throws IOException {
        // 直接使用应用类加载器 来获取所有的资源
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        // 指定了 子路径文件 就会扫描类加载器可以获取的URL
        // 1. 通过C的根类加载器加载
        // 2. 通过exc加载器加载
        // 3.通过应用类加载器 把java所有的类都加载进来
        Enumeration<URL> resources =
                systemClassLoader.getResources(SpringFactoriesLoader.FACTORIES_RESOURCE_LOCATION);

        // 枚举包装 这个容易真的不容易啊 我他妈开发重来不用！！！ 这种东西都是java1.0的老掉牙的接口了 iterator确实香
        while (resources.hasMoreElements()) {
            System.out.println(resources.nextElement());
        }

        // jar:file:/E:/doc1/doc2/1.jar
        //jar:file:/E:/repository/org/springframework/spring-beans/5.3.1/spring-beans-5.3.1.jar!/META-INF/spring.factories
        //jar:file:/E:/repository/org/springframework/boot/spring-boot-devtools/2.4.0/spring-boot-devtools-2.4.0.jar!/META-INF/spring.factories
        //jar:file:/E:/repository/org/springframework/boot/spring-boot/2.4.0/spring-boot-2.4.0.jar!/META-INF/spring.factories
        //jar:file:/E:/repository/org/springframework/boot/spring-boot-autoconfigure/2.4.0/spring-boot-autoconfigure-2.4.0.jar!/META-INF/spring.factories
        //jar:file:/E:/repository/org/mybatis/spring/boot/mybatis-spring-boot-autoconfigure/2.1.4/mybatis-spring-boot-autoconfigure-2.1.4.jar!/META-INF/spring.factories
    }


    @Test
    public void springFactoriesLoaderTest() {
        // 这个类负责加载了所有的 /META-INF/spring.factories 文件下所有的工厂方法都取出来
        // 这个类里定义了一个全局的静态缓存
    }


    @Test
    public void concurrentReferenceHashMapTest() {
        // 为什么这里使用了线程安全的引用容器？ 情景, 设计这个容器的原理是什么？
        //
        //
        // todo: java的一个专题学习
        // jdk1.8 1.7对于  ConcurrentHashMap 的实现, 一个锁 segment lock
        new ConcurrentReferenceHashMap<>();
    }



}
