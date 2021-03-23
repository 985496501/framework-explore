package org.example.thread.executor.aqs;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

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

    @Test
    public void predecessorTest() {
        //  [ˈpredəsesər]
    }


    /**
     * CLH locks are normally used for spinlock.
     */
    @Test
    public void clhTest() {

    }

    @Test
    public void nodeTest() {
        Node current = new Node(1);
        // 前驱
        Node predecessor = new Node(0);
        // 后继
        Node successor = new Node(2);

        // 使用双向链表
        current.next = successor;
        current.prev = current;

        predecessor.next = current;
        successor.prev = current;
    }


    /**
     * node
     * 提供了几个构造方法,
     * 这几个构造方法用于各种情况
     */
    static class Node {
        /**
         * lazily initialized:
         */
        Node prev;
        Node next;
        final int val;

        public Node(int val) {
            this.val = val;
        }
    }


    @Test
    public void countLatchTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        countDownLatch.countDown();
        countDownLatch.await();
    }

}
