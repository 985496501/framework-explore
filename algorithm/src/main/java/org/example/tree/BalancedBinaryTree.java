package org.example.tree;

/**
 * 二叉平衡树 又称 二叉搜索树
 * <p>
 * 创建一个树, 实际就是一个
 *
 * @author: jinyun
 * @date: 2021/4/21
 */
public class BalancedBinaryTree {


    /**
     * 简单的二叉搜素树, 不支持并发
     * 用于存储数据, 便于查询数据
     * <p>
     * <p>
     * <p>
     * 1、创建一个树 构造方法
     * 2、
     */
    static class BBTree {
        // 定义的tree的root节点, 这是单数, 可以加入数组, 形成多数的数据结构
        private final Node root;


        // 没创建一个tree 实际上就是都是一个头节点
        public BBTree(Node root) {
            this.root = root;
        }


    }


    /**
     * 定义一个节点
     *
     * 因为我们要创建的是 平衡搜索树
     * 肯定 会有比较
     *
     * 散列表要求：
     *
     * see {@link Object}
     * 两个对象如果hash 相同, 那么这两个对象不一定相同
     * 如果两个对象equals相同, 那么必须重写hashCode() 相同；
     * 因为tree的搜索比较,
     *
     * see {@link java.util.TreeMap}
     */
    static class Node {
        final int i;
        Node node;

        public Node(int i) {
            this.i = i;
            this.node = null;
        }

        /**
         * hash()/n == 这个值 确定这个对象落到 哪个bin
         *
         * 同一个对象无论调用多少次这个hashCode都要保证这个词都是一个定值
         *
         * @return
         */
        @Override
        public int hashCode() {
            return this.i;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Node) {
                Node e = (Node) obj;
                return e.i == this.i;
            }

            return super.equals(obj);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

}
