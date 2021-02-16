package org.example.thread.executor;

import cn.hutool.core.thread.NamedThreadFactory;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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

    @Test
    public void executorTest() throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                        new ArrayBlockingQueue<>(100), new NamedThreadFactory("test", false));
        threadPoolExecutor.submit(CodingTask.TASK);

        TimeUnit.SECONDS.sleep(1);
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
