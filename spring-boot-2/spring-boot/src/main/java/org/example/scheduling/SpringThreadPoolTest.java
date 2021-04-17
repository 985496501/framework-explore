package org.example.scheduling;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

/**
 * spring 封装的线程池
 *
 * @author: jinyun
 * @date: 2021/3/24
 */
public class SpringThreadPoolTest {

    private static void run() {
        System.out.println("hello world");
    }

    /**
     * 一共7个参数 我们普通线程池的定义
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class ThreadPoolExecutorConfig {
        private Integer core;
        private Integer max;
        private Long keepAlive;
        private TimeUnit timeUnit;
        private BlockingQueue<Runnable> blockingQueue;
        private ThreadFactory threadFactory;
        private RejectedExecutionHandler rejectedExecutionHandler;
    }

    /**
     * 普通创建线程池
     */
    @Test
    public void normalThreadPoolTest() {
        ThreadPoolExecutorConfig threadPoolExecutorConfig = new ThreadPoolExecutorConfig();
        ExecutorService executorService = new ThreadPoolExecutor(
                threadPoolExecutorConfig.core,
                threadPoolExecutorConfig.max,
                threadPoolExecutorConfig.keepAlive,
                threadPoolExecutorConfig.timeUnit,
                threadPoolExecutorConfig.blockingQueue,
                threadPoolExecutorConfig.threadFactory,
                threadPoolExecutorConfig.rejectedExecutionHandler);

    }

    /**
     * spring 创建线程池的方式
     *
     * @EnableAsync
     * @EnableScheduled
     */
    @Test
    public void springThreadPoolTest() {
        ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.initialize();
        poolTaskExecutor.execute(SpringThreadPoolTest::run);
    }

    /**
     * linked blocking queue.
     * 不能自动扩容. 满了就不能放入了
     */
    @Test
    public void linkedBlockingTest() {
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>(4);
        for (int i = 0; i < 5; i++) {
            queue.add("a");
        }

        // throws ex.
    }
}
