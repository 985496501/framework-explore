package org.example.thread.executor.service;

import cn.hutool.core.thread.NamedThreadFactory;
import org.example.thread.executor.queue.DelayedTask;
import org.example.thread.util.Sleeper;
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
 * @author: Liu Jinyun
 * @date: 2021/2/10/20:17
 */
public class ThreadPoolExecutorTest {

    /**
     * default rejected handler execution: AbortPolicy, throws exception.
     */
    ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(4, 10, 0L, TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(1024), new NamedThreadFactory("async-test", false));

    public static final int MAX_TASK_SIZE = 1024;




    /**
     * todo: 后面继续探索线程池的实现细节，以及设计思想
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void threadPoolExecutorTest() throws InterruptedException {
        for (int i = 0; i < 1034; i++) {
            Future<String> future = threadPoolExecutor.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    Sleeper.sleep(20);
                    return "hello world";
                }
            });
        }


        TimeUnit.SECONDS.sleep(100);
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

    /**
     * Object
     */
    @Test
    public void jsonTest() {
//        JSONUtil.toJsonPrettyStr()
    }


    @Test
    public void bitCompute() {

    }
}
