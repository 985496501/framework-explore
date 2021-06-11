package com.example.thread;

import io.netty.channel.DefaultEventLoopGroup;

import java.util.concurrent.TimeUnit;

/**
 * @author: jinyun
 * @date: 2021/6/3
 */
public class EventLoopGroup {

    public static void main(String[] args) throws InterruptedException {
        DefaultEventLoopGroup eventExecutors = new DefaultEventLoopGroup();
        eventExecutors.next().execute(()-> System.out.println("hello world"));

        TimeUnit.SECONDS.sleep(1);
        eventExecutors.shutdownGracefully();
    }
}
