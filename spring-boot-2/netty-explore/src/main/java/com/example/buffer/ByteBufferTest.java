package com.example.buffer;

import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * {@link java.nio.ByteBuffer} NIO相关的编程, 我们先看 数据的存储, ByteBuffer, 字节缓冲区
 * 字节缓冲区就是一个对象; 但是这个对象可以在 堆上创建分配, 也可以在直接内存上创建 分配;
 * 这就要看 操作系统 相关的虚拟内存, 应用程序如何操作 主存;
 * 基本操作有6种;
 * get/put read/write single bytes;
 * bulk get/put that transfer contiguous sequences of bytes from.. into...
 * ...
 *
 * 创建这个对象的方式： allocation;  wrapping
 * 要么是调用alloc分配内存创建一个空的 ByteBuffer;
 * 要么就是通过一个 byte array 来创建一个包裹字节数组的 byteBuffer
 *
 * 直接内存 和 堆内存
 * is either direct or non-direct;
 * 如果是直接内存创建的 字节缓冲区, JVM will make a best effort to perform native I/O
 * operations directly upon it. 就是 不需要把这个buffer的content 也就是byte array copy 到
 * an intermediate buffer 在调用底层操作系统本地I/O之前之后,
 * intermediate: 中等的, 两者状态之间的; 这里就是不需要 把byteArray 在内核中来回的copy了.
 * 我们的应用程序 直接调用native方法, JVM会帮我们直接操作 这块内存, 比如直接计算, 然后发送的网卡;
 * 因为这个内存被 我们java中的一个引用 指着; 我们可以直接操作我们这个引用变量;
 * 通过 allocateDirect(); by this method typically have somewhat higher allocation and deallocation costs than non-direct.
 * 使用这个方法 分配和释放 资源的开销 要比 堆内存要有 somewhat higher cost;  somewhat: to some degree, 稍微 有一点;
 *
 * The contents of direct buffers may reside outside of the normal garbage-collected heap, and so their impact upon the
 * memory footprint of an application might not be obvious.
 *
 * It is recommended that direct buffers (should) be allocated primarily for large, long-lived buffers that are subject
 * to the underlying system's native I/O operations.
 *
 *
 *
 *
 * @author: Liu Jinyun
 * @date: 2021/5/3/16:43
 */
public class ByteBufferTest {
    /**
     * 首先java 通过系统调用  分配一块直接内存 建立文件系统的channel, 通过channel
     * 把数据读到 直接内存, 然后通过这个直接内存引用对象把数据读到 JVM的内存中;
     *
     * @throws IOException
     */
    @Test
    public void fileTest() throws IOException {
        RandomAccessFile accessFile = new RandomAccessFile("D:\\mission\\v2\\framework-explore\\spring-boot-2\\netty-explore\\src\\main\\resources\\1.txt", "rw");
        FileChannel channel = accessFile.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(16);

        // 通过 channel 读byte到buffer, buffer 处于写模式
        channel.read(byteBuffer);
        int position = byteBuffer.position();
        log(byteBuffer);

        // 修改成 读 模式; pos = 0, limit = pos;
        byteBuffer.flip();
        log(byteBuffer);

        byte[] bytes = new byte[position];
        if (byteBuffer.hasRemaining()) {
            // pos < limit
            byteBuffer.get(bytes, 0, bytes.length);
            byteBuffer.clear();
            log(byteBuffer);
        }
        channel.close();
        System.out.println(new String(bytes));
    }

    private void log(ByteBuffer byteBuffer) {
        String readByteBuffer = String.format("position: %d, limit: %d, capacity: %d",
                byteBuffer.position(), byteBuffer.limit(), byteBuffer.capacity());
        System.out.println(readByteBuffer);
    }
}

