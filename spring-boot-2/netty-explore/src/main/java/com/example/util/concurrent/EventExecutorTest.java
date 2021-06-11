package com.example.util.concurrent;

import org.junit.Test;

import java.nio.charset.Charset;

/**
 * netty 对 事件执行器 的抽象
 *
 * @author: jinyun
 * @date: 2021/4/25
 */
public class EventExecutorTest {

    /**
     * todo: {@link io.netty.util.concurrent.SingleThreadEventExecutor} 这个是他妈的重点中的重点
     * 先不看了, 后面再说吧;
     */
    @Test
    public void singleThreadEventExecutorTest() {
        System.out.println(Charset.defaultCharset().name());
    }




    /**
     * 事件执行器对象抽象这个就核心了; 事件执行器, 就是执行事件任务的, 也就是异步执行IO事件的;
     *
     * {@link io.netty.channel.EventLoop} 具体看channel 什么是  事件循环
     */
    @Test
    public void eventExecutorTest() {

    }

    /**
     *
     */
    @Test
    public void abstractEventExecutorTest() {

    }


    /**
     * 这是netty 对java执行器开始扩展实现, 它扩展了 ScheduledServiceExecutor
     * 提供了优雅关闭 执行器的接口定义
     * 统一 提供了 从这个组 获取 下一个 EventExecutor 的方法： next()
     */
    @Test
    public void eventExecutorGroupTest() {
    }


}
