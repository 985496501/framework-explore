package org.example.thread.executor.aqs;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author: jinyun
 * @date: 2021/3/19
 */
public class CountDownLatchTest {
    @Test
    public void countDownLatchTest() {
        final CountDownLatch latch = new CountDownLatch(2);
        System.out.println("主线程执行.....");


    }
}
