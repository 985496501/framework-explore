package org.example.thread;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * java有关多线程的操作都在这
 * <p>线程的基础知识：</p>
 * <ul>
 *     <li>1. 线程是什么? 线程是操作系统CPU调度的基本单位, 每个进程都有一个主线程执行入口, 但是会有多个子线程分别执行任务;
 *     操作系统会根据自身的线程调度算法来调度线程的执行, 会分配高速缓存来缓存线程的执行状态, 用于恢复线程上下文, 这就是
 *     线程的切换任务, 是十分耗费资源的(相对概念), 这就有个问题, 线程并不是越多越好, 越多线程时间片分配的越少, 调度切换
 *     的频率就快, 那么执行的相对任务就少了.</li>
 *     <li>2. 单核cpu使用多线程可以吗? 可以, 因为CPU只管计算, 而线程任务不都是计算 还设计到IO, 当IO的时候, 将线程切换到其他线程
 *     继续计算, 充分发挥CPU的利用率。</li>
 *     <li>3. 线程设置的数量：n=u*r*(1+w/c)</li>
 * </ul>
 * <p>
 * 多线程编程的同步和互斥:
 * 同步：一个线程的执行依赖另外一个线程的消息, 比如notify()唤醒等待执行的线程.
 * 互斥：一个进程的共享内存资源一个时刻只允许一个线程使用, 其他线程必须等待, 知道独占线程使用完毕释放资源.</p>
 *
 * <p>并发3大特性:</p>
 * <ul>
 *     <li>
 *         1> 可见性: 操作系统为了提高计算性能, 采用了3级缓存. 这就使内存数据会发生不一致.
 *         synchronized, volatile都可以保证内存修改的可见性, 体现在哪呢? 如果一个线程修改了volatile修饰的变量, 如果计算完毕,
 *         需要把最新的值重新刷回主存. 同时通知其他线程使用了该变量的值 需要重新读取最新的值。
 *         普通变量计算 不会平白无故的 计算完就刷回内容, 除非整个方法执行完毕, 方法出栈. 但是有特殊修饰就会执行操作系统的指令.
 *         内存读取： 块读, 会把需要的变量及其周围变量整块读入.</li>
 *     <li>
 *         2> 有序性: 单例模式 创建对象
 *         机器指令可以乱序执行. 指令可以乱序, 但是都要保证单线程最终一致性.
 *         不要在构造方法中使用线程, 因为this可能半初始化, 线程就执行了.
 *         DCL: double check lock 双重检验锁 应对并发数据不一致问题.</li>
 *     <li>
 *         3> 原子性: 线程的执行权和线程的优先级没有直接关系, 取决于操作系统的调度算法.
 *         happen-before:  单线程最终一致性:</li>
 * </ul></p>
 *
 * For example:
 * <pre class="code">
 *             x=1,y=1 换不换都是一样的
 *             x=0,x++ 这个不能换, 这个都是在编译器实现的
 *         </pre>
 *
 * <p>下面这个东西慢慢搞吧</p>
 * <ul>
 * <li>1. volatile</li>
 * <li>2. synchronized</li>
 * <li>3. reentrantLock</li>
 * <li>4. AQS</li>
 * <li>5. happen-before jvm memory consistency. 理论的学习 以及这个篇章中对java领域线程的规范</li>
 * <ul/>
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
     * <p>
     * 用于测试 内存变量的可见性
     */
    static volatile boolean n = true;

    private static final Runnable runnable = ThreadMainTest::run;


    public static void main(String[] args) throws InterruptedException, IOException {
        new Thread(runnable).start();
        TimeUnit.SECONDS.sleep(6);
        n = false;
        System.in.read();
    }

    private static void run() {
        System.out.println("开始打印...");
        while (n) {
        }
        System.out.println("结束打印...");
    }


    @Test
    public void mainTest() {

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
