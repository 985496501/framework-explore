package org.example.thread.executor.aqs;

import org.example.thread.executor.service.ThreadPoolExecutorTest;
import org.example.thread.util.Sleeper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 自定义一个排他锁 看看是否可以执行
 * <p>
 * 内部自行维护一个具体的 队列 同步器
 * 通过这个类来 探索AQS的实现 以及其他JUC相关的同步机制
 * AQS 实际就是一个 Sync 的framework 使用模板设计模式, 抽象出公共的方法, 然后暴露出骨架的埋点让 具体的类型进行实现。
 *
 * @author: jinyun
 * @date: 2021/3/19
 */
public class MyCountDownLatch {

    public static final ThreadPoolExecutor threadPoolExecutor = ThreadPoolExecutorTest.threadPoolExecutor;

    public static final CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) {
        threadPoolExecutor.execute(()-> {
            System.out.println("首先执行的任务, 但是如果我执行, 我不会被执行直接AQS park了");
            // 如果这个线程先执行直接park
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("首先执行的任务, 别人执行完毕了我要执行了");
        });

        threadPoolExecutor.execute(()-> {
            System.out.println("后来执行的任务, 但是我被调用方要求首先执行 然后让我前面的再执行");
            Sleeper.sleep(2);
            System.out.println("后来执行的任务, 我执行完毕了我要 唤醒其他 阻塞的线程了");
            latch.countDown();
        });

        Sleeper.sleep(5);

        // 如果不关闭线程池 线程池里面的Set<Thread>保持着运行状态的线程
        // jvm就不会退出, 使用这个方法关闭线程池, 退出jvm
        // 调用这个方法不会等待所有任务执行完毕, 首先就是一调用这个方法, 新的任务就不会接收了
         threadPoolExecutor.shutdown();

    }

    private final Sync sync;

    public MyCountDownLatch(int count) {
        if (count < 0) throw new IllegalArgumentException("count < 0");
        this.sync = new Sync(count);
    }


    public long getCount() {
        return sync.getCount();
    }

    /**
     * 释放锁
     */
    public void countDown() {
        sync.releaseShared(1);
    }

    /**
     * 这个相当于加锁入队
     */
    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    /**
     * <p><b>Subclass should be defined as a internal non-public helper class.</b></p>
     * <pre> {@code
     * private static final Sync extends AbstractQueuedSynchronizer {
     *
     * }}</pre>
     * <p>
     * 借助AQS的state变量 volatile int state;
     * <p>mimic a CountLatch  &nbsp;<b>that allows one or more threads to wait until a set of operations 'being performed in other thread' completed.</b></p>
     * By inspecting and monitoring synchronization state by
     * <ol>
     *     <li>{@link AbstractQueuedSynchronizer#setState(int)}</li>
     *     <li>{@link AbstractQueuedSynchronizer#getState()}</li>
     *     <li>{@link AbstractQueuedSynchronizer#compareAndSetState(int, int)}</li>
     * </ol>
     *
     *
     *
     * </p>
     * Redefine these methods:
     * <ul>
     *     <li>tryAcquire</li>
     *     <li>tryRelease</li>
     *     <li>tryAcquireShared</li>
     *     <li>tryReleaseShared</li>
     *     <li>isHeldExclusively</li>
     * </ul>
     */
    private static final class Sync extends AbstractQueuedSynchronizer {

        public Sync(int count) {
            // 创建初始化进行set
            setState(count);
        }

        // 如果后面进行操作要使用 CAS 轻量级锁 spinning-set

        public final int getCount() {
            // 一定是同步的 基本数据类型 volatile
            return getState();
        }


        // -------下面的方法必须由具体的内部同步器实现------------------------------
        // ------- shared mode 可共享锁的实现-------

        // 通过一个原子的共享变量完成加锁

        /**
         * 这个抽象方法要求： 这个方法就是读 state
         * int < 0: 获取锁失败
         * int = 0: 成功但是后续就不能获取
         * int > 0: 成功, 后续的可能成功
         *
         * 通过这个共享变量 state 来控制是否可以获取可重入锁
         * 预先设定线程数量的state
         * 只要state=0的时候 才 acquire 成功
         * state != 0 返回 < 0 代表获取失败
         *
         * @param arg
         * @return
         */
        @Override
        protected int tryAcquireShared(int arg) {
            return getState() == 0 ? 1 : -1;
        }

        /**
         * 尝试释放
         * 就是修改state
         *
         * 0: 就不用释放了
         * > 0: 使用自旋保证原子操作 -1 操作。
         *
         * @param arg
         * @return
         */
        @Override
        protected boolean tryReleaseShared(int arg) {
            // decrement count; signal when transition to zero.
            for (; ; ) {
                int c = getState();
                if (c == 0)
                    return false;
                int nextc = c - 1;
                if (compareAndSetState(c, nextc)) {
                    return nextc == 0;
                }
            }
        }

        // ------- exclusive mode 排他锁的实现-------
        @Override
        protected boolean tryAcquire(int arg) {
            return super.tryAcquire(arg);
        }
        @Override
        protected boolean tryRelease(int arg) {
            return super.tryRelease(arg);
        }
        @Override
        protected boolean isHeldExclusively() {
            return super.isHeldExclusively();
        }
    }
}
