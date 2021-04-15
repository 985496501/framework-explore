package org.example.linkedlist;

import org.junit.Test;

/**
 * 逆转链表
 *
 * @author: jinyun
 * @date: 2021/3/2
 */
public class ReverseLinkedListTest {


    @Test
    public void linkedListTest() {
        int[] arr = {1, 2, 4, 5, 6, 7, 89};
        ListNode linkedList = createLinkedList(arr);
        traversal(linkedList);
        // choose one.
        traversal(reverse(linkedList));
        traversal(recursion(linkedList));
    }


    public ListNode reverse(ListNode head) {
        // 使用头插法完成链表的反转
        ListNode zeroHead = new ListNode(0);
        ListNode p = head;
        while (p != null) {
            // 进行链表的指向配置
            ListNode newHeadOfSubLinkedList = zeroHead.next;
            zeroHead.next = p;
            p = p.next;
            zeroHead.next.next = newHeadOfSubLinkedList;
        }

        return zeroHead.next;
    }


    public ListNode recursion(ListNode head) {
        // 使用递归完成逆转
        if (head == null || head.next == null) {
            return head;
        }

        // 先一般 后特殊
        ListNode newHead = recursion(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }



    public ListNode createLinkedList(int[] arr) {
        ListNode listNode = new ListNode(0);
        ListNode p = listNode;
        for (int i = 0; i < arr.length; i++) {
            p.next = new ListNode(arr[i]);
            p = p.next;
        }

        return listNode;
    }

    public void traversal(ListNode listNode) {
        ListNode p = listNode;
        StringBuilder sb = new StringBuilder("{");
        while (p != null) {
            sb.append(" ").append(p.val).append(",");
            p = p.next;
        }

        sb.insert(sb.lastIndexOf(","), "}");
        System.out.println(sb.toString());
    }

}
