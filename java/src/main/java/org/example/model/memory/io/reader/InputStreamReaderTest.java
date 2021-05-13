package org.example.model.memory.io.reader;

import org.junit.Test;

/**
 * IO流 是操作系统中非常重要的资源
 * 也是非常影响计算机性能的因素, 处理好IO能显著提高应用的性能, 都是同步IO, IO的序列是有序的, 并且计算机进行IO的时候,
 * 线程会被挂起, IO处理完毕之后, 才会唤醒线程重新到 ContendQueue.
 *
 * 输入流是相对概念, 我们常常需要把文件系统的data 或者 网卡缓冲区的 data, 读取到JVM管理的内存中区, 读取进来肯定是
 * 有字符编码格式的 byte[], 还要按照 data-type, 转成我们要的 format, 比如是 字符串, 对于字符串的操作java提供了
 * 字符流的操作, 性能比 字节流高很多. 如何高, 探究一下?
 *
 * 非常优秀的IO模型: 线程模型 + 内存模型
 * 探究一下为什么OKHttp性能高?
 * Consider using 'getBeanNamesForType' with the 'allowEagerInit' flag turned off, for example.
 *
 * @author: jinyun
 * @date: 2021/2/19
 */
public class InputStreamReaderTest {

    @Test
    public void inputSteamReaderTest() {

    }
}
