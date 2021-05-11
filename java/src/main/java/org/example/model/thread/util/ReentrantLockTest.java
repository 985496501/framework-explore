package org.example.model.thread.util;

import org.junit.Test;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * reentrant 可重入的, 就是进入了还能进入,
 * 获取了 即时获取不到就进入排列
 * 获取了 排他模式 还可以继续获取获取；
 *
 * 2021-5-11 前面看了AQS的源码实现 AQS要求具体的同步器 自行封装实现Sync
 * 原理 就是以同步的方法 对 临界资源 state 进行是否有权限进行读写,
 * 有... 怎么样
 * 没有... 怎么样
 *
 * state的状态： 0 是什么  !0 是什么
 *
 * @author: jinyun
 * @date: 2021/5/11
 */
public class ReentrantLockTest {
    /**
     * NonfairSync#lock(): 使用原子操作尝试看是否可以加锁成功, state=0表示无锁状态
     * 非0 表示已经有锁;
     * <p>如果是在无所状态下成功通过临界资源获取了锁(就是成功修改了state)
     * setExclusiveOwnerThread(currentThread) 就把获取资源的线程保存到同步器的成员变量中；
     * 这点就说明这个是一个排他锁, exclusiveOwner.
     * <p>如果修改失败了 就调用尝试获取 acquire(1);
     *
     */
    @Test
    public void reentrantTest() {
        // creates an instance of ReentrantLock, this is equivalent to using ReentrantLock. new NonfairSync();
        ReentrantLock reentrantLock = new ReentrantLock(false);
        // 在使用同步阻塞锁 一定要使用try包裹业务代码保证在finally{} release lock.
        reentrantLock.lock();
        try {
            // to operate the critical resources;
        } finally {
            reentrantLock.unlock();
        }
    }


    @Test
    public void selfInterruptTest() {
        // t.interrupt() 这个方法调用 实际是调用的线程实例的打断方法；
        // Unless the current thread is interrupting itself, which is always permitted.
        // convenience method to interrupt current thread.
        Thread.currentThread().interrupt();
    }


    /**
     * 线程中断相关的方法：
     * <p><p>
     * Thread#interrupt() 中断线程, 可以使 wait() sleep() join() 处于block状态的线程 调用 unblock(), 可以用于取消任务或者资源的释放
     * 中断仅仅使 刺激一下线程, 具体的逻辑处理 由应用线程自行处理;
     * <p><p>
     * Thread#isInterrupt() 判断当前线程是否处于 中断状态
     * <p><p>
     * Thread.interrupted() 判断当前线程是否处于中断状态, 但是会清除线程的中断标识位;
     *
     *
     * LockSupport.park() 会阻塞线程, 但是debug发现线程的状态还是 RUNNING;
     *
     */
    @Test
    public void interruptTest() {
        Thread thread = new Thread(()->{
            System.out.println("000000000000000000000000");
            LockSupport.park();
            System.out.println("000000000000000000000000001");
        }, "LockSupport#park()");
        thread.start();
        thread.interrupt();
        Sleeper.sleep(5);
    }

}
