package org.example.thread.executor.service;

import org.example.thread.util.Sleeper;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

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

    public static final ThreadFactory threadFactory = r -> {
        Thread t = new Thread(r);
        // 这个是阿里 mandatory. 指定线程的名字 用于监控平台监控
        t.setName("scheduled.test.Test");
        // 如果不设置 就是使用创建线程的守护状态
        t.setDaemon(false);
        return t;
    };

    public static final RejectedExecutionHandler defaultHandler =
            new ThreadPoolExecutor.AbortPolicy();


    public static final BlockingQueue<Runnable> blockQueue = new ArrayBlockingQueue<>(1024);

    /**
     * 底层使用这个 inbounded
     * DelayedWorkQueue.  延迟工作队列, 这个队列在 调度执行器 中一直使用。
     * 创建对象的时候直接  声明一个工厂 这个用法 在 nacos 的客户端代码中被广泛使用。
     * 我们手动指定了  corePoolSize 和 threadFactory
     * 还有一些默认的属性 todo: 后面看看这个延迟工作队列的具体实现
     * <ul>
     *     <li>Integer.MAX_VALUE</li>
     *     <li>0s</li>
     *     <li>DelayedWorkQueue(): </li>
     *     <li>AbortPolicy() 直接使程序异常终止</li>
     * </ul>
     * <p>
     * 我们知道只有队列满了才会开始 辅助线程完成操作。
     * see {@code ScheduledThreadPoolExecutor.DelayedWorkQueue}
     * compare to {@link DelayQueue}
     */
    public static final ScheduledThreadPoolExecutor scheduledPoolExecutor =
            new ScheduledThreadPoolExecutor(1, threadFactory);

    /**
     * 虽然构造是走的差不多 但是 Scheduled 有一些方法是继承了 抽象的执行器的功能
     */
    public static final ThreadPoolExecutor rawScheduledPoolExecutor =
            new ThreadPoolExecutor(1, Integer.MAX_VALUE, 0, TimeUnit.NANOSECONDS,
                    blockQueue, threadFactory, defaultHandler);


    public static AtomicInteger executeTaskTimes = new AtomicInteger(0);


    public static void main(String[] args) {
        // 看下这个延迟的实现, 这个宏观就是把一个任务 传入定义 然后 不断重复执行相同的任务
        // 用于检测 heartBeat 实时拉去动态配置等
        // 这个线程状态 就是 Sleeping 和 wait 来回的切换
        scheduledPoolExecutor.scheduleWithFixedDelay(() -> {
            Sleeper.sleep(1);
            System.out.println("我是scheduledThreadPoolExecutor... 2秒一触发调用, 进行逻辑业务处理, 用于检测 heartBeat 实时拉去动态配置等" + executeTaskTimes.incrementAndGet());
        }, 0, 2, TimeUnit.SECONDS);
    }

}
