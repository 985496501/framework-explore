package org.example.thread.executor;

import cn.hutool.core.thread.NamedThreadFactory;
import org.example.thread.util.Sleeper;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * 最基础的线程池的有关代码
 * 归根到底就是 ThreadPoolExecutor
 *
 *
 * BlockingQueue:
 *
 *
 * @author: Liu Jinyun
 * @date: 2021/2/10/20:17
 */
public class PoolTest {


    /**
     * todo: 后面继续探索线程池的实现细节，以及设计思想
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @Test
    public void executorTest() throws InterruptedException, ExecutionException {
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(4, 10, 0L, TimeUnit.MILLISECONDS,
                        new ArrayBlockingQueue<>(1024), new NamedThreadFactory("async-test", false));


        Future<String> future = threadPoolExecutor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Sleeper.sleep(10);
                return "hello world";
            }
        });

        String s = future.get();
        System.out.println(s);

        TimeUnit.SECONDS.sleep(5);
    }


    @Test
    public void retryTest() {

    }










    static class CodingTask implements Runnable {
        static final CodingTask TASK = new CodingTask();
        @Override
        public void run() {
            System.out.println("I'm coding...");
        }

        private CodingTask() {}
    }

    /**
     * 阻塞队列:
     * 就是在满的时候, put线程会阻塞, take的时候会唤醒 put的线程
     * 在空的时候, take线程会阻塞, put的时候会唤醒 take的线程
     */
    @Test
    public void blockingQueueTest() {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(16);
    }



    @Test
    public void bitCompute() {

    }
}
