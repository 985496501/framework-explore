package org.example.thread.executor.service;

import org.example.thread.util.Sleeper;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * 看下这个线程执行器
 * 它的类图 继承了 ThreadPoolExecutor 实现了 ScheduledExecutorService.
 * see {@link ThreadPoolExecutor} and {@link ScheduledExecutorService},  since JDK1.5
 * ThreadPoolExecutor: 这个具体的探究看 {@link ThreadPoolExecutorTest}
 * <p>
 * ScheduledExecutorService: after a given delay, or to execute periodically.
 *
 * @author: jinyun
 * @date: 2021/3/30
 */
public class ScheduledThreadPoolExecutorTest {

    /**
     * 底层使用这个 inbounded
     * DelayedWorkQueue.  延迟工作队列, 这个队列在 调度执行器 中一直使用。
     * 创建对象的时候直接  声明一个工厂 这个用法 在 nacos 的客户端代码中被广泛使用。
     * 我们手动指定了  corePoolSize 和 threadFactory
     * 还有一些默认的属性
     * <ul>
     *     <li>Integer.MAX_VALUE</li>
     *     <li>0s</li>
     *     <li>DelayedWorkQueue()</li>
     *     <li>AbortPolicy() 直接使程序异常终止</li>
     * </ul>
     * <p>
     * 我们知道只有队列满了才会开始 辅助线程完成操作。
     * see {@code ScheduledThreadPoolExecutor.DelayedWorkQueue}
     * compare to {@link DelayQueue}
     */
    public static final ScheduledThreadPoolExecutor scheduledPoolExecutor =
            new ScheduledThreadPoolExecutor(1, r -> {
                Thread t = new Thread(r);
                // 这个是阿里 mandatory. 指定线程的名字 用于监控平台监控
                t.setName("scheduled.test.Test");
                // 如果不设置 就是使用创建线程的守护状态
                t.setDaemon(true);
                return t;
            });

    @Test
    public void scheduledThreadPoolExecutorTest() {
        // 看下这个延迟的实现
        scheduledPoolExecutor.scheduleWithFixedDelay(() -> {
            Sleeper.sleep(2);
            System.out.println("我是scheduledThreadPoolExecutor... 2秒一触发调用");
            System.out.println("进行逻辑业务处理");
        }, 0, 2, TimeUnit.SECONDS);

        Sleeper.sleep(20);
    }
}
