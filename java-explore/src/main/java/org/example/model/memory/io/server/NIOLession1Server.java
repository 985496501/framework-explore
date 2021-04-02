package org.example.model.memory.io.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author: jinyun
 * @date: 2021/4/2
 */
public class NIOLession1Server {
    public static int times = 0;

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        // 设置这个非阻塞
        serverSocket.configureBlocking(false);
        // 设置服务的端口
        serverSocket.bind(new InetSocketAddress(8080));

        for (; ; ) {
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
}
