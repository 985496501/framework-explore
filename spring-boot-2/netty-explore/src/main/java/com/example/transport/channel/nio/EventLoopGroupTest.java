package com.example.transport.channel.nio;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.DefaultSelectStrategyFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.*;
import io.netty.util.internal.PlatformDependent;

import java.nio.channels.spi.SelectorProvider;

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
}
