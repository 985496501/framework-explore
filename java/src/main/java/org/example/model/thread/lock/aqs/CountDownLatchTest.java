package org.example.model.thread.lock.aqs;

import org.example.model.thread.util.Sleeper;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch用于控制线程的执行顺序
 *
 * @author: jinyun
 * @date: 2021/3/19
 */
public class CountDownLatchTest {
    @Test
    public void countDownLatchTest() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(2);
        System.out.println("主线程执行.....");
        new Thread(() -> {
            System.out.println("线程1开始执行before....");
            latch.countDown();
            System.out.println("线程1开始执行after....");
        }).start();

        //latch.countDown();
        latch.await();
    }

    /**
     * 有
     * [a,1,b,2] 使用两个线程交替输出
     * 实际上就是：
     * 一个字符数组 一个整数数量
     * 先输出字符数组 然后交替输出
     */
    @Test
    public void startOrderTest() {
        char[] chars = new char[]{'a', 'b', 'c', 'd', 'e'};
        int[] digits = new int[]{1, 2, 3, 4, 5};

        CountDownLatch latch = new CountDownLatch(1);
        final Object monitor = new Object();

        new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < chars.length; i++) {
                synchronized (monitor) {
                    try {
                        monitor.notify();
                        System.out.print(chars[i] + " ");
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(() -> {
            latch.countDown();
            for (int i = 0; i < digits.length; i++) {
                synchronized (monitor) {
                    try {
                        monitor.notify();
                        System.out.print(digits[i] + " ");
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        Sleeper.sleep(1);
    }
}
