package com.bdp.demo.datastructure;

/**
 * Definition for singly-linked list.  数组和链表
 * public class ListNode {
 * int val;
 * ListNode next;
 * ListNode(int x) { val = x; }
 * }
 */
public class arrayTest {
//    public ListNode reverseList(ListNode head) {
////定义一个前置节点变量，默认是null，因为对于第一个节点而言没有前置节点
//        ListNode pre = null;
////定义一个当前节点变量，首先将头节点赋值给它
//        ListNode curr = head;
////遍历整个链表，直到当前指向的节点为空，也就是最后一个节点了
//        while (curr != null) {
////在循环体里会去改变当前节点的指针方向，本来当前节点的指针是指向的下一个节点，
//// 现在需要改为指向前一个节点，但是如果直接就这么修改了，那链条就断了，再也找不到后面的节点了，
//// 所以首先需要将下一个节点先临时保存起来，赋值到temp中，以备后续使用
//            ListNode temp = curr.next;
////开始处理当前节点，将当前节点的指针指向前面一个节点
//            curr.next = pre;
////将当前节点赋值给变量pre，也就是让pre移动一步，pre指向了当前节点
//            pre = curr;
////将之前保存的临时节点（后面一个节点）赋值给当前节点变量
//            curr = temp;
////循环体执行链表状态变更情况：
////NULL<-1 2->3->4->5->NULL
////NULL<-1<-2 3->4->5->NULL
////NULL<-1<-2<-3 4->5->NULL
////NULL<-1<-2<-3<-4 5->NULL
////NULL<-1<-2<-3<-4<-5
////循环体遍历完之后，pre指向5的节点
//        }
////完成，时间复杂度为O(n)
//        return pre;
//    }
}
