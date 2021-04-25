package org.example.pattern.structure.proxy;

import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 这里我们看下jdk的动态代理技术，生成代理对象
 *
 * 它所用的 是jvm 提供的  动态 字节码增强方案;
 *
 * ClassLoader 负责把磁盘上的class文件加载到方法区, 然后在堆生成一个class对象;
 * 扩展方法： 编译的时候修改源代码, 比如lombok
 *
 * 前提是只要我们有一个接口, 只要有一个行为规范即可, 可以通过 动态字节码完成 代理对象的创建
 *
 *
 * todo: 具体看一下 mybatis 或者  openFeign 的动态代理技术
 *
 * 他们都是为了代理 真正干活的第三方对象, 代理了客户端, 比如代理了mysql的sqlSession; feign的httpClient;
 *
 * @author: jinyun
 * @date: 2021/4/25
 */
public class JDKProxyTest {

    interface Server {
        String start();
    }


    static class WindowsServer implements Server {
        @Override
        public String start() {
            System.out.println("windows server is starting...");
            return "windows";
        }
    }

    /**
     * 我们就是执行器的接口为例 我们代理它
     * 源码看下在 java 方面做了那些事情;
     *
     * 把我们传入的接口的字节码克隆一份
     *
     * proxyClass = getProxyClass0(loader, interfaces)
     * proxyClassCache.get(loader, interfaces): 使用了weakCahce
     *
     *
     */
    @Test
    public void executorProxyTest() {

        /**
         * 生成的代理对象自动继承 Proxy, 实现了我们传入的接口, 可以实现多个接口
         */
        Server server = (Server)Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Server.class}, new InvocationHandler() {
            // 代理对象执行的模板方法, 这个用来 依赖 被代理对象;
            private final Server server = new WindowsServer();

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("准备server环境...");
                Object invoke = method.invoke(server, args);
                System.out.println("释放资源.....");
                return invoke;
            }
        });

        server.start();
    }
}
