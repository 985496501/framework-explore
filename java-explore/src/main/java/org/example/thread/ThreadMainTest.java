package org.example.thread;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * java有关多线程的操作都在这
 * <p>
 * 1. volatile
 * 2. synchronized
 * 3. reentrantLock
 * 4. AQS
 * 5. happen-before jvm memory consistency. 理论的学习 以及这个篇章中对java领域线程的规范
 *
 * @author: jinyun
 * @date: 2021/2/9
 */
public class ThreadMainTest {

    /**
     * 这就出现线程安全问题
     * volatile
     * <p>
     * 线程安全问题出现在哪？ n++
     * 计算机的计算的原子问题
     */
    static int n = 0;

    @Test
    public void mainTest() {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> n++).start();
        }

        System.out.println(n);
    }


    // ----------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------

    /**
     * volatile
     */
    static int v1 = 5;

    static int v2 = 5;

    /**
     * volatile 保证了内存的可见性
     * 线程安全出现在哪？
     */
    @Test
    public void volatileTest1() throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(v1);
                v1 = 8;
            }
        }).start();

        TimeUnit.SECONDS.sleep(3);
        int v11 = v1;
        System.out.println(v11);
    }

    @Test
    public void volatileTest2() throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(v1);
                v1 = 8;
            }
        }).start();

        TimeUnit.SECONDS.sleep(3);
        System.out.println(v1);
    }

}
