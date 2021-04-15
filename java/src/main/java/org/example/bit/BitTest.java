package org.example.bit;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThreadPoolExecutor 的部分代码设计
 *
 * 使用了计算机组成原理的一些知识：
 *
 * 计算机最简单的数据类型： int 就是普通的数字(有符号整数)
 * 一般我们都说int 占 4个 byte    8bit/byte  所以一共是32位, x86 32位机器指令。
 * 0000 0000 0000 0000 0000 0000 0000 0000
 *
 * 我们先探究 1 byte == 也就是8个二进制
 * 0000 0000       8bit 表示的数值范围：-128-127  那么为什么这么表示?
 * 一个数在计算机中的表示 成为 machine code;  但是机器数在 计算机中的表示并不等于真正的数值，因为 前面有符号位导致的，
 * 那么 我们给出了一个真正的数的概念， 真值, 就是我们 1001 这就是我们现实世界说的 -1 也就是机器数的真值，但是计算处理这种电信号 就 1001 看样子就是9
 *
 * 原码：符号位+真值的绝对值
 * [1111 1111, 0111 1111] 就是我们说的[-127, 127], 但是原码 有个问题:
 * 1000 0000 = -0, 0000 0000 = +0 实际上都是 0
 * 1000 0000 = -0, 使用补码,舍弃了-0, 他的补码：1
 *
 * 引出反码： 符号位不变, 其他位取反, 正数不变， 但是负数就不容易看出来了, 只能转 原码计算。
 * 补码：正数还是相同  负数需要反码 末尾 + 1
 * 1111 1111 的补码:  1000 0001  = -1
 * 1000 0000 的补码：　
 * 为什么?
 *
 *
 */
public class BitTest {
    private final AtomicInteger ctl = new AtomicInteger(ctlOf(RUNNING, 0));

    /**
     * Integer.SIZE = 32
     * COUNT_BITS = 32-3=29
     *
     * 线程池使用高3位作为线程的状态位  低29位作为线程的数量
     */
    private static final int COUNT_BITS = Integer.SIZE - 3;


    /**
     * 0000 0000 0000 0000 0000 0000 0000 0001
     * 0001 0000 0000 0000 0000 0000 0000 0000
     * 0000 1111 1111 1111 1111 1111 1111 1111           capacity = 2^29 - 1 计数的最高位0 符号位表示正数
     * 1 符号右移 29 位,
     */
    private static final int CAPACITY   = (1 << COUNT_BITS) - 1;

    // runState is stored in the high-order bits

    /**
     * 原码: 1000 0000 0000 0000 0000 0000 0000 0001
     * RUNNING:
     * 反码：1000 0000 0000 0000 0000 0000 0000 0000
     *
     *
     *
     */
    private static final int RUNNING    = -1 << COUNT_BITS;
    private static final int SHUTDOWN   =  0 << COUNT_BITS;
    private static final int STOP       =  1 << COUNT_BITS;
    private static final int TIDYING    =  2 << COUNT_BITS;
    private static final int TERMINATED =  3 << COUNT_BITS;


    /**
     * Packing and unpacking ctl
     * @param c
     * @return
     */
    private static int runStateOf(int c)     { return c & ~CAPACITY; }
    private static int workerCountOf(int c)  { return c & CAPACITY; }
    private static int ctlOf(int rs, int wc) { return rs | wc; }


    @Test
    public void printBinaryTest() {
        // 11101 29 + 2 = 31  2^5-1
        System.out.println("COUNT_BITS: " + Integer.toBinaryString(COUNT_BITS));
        System.out.println("COUNT_BITS: (int) " + COUNT_BITS + "\n");

        // -1 << 29 : 1110 0000 0000 0000 0000 0000 0000 0000
        // 原码：1000 0000 0000 0000 0000 0000 0000 0001
        // 反码：1111 1111 1111 1111 1111 1111 1111 1110
        // 补码：1111 1111 1111 1111 1111 1111 1111 1111  <-- 29位
        // 标识：1110 0000 0000 0000 0000 0000 0000 0000
        // 实际上是把 低三位 --> 高三位
        System.out.println("RUNNING: -1 << COUNT_BITS " + Integer.toBinaryString(RUNNING));
        System.out.println("RUNNING: (int) " + RUNNING + "\n");

        // (1 << 29) - 1 : 0000 1111 1111 1111 1111 1111 11111 1111,     2^29-1
        System.out.println("CAPACITY: (1 << COUNT_BITS) - 1 " + Integer.toBinaryString(CAPACITY));
        System.out.println("RUNNING: (int) " + CAPACITY + "\n");

        // 000
        System.out.println("SHUTDOWN: 0 << COUNT_BITS " + Integer.toBinaryString(SHUTDOWN));
        System.out.println("SHUTDOWN: (int) " + SHUTDOWN + "\n");

        // 001  1
        System.out.println("STOP: 1 << COUNT_BITS " + Integer.toBinaryString(STOP));
        System.out.println("STOP: (int) " + STOP + "\n");

        // 010  2  TIDYING
        // 011  3  TERMINATED
        // ctlOf(RUNNING, 0)
        // 1110 0000 0000 0000 0000 0000 0000 0000
        // 0000 0000 0000 0000 0000 0000 0000 0000
        // 1110 0000 0000 0000 0000 0000 0000 0000
        System.out.println("ctl: RUNNING | 0 " + Integer.toBinaryString(ctl.get()));
        System.out.println("ctl: (int) " + ctl.get() + "\n");
        System.out.println(ctl.get());
    }


    @Test
    public void threadWrapperWorkerTest() {
        retry:
        for (;;) {
            int c = ctl.get();
            int rs = runStateOf(c);

            for (;;) {
                int wc = workerCountOf(c);
                if (ctl.compareAndSet(c, c + 1))
                    break retry;
                c = ctl.get();  // Re-read ctl
                if (runStateOf(c) != rs)
                    continue retry;
                // else CAS failed due to workerCount change; retry inner loop
            }
        }
    }

    /**
     * precious word: spinning:
     */
    @Test
    public void loopTest() {

    }
}


