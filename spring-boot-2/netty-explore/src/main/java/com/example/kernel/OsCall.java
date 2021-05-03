package com.example.kernel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 首先记录需要明白一个非常重要的系统函数 select()
 * fd_set(): 文件描述符 的集合, 实际就是 long[], 我们都知道int占  4bytes = 32bit
 * long 是int的两倍 8bytes = 64bit
 * kernel requires at most 1024 fd. 0-1023
 * 一个long可以通过bitmap表示有64个fd, 1024 需要多少long呢? 1024=2^10
 * 64=2^6  也就是需要 2^4=16
 * fd_set = new long[16];
 *
 * 常见的fd有什么呢? stdin: 标准的输入输出,  socket: 建立网络连接
 *
 * select(int nfds, readfds, wrietefds, exceptionfds, timeout) int;
 * nfds: 表示需要监听的最大的文件号 + 1, 比如建立socket fd=15, 这里需要监听它就要传16, 就是从0-15一共16个都要监听;
 *      listen(80) = fd(15), 我们调用listen()函数 其实就是创建一个socket连接的 fd;
 *      linux 是属于文件系统, 一切都是文件的形式
 *
 * r: 读事件　　可读文件句柄　集合;
 * w: 写事件    可写文件句柄  集合;
 * e: 异常      发生异常的文件句柄 集合;
 * timeout:    本次查询的超时时间, select() 函数就是 long[16] 上进行遍历判断, 直到有 发生事件数 int返回,
 *      没有事件就一直loop, 直到timeout;  精确到百万分之一秒;
 *
 * select() 函数有3中特性：超时结束, 无超时阻塞, 轮训 3中方式;  非阻塞, non-blocking
 *      效率较高, 它仅仅是通过监控 文件描述符 的变化情况 来监听是否是 事件 到达,
 *      具体需要进行处理事件 还是要应用程序就处理事件;
 *
 * connect(), accept(), recv(), recvfrom() 这都是阻塞程序, 需要其他线程或者设备唤醒;
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 * @author: Liu Jinyun
 * @date: 2021/5/2/18:08
 */
public class OsCall {
    /**
     * java封装的Selector 就是一个 multiplexor of SelectableChannel, 多路复用器
     * 就是将多个channel 注册到这个 selector到, 监听IO event;
     *
     * OS 相关的实现 下面就不看了就是封装了系统调用相关的函数, 直接调用open就自动创建, 系统资源不用需要调用 close() 关闭;
     * channel又通过key 注册到Selector上, {@link java.nio.channels.SelectionKey}
     * 定义了3条语义：
     * 1. key set 包含的key 代表了当前channel注册到这个selector上的key, keys可以获取;
     * 2. selectedKey set represents each key's channel was detected to be ready for at least one of
     * the operations identified in the key's interest. 发生时间的key
     * 3. cancelledKey 代表key已经被取消, 但是channels还没有注销
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        // 启动一个serverSocket系统调用listen 8080 端口
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
//        serverSocketChannel.configureBlocking(false);

        // blocking method
        SocketChannel socketChannel = serverSocketChannel.accept();
        System.out.println("connection comes...");

        // 到这是已经有人连接上来了, 已经完成了3次握手, socket已经在 accept Queue
        // 下面就是 监听是否可读 可写
//        socketChannel.read()


    }


    /**
     * {@link java.nio.channels.Channel}: java定义这个信道, 或者直接称谓它 为Channel;
     * a nexus for IO operations.
     * A channel represents an open connection to an entity such as Hardware device, a file, a network socket.
     * or a program component that is capable of performing one or more distinct I/O
     * operations, rw.
     */
}
