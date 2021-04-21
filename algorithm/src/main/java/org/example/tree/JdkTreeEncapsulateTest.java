package org.example.tree;

import java.util.TreeSet;

/**
 * 这个看下jdk封装的 map
 *
 * 如果面试真正想问树结构, 其实可以 考查 TreeSet TreeMap
 * 相对简单的数据结构
 *
 * 而HashMap 是真的算得上比较复杂的数据结构了, 里面涵盖了很多数学的概率分析, 链化, 树化
 * 各种变形, 还有非常核心的算法, resize() 这个方法;
 *
 * @author: jinyun
 * @date: 2021/4/21
 */
public class JdkTreeEncapsulateTest {

    /**
     * Navigable: 搜索树
     *
     * @param args
     */
    public static void main(String[] args) {
        //
        TreeSet<Integer> treeSet = new TreeSet<>();
        treeSet.add(1);
    }

}
