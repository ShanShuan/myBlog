package com.shanshuan.ListNode;

/**
 * 判断是否有环
 */
public class Solution {
    class ListNode {
      int val;
      ListNode next;
      ListNode(int x) {
          val = x;
          next = null;
      }
    }

    public boolean hasCycle(ListNode head) {
       if(head==null||head.next==null){
           return false;
       }
        // 慢指针
        ListNode slow = head;
        // 快指针
        ListNode fast = head;
        while (fast.next != null && fast.next.next != null) {
            // 慢指针每次走一步
            slow = slow.next;
            // 快指针每次走两步
            fast = fast.next.next;
            // 当快慢指针相同时，则说明是在圆环里面
            if (slow == fast) {
                return true;
            }
        }
        return false;
    }




}
