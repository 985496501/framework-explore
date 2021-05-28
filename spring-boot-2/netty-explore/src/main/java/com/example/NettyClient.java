package com.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Netty是非常优秀的网络通信框架, 值得 已经高级java开发工程师的学习;!!!
 * 好东西 就是要 一遍 一遍的学习, 直到精通;
 * 学习， 写个demo, 摸索, 看实现, 看设计, 最后不断的品味，
 * 逐渐建立完成的知识体系, 那时候才会 全部茅塞顿开;
 *
 *
 * @author: jinyun
 * @date: 2021/5/28
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup(2);
        try {
            Bootstrap b = new Bootstrap();
            b.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    // turn off the nagle algorithm.
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            ChannelFuture f = b.connect("localhost", 9632).sync();
            f.channel().closeFuture().sync();
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }

    public static class EchoClientHandler implements ChannelHandler {

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        }
    }
}
