package com.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * 继续探索netty
 * netty分包 以及各个 层级的设计
 * 是现在java 分布式项目 通信方面性能优秀的组件.
 *
 * todo: 服务的心跳机制与断线重连，Netty底层是怎么实现的?
 *
 * 1. 心跳机制
 * 2. 断线重连
 * 3. 编解码
 * 4. 自定义协议
 *
 * 线程模型
 * IO模型
 * 事件驱动
 * 其他框架对netty包的使用
 *
 * Netty的架构设计, 接口的分层设计
 *
 *
 * 首先提出问题
 *
 * @author: jinyun
 * @date: 2021/2/9
 */
public class MainNetty {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(4);
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);


    }
}
