package org.example.pattern.structure.proxy;

/**
 * 静态代理
 * <p>
 * 优点：不修改源代码
 * 缺点：如果接口的方法很多, 而我们需要的代理的增强的方法又很少, 那么用一个新类实现所有的接口
 * 是不是不合适; 如果所有代理的方法都执行的是相同的逻辑, 比如打开事务, 关闭事务是不是会有大量的重复代码呢
 * 如果你不烦, 就手动的一个一个的敲吧;
 * <p>
 * JDK一定提供了解决方案: JDK Proxy 动态代理技术;
 *
 * @author: jinyun
 * @date: 2021/3/15
 */
public class StaticProxy {

    interface Executor {
        void run();
    }

    static class BasicExecutor implements Executor {
        @Override
        public void run() {
            System.out.println("默认实现");
        }
    }


    static class ProxyExecutor implements Executor {
        private final Executor executor;

        public ProxyExecutor(Executor executor) {
            this.executor = executor;
        }

        @Override
        public void run() {
            System.out.println("before 调用...");
            executor.run();
            System.out.println("after 调用...");
        }
    }

    /**
     * 这他妈和 Delegate 和 Decorator  有啥不一样吗?
     *
     * @param args
     */
    public static void main(String[] args) {
        new ProxyExecutor(new BasicExecutor()).run();
    }
}
