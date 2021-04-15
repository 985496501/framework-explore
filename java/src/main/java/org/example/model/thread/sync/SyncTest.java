package org.example.model.thread.sync;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: Liu Jinyun
 * @date: 2021/2/12/22:51
 */
public class SyncTest {


    /**
     * sync关键字加锁 是由jvm自动加锁
     * 悲观锁
     * 4种锁状态：
     * 无锁: 001
     * private final Object monitor = new Object();
     * 创建一个锁对象 新建NEW状态 是无锁的
     * 如果提前已经打开了偏向锁, 那么创建对象的时候就是偏向锁状态, 就是这个对象已经准备好被竞争了
     *
     * 偏向锁: 101
     * synchronized(monitor){
     *
     * }
     * 第一个线程抢占了这把锁 就把该对象的markword的锁标识位修改一下, 代表这个对象已经被这个
     * 线程抢占了. 如果一个资源确实存在高并发环境下竞争, 其实可以关闭这个偏向锁. 因为这个步骤可以省略,
     * 升级的过程是很浪费时间的, 如果确实没啥竞争这个是不错的.
     * 这个可以通过JVM option 设置, JVM默认是创建对象之后 4秒之后开启偏向锁.
     *
     * 轻量级锁：
     * new Thread(()-> {
     *     synchronized(monitor){
     *
     *     }
     * }).start();
     * 当又有一个线程需要抢占这个锁对象, 它先读取这个这个锁的markword的标识 看看有没有线程正在占用,
     * 如果有它会找到 占用线程看看这个线程是否存活 如果还存活 就把这个对象的标识改成轻量级锁 然后自旋
     *
     * 重量级锁：
     * 如果有大量线程都要抢占这把锁, 都自旋的话, 会让CPU空转, 这是非常不合适的,
     * 所以当大量线程都在自旋的话, 那就会切换到操作系统内核, 将这个线程 block
     *
     * <p>
     * 唤醒其他线程 具有随机性 不能指定线程唤醒
     */
    public synchronized void syncTest() throws InterruptedException {
        this.wait();
        this.notify();
    }

    /**
     * 这个也是悲观锁
     * 它最大的特点就是 condition 可以指定唤醒绑定它身上这个线程
     */
    public void reentrantTest() {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        lock.lock();
        try {
            condition.wait();
            System.out.println("hello world");
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    int i = 0;


    /**
     * 乐观锁
     * cas 和 自旋 完成的
     * 比较不同：线程上下文的切换, 这个线程上下文的切换是非常耗费资源的
     * 但是这个自旋也有问题 就是 CPU的空转
     * 就是这个值一直没有该到想要的值
     * <p>
     * 于是不能让这个方法无限制的自旋
     * 需要加锁, 将线程状态挂起
     * LockSupport.park()
     */
    public void casTest() {
        for (; ; ) {
            if (true) {
                return;
            } //Unsafe.getUnsafe().compareAndSwapInt(this, Unsafe.getUnsafe().objectFieldOffset(Field.))
        }
    }

    // 阻塞队列：如果满了你就等着有空位你再进来 入队；如果空了你还想获取你还得候着 出队
    // 每次入队就 唤醒一下获取的等待线程无论有没有, 每次出队就唤醒一下 入队的等待线程

    // ArrayBlockingQueue  使用的是同一把锁 静态数组
    // LinkedBlockingQueue 使用两把锁 读写可能操作的是不同的节点Node, 读写互不干扰
    // SynchronizedQueue 同步队列 无缓冲堵塞队列 公平模式 FIFO   非公平模式:FILO producer-consumer

    /**
     * LockSupport: 都是静态方法
     * 都是通过 Unsafe类完成 线程的阻塞 或 唤醒
     *
     *
     * @throws InterruptedException
     */
    @Test
    public void lockSupportTest() throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println("我开启了一条线程开始执行了...");
            LockSupport.park(this);
            System.out.println("我被唤醒了 又开始执行了");
        });

        t.start();
        System.out.println("我是主线任务线程, 我处理一下我的逻辑");
        TimeUnit.SECONDS.sleep(3);
        LockSupport.unpark(t);
        // 等待线程t执行完毕并销毁
        t.join();
    }

}
