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
         * 我们需要看这个NioEventLoopGroup()的全参构造方法
         *
         *
         */
        EventLoopGroup eventExecutors = new NioEventLoopGroup(1);

//        new NioEventLoopGroup();
    }
}
