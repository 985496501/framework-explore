package org.example.model.memory.io.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 传统的io 服务器
 *
 * @author: jinyun
 * @date: 2021/4/2
 */
public class MultiThreadsMultiConnectionsTest {
    public static final AtomicInteger connections = new AtomicInteger(0);

    /**
     * 不要再提示我不要用他们提供的工厂方法, 阿里的顶级框架中都是直接使用的。 但是还是自定义线程池
     * 这个cachedThreadPool就是没有核心线程数, 队列中也只有一个, 就会无脑的创建线程, 并且缓存线程  一分钟
     */
    public static final ExecutorService executor = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r);
        t.setName("multi-thread-");
        t.setDaemon(false);
        return t;
    });

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        for (; ; ) {
            // accept a new connection client, blocking method
            Socket socket = serverSocket.accept();
            // 只要有一个socket连接上来我们就起一个新的线程去处理 ==> 数据读写io
            executor.submit(() -> {
                System.out.println("一个新的连接[" + connections.incrementAndGet() + "]接收，" + socket.getInetAddress().getHostName());
                InputStream inputStream = null;
                try {
                    // socketInputStream
                    inputStream = socket.getInputStream();
                    // blocking method 0-255  如果有数据进来就读取它, 一次连接多次读取, 同时只能读一次 我使用telnet每键入一个字符就会被读取
                    // 没有数据就是-1, 关闭了telnet就会自动-1.
                    // 注意这个线程的 状态是 RUNNING, 说明这个线程一直可以被操作系统调度, 也就是会被分配时间片, 这是非常浪费计算资源的。
                    while (inputStream.read() > 0) {
                        byte[] bytes = new byte[1024];
                        int read = inputStream.read(bytes);
                        System.out.println("actually read: " + read + ", data: " + new String(bytes));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        // 申请的操作系统的资源, 直接关闭, 虽然关闭了jvm, 但是操作系统的钩子没有触发, 需要手动调用jvm的本地方法
        // 然后告诉OS 把申请的资源释放掉
        // 这样虽然可以 通过多线程处理 多连接数据读写问题, 主要解决了读写的阻塞问题, 可以让服务端一直可以监听socket并处理
        // 但是有一个问题是： 这个分配的线程要一直专属为这个连接工作吗？ 就是这个线程资源要等着IO结束然后在进行计算吗
        // 线程的任务就是 计算, 而一个连接任务, 往往不是密集的数据IO, 还有IO也占用了线程这份资源。
        // 如何解决这个问题呢？ 我们让一个 worker 线程同时处理多个IO事件就行了, 但是要求这个查询IO是否有写入事件
        // 没有应该立即返回没有的事件让 worker 继续其他IO的处理, 如果有就处理数据进行计算, 如果循环了很长事件, 所有的连接都没有写入事件
        // 还应该让工作线程一直 loop, 去检测并处理事件吗.
        // 如果一个连接断开, 应该优雅的把 注册的socket移除掉.

        // 综上所诉, 操作系统必须支持 非阻塞的 系统调用方法。
    }
}
