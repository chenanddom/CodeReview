package com.itdom.linear;

/**
 * 快慢指针解决中间值问题
 */
public class SlowFastPoint {
    public static void main(String[] args) {
        Node<String> first = new Node<String>("aa", null);
        Node<String> second = new Node<String>("bb", null);
        Node<String> third = new Node<String>("cc", null);
        Node<String> fourth = new Node<String>("dd", null);
        Node<String> fifth = new Node<String>("ee", null);
        Node<String> six = new Node<String>("ff", null);
        Node<String> seven = new Node<String>("gg", null);
//完成结点之间的指向
        first.next = second;
        second.next = third;
        third.next = fourth;
        fourth.next = fifth;
        fifth.next = six;
        six.next = seven;
        //查找中间值
        String mid = getMid(first);
        System.out.println("中间值为："+mid);
    }
    /**
     * @param first 链表的首结点
     * @return 链表的中间结点的值
     */
    public static String getMid(Node<String> first) {
        Node<String> fast=first;
        Node<String> slow = first;
        while (fast!=null&&fast.next!=null){
            fast = fast.next.next;
            slow = slow.next;
        }
        //当快指针走到结尾的时候慢指针刚好走到中间的位置
        return slow.item;
    }



    //结点类
    private static class Node<T> {
        //存储数据
        T item;
        //下一个结点
        Node next;
        public Node(T item, Node next) {
            this.item = item;
            this.next = next;
        }
    }

}

