package com.shanshuan.ListNode;

/**
 * Created by wangzifeng on 2020/3/24.
 *
 *给定一个链表，返回链表开始入环的第一个节点。 如果链表无环，则返回 null。
 *
 * 为了表示给定链表中的环，我们使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。 如果 pos 是 -1，则在该链表中没有环。
 *
 * 2(F + a) = F + N(a + b) + a
 *  2F + 2a = F + 2a + b + (N - 1)(a + b)
 *        F = b + (N - 1)(a + b)
 *
 *        F是到达入口点长度
 *        N为ptr2跑第几圈会与ptr1相遇
 */
public class MySolution {

    class ListNode {
        int val;
        ListNode next;
        ListNode(int x) {
            val = x;
            next = null;
        }
    }


    /**
     * 获取第一次交点
     * @param head
     * @return
     */
    private ListNode getIntersect(ListNode head) {
        ListNode tortoise = head;
        ListNode hare = head;

       while(hare!=null&&hare.next!=null){
           tortoise=tortoise.next;
           hare=hare.next.next;
           if(tortoise==hare){
               return hare;
           }
       }
        return null;
    }

    public ListNode detectCycle(ListNode head) {
        if (head == null) {
            return null;
        }

        // If there is a cycle, the fast/slow pointers will intersect at some
        // node. Otherwise, there is no cycle, so we cannot find an e***ance to
        // a cycle.
        ListNode intersect = getIntersect(head);
        if (intersect == null) {
            return null;
        }

            ListNode part1=head;
            ListNode part2=intersect;
            while (part1!=part2) {
                part1 = part1.next;
                part2 = part2.next;
                return part1;
            }
        return null;
    }

}
