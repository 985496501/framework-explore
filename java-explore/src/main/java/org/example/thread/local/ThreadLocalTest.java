package org.example.thread.local;

import org.junit.Test;

/**
 * 线程局部变量
 * 这个是保证线程 共享变量 性能最高的解决方案?
 *
 * 先看看 ThreadLocal 的 document
 *
 * 一般我们开发中用到它 就是保存用户的登录信息.
 * private static final ThreadLocal<UserDetail> currentLoginUser = new ThreadLocal<>();
 * 使用私有静态成员, 保证这个对象全局唯一. globally unique.
 *
 * 保证当前这个请求的任意地方都可以获取到这个登录人的相关信息
 * once request mapping a user info.
 *
 *
 * @author: Liu Jinyun
 * @date: 2021/2/17/1:42
 */
public class ThreadLocalTest {
    /**
     * 就是分配了一个内存 然后引用了一下就完事了
     */
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    @Test
    public void localTest() {
        // 真正工作是调用了set方法
        String o = "hello threadLocal";
        // new ThreadLocalMap<threadLocal, T>();
        THREAD_LOCAL.set(o);
        // 涉及了 reference 这个也是java中基础 和gc紧密相关.
        String s = THREAD_LOCAL.get();
        System.out.println(s);

        // remove() 做了什么?
        THREAD_LOCAL.remove();
    }
}
