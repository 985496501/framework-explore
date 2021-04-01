package org.example.model.thread.executor.service;

import cn.hutool.core.thread.NamedThreadFactory;
import org.example.model.thread.executor.queue.DelayedTask;
import org.example.model.thread.util.Sleeper;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 最基础的线程池的有关代码
 * 归根到底就是 ThreadPoolExecutor
 * <p>
 * <p>
 * BlockingQueue:
 *
 *
 * <h2>RejectedPolicy</h2>
 * <ul>
 *     <li>AbortPolicy 直接抛异常</li>
 *     <li>DiscardPolicy 直接丢弃 不报异常</li>
 *     <li>DiscardOldestPolicy 直接丢弃队列 出队让出一个位置</li>
 *     <li>CallerPolicy 让主线程执行</li>
 * </ul>
 *
 *
 * @author: Liu Jinyun
 * @date: 2021/2/10/20:17
 */
public class ThreadPoolExecutorTest {


    private static final int corePoolSize = 4;
    private static final int maximumPoolSize = 10;
    private static final ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(1024);

    /**
     * default rejected handler execution: AbortPolicy, throws exception.
     */
    public static final ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 0L, TimeUnit.MILLISECONDS,
                    workQueue, new NamedThreadFactory("async-test", false));

    public static final int MAX_TASK_SIZE = 1024;

    public static final Runnable r = () -> {
        System.out.println("我就是一个简单的模拟的任务, 大约执行1s");
        Sleeper.sleep(1);
    };


    public static void main(String[] args) {
        // 线程执行完之后 就会被挂起来 wait 就不会占用CPU的资源空旋
        // 这个是主动调用执行任务, 线程池如果接受不了就存入内部维护的任务队列里面
        threadPoolExecutor.execute(r);

        // 不会主动退出, 主要手动销毁线程池
    }


    @Test
    public void casTest() throws InterruptedException {
        // lock cmpxchg ==> compare and exchange
        // cas的实践：
        // cas是乐观锁的实现, 通过机器指令完成锁
        // 比较的对象需要 借助 volatile 来完成, do{取值} while(比较取值);
        // volatile: 内存屏障保证指令的有序性 保存内存的可见性 任何getValue的操作 都必须从主存中同步最新的数据到工作内存中
        // 并发冲突较小的时候 性能是非常高的 但是超高并发的时候 线程的数量特别多 然后所有线程都要自旋 都在抢占cpu进行计算 要使用sync
        AtomicInteger atomicInteger = new AtomicInteger(1);
        for (int i = 0; i < MAX_TASK_SIZE; i++) {
            threadPoolExecutor.submit(() -> System.out.println(atomicInteger.getAndIncrement()));
        }

        // wait()
//        Thread.currentThread().join();
//        Sleeper.sleep(10);
    }


    @Test
    public void rejectedExecutionTest() {
        // CallerRuns, Discard, DiscardOldest, Abort
        // 4种策略,  可以自定义其他策略
        RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.DiscardPolicy();
    }


    /**
     * 阻塞队列:
     * 就是在满的时候, put线程会阻塞, take的时候会唤醒 put的线程
     * 在空的时候, take线程会阻塞, put的时候会唤醒 take的线程
     */
    @Test
    public void blockingQueueTest() {
//        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(16)
        TimeUnit timeUnit = TimeUnit.SECONDS;
//        Delayed delayed = new DelayedTask();
        BlockingQueue<DelayedTask> blockingQueue = new DelayQueue<>();
    }

}
