package org.example.pattern.singleton;

/**
 * singleton pattern.
 *
 * @author: Liu Jinyun
 * @date: 2021/2/13/14:41
 */
public class SingletonTest {

    /**
     * 最简单的饿汉式单例模式
     * hungry-singleton pattern
     */
    public static class HungrySingleton {
        static class HungrySingletonHolder {
            private static final HungrySingleton SINGLETON = new HungrySingleton();
        }

        public static HungrySingleton getInstance() {
            return HungrySingletonHolder.SINGLETON;
        }

        private HungrySingleton() {}

        public static void sayHello () {
            System.out.println("hello");
        }
    }

    public static void main(String[] args) {
        HungrySingleton.sayHello();
    }

    public static class LazySingleton {
        // 如果多线程环境下, 第一个线程可以创建对象, 其他对象获取对象
        // 如果 创建对象发生了指令重排, 获取了一个半初始化实例对象, 会发生数据不一致
        // 我们使用 volatile 来保证指令的有序  astore指令
        // 几率很小 但是有可能发生
        private static volatile LazySingleton instance;

        public static LazySingleton getInstance() {
            if (instance == null) {
                // 多个线程进来都是null, 然后竞争锁 串行了.
                // synchronized 可以保证同步, 但是不能保证指令是否重排.
                synchronized (LazySingleton.class) {
                    // 第一个线程释放了锁 其他的线程竞争了锁 还是可以创建对象
                    if (instance == null) {
                        return new LazySingleton();
                    }
                }
            }

            return instance;
        }

        private LazySingleton(){}
    }
}
