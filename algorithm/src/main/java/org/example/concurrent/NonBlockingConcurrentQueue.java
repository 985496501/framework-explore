package org.example.concurrent;

import java.util.concurrent.*;

/**
 * 今天看到了一个同步队列, ConcurrentLinkedQueue, 于是就想看看这个队列的特点, 以及为什么在众多的同步队列中选择这个队列;
 * {@link java.util.concurrent.ConcurrentLinkedQueue}, 底层 是一个链表的  数据结构
 * 看到作者 里面提到, this implementation employs an efficient non-blocking algorithm based on one described in
 * Simple, Fast and Practical Non-blocking and Blocking concurrent queue algorithm.
 * <p>
 * 作者提到了 使用了一个 高效的非阻塞 的同步算法;
 * 简单 快速 实践 的非阻塞 和 阻塞的 同步队列算法;
 * <p>
 * 特点： 同步队列, unbounded, not permit null, FIFO
 * <p>
 * 这个实现是算法的 一种变体 主要 adapted for a garbage-collected environment.
 * 为了支持 删除一个node;
 * <p>
 * <p>
 * 像大多数的 非阻塞 算法一样, 这个实现也是在一个垃圾回收的环境中,
 * 在垃圾回收的环境中,
 * <p>
 * 这个算法依靠 一个在垃圾回收系统 的事实, 因为会回收节点不会出现ABA的问题, 因此 没有必要使用 counted pointer
 * or related techniques seen in versions used in non-GCed settings.
 * <p>
 * invariants are:
 * exactly 保证只有一个node 的next 是 null reference. cas 进行入队; 优化的一点, 使用tail, 可以o(1)的复杂度到tail,
 * 这是一个小小的优化, 就是从head->tail o(n) 的时间复杂度优化;
 * 队列中的元素 不能是null, cas操作一个null, 就会把这个节点干掉,
 *
 * @author: jinyun
 * @date: 2021/5/28
 */
public class NonBlockingConcurrentQueue {

    // head, tail= new Node(null, null) unbounded. poll()  non-blocking;
    static final ConcurrentLinkedQueue<Runnable> NON_BLOCKING_QUEUE = new ConcurrentLinkedQueue<>();

    static final BlockingQueue<Runnable> BLOCKING_QUEUE = new LinkedBlockingQueue<>();

    static final ScheduledExecutorService SERVICE = Executors.newScheduledThreadPool(3);


    public static void main(String[] args) {
        Runnable poll = NON_BLOCKING_QUEUE.poll();
        System.out.println(poll);

        try {
            Runnable poll1 = BLOCKING_QUEUE.poll(2, TimeUnit.SECONDS);
            System.out.println(poll1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
