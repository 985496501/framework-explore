package org.example.util;

import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.util.CustomizableThreadCreator;

/**
 * Spring封装了一个创建线程的类
 *
 * @author: jinyun
 * @date: 2021/3/24
 */
public class SpringThreadCreatorTest {

    @Getter
    static class ThreadConfig {
        private final ThreadGroup group;
        private final String threadName;
        private final Runnable runnable;

        public ThreadConfig(ThreadGroup group, String threadName, Runnable runnable) {
            this.group = group;
            this.threadName = threadName;
            this.runnable = runnable;
        }
    }

    /**
     * 普通创建线程方法的方法 就是这5个属性的设置
     * 每次new 都要装配属性
     */
    @Test
    public void normalCreateThreadTest() {
        ThreadConfig threadConfig = new ThreadConfig(null, null, null);
        Thread t = new Thread(threadConfig.getGroup(), threadConfig.getRunnable(), threadConfig.getThreadName());
        t.setDaemon(false);
        t.setPriority(Thread.NORM_PRIORITY);
        t.setUncaughtExceptionHandler(null);
    }

    /**
     * 一次配置一直可以new, 不需要重复使用设置那些通用的字段
     * <p>
     * spring创建下线程
     */
    @Test
    public void springThreadCreatorTest() {
        // 创建对象一次配置 可以一直生产不需要重复配置
        CustomizableThreadCreator threadCreator = new CustomizableThreadCreator();
        threadCreator.setThreadNamePrefix("test");
        threadCreator.setDaemon(false);
        threadCreator.setThreadPriority(Thread.NORM_PRIORITY);
        threadCreator.setThreadGroupName("test-group");

        Thread thread = threadCreator.createThread(null);

        // CustomizableThreadFactory
        // 自定义工厂 继承了这个类  同时实现了线程工厂类
    }
}
