package com.example.core;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * netty 抽象的  事件循环组
 * 看看它做了什么事情
 *
 * @author: Liu Jinyun
 * @date: 2021/4/10/23:42
 */
public class EventLoopGroupTest {
    public static void main(String[] args) {
        /**
         * 我们需要看这个NioEventLoopGroup()的全参构造方法 我们看下这个究竟看了些事情, 向操作系统申请了那些资源
         *
         * 所有的中间件 再网络通信方便都使用了 EventLoopGroup 这个线程模型;
         */
        EventLoopGroup eventExecutors = new NioEventLoopGroup(1);
    }
}
