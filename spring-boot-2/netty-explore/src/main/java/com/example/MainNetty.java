package com.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 继续探索netty
 * netty分包 以及各个 层级的设计
 * 是现在java 分布式项目 通信方面性能优秀的组件.
 * <p>
 * todo: 服务的心跳机制与断线重连，Netty底层是怎么实现的?
 * <p>
 * 1. 心跳机制
 * 2. 断线重连
 * 3. 编解码
 * 4. 自定义协议
 * <p>
 * 线程模型
 * IO模型
 * 事件驱动
 * 其他框架对netty包的使用
 * <p>
 * Netty的架构设计, 接口的分层设计
 * <p>
 * <p>
 * 首先提出问题
 *
 * @author: jinyun
 * @date: 2021/2/9
 */
public class MainNetty {

    /**
     * 先按照设计者的思路写一个标准的 server
     * <p>
     * nthread = ncpu + tio/tcpu * ncpu
     * 也就是如果 io的时间是 计算的两倍 线程的数量=3
     *
     * @param args
     */
    public static void main(String[] args) {
        // 线程的最大效率：　那个公式 看下操作系统的课本 学习下操作系统回去 2021-4-23
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(4);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) throws Exception {

                        }
                    });

            ChannelFuture f = b.bind(9632).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
}
