package com.example.transport.channel;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Netty抽象组件:
 * 什么是Channel, 信道, 通道, 文档中它是这么定义的, 它是连接 网卡socket 和 一个具有IO能力 组件的枢纽;
 * 什么是IO: read, write, connect, bind. 这4种都是需要系统调用; todo: 操作系统中对系统调用的解释;
 * 封装这个提供给用户什么能力呢?
 * 1. 当前channel的状态; 比如 它有没有连接, 有没有open?
 * 2. 这个channel的配置参数, 比如 receive buffer size.
 * 3. 这个channel支持的IO 操作, netty中所有的IO都是异步的, Future发挥了作用 {@link io.netty.channel.ChannelFuture}
 * 4. 这个channel绑定的ChannelPipeline, 它就是一个handler的链表, 用于处理data event.
 * 5. Channel是分层的, SocketChannel 是 ServerSocketChannel accepte()的时候创建的,所以它的parent就是前者;
 *
 *
 * {@link java.net.SocketAddress}: net包下, 这个socket地址抽象类, 没有任何实现, 没有任何协议；
 * {@link java.net.InetAddress}: net包, 代表了IP协议的实现, 主要用于ip地址的获取和封装 IPv4 IPv6
 * {@link java.net.InetSocketAddress}: 扩展了SocketAddress, 有ip和端口号的封装
 *
 *
 *
 *
 * @author: jinyun
 * @date: 2021/4/25
 */
public class ChannelTest {






    @Test
    public void InetAddressTest() throws UnknownHostException {
        // 通过注解hosts文件中配置的信息解析ip地址
        InetAddress localhost = InetAddress.getByName("localhost");
        System.out.println(localhost.getHostAddress()); // 127.0.0.1

    }


}
