package org.example.thread.executor.aqs;

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

    private final Sync sync;

    public MyCountDownLatch(int count) {
        if (count < 0) throw new IllegalArgumentException("count < 0");
        this.sync = new Sync(count);
    }


    public long getCount() {
        return sync.getCount();
    }


    public void countDown() {
        sync.releaseShared(1);
    }

    public void await() {

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
        @Override
        protected int tryAcquireShared(int arg) {
            return getState() == 0 ? 1 : -1;
        }


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
