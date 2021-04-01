package org.example.model.memory.nio;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * NIO 的入门探索。 java是如何使用直接内存的, 然后为了支持 直接内存的回收工作, 引入了一种引用类型. 虚引用
 * see {@link ByteBuffer} 这个是直接内存操作的核心入口, java提供的最基础 支持。
 * jdk1.4开始支持.
 *
 * @author: jinyun
 * @date: 2021/4/1
 */
public class NioGuideTest {
    public static final int DEFAULT_CAPACITY = 1024;

    public static void main(String[] args) {
        // 这样一个简单的简单实现：
        // 我们编码写的 hello world 这个简单的字符串 写到 直接内存里, 然后再从直接内存读出来
        String str = "hello world";
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        // 调用这个入口函数 相当于暴露出来的工厂方法, 实际的 类不能被外部调用, 被开发者私有
        ByteBuffer directMemory = ByteBuffer.allocateDirect(bytes.length);
        // put(src, 0, src.length), 这个就是循环调用 JVM 的putByte()  bulk put.
        directMemory.put(bytes);


        // JVM heap分配一个内存
        byte[] heapMemory = new byte[bytes.length];
        directMemory.get(heapMemory);
        System.out.println(new String(heapMemory));
    }
}
