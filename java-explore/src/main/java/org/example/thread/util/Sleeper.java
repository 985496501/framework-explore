package org.example.thread.util;

import java.util.concurrent.TimeUnit;

/**
 * @author: Liu Jinyun
 * @date: 2021/2/14/15:55
 */
public class Sleeper {
    /**
     * 最终调用的还是Thread的  static native sleep()
     * Note: sleep() 并不会释放锁
     * Thread does not lose the ownership of any monitors.
     * 本地方法实现：应该是把此线程放入sleep线程队列里面, 通过timer的不断扫描, 自动将 thread加入就绪状态,
     * 等待OS的调度执行.
     *
     * @param second
     */
    public static void sleep(int second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException e) {
            System.out.println("线程中断");
        }
    }
}
