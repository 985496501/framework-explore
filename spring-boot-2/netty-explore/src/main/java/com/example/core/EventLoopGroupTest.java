package com.example.core;

import io.netty.channel.DefaultSelectStrategyFactory;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.MultithreadEventExecutorGroup;
import io.netty.util.concurrent.ThreadPerTaskExecutor;

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
         *
         *
         * 2.
         *
         *
         *
         */
        EventLoopGroup eventExecutors = new NioEventLoopGroup();
    }
}
