package com.example;

import com.example.transport.channel.StringChannelInboundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultThreadFactory;

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
 * netty对粘包 拆包 的支持：
 *
 * <p>
 * Netty的架构设计, 接口的分层设计
 * <p>
 * <p>
 * 首先提出问题
 *
 * <a href="https://www.wireshark.org/download.html">抓包工具</a>
 * 什么时候这个抓包工具适配了 MAC OS,  arm版本 我就买MAC
 * <p>
 * 使用netty开发一个http2.0协议的server,
 * springboot是可以使用netty作为内置  server 服务的, 可以玩一下;
 * <p>
 * 我们画的netty的  <a href="https://www.processon.com/diagraming/5d32e637e4b043dcf83f65de}">事件处理模型</a>
 *
 * @author: jinyun
 * @date: 2021/2/9
 */
public class NettyServer {

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
        // thread: boss-poolId-nextId, 仅仅会有一个boss-1-1 RUNNING, 这个nthreads 你无论传什么 都是1
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("boss"));
        // 没有工作线程, 应该是懒的, 有任务需求的时候才会创建工作线程
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(4, new DefaultThreadFactory("worker"));

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel channel) {
                            ChannelPipeline pipeline = channel.pipeline();
                            // pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(8, 0, 2, 0, 2));
                            // pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                            pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                            pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                            // channel.pipeline().addLast(new ByteArrayChannelInboundHandler());
                            channel.pipeline().addLast("stringHandler", new StringChannelInboundHandler());
                        }
                    });
            ChannelFuture f = b.bind(9632).sync();
            // main 线程wait, 等待关闭唤醒
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
