package org.example.model.memory.io.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * 我们使用了NIO的 ServerSocketChannel 但是好像这个东西它没有可以判断可以读的方法
 * 并且 获取连接这个必须无脑自旋 让CPU计算去判断, 这就必须使用 Selector了
 *
 * @author: jinyun
 * @date: 2021/4/2
 */
public class NIOLession2WithSelectorServer {
    public static int times = 0;

    public static void main(String[] args) throws IOException {
        // 获取操作系统实现的 多路复用器, windows linux都实现了 这里dubbo实现了区分跨平台创建 EventLoopGroup
        // 单例获取一个统一的 多路复用器
        Selector selector = Selector.open();

        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        // 设置这个非阻塞
        serverSocket.configureBlocking(false);
        // 设置服务的端口
        serverSocket.bind(new InetSocketAddress(8080));
        // 把serverSocket一些事件注册给 这个多路复用器

        // 具体查看 SelectionKey.OP_ACCEPT
        // 表示有外部的连接事件
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        // 表示成功建立连接
//        serverSocket.register(selector, SelectionKey.OP_CONNECT);
        // 如果可以读了
        serverSocket.register(selector, SelectionKey.OP_READ);

        for (; ; ) {
            // selector暴露的方法 select(): 判断是否有IO时间，至少返回1，不然就会被操作系统阻塞, select() 阻塞方法
            if (selector.select() > 0) {
                //
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                selectionKeys.forEach(k-> {
                    if (k.isAcceptable()) {
                        System.out.println("有新的连接进入...");
                    }

//                    if (k.isConnectable()) {
//                        System.out.println("tcp已经完成三次握手成功建立了连接");
//                    }

                    if (k.isReadable()) {
                        System.out.println("有数据写入进来了, 已经写入到咱们的网卡缓冲区了, 可以读取了哦|");
                    }
                });
            }
        }

    }


    private static void template(ServerSocketChannel serverSocket) throws IOException {
        // 非阻塞的IO 这个如果没有连接就会一直浪费CPU了
        SocketChannel socketChannel = serverSocket.accept();
        if (socketChannel == null) {
            // 这回疯狂输出
            System.out.println("cpu空转：" + (++times));
        } else {
            // 需要判断是否有数据进来啊 但是它没有具备判断可读能力的方法啊
            // 如果有连接进来 然后开始一直扫描缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
            // 在 堆外内存申请了1K的内容 然后把网卡的数据直接读到  直接内存
            socketChannel.read(byteBuffer);
        }
    }
}
