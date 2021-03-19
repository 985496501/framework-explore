package org.example.thread.thread;

import org.example.thread.util.Sleeper;
import org.junit.Test;

/**
 * guc提供的一些工具包 实际上就是对这个类进行的封装 要熟悉这个类的方法.
 * <p>
 * 我们最常用的 new Thread(r).start() 方法的使用讲解:
 * <p>
 * 1. t = new Thread() 这个仅仅是java创建一个线程对象, 分配了heap内存 和 相关的引用t.
 * volatile int threadStatus = 0 默认就是这个代表 线程状态[NEW].
 * public Thread(ThreadGroup group, Runnable target, String name, long stackSize) {
 * init(group, target, name, stackSize);
 * }
 * 这个是java语言对线程的封装构造方法最全的一个构造方法, 它的作用是什么?
 * Allocate a new thread object with a target object, preparing to be executed by JVM(OS).
 * 这个方法会调用私有方法init() 主要就是如果没有线程组 就使用父线程的线程组, 使用父线程的状态(daemon, priority)
 * 然后回创建一个名称, 分配一个线程id(同步生成唯一)
 * <p>
 * 2. t.start() 这个就是真正的系统调用完成系统线程的分配并使线程处于Runnable状态, 等待os调度, 然后处于Running.
 * start()方法干了什么事情呢?
 * 首先如果这个线程状态不是NEW, 就不会执行, 然后把当前线程对象放入 this.threadGroup, 然后调用 native 方法,
 * 向 OS 申请线程资源, 完成了java线程和 OS p_thread 的 binding.
 * 如果 os p_thread 分配完毕 ==> Runnable Queue ==> os调度这个thread ==> Running ==> 调用target
 * <p>
 * Note: 线程创建容易, 销毁难, 原因：线程使协作式工作, thread资源的释放由线程自己决定.
 * destroy() 销毁线程已经 deprecated. 并且永远都不会实现这个方法, 因为线程这个对象可以销毁, 但是 monitor 不会
 * 销毁, 如果这个monitor lock critical system resources, 那么这系统资源永远不会被其他线程获取, 如果其他线程
 * 也要lock, deadlock will occur.
 * stop(), resume(), suspend() 都 deprecated. 具体的后面遇到再继续探索.
 *
 * happen-before:
 * JVMM最核心的概念, java多线程工作是通过共享内存进行通信的, 每个线程都是通过读取共享内存的变量到本地工作内存,
 * 操作完毕之后把最新的值刷回主存, 但是如果一个线程修改了值, 没有及时同步共享内存的值, 那么另外的线程其实就是读取了
 * 错误的值, 就是 dirty-read,(db: 读取了未提交的数据)
 * 怎么解决这个问题? 这就是线程安全问题, 这就要让线程同步, 说白了就是让线程一个一个的执行, 串行执行, 一个线程执行完毕
 * 把最终的数据 同步到主存之后, 其他线程再工作, 也可以通过 volatile 关键字强制把变量刷回主存.
 *
 *
 * 线程中调用 sleep() wait() accept() read() write() 都会阻塞线程
 * 阻塞线程分: 永久阻塞必须被其他线程唤醒 和 定时阻塞阻塞一定的时间就会从Blocking状态->Ready-->Running
 * 阻塞线程的目的就是避免CPU的调度, 或者避免cpu的空转, 一般在自旋中会在一定限制内直接 阻塞线程.
 *
 * @author: Liu Jinyun
 * @date: 2021/2/14/15:51
 */
public class ThreadTest {
    /**
     * Thread类是对操作系统线程的java封装
     */
    @Test
    public void threadTest() throws InterruptedException {
        Thread thread = new Thread(() -> {
            // 线程的 static native 方法, 是 Running->Ready状态, 都处于Runnable状态
            // 其实没啥多大的效果, 就是让当前线程重新竞争cpu的执行权。
            Thread.yield();
            System.out.println("hello");
        });

        thread.start();
        Sleeper.sleep(5);
        // native isAlive() 判断绑定的线程是否释放, 一般创建的线程的销毁由操作系统来回收线程资源
        // 线程执行完 target 方法就会回收线程
        System.out.println(thread.isAlive());
        // join() 连接 同步方法 synchronized , 如果调用线程还存活, 就wait()
        // thread 这个对象调用 wait() 只要这个线程活着就加入到阻塞队列中,
        // thread就是monitor, 然后是主线程会执行sync方法,所以 wait()
        // 会让主线程通过thread(monitor来进入wait set)
        thread.join();
    }

    /**
     * Object 中定义了 wait() notify() 两个方法
     * 这两个方法必须在 同步代码 中使用, 前提是必须获取锁才可以
     */
    @Test
    public void waitNotifyTest() throws InterruptedException {
        synchronized (this) {
            // Object的wait()方法, 适用于必须获取锁对象才行
            // wait() 空参一旦调用, 当前线程阻塞队列中, 然后释放锁, 就是把这个对象头中的状态修改一下.
            // wait() 调用之后, 这个对象应该会保存 这个对象关联的wait队列中的tid, 锁的状态重置。
            // 然后这个对象的 竞争队列 中的线程会尝试去 竞争 这个锁, 竞争到了就当这个对象当前线程绑定一下
            // 同时修改 一下 锁的标识位, 其他没有竞争到的线程看到状态有人了, 就等着再次竞争。

            // 它会一直阻塞, 知道其他线程获取该对象锁, 调用 notify() 让wait()队列中的一个线程 加入到 竞争队列
            // notifyAll() 让所有wait set 中的线程都加入到 contend Queue 中去等待锁释放之后, 竞争锁

            // 带有参数的, elapsed 自动会把这个wait Set的tid加入到 contend Set.
            wait(5);
            System.out.println("hello waiter");
        }
    }

    /**
     * 多线程编程 要时刻处理线程中断
     * 中断请求是非常必须的 在整个操作系统任务调度中都发挥着重要的作用
     * 目前理解： 就是这个线程中断, 引起了这个线程的注意, 可能是继续执行, 也可能是用于关闭这个线程的资源
     * 具体怎么操作应该由线程来完成。
     *
     */
    @Test
    public void interruptTest() {
        // 当前线程被中断了吗？
        // 这个是调用的是线程的静态方式 但是这个调用完毕之后会自动清空中断的状态
        boolean interrupted = Thread.interrupted();
        System.out.println(interrupted);
    }


    @Test
    public void interrupt2Test() {
        Thread t = new Thread(() -> {
            System.out.println("执行任务");
            // 这个仅仅调用实例方法判断是否中断
            Thread thread = Thread.currentThread();
            thread.interrupt();
            boolean interrupted = thread.isInterrupted();
            System.out.println("threadName: " + thread.getName() + " ‘s 实例中断状态： " + interrupted);

            boolean staticInterrupted = Thread.interrupted();
            System.out.println("threadName: " + thread.getName() + " ‘s 静态中断状态： " + staticInterrupted);

            // 一旦调用了静态的判断中断方法就要进行 进行判断如果这个线程被中断了 还要继续把中断继续往外抛
            if (staticInterrupted) {
                thread.interrupt();
            }
        });

        t.start();


    }
}
