package org.example.collection.tree;

import org.junit.Test;

/**
 * 主要针对 树  的学习
 * 树的定义是递归定义的, 要理解子树的概念。
 * <p>
 * 树的基本概念：
 * <p>1. 一个树有且仅有一个根, 但是树是递归定义的, 所以任何一个节点都可以看做它所在的子树的根, [node]
 * <p>2. 一个节点的分支 成为 度, max{一个数的任意节点的度} == tree [degree]
 * <p>3. 两个节点的连线：
 * <p>4. 树的高度 [height]  从底向上的,  树的深度, 从上向下的。
 *
 *
 * @author: jinyun
 * @date: 2021/3/1
 */
public class TreeTest {

    /**
     * B-B-Tree：
     * Definition：
     * 1. 二叉树
     * 2. 左右节点的高度差不能大于1
     * <p>
     * 树的话 给的是一个 TreeNode- root
     * <p>
     * Description:
     * Input：root = [3,9,20,null,null,15,7]
     * <p>
     * Output：true
     */
    @Test
    public void isBalancedTreeTest() {
        TreeNode root = new TreeNode(3);
        TreeNode left1 = new TreeNode(9);
        TreeNode right1 = new TreeNode(20);
        TreeNode left21 = new TreeNode(15);
        TreeNode right21 = new TreeNode(7);

        root.left = left1;
        root.right = right1;
        right1.left = left21;
        right1.right = right21;

        boolean b = isBalanced(root);
        System.out.println(b);
        print(root);
    }

    private boolean isBalanced(TreeNode root) {
        // 可以比较各个叶子节点的高度 是否大于1 根据定义判断
        if (root == null) {
            return true;
        } else {
            return isBalanced(root.right) && isBalanced(root.left) && Math.abs(height(root.right) - height(root.left)) <= 1;
        }
    }


    /**
     * 计算每个节点的高
     * 递归计算会存在一定的重复计算的步骤 优化掉这一部分 就计算性能就提升了.
     * <p>
     * null节点 默认为 0
     * 叶子节点 默认为 1
     *
     * @param node n
     * @return i
     */
    public int height(TreeNode node) {
        if (node == null) {
            return 0;
        }

        return Math.max(height(node.left), height(node.right)) + 1;
    }

    /**
     * 使用中序遍历树
     * 中跟遍历
     * 左 -- 中 ---右
     *
     * @param root r
     */
    private void print(TreeNode root) {
        if (root == null) {
            return;
        }

        print(root.left);
        System.out.println(root.val);
        print(root.right);
    }


    @Test
    public void heightTest() {
        TreeNode root = new TreeNode(3);
        TreeNode left1 = new TreeNode(9);
        TreeNode left11 = new TreeNode(9);
        root.left = left1;
           System.out.println(height(root));

    }
}

