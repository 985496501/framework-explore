package org.example.dynamic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.List;

/**
 * 首次感受动态规范的算法
 * 动态规范的介绍 理解动态规划：
 * <p>
 * <p>
 * 动态规划的要素：
 * <p>
 * <p>
 * 面对问题如何使用动态规划解决问题：
 * <p>
 * <p>
 * 常用的几个场景：
 * 最经典的问题就是 0-1背包问题
 * 数据结构的图中还有一个非常重要的算法， 最短路径算法，这个也是常常使用，也要搞明白这个问题。
 * 硬币问题：完全背包问题？ 应该是 必须把背包装满 而且价值相对最高。
 *
 *
 *
 * <p>
 * <p>
 * 入门爬楼梯的案例：
 * 递归
 * 递推
 * 临界值
 * <p>
 * <p>
 * 通过累加求和的案例可以看到：
 * <p>
 * 我们需要最后的解 只要知道前 n-1 项的解  【最优子结构】
 * 前 n-1 项的解 是 当前任务解的必要条件 f(n) = f(n-1) + n 【状态转移方程】
 * 按照这个方式往前递推 一定会出现一个临界值  如果没有这个临界值 那么会死循环 无法获取解  【边界】
 * <p>
 * 关系式：
 * 临界值：
 *
 * @author: jinyun
 * @date: 2021/2/26
 */
public class ZeroOnePackageTest {

    /**
     * 让你计算 1-99 的数值
     * 我们已经学习过高斯公司
     * (1 + 99) * 99 / 2
     * <p>
     * f(100) = 100 + f(99)
     * f(99) = 99 + f(98)
     * ....
     * f(2) = 2 + f(1)
     * f(1) = 1
     * <p>
     * f(n) = n + f(n-1)
     * 临界值：n = 1, f(1) = 1
     */
    @Test
    public void gaosiTest() {
        System.out.println(accumulate(100));
    }

    private int accumulate(int n) {
        if (n == 1) {
            return 1;
        }

        return n + accumulate(n - 1);
    }

    /**
     * f(n) = f(n-1) + f(n-2)
     * f(1)=1
     * f(2)=1
     */
    @Test
    public void fibonacciTest() {
        int fibonacci = fibonacci(10);
        System.out.println(fibonacci);
    }


    private int fibonacci(int n) {
        if (n == 1 || n == 2) {
            return 1;
        }

        return fibonacci(n - 1) + fibonacci(n - 2);
    }


    /**
     * 场景：
     * 1 -- 10 层楼梯 有几种爬法：
     * 每次只能上一层 或者 2层
     * 你想要到达10层
     * 有几种情况，要么你要到 9 上一步  到8上2步
     * f(10) = f(9) + f(8) --> f(n) = f(n-1) + f(n-2)
     * <p>
     * 再考虑临界值：f(1) = 1  f(2) = 2
     * 89种情况。
     */
    @Test
    public void mountStairsTest() {
        System.out.println(steps(10));
        System.out.println(stepsOptimized(10));
    }

    /**
     * 普通递归的方式
     *
     * @param n n
     * @return n
     */
    private int steps(int n) {
        if (n < 3) {
            return n;
        }

        return steps(n - 1) + steps(n - 2);
    }

    /**
     * 使用动态规范的方式保存上一步的状态量  避免重复计算  但是要开辟新的内存空间去存储这种数据
     * Time(O(n)) Space(O(1))
     *
     * @param n 0
     * @return 0
     */
    private int stepsOptimized(int n) {
        if (n == 1) return 1;
        if (n == 2) return 2;

        // f(n-2)
        int a = 1;
        // f(n-1)
        int b = 2;

        // f(n)
        int temp = 0;
        for (int i = 3; i <= n; i++) {
            temp = a + b;
            a = b;
            b = temp;
        }

        return temp;
    }

    /**
     * 01背包问题, 求最大的价值, 可以忽略如何装的
     *
     * 需要根据一般的情况抽象成通用的情况
     * 使用动态规划算法 完成01背包问题
     */
    @Test
    public void packageTest() {

    }

    private int fill(int totalWeight, List<Box> selectiveBox) {
        // 我们先定义一个二维数据
//        int[][] map = new int[][]
        return 0;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Box {
        private int weight;
        private int value;
    }

}
