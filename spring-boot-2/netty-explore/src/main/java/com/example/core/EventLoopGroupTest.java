package com.example.core;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.DefaultSelectStrategyFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.*;
import io.netty.util.internal.PlatformDependent;
import org.junit.Test;

import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.*;
import java.util.concurrent.Future;

/**
 * Netty 抽象的  事件循环组
 * 看看它做了什么事情
 *
 * @author: Liu Jinyun
 * @date: 2021/4/10/23:42
 */
public class EventLoopGroupTest {
    public static void main(String[] args) {
        /**
         * 我们需要看这个NioEventLoopGroup()的全参构造方法 我们看下这个究竟看了些事情, 向操作系统申请了那些资源
         * 所有的中间件 再网络通信方便都使用了 EventLoopGroup 这个线程模型;
         * new NioEventLoopGroup(); 实际上就是默认参数:
         * -nthread=Processor * 2
         * -executor=null
         * -DefaultEventExecutorChooserFactory.INSTANCE  默认使用了 simple round-robin todo: 具体看这个选择策略， 来了一个任务, 选择哪个 EventExecutor
         * -RejectedExecutionHandlers.reject() 拒绝策略
         * -selectProvider.provider()  {@link SelectorProvider} platform-standalone todo: 看这个平台相关的封装了系统调用的 多路复用器
         * -selectStrategyFactory  {@link DefaultSelectStrategyFactory} todo: 选择select() 系统调用的选择策略, 策略工厂 后面具体看
         *
         *
         * see{@link MultithreadEventExecutorGroup} 它就是 NioEventLoopGroup 的核心基类;
         * 多线程 事件执行器 组, 是事件执行器组的事件, 名字看出就是多线程 同时 处理任务;
         *
         *
         * 0. 创建一个执行器, {@link java.util.concurrent.Executor} 它的默认实现类： {@link ThreadPerTaskExecutor}
         * 我们仅仅看 执行器执行方法, 它就是一个来一个任务 通过工厂创建一个线程执行任务, 执行完就释放了, 创建这个对象需要一个工厂
         * 可以使用 Netty 的 名称工厂: {@link DefaultThreadFactory}
         *
         *
         * 1. 首先声明了 EventExecutor[nthread] {@link io.netty.util.concurrent.EventExecutor}, 默认就是 nthread = processor * 2;
         * 然后newChild(executor, args) 创建 事件执行器; 事件执行器应该有： 事件NIO事件 和 处理事件的线程就是 executor； 然后就跳到了
         * {@link NioEventLoopGroup} 也就是我们探究的这个类;
         * 创建 {@link io.netty.channel.EventLoop} 注意这个packageName;
         * 实际上创建的是 {@link io.netty.channel.nio.NioEventLoop} 都是channel包下的;
         * 创建这个对象需要的默认参数:
         * 1. {@link NioEventLoopGroup} parent;  not null;
         * 2. {@link java.util.concurrent.Executor} executor;
         * 3. {@link SelectorProvider} selectorProvider;
         * 4. {@link io.netty.channel.SelectStrategy} strategy;
         * 5. {@link io.netty.util.concurrent.RejectedExecutionHandler} rejectedExecutionHandler;
         * 6. {@link io.netty.channel.EventLoopTaskQueueFactory} queueFactory;
         *
         * 首先我们需要看它的父类也是一个模板类：{@link io.netty.channel.SingleThreadEventLoop}
         * 看到名字就知道是 单线程事件循环,
         * 它完成了哪些初始化方法: [申请了哪些资源]
         * parent, executor, false, taskQueue, tailTaskQueue, rejectedHandler
         * 这个单线程事件循环就干了一件事就是 维持了 tailTasks = tailTaskQueue;
         *
         * 又声明了它的父类：{@link io.netty.util.concurrent.SingleThreadEventExecutor}
         * 单线程事件执行器:
         *
         * 有调用了它的父类: {@link AbstractEventExecutor} 这个是 EventExecutor 的基类实现;
         * parent 就维护在这个抽象类里;
         *
         *
         *
         * static Queue<Runnable> newTaskQueue(EventLoopTaskQueueFactory) 因为这个方法在Nio
         * 事件循环这个类里面, 所以 所以它通过任务队列工厂 创建任务队列;
         * 最后创建的队列 {@link PlatformDependent#newMpscQueue(int)} todo: 如何利用无锁完成n生产者: 1消费者 的设计就在这  以及 linkedBlockingQueue的实现;
         *
         *
         * 2.
         *
         *
         *
         */
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
    }


    /**
     * 事件 执行器：
     * 什么是事件执行器? 一种特殊的 时间执行器组, 同时 它提供了方法来查看当前线程是不是在 事件循环中;
     * 它同时继承了事件执行器组, 提供了一般的获取方法;
     *
     *
     *
     */
    public void eventExecutorTest() {
        EventExecutor executor = new DefaultEventLoop();
    }

    /**
     * // todo: 抽取到 concurrent package 下;
     * {@link java.util.concurrent.Executor} 执行器, 执行提交的任务, 屏蔽了你用哪个线程执行任务, 具体调度的细节;
     * {@link java.util.concurrent.ExecutorService} 执行器服务; 它提供了关闭方法, 当这个不需要的时候需要关闭释放资源;
     * shutdown(): 一旦执行就不允许提交任务,
     * shutdownNow(): 所有的任务包括现在正在执行的任务都不会执行了;
     *
     * 同时引入了{@link java.util.concurrent.Future}能够追踪n个异步任务的结果; submit(): 提供了一个Future类可以用于任务的取消和结果的等待获取
     */
    @Test
    public void executorServiceTest() throws InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Runnable task = ()-> {
            System.out.println("我要开始执行任务了....");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                // 这就明白了这个中断异常的情况, 因为这个方法让线程阻塞了, 需要外接其他线程打断它让他继续执行
                // 还是立即返回 这个完全有任务本身决定 原来是在这啊
                // return;
                System.out.println("有人打断了我, 我可以继续执行了");
            }

            System.out.println("任务执行结束....");
        };

        java.util.concurrent.Future<?> future = executorService.submit(task);
        // 服务停止, 任务完成 或者 停止 或者异常都是 true
        try {
            Object o = future.get(1, TimeUnit.SECONDS);
        } catch (ExecutionException | TimeoutException e) {
            // 我们要求任务在1s内必须有返回结果 实时操作系统 的感觉：
            System.out.println("1s内我没有拿到结果, 我直接取消任务了... timeout...");

            // 如果这个任务还没有执行调用这个方法会让这个任务永远都不会执行, 如果已经执行则取决于参数
            // todo: 是否 中断, 这个中断 是 操作系统中非常非常 重要的概念;
            // false: 不中断 任务继续执行  true: 中断 强制任务结束
            future.cancel(true);
        }

        boolean done = future.isDone();
        System.out.println(done);

        TimeUnit.SECONDS.sleep(3);
        executorService.shutdown();
    }

    /**
     * 有结果的任务, Runnable 没有任何结果返回, jdk1.5之后加入
     * 可以有任务结果返回的接口, 同时配合 Executors 使用;
     */
    @Test
    public void callableTest() throws ExecutionException, InterruptedException {
        // 实际上就是创建一个包装对象啊 这个 这个就是我们给什么 就返回什么
        // Callable<Class<String>> hello = Executors.callable(() -> System.out.println("hello"), String.class);
        // 实际上我们是包装一个方法 有参数 有返回值的
        int i = 3, j = 5;
        Callable<Integer> callable = ()-> add(i, j);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Integer> submit = executorService.submit(callable);
        Integer integer = submit.get(); // block
        System.out.println(integer);

        executorService.shutdown();
    }

    /**
     * 模拟一个耗时的计算, 如果纯计算 线程的数量不用太多
     * 如果是IO的就 设置多一点线程, 但是这个线程一旦IO, 就是自动调用BLOCK 原语让自己阻塞, 从而不会浪费CPU的时间片
     * 等得IO结束, 操作系统的产生事件, 可能是IO中断, 来唤醒阻塞的thread 到就绪状态, 重新竞争CPU的执行权;
     */
    private int add(int i, int j) {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return i + j;
    }

    /**
     * {@link AbstractExecutorService}: 这个就是一个基类实现 执行器服务;
     * 默认实现了 ExecutorService的执行方法; submit(); invokeAll(); invokeAny()
     * 引入了一个接口 {@link RunnableFuture}; 它就是一个任务对象, 包装了最基本的任务和任务的返回值;
     * 这个对象 聚合了任务和结果, 既有任务又有结果, 可以执行它的任务, 然后把结果写到它的内存属性里面;
     * 这里默认就是我们给什么值 就返回了什么值; 而在实际的操作中, 我们是 异步获取计算的结果;
     *
     *
     * 这个抽象的服务, 提供了模板方法 RunnableFuture newTaskFor(Runnable, T) 这个默认方法; 默认是用了
     * {@link FutureTask}这个类作为默认实现, 同时它允许 子类覆盖这个方法, 自定义自己的任务;
     * FutureTask:
     * volatile int state; 1. 保证内存的可见性; 防止指令重排  todo: 如何保证, 既然是关键字, 那么编译器是怎么编译成OS保证的标识的呢?
     * 1. 创建这个任务的时候 state = NEW (0)
     * 2. 直接看它的run(); 先判断任务是不是新建状态啊, 不是直接返回；
     *      是新建状态, 使用原子操作 为当前任务设置执行线程, 如果失败直接返回, 这里就体现了多线程竞争任务的代码;
     *      然后设置了ran标识 try 包裹任务方法, 人家也不知道你的任务成功还是失败;
     *      如果成功了把结果给result; 先原子操作 设置任务状态为 Completing, 然后设置值,
     *      这里使用原子操作是因为有其他线程进行get(), 也就是说 Completing 代表任务执行成功了,
     *      没有异常的发生,但是你get()是获取不到值的, 然后再进行normal设置, 这就是最终的完成状态了,
     *      最后调用一个 finishCompletion() 这个方法肯定是唤醒get() 请求的线程的; 我猜;
     *      果然如此;
     *
     *
     */
    @Test
    public void abstractExecutorService() {

    }

}
