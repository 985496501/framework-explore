package org.example.feature.jol;

import org.example.feature.jol.util.LayoutUtil;
import org.example.model.thread.util.Sleeper;
import org.junit.Test;

/**
 * 研究对象的布局
 * java object layout.
 *
 * header + instance-data + padding.  [ˈpædɪŋ]
 *
 * 锁对象头： 8bytes = 64位   两行头  markword
 *
 *
 * @author: Liu Jinyun
 * @date: 2021/2/17/21
 */
public class ObjectTest {

    @Test
    public void objectTest() {
        Object o = new Object();
        LayoutUtil.print(o);
        // 16 bytes
        // header:    4bytes    01 00 00 00  16进制
        // 1 byte = 8 bit ,    4 bytes = 4组 16进制

        // 对象都是 8bytes的整数倍： 比如最简单的object对象在64位计算机中 是 16bytes
        // 一个int 是 4bytes, long 是 8 bytes

        Sleeper.sleep(2);
        LayoutUtil.print(o);

        synchronized (o) {
            // 48 e6 f2 02
            LayoutUtil.print(o);
        }

        // 既然是bit存储 根据计算机组成原理 分为:  大端存储 和 小端存储
        // 也是16bytes  int i 一样都会转成integer
//        Integer i = 1;
//        LayoutUtil.print(i);
    }
}
