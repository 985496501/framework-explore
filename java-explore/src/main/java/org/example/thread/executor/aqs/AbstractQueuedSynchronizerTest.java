package org.example.thread.executor.aqs;

import org.junit.Test;

/**
 * 抽象的队列同步器 AQS
 * extends AbstractOwnableSynchronizer
 * A synchronizer that may be exclusively owned by a thread. 它仅仅保证了一个同步器只能同时被一个线程拥有.
 *
 * @author: jinyun
 * @date: 2021/3/19
 */
public class AbstractQueuedSynchronizerTest {
    @Test
    public void abstractQueuedSynchronizerTest() {
        MyCountDownLatch count = new MyCountDownLatch(2);
        java.util.concurrent.CountDownLatch countDownLatch = new java.util.concurrent.CountDownLatch(2);

        count.countDown();
        countDownLatch.countDown();
        System.out.println("执行主线程");
    }
}
