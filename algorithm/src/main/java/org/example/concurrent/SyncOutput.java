package org.example.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 有一个算法题目 就是顺序输出 0a1b2c3d4e
 * <p>
 * 采用go的机制, 可以直接使用阻塞同步队列来实现, 直接操作线程说实话确实愚蠢
 *
 * 定义两个线程
 * 一个线程 输出 数字
 * 一个线程 输出 字符
 *
 * @author: jinyun
 * @date: 2021/5/28
 */
public class SyncOutput {

    private static final String TARGET_STR = "0a1b2c3d4e";


    public static void main(String[] args) {
        char[] chars = TARGET_STR.toCharArray();
        // 一个线程 线程生产者 两个队列消费者
        BlockingQueue<Character> charQueue = new ArrayBlockingQueue<>(1);
        BlockingQueue<Character> digitQueue = new ArrayBlockingQueue<>(1);
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.submit(() -> {
            for (int i = 0; i < chars.length; i++) {
                // 我这应该先生产数字 然后我的看下数字有没有消费, 如果消费了我在生产字符,
                digitQueue.add(chars[i]);

                charQueue.add(chars[i]);
            }
        });

        executorService.submit(() -> {
            while (true) {
                System.out.println(charQueue.poll());
            }
        });

        executorService.submit(() -> {
            while (true) {
                System.out.println(digitQueue.poll());
            }
        });
    }
}
