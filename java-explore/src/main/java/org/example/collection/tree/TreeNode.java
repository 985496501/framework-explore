package org.example.collection.tree;

/**
 * @author: jinyun
 * @date: 2021/3/1
 */
public class TreeNode {
    final int val;
    TreeNode left;
    TreeNode right;

    public TreeNode(int val) {
        this.val = val;
    }

    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}
