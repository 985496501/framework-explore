package com.example.transport.channel;

/**
 * EventLoop: 什么是事件循环?
 *
 * 事件循环就是可以处理 所有的IO事件, 一旦有channel注册到这个 事件循环器 实例上;
 * 一个 事件循环 往往处理多个 Channel, 这要看具体的实现是怎么样的?
 *
 * {@link io.netty.channel.EventLoopGroup}: 它继承了 {@link io.netty.util.concurrent.EventExecutorGroup}
 * 事件循环组 允许channel注册到 事件循环器上面来; 同时它可以维护多个事件循环器, 可以获取下一个事件循环器 进行注册事件；
 * 所以 EventLoop 可以注册事件;
 *
 *
 *
 *
 *
 * @author: jinyun
 * @date: 2021/4/25
 */
public class EventLoopTest {

}
