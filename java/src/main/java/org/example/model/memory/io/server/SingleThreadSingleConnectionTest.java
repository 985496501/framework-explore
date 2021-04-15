package org.example.model.memory.io.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 传统的io 服务器
 *
 * @author: jinyun
 * @date: 2021/4/2
 */
public class SingleThreadSingleConnectionTest {
    public static final AtomicInteger connections = new AtomicInteger(0);

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        // 单线程 让它能处理多次连接  但是同时只能一次处理一次
        for (; ; ) {
            // accept a new connection client, blocking method
            Socket socket = serverSocket.accept();
            System.out.println("一个新的连接[" + connections.incrementAndGet() + "]接收，" + socket.getInetAddress().getHostName());

            InputStream inputStream = socket.getInputStream();
            // blocking method 0-255 没有数据就是-1, 如果有数据进来就读取它, 一次连接多次读取, 同时只能读一次
            while (inputStream.read() > 0) {
                byte[] bytes = new byte[1024];
                int read = inputStream.read(bytes);
                System.out.println("actually read: " + read + ", data: " + new String(bytes));
            }
        }

        // 申请的操作系统的资源, 直接关闭, 虽然关闭了jvm, 但是操作系统的钩子没有触发, 需要手动调用jvm的本地方法
        // 然后告诉OS 把申请的资源释放掉
    }
}
